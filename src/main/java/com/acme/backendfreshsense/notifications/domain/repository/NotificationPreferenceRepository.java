package com.acme.backendfreshsense.notifications.domain.repository;

import com.acme.backendfreshsense.notifications.domain.model.NotificationPreference;

import java.util.Optional;

public interface NotificationPreferenceRepository {
    NotificationPreference save(NotificationPreference preference);
    Optional<NotificationPreference> findByUserId(Long userId);
}
