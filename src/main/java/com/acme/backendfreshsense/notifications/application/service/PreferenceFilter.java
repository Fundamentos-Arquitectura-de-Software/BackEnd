package com.acme.backendfreshsense.notifications.application.service;

import com.acme.backendfreshsense.notifications.domain.model.ChannelType;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPreference;
import com.acme.backendfreshsense.notifications.domain.repository.NotificationPreferenceRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtra los canales activos según las preferencias del usuario (DD-10).
 * Garantiza que solo se envíen notificaciones al canal y horario configurado.
 */
public class PreferenceFilter {

    private final NotificationPreferenceRepository preferenceRepository;

    public PreferenceFilter(NotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public List<INotificationChannel> filter(Long userId, List<INotificationChannel> allChannels) {
        NotificationPreference pref = preferenceRepository.findByUserId(userId)
                .orElse(defaultPreference(userId));

        if (isInQuietHours(pref)) {
            return List.of();
        }

        return allChannels.stream()
                .filter(channel -> isChannelEnabled(channel.getChannelType(), pref))
                .collect(Collectors.toList());
    }

    private boolean isChannelEnabled(ChannelType type, NotificationPreference pref) {
        return switch (type) {
            case IN_APP -> pref.isInAppEnabled();
            case EMAIL  -> pref.isEmailEnabled();
            case PUSH   -> pref.isPushEnabled();
            default     -> false;
        };
    }

    private boolean isInQuietHours(NotificationPreference pref) {
        if (pref.getQuietStart() == null || pref.getQuietEnd() == null) return false;
        try {
            LocalTime now   = LocalTime.now();
            LocalTime start = LocalTime.parse(pref.getQuietStart());
            LocalTime end   = LocalTime.parse(pref.getQuietEnd());
            if (start.isBefore(end)) {
                return !now.isBefore(start) && now.isBefore(end);
            } else {
                return !now.isBefore(start) || now.isBefore(end);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private NotificationPreference defaultPreference(Long userId) {
        return new NotificationPreference(userId); // in-app habilitado por defecto
    }
}
