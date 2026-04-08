package com.acme.backendfreshsense.accounts.application.service;

import com.acme.backendfreshsense.accounts.application.dto.LoginRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserRegistrationRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserResponse;
import com.acme.backendfreshsense.accounts.domain.model.Role;
import com.acme.backendfreshsense.accounts.domain.model.User;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        User user = new User(
                request.getEmail(),
                request.getPassword(), // TODO: en el futuro encriptar
                request.getFullName(),
                Role.USER
        );

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getFullName(),
                saved.getRole()
        );
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // TODO: usar password encriptado, esto es solo para skeleton
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }

    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
