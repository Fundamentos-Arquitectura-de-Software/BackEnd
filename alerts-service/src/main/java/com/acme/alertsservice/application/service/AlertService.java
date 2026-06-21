package com.acme.alertsservice.application.service;

import com.acme.alertsservice.application.dto.AlertRequest;
import com.acme.alertsservice.application.dto.AlertResponse;
import com.acme.alertsservice.domain.model.Alert;
import com.acme.alertsservice.domain.repository.AlertRepository;
import com.acme.alertsservice.infrastructure.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class AlertService {

    private final AlertRepository repo;

    public AlertService(AlertRepository repo) {
        this.repo = repo;
    }

    public List<AlertResponse> getAll(Long userId) {
        return repo.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public AlertResponse create(Long userId, AlertRequest request) {
        Alert alert = new Alert();
        alert.setUserId(userId);
        alert.setTitle(request.title());
        alert.setMessage(request.message());
        alert.setSeverity(request.severity());
        alert.setSource(request.source());
        alert.setState(request.state());
        alert.setTimeAgo(request.timeAgo());
        return toResponse(repo.save(alert));
    }

    public AlertResponse update(Long userId, Long id, AlertRequest request) {
        Alert existing = repo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id " + id));

        if (request.title()    != null) existing.setTitle(request.title());
        if (request.message()  != null) existing.setMessage(request.message());
        if (request.severity() != null) existing.setSeverity(request.severity());
        if (request.source()   != null) existing.setSource(request.source());
        if (request.timeAgo()  != null) existing.setTimeAgo(request.timeAgo());
        if (request.state()    != null) existing.setState(request.state());

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
