package com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonitoringJpaRepository extends JpaRepository<MonitoringReadingEntity, Long> {
    Optional<MonitoringReadingEntity> findTopByOrderByRecordedAtDesc();
    Optional<MonitoringReadingEntity> findTopByUserIdOrderByRecordedAtDesc(Long userId);
    List<MonitoringReadingEntity> findByUserId(Long userId);
}
