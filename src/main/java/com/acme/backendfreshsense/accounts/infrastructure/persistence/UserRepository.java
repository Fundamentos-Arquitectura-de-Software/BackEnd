package com.acme.backendfreshsense.accounts.infrastructure.persistence;

import com.acme.backendfreshsense.accounts.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
