package com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitoringJpaRepository extends JpaRepository<MonitoringReadingEntity, Long> {
    Optional<MonitoringReadingEntity> findTopByOrderByRecordedAtDesc();
}
