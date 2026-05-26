package com.acme.backendfreshsense.notifications.application.service;

import com.acme.backendfreshsense.notifications.domain.model.ChannelType;
import com.acme.backendfreshsense.notifications.domain.model.InAppNotification;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPayload;
import com.acme.backendfreshsense.notifications.domain.repository.InAppNotificationRepository;

public class InAppChannel implements INotificationChannel {

    private final InAppNotificationRepository repository;

    public InAppChannel(InAppNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.IN_APP;
    }

    @Override
    public void send(NotificationPayload payload) {
        InAppNotification notification = new InAppNotification(
                payload.getUserId(),
                payload.getTitle(),
                payload.getMessage(),
                payload.getAlertType()
        );
        repository.save(notification);
    }
}
