package com.acme.backendfreshsense.billing.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class Plan {

    private Long id;
    private String name;
    private PlanType type;
    private BigDecimal priceMonthly;
    private BigDecimal priceAnnual;
    private List<String> features;

    public Plan() {}

    public Plan(String name, PlanType type, BigDecimal priceMonthly, BigDecimal priceAnnual, List<String> features) {
        this.name = name;
        this.type = type;
        this.priceMonthly = priceMonthly;
        this.priceAnnual = priceAnnual;
        this.features = features;
    }

    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }
    public String getName()           { return name; }
    public PlanType getType()         { return type; }
    public BigDecimal getPriceMonthly() { return priceMonthly; }
    public BigDecimal getPriceAnnual()  { return priceAnnual; }
    public List<String> getFeatures() { return features; }
}
