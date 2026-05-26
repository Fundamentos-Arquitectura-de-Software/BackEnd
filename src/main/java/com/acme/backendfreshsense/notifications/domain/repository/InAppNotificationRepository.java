package com.acme.backendfreshsense.notifications.domain.repository;

import com.acme.backendfreshsense.notifications.domain.model.InAppNotification;

import java.util.List;

public interface InAppNotificationRepository {
    InAppNotification save(InAppNotification notification);
    List<InAppNotification> findByUserIdOrderByCreatedAtDesc(Long userId);
    void markAsRead(Long notificationId);
}
