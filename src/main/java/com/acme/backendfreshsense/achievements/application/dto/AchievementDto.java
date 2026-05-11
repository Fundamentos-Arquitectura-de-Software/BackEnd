package com.acme.backendfreshsense.achievements.application.dto;

import java.util.UUID;

public record AchievementDto(
        UUID id,
        Long userId,
        String name,
        int completionPercentage,
        boolean isDefault
) {}
