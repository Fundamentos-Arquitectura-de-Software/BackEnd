package com.acme.backendfreshsense.monitoring.infrastructure.persistence;

import com.acme.backendfreshsense.monitoring.domain.model.MonitoringReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitoringJpaRepository extends JpaRepository<MonitoringReading, Long> {
    Optional<MonitoringReading> findTopByOrderByRecordedAtDesc();
}
