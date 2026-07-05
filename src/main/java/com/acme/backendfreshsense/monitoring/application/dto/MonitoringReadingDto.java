package com.acme.backendfreshsense.monitoring.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Lectura de sensores IoT registrada en el sistema")
public record MonitoringReadingDto(
        @Schema(description = "Identificador único de la lectura", example = "42")
        Long id,

        @Schema(description = "Temperatura en grados Celsius", example = "4.5")
        Double temperature,

        @Schema(description = "Humedad relativa en porcentaje", example = "80.0")
        Double humidity,

        @Schema(description = "Nivel de etileno en ppm", example = "0.3")
        Double ethyleneLevel,

        @Schema(description = "Nivel de oxígeno en porcentaje", example = "20.5")
        Double oxygenLevel,

        @Schema(description = "Índice de madurez (0–100)", example = "65.0")
        Double ripeness,

        @Schema(description = "Índice de limpieza (0–100)", example = "90.0")
        Double cleanliness,

        @Schema(description = "Fecha y hora en que se registró la lectura (ISO 8601)", example = "2026-06-01T10:30:00")
        LocalDateTime recordedAt,

        @Schema(description = "Identificador del dispositivo IoT origen de la lectura", example = "esp32-freshsense-1")
        String deviceId,

        @Schema(description = "Estado de frescura (semáforo) clasificado por el Edge", example = "GREEN", allowableValues = {"GREEN", "YELLOW", "RED", "UNKNOWN"})
        String status,

        @Schema(description = "Categoría contra la que el Edge clasificó la lectura", example = "Lácteos")
        String category
) {}
