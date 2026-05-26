package com.acme.backendfreshsense.notifications.application.service;

import com.acme.backendfreshsense.notifications.domain.model.ChannelType;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPayload;

/**
 * Contrato de canal de notificación (DD-09).
 * Cada implementación despacha la notificación por un canal distinto
 * sin que NotificationService conozca los detalles de entrega.
 */
public interface INotificationChannel {
    ChannelType getChannelType();
    void send(NotificationPayload payload);
}
