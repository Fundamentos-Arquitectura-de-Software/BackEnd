package com.acme.alertsservice.infrastructure.web;

import com.acme.alertsservice.application.dto.AlertRequest;
import com.acme.alertsservice.application.dto.AlertResponse;
import com.acme.alertsservice.application.service.AlertService;
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
        return service.getAll();
    }

    @PostMapping
    public AlertResponse create(@RequestBody AlertRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public AlertResponse update(@PathVariable Long id, @RequestBody AlertRequest request) {
        return service.update(id, request);
    }
}
