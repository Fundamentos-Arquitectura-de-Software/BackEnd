package com.acme.backendfreshsense.alerts.interfaces.rest;
import com.acme.backendfreshsense.alerts.application.internal.AlertService;
import com.acme.backendfreshsense.alerts.domain.model.aggregates.Alert;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
public class AlertsController {

    private final AlertService service;

    public AlertsController(AlertService service) {
        this.service = service;
    }

    @GetMapping
    public List<Alert> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Alert create(@RequestBody Alert alert) {
        return service.create(alert);
    }

    @PutMapping("/{id}")
    public Alert update(
            @PathVariable Long id,
            @RequestBody Alert alert
    ) {
        return service.update(id, alert);
    }
}
