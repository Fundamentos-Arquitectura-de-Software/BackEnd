package com.acme.backendfreshsense.achievements.domain.model;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Achievement {
    private final UUID id;
    private final UUID userId;
    private final String name;
    private int completionPercentage;
    private final boolean isDefault;

    // ...existing code...
    public Achievement(UUID id, UUID userId, String name, int completionPercentage, boolean isDefault) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.name = Objects.requireNonNull(name);
        setCompletionPercentage(completionPercentage);
        this.isDefault = isDefault;
    }

    public static Achievement createNew(UUID userId, String name, boolean isDefault) {
        return new Achievement(UUID.randomUUID(), userId, name, 0, isDefault);
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = Math.max(0, Math.min(100, completionPercentage));
    }
}