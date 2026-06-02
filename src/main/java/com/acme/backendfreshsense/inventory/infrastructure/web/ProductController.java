package com.acme.backendfreshsense.inventory.infrastructure.web;

import com.acme.backendfreshsense.inventory.application.dto.ProductRequest;
import com.acme.backendfreshsense.inventory.application.dto.ProductResponse;
import com.acme.backendfreshsense.inventory.application.dto.UpdateProductRequest;
import com.acme.backendfreshsense.inventory.application.service.ProductService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inventory", description = "Gestión del inventario de productos alimenticios")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
        summary = "Registrar un producto",
        description = "Agrega un nuevo producto al inventario del usuario autenticado."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ProductRequest.class),
            examples = @ExampleObject(
                name = "Ejemplo de producto",
                value = """
                        {
                          "name": "Manzana Fuji",
                          "description": "Manzanas importadas de Chile",
                          "category": "Frutas",
                          "quantity": 12,
                          "imageUrl": "https://example.com/manzana.jpg"
                        }"""
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Producto registrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProductResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 5,
                              "name": "Manzana Fuji",
                              "description": "Manzanas importadas de Chile",
                              "category": "Frutas",
                              "quantity": 12,
                              "imageUrl": "https://example.com/manzana.jpg"
                            }"""
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (nombre vacío, cantidad negativa, categoría vacía)",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = "{\"name\": \"El nombre es obligatorio\", \"quantity\": \"La cantidad no puede ser negativa\"}"
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @Operation(
        summary = "Listar todos los productos",
        description = "Devuelve la lista completa de productos del inventario."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de productos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = """
                            [
                              {
                                "id": 1,
                                "name": "Leche entera",
                                "description": "1 litro",
                                "category": "Lácteos",
                                "quantity": 3,
                                "imageUrl": null
                              },
                              {
                                "id": 2,
                                "name": "Tomate",
                                "description": null,
                                "category": "Verduras",
                                "quantity": 8,
                                "imageUrl": null
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
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @Operation(
        summary = "Actualizar cantidad de un producto",
        description = "Modifica parcialmente el inventario — actualmente solo permite actualizar la cantidad (`quantity`)."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = UpdateProductRequest.class),
            examples = @ExampleObject(
                name = "Reducir cantidad",
                value = "{\"quantity\": 5}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ProductResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 1,
                              "name": "Leche entera",
                              "description": "1 litro",
                              "category": "Lácteos",
                              "quantity": 5,
                              "imageUrl": null
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Cantidad negativa",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"quantity\": \"La cantidad no puede ser negativa\"}"))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Producto no encontrado con id: 99\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @Parameter(description = "ID del producto a actualizar", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina permanentemente un producto del inventario por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado — sin cuerpo de respuesta"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Producto no encontrado con id: 99\"}"))),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del producto a eliminar", example = "1") @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
