package com.acme.backendfreshsense.accounts.application.service;

import com.acme.backendfreshsense.accounts.domain.model.Role;
import com.acme.backendfreshsense.accounts.domain.repository.UserRepository;
import com.acme.backendfreshsense.shared.domain.event.SubscriptionActivatedEvent;
import com.acme.backendfreshsense.shared.domain.event.SubscriptionCancelledEvent;
import com.acme.backendfreshsense.shared.domain.event.UserRoleChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RoleService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RoleService(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void onSubscriptionActivated(SubscriptionActivatedEvent event) {
        userRepository.findById(event.getUserId()).ifPresent(user -> {
            String previousRole = user.getRole().name();
            userRepository.updateRole(event.getUserId(), Role.USER_PREMIUM);
            eventPublisher.publishEvent(new UserRoleChangedEvent(event.getUserId(), previousRole, Role.USER_PREMIUM.name()));
        });
    }

    @EventListener
    public void onSubscriptionCancelled(SubscriptionCancelledEvent event) {
        userRepository.findById(event.getUserId()).ifPresent(user -> {
            String previousRole = user.getRole().name();
            userRepository.updateRole(event.getUserId(), Role.USER_STANDARD);
            eventPublisher.publishEvent(new UserRoleChangedEvent(event.getUserId(), previousRole, Role.USER_STANDARD.name()));
        });
    }
}
