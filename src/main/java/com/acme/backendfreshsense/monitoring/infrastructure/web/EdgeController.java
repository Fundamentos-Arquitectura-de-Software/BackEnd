package com.acme.backendfreshsense.monitoring.infrastructure.web;

import com.acme.backendfreshsense.monitoring.application.dto.EdgeReadingRequest;
import com.acme.backendfreshsense.monitoring.application.dto.MonitoringReadingDto;
import com.acme.backendfreshsense.monitoring.application.service.DeviceService;
import com.acme.backendfreshsense.monitoring.application.service.MonitoringService;
import com.acme.backendfreshsense.monitoring.domain.model.Device;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Punto de entrada para el Edge/IoT. El dispositivo físico (ESP32) envía aquí sus lecturas.
 * Flujo: IoT -> Edge -> este endpoint -> backend.
 *
 * <p>Autenticación: cabecera {@code X-Device-Key} con el secreto del dispositivo (no usa JWT).
 * El usuario dueño se resuelve a partir del dispositivo registrado.</p>
 */
@Tag(name = "Edge/IoT", description = "Ingesta de lecturas desde el Edge (dispositivos IoT)")
@RestController
@RequestMapping("/api/edge")
public class EdgeController {

    private static final Logger log = LoggerFactory.getLogger(EdgeController.class);
    private static final DateTimeFormatter EDGE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DeviceService deviceService;
    private final MonitoringService monitoringService;

    public EdgeController(DeviceService deviceService, MonitoringService monitoringService) {
        this.deviceService = deviceService;
        this.monitoringService = monitoringService;
    }

    @Operation(summary = "Recibir una lectura del Edge",
            description = "Recibe temperatura y humedad de un dispositivo IoT. Requiere cabecera X-Device-Key válida. " +
                          "El campo 'id' es el identificador de la lectura generado en el Edge; 'deviceId' identifica al sensor.")
    @PostMapping("/readings")
    public ResponseEntity<MonitoringReadingDto> ingest(
            @RequestHeader(value = "X-Device-Key", required = false) String deviceKey,
            @Valid @RequestBody EdgeReadingRequest request) {

        Device device = deviceService.authenticate(request.deviceId(), deviceKey);
        if (device == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Dispositivo no autorizado");
        }

        LocalDateTime recordedAt = parseTime(request.time());
        MonitoringReadingDto saved = monitoringService.saveFromEdge(
                device.getUserId(), request.deviceId(), request.id(),
                request.temperature(), request.humidity(), recordedAt);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    private LocalDateTime parseTime(String time) {
        if (time == null || time.isBlank()) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(time.trim(), EDGE_TIME);
        } catch (Exception e) {
            log.warn("Formato de 'time' no válido ({}), se usa la hora actual. Esperado dd/MM/yyyy HH:mm", time);
            return LocalDateTime.now();
        }
    }
}
