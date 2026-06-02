package com.acme.backendfreshsense.notifications.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.notifications.domain.model.InAppNotification;
import com.acme.backendfreshsense.notifications.domain.repository.InAppNotificationRepository;
import com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa.InAppNotificationEntity;
import com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa.InAppNotificationJpaRepository;

import java.util.List;

public class InAppNotificationRepositoryAdapter implements InAppNotificationRepository {

    private final InAppNotificationJpaRepository jpa;

    public InAppNotificationRepositoryAdapter(InAppNotificationJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public InAppNotification save(InAppNotification n) {
        InAppNotificationEntity entity = new InAppNotificationEntity(n.getUserId(), n.getTitle(), n.getMessage(), n.getAlertType());
        InAppNotificationEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<InAppNotification> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return jpa.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        jpa.markAsRead(notificationId);
    }

    private InAppNotification toDomain(InAppNotificationEntity e) {
        InAppNotification n = new InAppNotification(e.getUserId(), e.getTitle(), e.getMessage(), e.getAlertType());
        n.setId(e.getId());
        n.setRead(e.isRead());
        return n;
    }
}
