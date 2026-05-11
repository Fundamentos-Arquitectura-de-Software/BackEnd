package com.acme.backendfreshsense.monitoring.application.dto;

import java.time.LocalDateTime;

public record MonitoringReadingDto(
        Long id,
        Double temperature,
        Double humidity,
        Double ethyleneLevel,
        Double oxygenLevel,
        Double ripeness,
        Double cleanliness,
        LocalDateTime recordedAt
) {}
