package com.acme.backendfreshsense.recipes.infrastructure.web;

import com.acme.backendfreshsense.recipes.application.dto.CreateRecipeRequest;
import com.acme.backendfreshsense.recipes.application.dto.RecipeResponse;
import com.acme.backendfreshsense.recipes.infrastructure.feign.RecipesFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Recipes", description = "Recetas sugeridas según ingredientes disponibles en el inventario")
@RestController
@RequestMapping(value = "/api/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RecipesController {

    private final RecipesFeignClient recipesFeignClient;

    @Operation(
        summary = "Listar todas las recetas",
        description = "Devuelve el catálogo completo de recetas disponibles. Los campos `level` (Easy, Medium, Hard), " +
                      "`type` (Vegetarian, Vegan, Omnivore) y `time` indican dificultad, tipo de dieta y tiempo estimado de preparación."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Listado de recetas",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                            [
                              {
                                "id": 1,
                                "title": "Ensalada de tomate y pepino",
                                "description": "Receta fresca para aprovechar verduras próximas a vencer",
                                "image": "https://example.com/ensalada.jpg",
                                "rating": 4,
                                "level": "Easy",
                                "type": "Vegan",
                                "time": "10 min",
                                "ingredients": ["Tomate", "Pepino", "Aceite de oliva", "Sal"],
                                "steps": ["Cortar tomate y pepino", "Mezclar con aceite y sal", "Servir frío"]
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
    public List<RecipeResponse> getAll() {
        return recipesFeignClient.getAll();
    }

    @Operation(
        summary = "Obtener una receta por ID",
        description = "Devuelve el detalle completo de una receta específica, incluyendo ingredientes y pasos de preparación."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Receta encontrada",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RecipeResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 1,
                              "title": "Ensalada de tomate y pepino",
                              "description": "Receta fresca para aprovechar verduras próximas a vencer",
                              "image": "https://example.com/ensalada.jpg",
                              "rating": 4,
                              "level": "Easy",
                              "type": "Vegan",
                              "time": "10 min",
                              "ingredients": ["Tomate", "Pepino", "Aceite de oliva", "Sal"],
                              "steps": ["Cortar tomate y pepino", "Mezclar con aceite y sal", "Servir frío"]
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Receta no encontrada con id: 99\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(
            @Parameter(description = "ID de la receta", example = "1") @PathVariable Long id) {
        return recipesFeignClient.getById(id);
    }

    @Operation(
        summary = "US19: Recetas exclusivas para suscriptores premium",
        description = "Devuelve recetas de nivel avanzado. Requiere rol `USER_PREMIUM` o `ADMIN`."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de recetas premium",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "403", description = "Acceso denegado — se requiere plan premium",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Acceso denegado\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @GetMapping("/premium")
    @PreAuthorize("hasAnyRole('USER_PREMIUM', 'ADMIN')")
    public ResponseEntity<List<RecipeResponse>> getPremium() {
        return recipesFeignClient.getPremium();
    }

    @Operation(
        summary = "Crear una receta (solo ADMIN)",
        description = "Agrega una nueva receta al catálogo. Requiere rol `ADMIN`. Los valores sugeridos para `level`: `Easy`, `Medium`, `Hard`. " +
                      "Para `type`: `Vegetarian`, `Vegan`, `Omnivore`."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CreateRecipeRequest.class),
            examples = @ExampleObject(
                name = "Nueva receta",
                value = """
                        {
                          "title": "Smoothie de plátano y leche",
                          "description": "Ideal para plátanos maduros que no se van a consumir",
                          "image": "https://example.com/smoothie.jpg",
                          "rating": 5,
                          "level": "Easy",
                          "type": "Vegetarian",
                          "time": "5 min",
                          "ingredients": ["Plátano", "Leche", "Miel"],
                          "steps": ["Pelar plátano", "Licuar con leche y miel", "Servir frío"]
                        }"""
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Receta creada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RecipeResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 10,
                              "title": "Smoothie de plátano y leche",
                              "description": "Ideal para plátanos maduros que no se van a consumir",
                              "image": "https://example.com/smoothie.jpg",
                              "rating": 5,
                              "level": "Easy",
                              "type": "Vegetarian",
                              "time": "5 min",
                              "ingredients": ["Plátano", "Leche", "Miel"],
                              "steps": ["Pelar plátano", "Licuar con leche y miel", "Servir frío"]
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acceso denegado — se requiere rol ADMIN",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Acceso denegado\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecipeResponse> create(@Valid @RequestBody CreateRecipeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipesFeignClient.create(request));
    }

    @Operation(
        summary = "Generar recetas nuevas con IA",
        description = "Genera un lote de recetas nuevas con OpenAI (5 desayunos, 10 comidas, 5 snacks), " +
                      "cada una con imagen de Pexels, evitando duplicar platos ya existentes, y las agrega al catálogo. " +
                      "La operación puede tardar 1-2 minutos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Recetas generadas y agregadas al catálogo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PostMapping("/generate-batch")
    public ResponseEntity<List<RecipeResponse>> generateBatch() {
        return ResponseEntity.status(HttpStatus.CREATED).body(recipesFeignClient.generateBatch());
    }
}
