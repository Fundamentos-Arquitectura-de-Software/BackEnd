package com.acme.backendfreshsense.monitoring.application.service;

import com.acme.backendfreshsense.monitoring.application.dto.MonitoringReadingDto;
import com.acme.backendfreshsense.monitoring.application.dto.MonitoringReadingRequest;
import com.acme.backendfreshsense.monitoring.domain.model.MonitoringReading;
import com.acme.backendfreshsense.monitoring.domain.repository.MonitoringReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class MonitoringService {

    private final MonitoringReadingRepository repository;

    public MonitoringReadingDto save(MonitoringReadingRequest request) {
        MonitoringReading reading = MonitoringReading.builder()
                .temperature(request.temperature())
                .humidity(request.humidity())
                .ethyleneLevel(request.ethyleneLevel())
                .oxygenLevel(request.oxygenLevel())
                .ripeness(request.ripeness())
                .cleanliness(request.cleanliness())
                .recordedAt(LocalDateTime.now())
                .build();
        return toDto(repository.save(reading));
    }

    public Optional<MonitoringReadingDto> getLatest() {
        return repository.findLatest().map(this::toDto);
    }

    public List<MonitoringReadingDto> getAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    private MonitoringReadingDto toDto(MonitoringReading r) {
        return new MonitoringReadingDto(
                r.getId(),
                r.getTemperature(),
                r.getHumidity(),
                r.getEthyleneLevel(),
                r.getOxygenLevel(),
                r.getRipeness(),
                r.getCleanliness(),
                r.getRecordedAt()
        );
    }
}
