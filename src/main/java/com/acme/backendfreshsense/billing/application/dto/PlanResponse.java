package com.acme.backendfreshsense.billing.application.dto;

import com.acme.backendfreshsense.billing.domain.model.PlanType;

import java.math.BigDecimal;
import java.util.List;

public class PlanResponse {

    private Long id;
    private String name;
    private PlanType type;
    private BigDecimal priceMonthly;
    private BigDecimal priceAnnual;
    private List<String> features;

    public PlanResponse(Long id, String name, PlanType type, BigDecimal priceMonthly, BigDecimal priceAnnual, List<String> features) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.priceMonthly = priceMonthly;
        this.priceAnnual = priceAnnual;
        this.features = features;
    }

    public Long getId()               { return id; }
    public String getName()           { return name; }
    public PlanType getType()         { return type; }
    public BigDecimal getPriceMonthly() { return priceMonthly; }
    public BigDecimal getPriceAnnual()  { return priceAnnual; }
    public List<String> getFeatures() { return features; }
}
