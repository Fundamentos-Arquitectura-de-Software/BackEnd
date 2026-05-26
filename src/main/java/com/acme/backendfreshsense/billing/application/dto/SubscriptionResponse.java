package com.acme.backendfreshsense.billing.application.dto;

import com.acme.backendfreshsense.billing.domain.model.SubscriptionStatus;

import java.time.LocalDate;

public class SubscriptionResponse {

    private Long id;
    private Long userId;
    private Long planId;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    public SubscriptionResponse(Long id, Long userId, Long planId, SubscriptionStatus status,
                                 LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId()                   { return id; }
    public Long getUserId()               { return userId; }
    public Long getPlanId()               { return planId; }
    public SubscriptionStatus getStatus() { return status; }
    public LocalDate getStartDate()       { return startDate; }
    public LocalDate getEndDate()         { return endDate; }
}
