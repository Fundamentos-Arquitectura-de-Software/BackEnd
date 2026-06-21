package com.acme.alertsservice.domain.repository;

import com.acme.alertsservice.domain.model.Alert;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    List<Alert> findByUserId(Long userId);
    Optional<Alert> findByIdAndUserId(Long id, Long userId);
    Alert save(Alert alert);
}
