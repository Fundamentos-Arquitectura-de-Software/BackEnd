package com.acme.backendfreshsense.billing.application.service;

import com.acme.backendfreshsense.billing.application.dto.PlanResponse;
import com.acme.backendfreshsense.billing.application.dto.SubscribeRequest;
import com.acme.backendfreshsense.billing.application.dto.SubscriptionResponse;
import com.acme.backendfreshsense.billing.domain.model.Plan;
import com.acme.backendfreshsense.billing.domain.model.Subscription;
import com.acme.backendfreshsense.billing.domain.repository.PlanRepository;
import com.acme.backendfreshsense.billing.domain.repository.SubscriptionRepository;
import com.acme.backendfreshsense.shared.domain.event.SubscriptionActivatedEvent;
import com.acme.backendfreshsense.shared.domain.event.SubscriptionCancelledEvent;
import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public class BillingService {

    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BillingService(PlanRepository planRepository,
                          SubscriptionRepository subscriptionRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<PlanResponse> getPlans() {
        return planRepository.findAll().stream().map(this::toPlanResponse).toList();
    }

    public SubscriptionResponse subscribe(Long userId, SubscribeRequest request) {
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado"));

        subscriptionRepository.cancelByUserId(userId);

        LocalDate now = LocalDate.now();
        Subscription subscription = new Subscription(
                userId,
                plan.getId(),
                now,
                now.plusMonths(1),
                request.getPaymentReference()
        );

        Subscription saved = subscriptionRepository.save(subscription);
        eventPublisher.publishEvent(new SubscriptionActivatedEvent(userId, plan.getName()));

        return toSubscriptionResponse(saved);
    }

    public void cancel(Long userId) {
        subscriptionRepository.cancelByUserId(userId);
        eventPublisher.publishEvent(new SubscriptionCancelledEvent(userId));
    }

    public SubscriptionResponse getActiveSubscription(Long userId) {
        return subscriptionRepository.findActiveByUserId(userId)
                .map(this::toSubscriptionResponse)
                .orElseThrow(() -> new ResourceNotFoundException("No hay suscripción activa"));
    }

    private PlanResponse toPlanResponse(Plan p) {
        return new PlanResponse(p.getId(), p.getName(), p.getType(), p.getPriceMonthly(), p.getPriceAnnual(), p.getFeatures());
    }

    private SubscriptionResponse toSubscriptionResponse(Subscription s) {
        return new SubscriptionResponse(s.getId(), s.getUserId(), s.getPlanId(), s.getStatus(), s.getStartDate(), s.getEndDate());
    }
}
