package com.acme.alertsservice.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertJpaRepository extends JpaRepository<AlertEntity, Long> {
    List<AlertEntity> findByUserId(Long userId);
    Optional<AlertEntity> findByIdAndUserId(Long id, Long userId);
}
