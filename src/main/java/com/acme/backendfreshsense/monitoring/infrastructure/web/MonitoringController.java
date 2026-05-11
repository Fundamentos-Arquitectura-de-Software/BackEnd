package com.acme.backendfreshsense.monitoring.infrastructure.web;

import com.acme.backendfreshsense.monitoring.application.dto.MonitoringReadingDto;
import com.acme.backendfreshsense.monitoring.application.dto.MonitoringReadingRequest;
import com.acme.backendfreshsense.monitoring.application.service.MonitoringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    @PostMapping
    public ResponseEntity<MonitoringReadingDto> create(@Valid @RequestBody MonitoringReadingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(monitoringService.save(request));
    }

    @GetMapping("/latest")
    public ResponseEntity<MonitoringReadingDto> getLatest() {
        return monitoringService.getLatest()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping
    public List<MonitoringReadingDto> getAll() {
        return monitoringService.getAll();
    }
}
