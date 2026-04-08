package com.acme.backendfreshsense.achievements.application.mapper;

import  com.acme.backendfreshsense.achievements.application.dto.AchievementDto;
import  com.acme.backendfreshsense.achievements.domain.model.Achievement;

public class AchievementMapper {
    public static AchievementDto toDto(Achievement a) {
        return new AchievementDto(a.getId(), a.getUserId(), a.getName(), a.getCompletionPercentage(), a.isDefault());
    }
}
