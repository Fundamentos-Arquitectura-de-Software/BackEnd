package com.acme.backendfreshsense.shared.infrastructure.security;

import com.acme.backendfreshsense.accounts.domain.model.RefreshToken;
import com.acme.backendfreshsense.accounts.domain.model.Role;
import com.acme.backendfreshsense.accounts.domain.model.User;
import com.acme.backendfreshsense.accounts.domain.repository.RefreshTokenRepository;
import com.acme.backendfreshsense.accounts.domain.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Maneja el exito del flujo OAuth2 (Google).
 * Crea o recupera el usuario en la BD, emite cookies JWT y redirige al frontend.
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${authorization.jwt.refresh.expiration.days:7}")
    private int refreshDays;

    public OAuth2SuccessHandler(UserRepository userRepository,
                                RefreshTokenRepository refreshTokenRepository,
                                JwtService jwtService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email    = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");

        // Buscar o crear el usuario
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(email, "", fullName != null ? fullName : email, Role.USER_STANDARD);
            return userRepository.save(newUser);
        });

        // Revocar refresh tokens anteriores
        refreshTokenRepository.revokeAllByUserId(user.getId());

        // Emitir tokens
        String accessToken  = jwtService.generateAccessToken(user.getEmail(), user.getId(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId());

        RefreshToken stored = new RefreshToken(refreshToken, user.getId(),
                Instant.now().plus(refreshDays, ChronoUnit.DAYS));
        refreshTokenRepository.save(stored);

        // Cookies HttpOnly
        Cookie authCookie = new Cookie("authToken", accessToken);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setAttribute("SameSite", "None");
        authCookie.setPath("/");
        authCookie.setMaxAge(15 * 60);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setAttribute("SameSite", "None");
        refreshCookie.setPath("/api/accounts/refresh");
        refreshCookie.setMaxAge(refreshDays * 24 * 3600);

        response.addCookie(authCookie);
        response.addCookie(refreshCookie);

        // Redirigir al frontend
        getRedirectStrategy().sendRedirect(request, response, frontendUrl + "/dashboard");
    }
}
