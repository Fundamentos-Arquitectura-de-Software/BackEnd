package com.acme.backendfreshsense.achievements.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateAchievementRequest(
        @Min(value = 0, message = "El porcentaje no puede ser menor que 0.")
        @Max(value = 100, message = "El porcentaje no puede ser mayor que 100.")
        int completionPercentage
) {}