package com.acme.backendfreshsense.monitoring.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.monitoring.domain.model.MonitoringReading;
import com.acme.backendfreshsense.monitoring.domain.repository.MonitoringReadingRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.MonitoringJpaRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.MonitoringReadingEntity;

import java.util.List;
import java.util.Optional;

public class MonitoringReadingRepositoryAdapter implements MonitoringReadingRepository {

    private final MonitoringJpaRepository jpa;

    public MonitoringReadingRepositoryAdapter(MonitoringJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public MonitoringReading save(MonitoringReading reading) {
        MonitoringReadingEntity saved = jpa.save(toEntity(reading));
        return toDomain(saved);
    }

    @Override
    public Optional<MonitoringReading> findLatest() {
        return jpa.findTopByOrderByRecordedAtDesc().map(this::toDomain);
    }

    @Override
    public Optional<MonitoringReading> findLatestByUser(Long userId) {
        return jpa.findTopByUserIdOrderByRecordedAtDesc(userId).map(this::toDomain);
    }

    @Override
    public List<MonitoringReading> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<MonitoringReading> findByUserId(Long userId) {
        return jpa.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    private MonitoringReading toDomain(MonitoringReadingEntity e) {
        return MonitoringReading.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .temperature(e.getTemperature())
                .humidity(e.getHumidity())
                .ethyleneLevel(e.getEthyleneLevel())
                .oxygenLevel(e.getOxygenLevel())
                .ripeness(e.getRipeness())
                .cleanliness(e.getCleanliness())
                .recordedAt(e.getRecordedAt())
                .build();
    }

    private MonitoringReadingEntity toEntity(MonitoringReading r) {
        return MonitoringReadingEntity.builder()
                .id(r.getId())
                .userId(r.getUserId())
                .temperature(r.getTemperature())
                .humidity(r.getHumidity())
                .ethyleneLevel(r.getEthyleneLevel())
                .oxygenLevel(r.getOxygenLevel())
                .ripeness(r.getRipeness())
                .cleanliness(r.getCleanliness())
                .recordedAt(r.getRecordedAt())
                .build();
    }
}
