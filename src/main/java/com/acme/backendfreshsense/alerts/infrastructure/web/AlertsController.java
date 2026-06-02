package com.acme.backendfreshsense.alerts.infrastructure.web;

import com.acme.backendfreshsense.alerts.application.dto.AlertRequest;
import com.acme.backendfreshsense.alerts.application.dto.AlertResponse;
import com.acme.backendfreshsense.alerts.application.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Alerts", description = "Alertas de vencimiento y stock bajo de productos")
@RestController
@RequestMapping("/api/alerts")
public class AlertsController {

    private final AlertService service;

    public AlertsController(AlertService service) {
        this.service = service;
    }

    @Operation(
        summary = "Listar todas las alertas",
        description = "Devuelve todas las alertas del sistema. Cada alerta indica severidad (`INFO`, `WARNING`, `CRITICAL`), fuente y estado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de alertas",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                            [
                              {
                                "id": 1,
                                "title": "Leche próxima a vencer",
                                "message": "La leche vence en 2 días",
                                "severity": "WARNING",
                                "source": "inventory",
                                "state": "active",
                                "timeAgo": "hace 10 min"
                              },
                              {
                                "id": 2,
                                "title": "Stock crítico de tomates",
                                "message": "Quedan menos de 3 unidades",
                                "severity": "CRITICAL",
                                "source": "inventory",
                                "state": "active",
                                "timeAgo": "hace 1 hora"
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
    public List<AlertResponse> getAll() {
        return service.getAll();
    }

    @Operation(
        summary = "Crear una alerta",
        description = "Registra una nueva alerta manualmente. Los valores válidos de `severity` son `INFO`, `WARNING` y `CRITICAL`."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AlertRequest.class),
            examples = @ExampleObject(
                name = "Alerta de vencimiento",
                value = """
                        {
                          "title": "Yogur próximo a vencer",
                          "message": "El yogur vence mañana",
                          "severity": "WARNING",
                          "source": "inventory",
                          "state": "active",
                          "timeAgo": "hace 5 min"
                        }"""
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Alerta creada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AlertResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 3,
                              "title": "Yogur próximo a vencer",
                              "message": "El yogur vence mañana",
                              "severity": "WARNING",
                              "source": "inventory",
                              "state": "active",
                              "timeAgo": "hace 5 min"
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PostMapping
    public AlertResponse create(@RequestBody AlertRequest request) {
        return service.create(request);
    }

    @Operation(
        summary = "Actualizar una alerta",
        description = "Reemplaza completamente los datos de una alerta existente por su ID."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = AlertRequest.class),
            examples = @ExampleObject(
                name = "Marcar como resuelta",
                value = """
                        {
                          "title": "Yogur próximo a vencer",
                          "message": "El yogur vence mañana",
                          "severity": "WARNING",
                          "source": "inventory",
                          "state": "resolved",
                          "timeAgo": "hace 30 min"
                        }"""
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Alerta actualizada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AlertResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 3,
                              "title": "Yogur próximo a vencer",
                              "message": "El yogur vence mañana",
                              "severity": "WARNING",
                              "source": "inventory",
                              "state": "resolved",
                              "timeAgo": "hace 30 min"
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Alerta no encontrada",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Alerta no encontrada con id: 99\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PutMapping("/{id}")
    public AlertResponse update(
            @Parameter(description = "ID de la alerta a actualizar", example = "3") @PathVariable Long id,
            @RequestBody AlertRequest request) {
        return service.update(id, request);
    }
}
