package com.acme.backendfreshsense.billing.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.billing.domain.model.Subscription;
import com.acme.backendfreshsense.billing.domain.model.SubscriptionStatus;
import com.acme.backendfreshsense.billing.domain.repository.SubscriptionRepository;
import com.acme.backendfreshsense.billing.infrastructure.persistence.jpa.SubscriptionEntity;
import com.acme.backendfreshsense.billing.infrastructure.persistence.jpa.SubscriptionJpaRepository;

import java.util.Optional;

public class SubscriptionRepositoryAdapter implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpa;

    public SubscriptionRepositoryAdapter(SubscriptionJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity saved = jpa.save(toEntity(subscription));
        return toDomain(saved);
    }

    @Override
    public Optional<Subscription> findActiveByUserId(Long userId) {
        return jpa.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE).map(this::toDomain);
    }

    @Override
    public void cancelByUserId(Long userId) {
        jpa.cancelActiveByUserId(userId);
    }

    private Subscription toDomain(SubscriptionEntity e) {
        Subscription s = new Subscription(e.getUserId(), e.getPlanId(), e.getStartDate(), e.getEndDate(), e.getPaymentReference());
        s.setId(e.getId());
        s.setStatus(e.getStatus());
        return s;
    }

    private SubscriptionEntity toEntity(Subscription s) {
        return new SubscriptionEntity(s.getUserId(), s.getPlanId(), s.getStartDate(), s.getEndDate(), s.getPaymentReference());
    }
}
