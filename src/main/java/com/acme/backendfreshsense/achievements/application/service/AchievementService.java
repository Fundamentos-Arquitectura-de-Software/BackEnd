package com.acme.backendfreshsense.achievements.application.service;

import com.acme.backendfreshsense.achievements.application.dto.AchievementDto;
import com.acme.backendfreshsense.achievements.application.mapper.AchievementMapper;
import com.acme.backendfreshsense.achievements.domain.model.Achievement;
import com.acme.backendfreshsense.achievements.domain.repository.AchievementRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AchievementService {

    private final AchievementRepository repository;

    public AchievementService(AchievementRepository repository) {
        this.repository = repository;
    }

    public void ensureDefaultAchievements(Long userId) {
        if (repository.existsByUserId(userId)) return;
        List<Achievement> defaults = List.of(
                Achievement.createNew(userId, "Primer inicio de sesión", true),
                Achievement.createNew(userId, "Completar perfil", true),
                Achievement.createNew(userId, "Primera acción en la app", true)
        );
        repository.saveAll(defaults);
    }

    public List<AchievementDto> listByUser(Long userId) {
        return repository.findByUserId(userId).stream().map(AchievementMapper::toDto).toList();
    }

    public AchievementDto updateProgress(Long userId, UUID achievementId, int percentage) {
        Achievement a = repository.findById(achievementId)
                .filter(x -> x.getUserId().equals(userId))
                .orElseThrow(() -> new IllegalArgumentException("Achievement no encontrado para el usuario"));
        a.setCompletionPercentage(percentage);
        repository.save(a);
        return AchievementMapper.toDto(a);
    }

    public AchievementDto updateByName(Long userId, String name, int percentage) {
        Achievement a = repository.findByUserIdAndName(userId, name)
                .orElseThrow(() -> new IllegalArgumentException("Achievement no encontrado por nombre"));
        a.setCompletionPercentage(percentage);
        repository.save(a);
        return AchievementMapper.toDto(a);
    }
}
