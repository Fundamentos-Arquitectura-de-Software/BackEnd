package com.acme.backendfreshsense.accounts.domain.repository;

import com.acme.backendfreshsense.accounts.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);
    User save(User user);
    void updateRole(Long userId, com.acme.backendfreshsense.accounts.domain.model.Role role);
}
