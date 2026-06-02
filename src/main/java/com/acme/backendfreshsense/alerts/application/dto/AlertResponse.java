package com.acme.backendfreshsense.alerts.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de una alerta del sistema")
public record AlertResponse(
        @Schema(description = "Identificador único de la alerta", example = "1")
        Long id,

        @Schema(description = "Título corto de la alerta", example = "Leche próxima a vencer")
        String title,

        @Schema(description = "Mensaje descriptivo de la alerta", example = "La leche vence en 2 días")
        String message,

        @Schema(description = "Nivel de criticidad: INFO, WARNING o CRITICAL", example = "WARNING", allowableValues = {"INFO", "WARNING", "CRITICAL"})
        String severity,

        @Schema(description = "Origen de la alerta", example = "inventory")
        String source,

        @Schema(description = "Estado actual de la alerta", example = "ACTIVE")
        String state,

        @Schema(description = "Tiempo transcurrido desde que se generó (texto legible)", example = "Hace 3 horas")
        String timeAgo
) {}
