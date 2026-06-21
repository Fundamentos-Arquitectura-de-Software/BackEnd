package com.acme.backendfreshsense.monitoring.infrastructure.web;

import com.acme.backendfreshsense.monitoring.application.dto.MonitoringReadingDto;
import com.acme.backendfreshsense.monitoring.application.dto.MonitoringReadingRequest;
import com.acme.backendfreshsense.monitoring.application.service.MonitoringService;
import com.acme.backendfreshsense.shared.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Monitoring", description = "Registro y consulta de lecturas de sensores IoT (temperatura, humedad, etileno, oxígeno)")
@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    @Operation(
        summary = "Registrar lectura de sensor",
        description = "Guarda una nueva lectura de los sensores IoT. Todos los campos son obligatorios. " +
                      "Los valores típicos: temperatura en °C (0–30), humedad en % (0–100), " +
                      "etileno en ppm (0–10), oxígeno en % (0–21), madurez y limpieza en escala 0–100."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = MonitoringReadingRequest.class),
            examples = @ExampleObject(
                name = "Lectura de sensor",
                value = """
                        {
                          "temperature": 4.5,
                          "humidity": 80.0,
                          "ethyleneLevel": 0.3,
                          "oxygenLevel": 20.5,
                          "ripeness": 65.0,
                          "cleanliness": 90.0
                        }"""
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Lectura registrada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MonitoringReadingDto.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 42,
                              "temperature": 4.5,
                              "humidity": 80.0,
                              "ethyleneLevel": 0.3,
                              "oxygenLevel": 20.5,
                              "ripeness": 65.0,
                              "cleanliness": 90.0,
                              "recordedAt": "2026-06-01T10:30:00"
                            }"""
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Algún campo es nulo o falta en el body",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = "{\"temperature\": \"must not be null\", \"humidity\": \"must not be null\"}"
                ))
        ),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PostMapping
    public ResponseEntity<MonitoringReadingDto> create(@Valid @RequestBody MonitoringReadingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(monitoringService.save(CurrentUser.id(), request));
    }

    @Operation(
        summary = "Obtener la lectura más reciente",
        description = "Devuelve la última lectura registrada por los sensores. Retorna 204 si aún no hay ninguna lectura."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lectura más reciente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = MonitoringReadingDto.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 42,
                              "temperature": 4.5,
                              "humidity": 80.0,
                              "ethyleneLevel": 0.3,
                              "oxygenLevel": 20.5,
                              "ripeness": 65.0,
                              "cleanliness": 90.0,
                              "recordedAt": "2026-06-01T10:30:00"
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "204", description = "No hay lecturas registradas aún — sin cuerpo de respuesta"),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @GetMapping("/latest")
    public ResponseEntity<MonitoringReadingDto> getLatest() {
        return monitoringService.getLatestByUser(CurrentUser.id())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @Operation(
        summary = "Listar todas las lecturas",
        description = "Devuelve el historial completo de lecturas de sensores ordenadas por fecha de registro."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Historial de lecturas",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                            [
                              {
                                "id": 41,
                                "temperature": 5.0,
                                "humidity": 78.0,
                                "ethyleneLevel": 0.2,
                                "oxygenLevel": 20.8,
                                "ripeness": 60.0,
                                "cleanliness": 92.0,
                                "recordedAt": "2026-06-01T09:00:00"
                              },
                              {
                                "id": 42,
                                "temperature": 4.5,
                                "humidity": 80.0,
                                "ethyleneLevel": 0.3,
                                "oxygenLevel": 20.5,
                                "ripeness": 65.0,
                                "cleanliness": 90.0,
                                "recordedAt": "2026-06-01T10:30:00"
                              }
                            ]"""
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @GetMapping
    public List<MonitoringReadingDto> getAll() {
        return monitoringService.getAllByUser(CurrentUser.id());
    }
}
