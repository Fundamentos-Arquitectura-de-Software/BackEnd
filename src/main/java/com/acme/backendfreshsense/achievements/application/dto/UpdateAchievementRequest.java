package com.acme.backendfreshsense.achievements.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para actualizar el progreso de un logro")
public record UpdateAchievementRequest(
        @Schema(description = "Nuevo porcentaje de completitud del logro (0–100)", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
        int completionPercentage
) {}
