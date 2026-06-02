package com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationPreferenceJpaRepository extends JpaRepository<NotificationPreferenceEntity, Long> {
    Optional<NotificationPreferenceEntity> findByUserId(Long userId);
}
