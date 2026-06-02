package com.acme.backendfreshsense.achievements.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Datos de un logro del usuario")
public record AchievementDto(
        @Schema(description = "Identificador único del logro (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "ID del usuario propietario del logro", example = "1")
        Long userId,

        @Schema(description = "Nombre del logro", example = "Primer inicio de sesión")
        String name,

        @Schema(description = "Porcentaje de completitud del logro (0–100)", example = "75")
        int completionPercentage,

        @Schema(description = "Indica si el logro es parte del conjunto predeterminado del sistema", example = "true")
        boolean isDefault
) {}
