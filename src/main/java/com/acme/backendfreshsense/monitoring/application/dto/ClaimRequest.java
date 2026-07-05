package com.acme.backendfreshsense.monitoring.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Canje de un código de emparejamiento por parte del Edge")
public record ClaimRequest(
        @Schema(description = "Código de emparejamiento mostrado en la app", example = "7K4Q2P", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String code
) {}
