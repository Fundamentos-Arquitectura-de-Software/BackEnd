package com.acme.backendfreshsense.notifications.infrastructure.persistence.config;

import com.acme.backendfreshsense.notifications.application.service.*;
import com.acme.backendfreshsense.notifications.domain.repository.InAppNotificationRepository;
import com.acme.backendfreshsense.notifications.domain.repository.NotificationPreferenceRepository;
import com.acme.backendfreshsense.notifications.infrastructure.persistence.adapter.InAppNotificationRepositoryAdapter;
import com.acme.backendfreshsense.notifications.infrastructure.persistence.adapter.NotificationPreferenceRepositoryAdapter;
import com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa.InAppNotificationJpaRepository;
import com.acme.backendfreshsense.notifications.infrastructure.persistence.jpa.NotificationPreferenceJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class NotificationsPersistenceConfig {

    @Bean
    public NotificationPreferenceRepository notificationPreferenceRepository(
            NotificationPreferenceJpaRepository jpa) {
        return new NotificationPreferenceRepositoryAdapter(jpa);
    }

    @Bean
    public InAppNotificationRepository inAppNotificationRepository(
            InAppNotificationJpaRepository jpa) {
        return new InAppNotificationRepositoryAdapter(jpa);
    }

    @Bean
    public InAppChannel inAppChannel(InAppNotificationRepository repository) {
        return new InAppChannel(repository);
    }

    @Bean
    public EmailChannel emailChannel() {
        return new EmailChannel();
    }

    @Bean
    public PreferenceFilter preferenceFilter(NotificationPreferenceRepository preferenceRepository) {
        return new PreferenceFilter(preferenceRepository);
    }

    @Bean
    public NotificationService notificationService(InAppChannel inAppChannel,
                                                    EmailChannel emailChannel,
                                                    PreferenceFilter preferenceFilter,
                                                    NotificationPreferenceRepository preferenceRepository,
                                                    InAppNotificationRepository inAppNotificationRepository) {
        List<INotificationChannel> channels = List.of(inAppChannel, emailChannel);
        return new NotificationService(channels, preferenceFilter, preferenceRepository, inAppNotificationRepository);
    }
}
