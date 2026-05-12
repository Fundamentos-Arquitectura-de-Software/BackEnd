package com.acme.backendfreshsense.monitoring.domain.repository;

import com.acme.backendfreshsense.monitoring.domain.model.MonitoringReading;

import java.util.List;
import java.util.Optional;

public interface MonitoringReadingRepository {
    MonitoringReading save(MonitoringReading reading);
    Optional<MonitoringReading> findLatest();
    List<MonitoringReading> findAll();
}
