package com.acme.backendfreshsense.reports.infrastructure.web;

import com.acme.backendfreshsense.reports.application.dto.HistoryCreateRequestDto;
import com.acme.backendfreshsense.reports.application.dto.HistoryResponseDto;
import com.acme.backendfreshsense.reports.application.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reports", description = "Historial de acciones sobre el inventario (consumo y descarte) para reportes semanales")
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService service;

    public HistoryController(HistoryService service) {
        this.service = service;
    }

    @Operation(
        summary = "Obtener historial de acciones",
        description = "Devuelve todas las entradas del historial de acciones. Cada entrada registra si un producto fue " +
                      "consumido (`CONSUMED`) o descartado (`DISCARDED`), junto con la cantidad y la fecha de la acción."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Historial de acciones",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                            [
                              {
                                "id": 1,
                                "productId": 3,
                                "productName": "Leche entera",
                                "category": "Lácteos",
                                "action": "CONSUMED",
                                "quantity": 1,
                                "date": "2026-05-30T08:15:00"
                              },
                              {
                                "id": 2,
                                "productId": 7,
                                "productName": "Pan de molde",
                                "category": "Panadería",
                                "action": "DISCARDED",
                                "quantity": 2,
                                "date": "2026-05-31T19:00:00"
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
    public List<HistoryResponseDto> getAll() {
        return service.getAll();
    }

    @Operation(
        summary = "Registrar una acción en el historial",
        description = "Guarda una acción realizada sobre un producto. Los valores válidos para `action` son `CONSUMED` (producto consumido) " +
                      "y `DISCARDED` (producto descartado/desperdiciado). La fecha se asigna automáticamente en el servidor."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = HistoryCreateRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Producto consumido",
                    value = """
                            {
                              "productId": 3,
                              "productName": "Leche entera",
                              "category": "Lácteos",
                              "action": "CONSUMED",
                              "quantity": 1
                            }"""
                ),
                @ExampleObject(
                    name = "Producto descartado",
                    value = """
                            {
                              "productId": 7,
                              "productName": "Pan de molde",
                              "category": "Panadería",
                              "action": "DISCARDED",
                              "quantity": 2
                            }"""
                )
            }
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Acción registrada en el historial",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = HistoryResponseDto.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 3,
                              "productId": 3,
                              "productName": "Leche entera",
                              "category": "Lácteos",
                              "action": "CONSUMED",
                              "quantity": 1,
                              "date": "2026-06-01T11:45:00"
                            }"""
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (productId nulo, acción vacía, cantidad negativa)",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = "{\"productId\": \"El productId es obligatorio\", \"action\": \"La acción es obligatoria\"}"
                ))
        ),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PostMapping
    public HistoryResponseDto create(@Valid @RequestBody HistoryCreateRequestDto dto) {
        return service.create(dto);
    }
}
