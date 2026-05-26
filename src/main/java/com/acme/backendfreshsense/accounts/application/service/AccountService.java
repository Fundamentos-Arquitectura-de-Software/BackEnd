package com.acme.backendfreshsense.accounts.application.service;

import com.acme.backendfreshsense.accounts.application.dto.LoginRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserRegistrationRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserResponse;
import com.acme.backendfreshsense.accounts.domain.model.RefreshToken;
import com.acme.backendfreshsense.accounts.domain.model.Role;
import com.acme.backendfreshsense.accounts.domain.model.User;
import com.acme.backendfreshsense.accounts.domain.repository.RefreshTokenRepository;
import com.acme.backendfreshsense.accounts.domain.repository.UserRepository;
import com.acme.backendfreshsense.shared.domain.event.UserRegisteredEvent;
import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
import com.acme.backendfreshsense.shared.infrastructure.security.JwtService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Transactional
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AccountService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          RefreshTokenRepository refreshTokenRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.eventPublisher = eventPublisher;
    }

    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName(),
                Role.USER_FREE
        );

        User saved = userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisteredEvent(saved.getId(), saved.getEmail()));

        String accessToken = jwtService.generateAccessToken(saved.getEmail(), saved.getId(), saved.getRole().name());
        String refreshToken = issueRefreshToken(saved.getId(), saved.getEmail());

        return new UserResponse(saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole(), accessToken, refreshToken);
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        refreshTokenRepository.revokeAllByUserId(user.getId());

        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId(), user.getRole().name());
        String refreshToken = issueRefreshToken(user.getId(), user.getEmail());

        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole(), accessToken, refreshToken);
    }

    public UserResponse refresh(String rawRefreshToken) {
        RefreshToken stored = refreshTokenRepository.findByToken(rawRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expirado o revocado");
        }

        User user = userRepository.findById(stored.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        refreshTokenRepository.revokeByToken(rawRefreshToken);

        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), user.getId(), user.getRole().name());
        String newRefreshToken = issueRefreshToken(user.getId(), user.getEmail());

        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole(), newAccessToken, newRefreshToken);
    }

    public void revokeRefreshToken(String rawRefreshToken) {
        refreshTokenRepository.revokeByToken(rawRefreshToken);
    }

    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole(), null, null);
    }

    private String issueRefreshToken(Long userId, String email) {
        String raw = jwtService.generateRefreshToken(email, userId);
        RefreshToken token = new RefreshToken(
                raw,
                userId,
                Instant.now().plus(7, ChronoUnit.DAYS)
        );
        refreshTokenRepository.save(token);
        return raw;
    }
}
