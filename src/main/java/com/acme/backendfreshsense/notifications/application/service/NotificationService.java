package com.acme.backendfreshsense.notifications.application.service;

import com.acme.backendfreshsense.notifications.domain.model.ChannelType;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPayload;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPreference;
import com.acme.backendfreshsense.notifications.domain.repository.InAppNotificationRepository;
import com.acme.backendfreshsense.notifications.domain.repository.NotificationPreferenceRepository;
import com.acme.backendfreshsense.shared.domain.event.ExpirationAlertRaisedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Orquesta el despacho de notificaciones a través de INotificationChannel.
 * Escucha ExpirationAlertRaisedEvent para notificar automáticamente.
 */
@Transactional
public class NotificationService {

    private static final ZoneId LIMA = ZoneId.of("America/Lima");

    private final List<INotificationChannel> channels;
    private final PreferenceFilter preferenceFilter;
    private final NotificationPreferenceRepository preferenceRepository;
    private final InAppNotificationRepository inAppNotificationRepository;

    public NotificationService(List<INotificationChannel> channels,
                               PreferenceFilter preferenceFilter,
                               NotificationPreferenceRepository preferenceRepository,
                               InAppNotificationRepository inAppNotificationRepository) {
        this.channels = channels;
        this.preferenceFilter = preferenceFilter;
        this.preferenceRepository = preferenceRepository;
        this.inAppNotificationRepository = inAppNotificationRepository;
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

    /**
     * Observer — escucha ExpirationAlertRaisedEvent (lo publica el job de frescura).
     * Dedup diario: si hoy ya se envió una notificación idéntica al usuario, se omite —
     * así el job puede correr cada pocos minutos sin llenar la bandeja ni la BD.
     */
    @EventListener
    public void onExpirationAlertRaised(ExpirationAlertRaisedEvent event) {
        String message = "Tu producto '" + event.getProductName() + "' tiene estado: " + event.getSeverity();
        if (alreadyNotifiedToday(event.getUserId(), message)) return;

        NotificationPayload payload = new NotificationPayload(
                event.getUserId(),
                "Alerta de vencimiento",
                message,
                "EXPIRATION",
                null // usa todos los canales activos del usuario
        );
        send(payload);
    }

    private boolean alreadyNotifiedToday(Long userId, String message) {
        Instant startOfToday = LocalDate.now(LIMA).atStartOfDay(LIMA).toInstant();
        return inAppNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .anyMatch(n -> message.equals(n.getMessage())
                        && n.getCreatedAt() != null
                        && !n.getCreatedAt().isBefore(startOfToday));
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
