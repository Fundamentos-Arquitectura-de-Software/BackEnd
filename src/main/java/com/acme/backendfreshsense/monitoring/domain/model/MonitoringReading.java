package com.acme.backendfreshsense.monitoring.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringReading {

    private Long id;
    private Double temperature;
    private Double humidity;
    private Double ethyleneLevel;
    private Double oxygenLevel;
    private Double ripeness;
    private Double cleanliness;
    private LocalDateTime recordedAt;
}
