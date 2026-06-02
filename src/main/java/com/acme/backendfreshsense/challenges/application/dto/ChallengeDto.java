package com.acme.backendfreshsense.challenges.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Datos de un reto disponible en el sistema")
public record ChallengeDto(
        @Schema(description = "Identificador único del reto", example = "1")
        Long id,

        @Schema(description = "Título del reto", example = "Semana sin desperdicios")
        String title,

        @Schema(description = "Descripción detallada del reto y sus reglas", example = "Consuma o conserve todos sus alimentos durante 7 días consecutivos sin descartar ninguno")
        String description,

        @Schema(description = "Puntos de recompensa al completar el reto", example = "150")
        Integer rewardPts,

        @Schema(description = "Fecha de inicio del reto (ISO 8601)", example = "2026-06-01")
        LocalDate startAt,

        @Schema(description = "Fecha de fin del reto (ISO 8601)", example = "2026-06-07")
        LocalDate endAt,

        @Schema(description = "Tipo de objetivo del reto (p. ej. NO_WASTE, CONSUME, SAVE)", example = "NO_WASTE")
        String goalType,

        @Schema(description = "Valor numérico objetivo a alcanzar para completar el reto", example = "7")
        Integer goalTarget,

        @Schema(description = "Estado del reto: ACTIVE, UPCOMING o FINISHED", example = "ACTIVE", allowableValues = {"ACTIVE", "UPCOMING", "FINISHED"})
        String status,

        @Schema(description = "URL de imagen o banner representativo del reto", example = "https://example.com/challenge-banner.jpg")
        String bannerUrl
) {}
