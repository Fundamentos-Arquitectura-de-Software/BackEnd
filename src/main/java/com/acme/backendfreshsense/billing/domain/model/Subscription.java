package com.acme.backendfreshsense.billing.domain.model;

import java.time.LocalDate;

public class Subscription {

    private Long id;
    private Long userId;
    private Long planId;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String paymentReference; // cifrado AES-256 en BD

    public Subscription() {}

    public Subscription(Long userId, Long planId, LocalDate startDate, LocalDate endDate, String paymentReference) {
        this.userId = userId;
        this.planId = planId;
        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentReference = paymentReference;
    }

    public Long getId()                   { return id; }
    public void setId(Long id)            { this.id = id; }
    public Long getUserId()               { return userId; }
    public Long getPlanId()               { return planId; }
    public SubscriptionStatus getStatus() { return status; }
    public void setStatus(SubscriptionStatus status) { this.status = status; }
    public LocalDate getStartDate()       { return startDate; }
    public LocalDate getEndDate()         { return endDate; }
    public String getPaymentReference()   { return paymentReference; }
}
