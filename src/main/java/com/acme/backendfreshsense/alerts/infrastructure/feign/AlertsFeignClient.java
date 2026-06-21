package com.acme.backendfreshsense.alerts.infrastructure.feign;

import com.acme.backendfreshsense.alerts.application.dto.AlertRequest;
import com.acme.backendfreshsense.alerts.application.dto.AlertResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cliente Feign que delega las llamadas al microservicio alerts-service.
 * Eureka resuelve "alerts-service" al host/puerto real en tiempo de ejecución.
 */
@FeignClient(name = "alerts-service")
public interface AlertsFeignClient {

    @GetMapping("/api/alerts")
    List<AlertResponse> getAll(@RequestParam("userId") Long userId);

    @PostMapping("/api/alerts")
    AlertResponse create(@RequestParam("userId") Long userId, @RequestBody AlertRequest request);

    @PutMapping("/api/alerts/{id}")
    AlertResponse update(@RequestParam("userId") Long userId, @PathVariable("id") Long id, @RequestBody AlertRequest request);
}
