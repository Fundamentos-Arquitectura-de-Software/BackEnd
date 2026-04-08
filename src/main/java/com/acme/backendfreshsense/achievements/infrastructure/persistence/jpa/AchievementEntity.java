package com.acme.backendfreshsense.achievements.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "achievements", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name"}))
public class AchievementEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    private UUID userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "completion_percentage", nullable = false)
    private int completionPercentage;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    // ...existing code...
    public AchievementEntity() {}

    public AchievementEntity(UUID id, UUID userId, String name, int completionPercentage, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.completionPercentage = completionPercentage;
        this.isDefault = isDefault;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getName() { return name; }
    public int getCompletionPercentage() { return completionPercentage; }
    public boolean isDefault() { return isDefault; }

    public void setId(UUID id) { this.id = id; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setCompletionPercentage(int completionPercentage) { this.completionPercentage = completionPercentage; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
}