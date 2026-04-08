package com.acme.backendfreshsense.alerts.infrastructure.persistence.jpa;

import com.acme.backendfreshsense.alerts.domain.model.aggregates.Alert;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}


