package com.acme.backendfreshsense.shared.domain.event;

public class SubscriptionCancelledEvent extends DomainEvent {

    private final Long userId;

    public SubscriptionCancelledEvent(Long userId) {
        super();
        this.userId = userId;
    }

    public Long getUserId() { return userId; }
}
