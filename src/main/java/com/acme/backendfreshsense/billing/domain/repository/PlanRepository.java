package com.acme.backendfreshsense.billing.domain.repository;

import com.acme.backendfreshsense.billing.domain.model.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {
    List<Plan> findAll();
    Optional<Plan> findById(Long id);
    Plan save(Plan plan);
}
