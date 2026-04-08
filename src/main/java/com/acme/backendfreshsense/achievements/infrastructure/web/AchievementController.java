package com.acme.backendfreshsense.achievements.infrastructure.web;

import com.acme.backendfreshsense.achievements.application.dto.AchievementDto;
import com.acme.backendfreshsense.achievements.application.dto.UpdateAchievementRequest;
import com.acme.backendfreshsense.achievements.application.service.AchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/achievements")
public class AchievementController {

    private final AchievementService service;

    public AchievementController(AchievementService service) {
        this.service = service;
    }

    @PostMapping("/init")
    public ResponseEntity<Void> initDefaults(@PathVariable UUID userId) {
        service.ensureDefaultAchievements(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<AchievementDto> list(@PathVariable UUID userId) {
        return service.listByUser(userId);
    }

    @PatchMapping("/{achievementId}")
    public AchievementDto update(
            @PathVariable UUID userId,
            @PathVariable UUID achievementId,
            @RequestBody UpdateAchievementRequest req) {
        return service.updateProgress(userId, achievementId, req.completionPercentage());
    }

    @PatchMapping("/by-name/{name}")
    public AchievementDto updateByName(
            @PathVariable UUID userId,
            @PathVariable String name,
            @RequestBody UpdateAchievementRequest req) {
        return service.updateByName(userId, name, req.completionPercentage());
    }
}