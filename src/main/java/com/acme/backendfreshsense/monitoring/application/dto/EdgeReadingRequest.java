package com.acme.backendfreshsense.monitoring.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Lectura enviada por el Edge/IoT. Formato de 'time': dd/MM/yyyy HH:mm")
public record EdgeReadingRequest(
        @Schema(description = "Identificador del dispositivo (sensor)", example = "esp32-freshsense-1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String deviceId,

        @Schema(description = "Temperatura en °C", example = "24", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Double temperature,

        @Schema(description = "Humedad relativa en %", example = "40", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Double humidity,

        @Schema(description = "Marca de tiempo de la lectura en el Edge", example = "21/06/2026 10:48")
        String time,

        @Schema(description = "Identificador de la lectura generado en el Edge", example = "34c0c05ad9f7e4397335")
        String id,

        @Schema(description = "Estado de frescura ya clasificado por el Edge (semáforo)", example = "GREEN", allowableValues = {"GREEN", "YELLOW", "RED", "UNKNOWN"}, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String status,

        @Schema(description = "Categoría contra la que el Edge clasificó la lectura (según el dispositivo)", example = "Lácteos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String category
) {}
