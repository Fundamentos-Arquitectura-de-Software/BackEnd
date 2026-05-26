package com.acme.backendfreshsense.accounts.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.accounts.domain.model.Role;
import com.acme.backendfreshsense.accounts.domain.model.User;
import com.acme.backendfreshsense.accounts.domain.repository.UserRepository;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa.UserEntity;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa.UserJpaRepository;

import java.util.Optional;

public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpa;

    public UserRepositoryAdapter(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        UserEntity saved = jpa.save(toEntity(user));
        return toDomain(saved);
    }

    @Override
    public void updateRole(Long userId, Role role) {
        jpa.findById(userId).ifPresent(entity -> {
            entity.setRole(role);
            jpa.save(entity);
        });
    }

    private User toDomain(UserEntity e) {
        User u = new User(e.getEmail(), e.getPassword(), e.getFullName(), e.getRole());
        u.setId(e.getId());
        return u;
    }

    private UserEntity toEntity(User u) {
        return new UserEntity(u.getEmail(), u.getPassword(), u.getFullName(), u.getRole());
    }
}
