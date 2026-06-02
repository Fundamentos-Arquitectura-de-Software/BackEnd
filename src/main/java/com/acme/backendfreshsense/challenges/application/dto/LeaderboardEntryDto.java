package com.acme.backendfreshsense.challenges.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entrada del ranking de participantes en un reto")
public record LeaderboardEntryDto(
        @Schema(description = "ID del usuario participante", example = "7")
        Long userId,

        @Schema(description = "ID del reto al que pertenece esta entrada", example = "1")
        Long challengeId,

        @Schema(description = "Progreso actual del usuario en el reto (unidades completadas)", example = "5")
        Integer progress,

        @Schema(description = "Posición del usuario en el ranking (1 = primero)", example = "1")
        int rank
) {}
