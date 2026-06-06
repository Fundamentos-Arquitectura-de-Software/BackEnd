package com.acme.backendfreshsense.achievements.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "Datos para actualizar el progreso de un logro")
public record UpdateAchievementRequest(
        @Schema(description = "Nuevo porcentaje de completitud del logro (0–100)", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 0, message = "El porcentaje no puede ser menor que 0.")
        @Max(value = 100, message = "El porcentaje no puede ser mayor que 100.")
        int completionPercentage
) {}
