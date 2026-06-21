package com.acme.backendfreshsense.challenges.infrastructure.web;

import com.acme.backendfreshsense.challenges.application.dto.ChallengeDto;
import com.acme.backendfreshsense.challenges.application.dto.LeaderboardEntryDto;
import com.acme.backendfreshsense.challenges.application.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.acme.backendfreshsense.shared.infrastructure.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Challenges", description = "Retos de gamificación: inscripción, abandono y clasificación de participantes")
@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @Operation(
        summary = "Listar todos los retos",
        description = "Devuelve el catálogo completo de retos disponibles. El campo `status` indica si el reto está " +
                      "`ACTIVE`, `UPCOMING` o `FINISHED`. `goalType` describe el tipo de meta (ej. `REDUCE_WASTE`)."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de retos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                            [
                              {
                                "id": 1,
                                "title": "Reto Sin Desperdicio",
                                "description": "Reduce tu descarte de alimentos a 0 durante una semana",
                                "rewardPts": 500,
                                "startAt": "2026-06-01",
                                "endAt": "2026-06-07",
                                "goalType": "REDUCE_WASTE",
                                "goalTarget": 0,
                                "status": "ACTIVE",
                                "bannerUrl": "https://example.com/banner1.jpg"
                              },
                              {
                                "id": 2,
                                "title": "Semana Vegetariana",
                                "description": "Consume solo recetas vegetarianas por 7 días",
                                "rewardPts": 300,
                                "startAt": "2026-06-08",
                                "endAt": "2026-06-14",
                                "goalType": "VEGETARIAN_STREAK",
                                "goalTarget": 7,
                                "status": "UPCOMING",
                                "bannerUrl": "https://example.com/banner2.jpg"
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
    public List<ChallengeDto> getAll() {
        return challengeService.getAllChallenges();
    }

    @Operation(
        summary = "Inscribirse en un reto",
        description = "Inscribe al usuario autenticado en el reto indicado. El `userId` se extrae automáticamente del token JWT — no se envía en el body."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inscripción exitosa — sin cuerpo de respuesta"),
        @ApiResponse(responseCode = "404", description = "Reto no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Reto no encontrado con id: 99\"}"))),
        @ApiResponse(responseCode = "409", description = "El usuario ya está inscrito en este reto",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"El usuario ya está inscrito en este reto\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PostMapping("/{challengeId}/enroll")
    public ResponseEntity<Void> enroll(
            @Parameter(description = "ID del reto al que inscribirse", example = "1") @PathVariable Long challengeId) {
        challengeService.enroll(challengeId, CurrentUser.id());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Abandonar un reto",
        description = "Cancela la inscripción del usuario autenticado en el reto indicado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Inscripción cancelada — sin cuerpo de respuesta"),
        @ApiResponse(responseCode = "404", description = "Reto no encontrado o usuario no estaba inscrito",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Inscripción no encontrada\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @DeleteMapping("/{challengeId}/enroll")
    public ResponseEntity<Void> leave(
            @Parameter(description = "ID del reto que se quiere abandonar", example = "1") @PathVariable Long challengeId) {
        challengeService.leave(challengeId, CurrentUser.id());
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Ver tabla de clasificación de un reto",
        description = "Devuelve el ranking de participantes del reto ordenados por `rank`. El campo `progress` " +
                      "indica el avance del usuario hacia la meta del reto."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Tabla de clasificación",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = LeaderboardEntryDto.class),
                examples = @ExampleObject(
                    value = """
                            [
                              {
                                "userId": 3,
                                "challengeId": 1,
                                "progress": 100,
                                "rank": 1
                              },
                              {
                                "userId": 7,
                                "challengeId": 1,
                                "progress": 80,
                                "rank": 2
                              },
                              {
                                "userId": 1,
                                "challengeId": 1,
                                "progress": 40,
                                "rank": 3
                              }
                            ]"""
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Reto no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Reto no encontrado con id: 99\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @GetMapping("/{challengeId}/leaderboard")
    public List<LeaderboardEntryDto> getLeaderboard(
            @Parameter(description = "ID del reto", example = "1") @PathVariable Long challengeId) {
        return challengeService.getLeaderboard(challengeId);
    }

    @Operation(
        summary = "Actualizar el progreso del usuario en un reto",
        description = "Fija el progreso (0–100) del usuario autenticado en el reto indicado. Útil para reflejar avance en el leaderboard."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Progreso actualizado — sin cuerpo de respuesta"),
        @ApiResponse(responseCode = "404", description = "Inscripción no encontrada",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Inscripción no encontrada\"}")))
    })
    @PatchMapping("/{challengeId}/progress")
    public ResponseEntity<Void> updateProgress(
            @Parameter(description = "ID del reto", example = "1") @PathVariable Long challengeId,
            @RequestBody java.util.Map<String, Integer> body) {
        int progress = body.getOrDefault("progress", 0);
        challengeService.updateProgress(challengeId, CurrentUser.id(), progress);
        return ResponseEntity.noContent().build();
    }
}
