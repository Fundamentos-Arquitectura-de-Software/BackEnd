package com.acme.backendfreshsense.alerts.domain.repository;

import com.acme.backendfreshsense.alerts.domain.model.aggregates.Alert;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    List<Alert> findByUserId(Long userId);
    Optional<Alert> findById(Long id);
    Alert save(Alert alert);
}
