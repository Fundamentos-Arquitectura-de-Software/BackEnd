package com.acme.backendfreshsense.notifications.application.service;

import com.acme.backendfreshsense.notifications.domain.model.ChannelType;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Canal de email — stub.
 * Cuando se integre un SMTP relay (ej. SendGrid, SES), reemplazar el log por el envío real.
 */
public class EmailChannel implements INotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(EmailChannel.class);

    @Override
    public ChannelType getChannelType() {
        return ChannelType.EMAIL;
    }

    @Override
    public void send(NotificationPayload payload) {
        // TODO: integrar SMTP relay (SendGrid / SES)
        log.info("[EMAIL STUB] To userId={} | {} | {}", payload.getUserId(), payload.getTitle(), payload.getMessage());
    }
}
