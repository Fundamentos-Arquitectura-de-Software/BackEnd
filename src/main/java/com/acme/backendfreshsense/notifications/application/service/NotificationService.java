package com.acme.backendfreshsense.notifications.application.service;

import com.acme.backendfreshsense.notifications.domain.model.ChannelType;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPayload;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPreference;
import com.acme.backendfreshsense.notifications.domain.repository.NotificationPreferenceRepository;
import com.acme.backendfreshsense.shared.domain.event.ExpirationAlertRaisedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Orquesta el despacho de notificaciones a través de INotificationChannel.
 * Escucha ExpirationAlertRaisedEvent para notificar automáticamente.
 */
@Transactional
public class NotificationService {

    private final List<INotificationChannel> channels;
    private final PreferenceFilter preferenceFilter;
    private final NotificationPreferenceRepository preferenceRepository;

    public NotificationService(List<INotificationChannel> channels,
                               PreferenceFilter preferenceFilter,
                               NotificationPreferenceRepository preferenceRepository) {
        this.channels = channels;
        this.preferenceFilter = preferenceFilter;
        this.preferenceRepository = preferenceRepository;
    }

    /** TS32 — Enviar notificación a un usuario por canal específico o todos los activos. */
    public void send(NotificationPayload payload) {
        List<INotificationChannel> activeChannels;

        if (payload.getChannelType() != null && payload.getChannelType() != ChannelType.IN_APP) {
            // Canal explícito solicitado
            activeChannels = channels.stream()
                    .filter(c -> c.getChannelType() == payload.getChannelType())
                    .toList();
        } else {
            // Aplicar PreferenceFilter (DD-10)
            activeChannels = preferenceFilter.filter(payload.getUserId(), channels);
        }

        activeChannels.forEach(channel -> channel.send(payload));
    }

    /** Observer — escucha ExpirationAlertRaisedEvent desde el módulo Alerts. */
    @EventListener
    public void onExpirationAlertRaised(ExpirationAlertRaisedEvent event) {
        NotificationPayload payload = new NotificationPayload(
                event.getUserId(),
                "Alerta de vencimiento",
                "Tu producto '" + event.getProductName() + "' tiene estado: " + event.getSeverity(),
                "EXPIRATION",
                null // usa todos los canales activos del usuario
        );
        send(payload);
    }

    public NotificationPreference getPreferences(Long userId) {
        return preferenceRepository.findByUserId(userId)
                .orElseGet(() -> new NotificationPreference(userId));
    }

    public NotificationPreference updatePreferences(Long userId, NotificationPreference updated) {
        NotificationPreference pref = preferenceRepository.findByUserId(userId)
                .orElseGet(() -> new NotificationPreference(userId));
        pref.setInAppEnabled(updated.isInAppEnabled());
        pref.setEmailEnabled(updated.isEmailEnabled());
        pref.setPushEnabled(updated.isPushEnabled());
        pref.setQuietStart(updated.getQuietStart());
        pref.setQuietEnd(updated.getQuietEnd());
        return preferenceRepository.save(pref);
    }
}
