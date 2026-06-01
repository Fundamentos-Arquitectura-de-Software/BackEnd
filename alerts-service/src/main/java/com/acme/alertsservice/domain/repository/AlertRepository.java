package com.acme.alertsservice.domain.repository;

import com.acme.alertsservice.domain.model.Alert;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    List<Alert> findAll();
    Optional<Alert> findById(Long id);
    Alert save(Alert alert);
}
