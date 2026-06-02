package com.acme.backendfreshsense.billing.infrastructure.persistence.jpa;

import com.acme.backendfreshsense.billing.domain.model.SubscriptionStatus;
import com.acme.backendfreshsense.shared.infrastructure.crypto.AES256Converter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    /** Referencia de pago cifrada con AES-256-GCM. */
    @Convert(converter = AES256Converter.class)
    @Column(length = 512)
    private String paymentReference;

    protected SubscriptionEntity() {}

    public SubscriptionEntity(Long userId, Long planId, LocalDate startDate, LocalDate endDate, String paymentReference) {
        this.userId = userId;
        this.planId = planId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentReference = paymentReference;
    }

    public Long getId()                   { return id; }
    public Long getUserId()               { return userId; }
    public Long getPlanId()               { return planId; }
    public SubscriptionStatus getStatus() { return status; }
    public void setStatus(SubscriptionStatus s) { this.status = s; }
    public LocalDate getStartDate()       { return startDate; }
    public LocalDate getEndDate()         { return endDate; }
    public String getPaymentReference()   { return paymentReference; }
}
