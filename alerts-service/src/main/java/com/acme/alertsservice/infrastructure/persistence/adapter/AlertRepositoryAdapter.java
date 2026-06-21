package com.acme.alertsservice.infrastructure.persistence.adapter;

import com.acme.alertsservice.domain.model.Alert;
import com.acme.alertsservice.domain.repository.AlertRepository;
import com.acme.alertsservice.infrastructure.persistence.jpa.AlertEntity;
import com.acme.alertsservice.infrastructure.persistence.jpa.AlertJpaRepository;

import java.util.List;
import java.util.Optional;

public class AlertRepositoryAdapter implements AlertRepository {

    private final AlertJpaRepository jpa;

    public AlertRepositoryAdapter(AlertJpaRepository jpa) {
        this.jpa = jpa;
    }

    private static Alert toDomain(AlertEntity e) {
        Alert a = new Alert();
        a.setId(e.getId());
        a.setUserId(e.getUserId());
        a.setTitle(e.getTitle());
        a.setMessage(e.getMessage());
        a.setSeverity(e.getSeverity());
        a.setSource(e.getSource());
        a.setState(e.getState());
        a.setTimeAgo(e.getTimeAgo());
        return a;
    }

    private static AlertEntity toEntity(Alert a) {
        AlertEntity e = new AlertEntity();
        e.setId(a.getId());
        e.setUserId(a.getUserId());
        e.setTitle(a.getTitle());
        e.setMessage(a.getMessage());
        e.setSeverity(a.getSeverity());
        e.setSource(a.getSource());
        e.setState(a.getState());
        e.setTimeAgo(a.getTimeAgo());
        return e;
    }

    @Override
    public List<Alert> findByUserId(Long userId) {
        return jpa.findByUserId(userId).stream().map(AlertRepositoryAdapter::toDomain).toList();
    }

    @Override
    public Optional<Alert> findByIdAndUserId(Long id, Long userId) {
        return jpa.findByIdAndUserId(id, userId).map(AlertRepositoryAdapter::toDomain);
    }

    @Override
    public Alert save(Alert alert) {
        return toDomain(jpa.save(toEntity(alert)));
    }
}
