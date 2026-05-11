package com.acme.backendfreshsense.alerts.application.internal;

import com.acme.backendfreshsense.alerts.domain.model.aggregates.Alert;
import com.acme.backendfreshsense.alerts.infrastructure.persistence.jpa.AlertRepository;
import com.acme.backendfreshsense.alerts.interfaces.rest.resources.AlertRequest;
import com.acme.backendfreshsense.alerts.interfaces.rest.resources.AlertResponse;
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

    public List<AlertResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public AlertResponse create(AlertRequest request) {
        Alert alert = new Alert();
        alert.setTitle(request.title());
        alert.setMessage(request.message());
        alert.setSeverity(request.severity());
        alert.setSource(request.source());
        alert.setState(request.state());
        alert.setTimeAgo(request.timeAgo());
        return toResponse(repo.save(alert));
    }

    public AlertResponse update(Long id, AlertRequest request) {
        Alert existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id " + id));

        if (request.title() != null) existing.setTitle(request.title());
        if (request.message() != null) existing.setMessage(request.message());
        if (request.severity() != null) existing.setSeverity(request.severity());
        if (request.source() != null) existing.setSource(request.source());
        if (request.timeAgo() != null) existing.setTimeAgo(request.timeAgo());
        if (request.state() != null) existing.setState(request.state());

        return toResponse(repo.save(existing));
    }

    private AlertResponse toResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getTitle(),
                alert.getMessage(),
                alert.getSeverity(),
                alert.getSource(),
                alert.getState(),
                alert.getTimeAgo()
        );
    }
}
