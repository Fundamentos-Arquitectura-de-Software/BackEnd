package com.acme.backendfreshsense.billing.domain.repository;

import com.acme.backendfreshsense.billing.domain.model.Subscription;

import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Optional<Subscription> findActiveByUserId(Long userId);
    void cancelByUserId(Long userId);
}
