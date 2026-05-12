package com.acme.backendfreshsense.monitoring.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.monitoring.domain.model.MonitoringReading;
import com.acme.backendfreshsense.monitoring.domain.repository.MonitoringReadingRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.MonitoringJpaRepository;

import java.util.List;
import java.util.Optional;

public class MonitoringReadingRepositoryAdapter implements MonitoringReadingRepository {

    private final MonitoringJpaRepository jpa;

    public MonitoringReadingRepositoryAdapter(MonitoringJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public MonitoringReading save(MonitoringReading reading) {
        return jpa.save(reading);
    }

    @Override
    public Optional<MonitoringReading> findLatest() {
        return jpa.findTopByOrderByRecordedAtDesc();
    }

    @Override
    public List<MonitoringReading> findAll() {
        return jpa.findAll();
    }
}
