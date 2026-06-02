package com.acme.backendfreshsense.billing.infrastructure.persistence.jpa;

import com.acme.backendfreshsense.billing.domain.model.PlanType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "plans")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlanType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMonthly;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAnnual;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> features;

    protected PlanEntity() {}

    public PlanEntity(String name, PlanType type, BigDecimal priceMonthly, BigDecimal priceAnnual, List<String> features) {
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
