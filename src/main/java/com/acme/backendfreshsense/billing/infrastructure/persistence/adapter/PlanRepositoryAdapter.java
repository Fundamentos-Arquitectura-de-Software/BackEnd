package com.acme.backendfreshsense.billing.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.billing.domain.model.Plan;
import com.acme.backendfreshsense.billing.domain.repository.PlanRepository;
import com.acme.backendfreshsense.billing.infrastructure.persistence.jpa.PlanEntity;
import com.acme.backendfreshsense.billing.infrastructure.persistence.jpa.PlanJpaRepository;

import java.util.List;
import java.util.Optional;

public class PlanRepositoryAdapter implements PlanRepository {

    private final PlanJpaRepository jpa;

    public PlanRepositoryAdapter(PlanJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Plan> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Plan> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    public Plan save(Plan plan) {
        PlanEntity saved = jpa.save(toEntity(plan));
        return toDomain(saved);
    }

    private Plan toDomain(PlanEntity e) {
        Plan p = new Plan(e.getName(), e.getType(), e.getPriceMonthly(), e.getPriceAnnual(), e.getFeatures());
        p.setId(e.getId());
        return p;
    }

    private PlanEntity toEntity(Plan p) {
        return new PlanEntity(p.getName(), p.getType(), p.getPriceMonthly(), p.getPriceAnnual(), p.getFeatures());
    }
}
