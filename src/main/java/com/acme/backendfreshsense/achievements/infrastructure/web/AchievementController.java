package com.acme.backendfreshsense.achievements.infrastructure.web;

import com.acme.backendfreshsense.achievements.application.dto.AchievementDto;
import com.acme.backendfreshsense.achievements.application.dto.UpdateAchievementRequest;
import com.acme.backendfreshsense.achievements.application.service.AchievementService;
import com.acme.backendfreshsense.shared.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Achievements", description = "Logros y sistema de gamificación del usuario autenticado")
@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService service;

    public AchievementController(AchievementService service) {
        this.service = service;
    }

    @Operation(
        summary = "Inicializar logros por defecto",
        description = "Crea el conjunto de logros predeterminados para un usuario recién registrado. " +
                      "Se llama automáticamente al crear la cuenta. Idempotente: si ya existen, no genera duplicados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logros inicializados correctamente — sin cuerpo de respuesta"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado con id: 99\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PostMapping("/init")
    public ResponseEntity<Void> initDefaults() {
        service.ensureDefaultAchievements(CurrentUser.id());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Listar logros de un usuario",
        description = "Devuelve todos los logros del usuario indicado. El campo `completionPercentage` va de 0 a 100. " +
                      "`isDefault` indica si el logro es parte del conjunto predeterminado del sistema."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de logros del usuario",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                            [
                              {
                                "id": "a1b2c3d4-e5f6-7890-ab12-cd34ef567890",
                                "userId": 1,
                                "name": "Primer inventario",
                                "completionPercentage": 100,
                                "isDefault": true
                              },
                              {
                                "id": "b2c3d4e5-f6a7-8901-bc23-de45fg678901",
                                "userId": 1,
                                "name": "Sin desperdicio",
                                "completionPercentage": 45,
                                "isDefault": true
                              }
                            ]"""
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado con id: 99\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @GetMapping
    public List<AchievementDto> list() {
        return service.listByUser(CurrentUser.id());
    }

    @Operation(
        summary = "Actualizar progreso de un logro por ID",
        description = "Modifica el `completionPercentage` de un logro específico identificado por su UUID. " +
                      "El valor debe estar entre 0 y 100."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = UpdateAchievementRequest.class),
            examples = @ExampleObject(
                name = "Marcar al 75%",
                value = "{\"completionPercentage\": 75}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Logro actualizado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AchievementDto.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": "a1b2c3d4-e5f6-7890-ab12-cd34ef567890",
                              "userId": 1,
                              "name": "Sin desperdicio",
                              "completionPercentage": 75,
                              "isDefault": true
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Logro o usuario no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Logro no encontrado\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PatchMapping("/{achievementId}")
    public AchievementDto update(
            @Parameter(description = "UUID del logro", example = "a1b2c3d4-e5f6-7890-ab12-cd34ef567890") @PathVariable UUID achievementId,
            @Valid @RequestBody UpdateAchievementRequest req) {
        return service.updateProgress(CurrentUser.id(), achievementId, req.completionPercentage());
    }
}
