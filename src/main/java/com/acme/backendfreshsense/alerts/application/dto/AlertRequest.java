package com.acme.backendfreshsense.alerts.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para crear o actualizar una alerta")
public record AlertRequest(
        @Schema(description = "Título corto de la alerta", example = "Leche próxima a vencer", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Mensaje descriptivo de la alerta", example = "La leche vence en 2 días", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Nivel de criticidad: INFO (informativo), WARNING (advertencia), CRITICAL (crítico)", example = "WARNING", allowableValues = {"INFO", "WARNING", "CRITICAL"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String severity,

        @Schema(description = "Origen de la alerta: sensor IoT, inventario, sistema, etc.", example = "inventory", requiredMode = Schema.RequiredMode.REQUIRED)
        String source,

        @Schema(description = "Estado actual de la alerta (p. ej. ACTIVE, RESOLVED)", example = "ACTIVE", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String state,

        @Schema(description = "Tiempo transcurrido desde que se generó la alerta (texto legible)", example = "Hace 3 horas", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String timeAgo
) {}
