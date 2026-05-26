package com.acme.backendfreshsense.accounts.infrastructure.persistence.config;

import com.acme.backendfreshsense.accounts.application.service.AccountService;
import com.acme.backendfreshsense.accounts.application.service.RoleService;
import com.acme.backendfreshsense.accounts.domain.repository.RefreshTokenRepository;
import com.acme.backendfreshsense.accounts.domain.repository.UserRepository;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.adapter.RefreshTokenRepositoryAdapter;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.adapter.UserRepositoryAdapter;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa.RefreshTokenJpaRepository;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa.UserJpaRepository;
import com.acme.backendfreshsense.shared.infrastructure.security.JwtService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AccountsPersistenceConfig {

    @Bean
    public UserRepository userRepository(UserJpaRepository jpa) {
        return new UserRepositoryAdapter(jpa);
    }

    @Bean
    public RefreshTokenRepository refreshTokenRepository(RefreshTokenJpaRepository jpa) {
        return new RefreshTokenRepositoryAdapter(jpa);
    }

    @Bean
    public AccountService accountService(UserRepository userRepository,
                                         PasswordEncoder passwordEncoder,
                                         JwtService jwtService,
                                         RefreshTokenRepository refreshTokenRepository,
                                         ApplicationEventPublisher eventPublisher) {
        return new AccountService(userRepository, passwordEncoder, jwtService, refreshTokenRepository, eventPublisher);
    }

    @Bean
    public RoleService roleService(UserRepository userRepository,
                                   ApplicationEventPublisher eventPublisher) {
        return new RoleService(userRepository, eventPublisher);
    }
}
