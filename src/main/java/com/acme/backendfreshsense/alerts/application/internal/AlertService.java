package com.acme.backendfreshsense.alerts.application.internal;

import com.acme.backendfreshsense.alerts.domain.model.aggregates.Alert;
import com.acme.backendfreshsense.alerts.infrastructure.persistence.jpa.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository repo;

    public AlertService(AlertRepository repo) {
        this.repo = repo;
    }

    public List<Alert> getAll() {
        return repo.findAll();
    }

    public Alert create(Alert alert) {
        return repo.save(alert);
    }

    public Alert update(Long id, Alert updated) {
        // 1. Buscar la alerta existente
        Alert existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id " + id));

        // 2. Actualizar solo los campos que vengan no nulos
        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getMessage() != null) existing.setMessage(updated.getMessage());
        if (updated.getSeverity() != null) existing.setSeverity(updated.getSeverity());
        if (updated.getSource() != null) existing.setSource(updated.getSource());
        if (updated.getTimeAgo() != null) existing.setTimeAgo(updated.getTimeAgo());
        if (updated.getState() != null) existing.setState(updated.getState());

        // 3. Guardar
        return repo.save(existing);
    }
}
