package com.acme.backendfreshsense.alerts.infrastructure.web;

import com.acme.backendfreshsense.alerts.application.dto.AlertRequest;
import com.acme.backendfreshsense.alerts.application.dto.AlertResponse;
import com.acme.backendfreshsense.alerts.application.service.AlertService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertsController {

    private final AlertService service;

    public AlertsController(AlertService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlertResponse> getAll() {
        return service.getAll(currentUserId());
    }

    @PostMapping
    public AlertResponse create(@RequestBody AlertRequest request) {
        return service.create(request, currentUserId());
    }

    @PutMapping("/{id}")
    public AlertResponse update(@PathVariable Long id, @RequestBody AlertRequest request) {
        return service.update(id, request);
    }

    private Long currentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
