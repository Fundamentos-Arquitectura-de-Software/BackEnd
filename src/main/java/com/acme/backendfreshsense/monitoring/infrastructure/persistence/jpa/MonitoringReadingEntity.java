package com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "monitoring_readings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringReadingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;

    @Column(name = "ethylene_level", nullable = false)
    private Double ethyleneLevel;

    @Column(name = "oxygen_level", nullable = false)
    private Double oxygenLevel;

    @Column(nullable = false)
    private Double ripeness;

    @Column(nullable = false)
    private Double cleanliness;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}
