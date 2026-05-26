package com.acme.backendfreshsense.reports.infrastructure.web;

import com.acme.backendfreshsense.reports.application.dto.HistoryCreateRequestDto;
import com.acme.backendfreshsense.reports.application.dto.HistoryResponseDto;
import com.acme.backendfreshsense.reports.application.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
@Tag(name = "Reports", description = "Historial de consumo y reportes")
public class HistoryController {

    private final HistoryService service;

    public HistoryController(HistoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Historial de consumo (todos los usuarios)")
    public List<HistoryResponseDto> getAll() {
        return service.getAll();
    }

    /**
     * US17 — Análisis detallado de inventario (requiere USER_PREMIUM o ADMIN).
     * Devuelve KPIs agrupados: total consumido vs descartado por categoría.
     */
    @GetMapping("/advanced")
    @PreAuthorize("hasAnyRole('USER_PREMIUM', 'ADMIN')")
    @Operation(summary = "US17: Análisis detallado de inventario para suscriptores premium")
    public ResponseEntity<Map<String, Object>> getAdvancedAnalytics() {
        List<HistoryResponseDto> all = service.getAll();

        long totalConsumed  = all.stream().filter(h -> "CONSUME".equalsIgnoreCase(h.action())).count();
        long totalDiscarded = all.stream().filter(h -> "DISCARD".equalsIgnoreCase(h.action())).count();

        Map<String, Long> byCategory = all.stream()
                .collect(Collectors.groupingBy(
                        h -> h.category() != null ? h.category() : "Unknown",
                        Collectors.counting()
                ));

        return ResponseEntity.ok(Map.of(
                "totalConsumed",  totalConsumed,
                "totalDiscarded", totalDiscarded,
                "wasteRate",      totalConsumed + totalDiscarded > 0
                        ? (double) totalDiscarded / (totalConsumed + totalDiscarded) : 0.0,
                "byCategory",     byCategory
        ));
    }

    @PostMapping
    @Operation(summary = "Registrar acción de consumo/descarte")
    public HistoryResponseDto create(@Valid @RequestBody HistoryCreateRequestDto dto) {
        return service.create(dto);
    }
}
