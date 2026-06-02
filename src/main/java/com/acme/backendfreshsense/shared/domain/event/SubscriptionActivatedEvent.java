package com.acme.backendfreshsense.shared.domain.event;

public class SubscriptionActivatedEvent extends DomainEvent {

    private final Long userId;
    private final String planName;

    public SubscriptionActivatedEvent(Long userId, String planName) {
        super();
        this.userId = userId;
        this.planName = planName;
    }

    public Long getUserId()   { return userId; }
    public String getPlanName() { return planName; }
}
