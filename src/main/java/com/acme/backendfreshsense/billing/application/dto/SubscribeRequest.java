package com.acme.backendfreshsense.billing.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SubscribeRequest {

    @NotNull
    private Long planId;

    @Size(max = 255)
    private String paymentReference;

    public SubscribeRequest() {}

    public Long getPlanId()             { return planId; }
    public String getPaymentReference() { return paymentReference; }
}
