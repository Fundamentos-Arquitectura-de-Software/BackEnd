package com.acme.backendfreshsense.reports.infrastructure.web;

import com.acme.backendfreshsense.reports.application.dto.HistoryCreateRequestDto;
import com.acme.backendfreshsense.reports.application.dto.HistoryResponseDto;
import com.acme.backendfreshsense.reports.application.service.HistoryService;
import com.acme.backendfreshsense.shared.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Reports", description = "Historial de acciones sobre el inventario (consumo y descarte) para reportes semanales")
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService service;
    private final JwtService jwtService;

    public HistoryController(HistoryService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
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
    public List<HistoryResponseDto> getAll(HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        return service.getAllByUser(userId);
    }

    @Operation(
        summary = "US17: Análisis detallado de inventario (solo premium)",
        description = "Devuelve KPIs agrupados: total consumido vs descartado por categoría. Requiere rol `USER_PREMIUM` o `ADMIN`."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Análisis agrupado por categoría",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                            {
                              "totalConsumed": 42,
                              "totalDiscarded": 8,
                              "wasteRate": 0.16,
                              "byCategory": {
                                "Lácteos": 15,
                                "Verduras": 20,
                                "Panadería": 15
                              }
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado — se requiere plan premium",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Acceso denegado\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @GetMapping("/advanced")
    @PreAuthorize("hasAnyRole('USER_PREMIUM', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdvancedAnalytics(HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        List<HistoryResponseDto> all = service.getAllByUser(userId);

        long totalConsumed  = all.stream().filter(h -> "CONSUMED".equalsIgnoreCase(h.action())).count();
        long totalDiscarded = all.stream().filter(h -> "DISCARDED".equalsIgnoreCase(h.action())).count();

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
    public HistoryResponseDto create(HttpServletRequest httpRequest,
                                     @Valid @RequestBody HistoryCreateRequestDto dto) {
        Long userId = extractUserId(httpRequest);
        return service.create(userId, dto);
    }

    private Long extractUserId(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("authToken".equals(c.getName())) { token = c.getValue(); break; }
            }
        }
        if (token == null) {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) token = header.substring(7);
        }
        Long userId = token != null ? jwtService.extractUserId(token) : null;
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
        return userId;
    }
}
