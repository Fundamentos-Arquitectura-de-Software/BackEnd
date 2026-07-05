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

    // Campos opcionales: el sensor físico (ESP32) solo envía temperatura y humedad.
    @Column(name = "ethylene_level")
    private Double ethyleneLevel;

    @Column(name = "oxygen_level")
    private Double oxygenLevel;

    private Double ripeness;

    private Double cleanliness;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Column(name = "user_id")
    private Long userId;

    /** Identificador del dispositivo IoT que originó la lectura (p. ej. esp32-freshsense-1). */
    @Column(name = "device_id")
    private String deviceId;

    /** Identificador de la lectura generado en el Edge (no es la PK de la BD). */
    @Column(name = "external_id")
    private String externalId;

    /** Estado de frescura (semáforo) clasificado por el Edge: GREEN/YELLOW/RED/UNKNOWN. Nullable: las lecturas manuales no lo traen. */
    @Column(name = "status", length = 10)
    private String status;

    /** Categoría contra la que el Edge clasificó la lectura (etiqueta de referencia). */
    @Column(name = "category", length = 50)
    private String category;
}
