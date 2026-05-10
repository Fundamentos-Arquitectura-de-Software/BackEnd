package com.acme.backendfreshsense.alerts.application.internal;

import com.acme.backendfreshsense.alerts.domain.model.aggregates.Alert;
import com.acme.backendfreshsense.alerts.infrastructure.persistence.jpa.AlertRepository;
import com.acme.backendfreshsense.shared.infrastructure.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
        Alert existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id " + id));

        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getMessage() != null) existing.setMessage(updated.getMessage());
        if (updated.getSeverity() != null) existing.setSeverity(updated.getSeverity());
        if (updated.getSource() != null) existing.setSource(updated.getSource());
        if (updated.getTimeAgo() != null) existing.setTimeAgo(updated.getTimeAgo());
        if (updated.getState() != null) existing.setState(updated.getState());

        return repo.save(existing);
    }
}
