package com.acme.backendfreshsense.monitoring.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Lectura de sensores IoT para registrar en el sistema")
public record MonitoringReadingRequest(
        @Schema(description = "Temperatura en grados Celsius (rango típico: 0–30 °C)", example = "4.5", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Double temperature,

        @Schema(description = "Humedad relativa en porcentaje (rango: 0–100 %)", example = "80.0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Double humidity,

        @Schema(description = "Nivel de etileno en partes por millón (rango típico: 0–10 ppm)", example = "0.3", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Double ethyleneLevel,

        @Schema(description = "Nivel de oxígeno en porcentaje (rango típico: 0–21 %)", example = "20.5", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Double oxygenLevel,

        @Schema(description = "Índice de madurez de los alimentos en escala 0–100", example = "65.0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Double ripeness,

        @Schema(description = "Índice de limpieza del ambiente en escala 0–100", example = "90.0", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Double cleanliness
) {}
