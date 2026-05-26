package com.acme.backendfreshsense.notifications.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.notifications.domain.model.NotificationPreference;
import com.acme.backendfreshsense.notifications.domain.repository.NotificationPreferenceRepository;
import com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa.NotificationPreferenceEntity;
import com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa.NotificationPreferenceJpaRepository;

import java.util.Optional;

public class NotificationPreferenceRepositoryAdapter implements NotificationPreferenceRepository {

    private final NotificationPreferenceJpaRepository jpa;

    public NotificationPreferenceRepositoryAdapter(NotificationPreferenceJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public NotificationPreference save(NotificationPreference pref) {
        NotificationPreferenceEntity entity = jpa.findByUserId(pref.getUserId())
                .orElse(new NotificationPreferenceEntity(pref.getUserId()));
        entity.setInAppEnabled(pref.isInAppEnabled());
        entity.setEmailEnabled(pref.isEmailEnabled());
        entity.setPushEnabled(pref.isPushEnabled());
        entity.setQuietStart(pref.getQuietStart());
        entity.setQuietEnd(pref.getQuietEnd());
        NotificationPreferenceEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<NotificationPreference> findByUserId(Long userId) {
        return jpa.findByUserId(userId).map(this::toDomain);
    }

    private NotificationPreference toDomain(NotificationPreferenceEntity e) {
        NotificationPreference p = new NotificationPreference(e.getUserId());
        p.setId(e.getId());
        p.setInAppEnabled(e.isInAppEnabled());
        p.setEmailEnabled(e.isEmailEnabled());
        p.setPushEnabled(e.isPushEnabled());
        p.setQuietStart(e.getQuietStart());
        p.setQuietEnd(e.getQuietEnd());
        return p;
    }
}
