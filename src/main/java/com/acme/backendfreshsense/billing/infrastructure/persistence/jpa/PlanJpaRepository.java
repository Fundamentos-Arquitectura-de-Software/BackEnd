package com.acme.backendfreshsense.billing.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanJpaRepository extends JpaRepository<PlanEntity, Long> {
}
