package com.acme.backendfreshsense.alerts.infrastructure.web;

import com.acme.backendfreshsense.alerts.application.dto.AlertRequest;
import com.acme.backendfreshsense.alerts.application.dto.AlertResponse;
import com.acme.backendfreshsense.alerts.infrastructure.feign.AlertsFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertsController {

    private final AlertsFeignClient alertsFeignClient;

    @GetMapping
    public List<AlertResponse> getAll() {
        return alertsFeignClient.getAll();
    }

    @PostMapping
    public AlertResponse create(@RequestBody AlertRequest request) {
        return alertsFeignClient.create(request);
    }

    @PutMapping("/{id}")
    public AlertResponse update(@PathVariable Long id, @RequestBody AlertRequest request) {
        return alertsFeignClient.update(id, request);
    }
}
