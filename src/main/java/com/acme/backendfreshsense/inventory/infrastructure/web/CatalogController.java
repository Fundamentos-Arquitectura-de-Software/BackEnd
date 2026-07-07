package com.acme.backendfreshsense.inventory.infrastructure.web;

import com.acme.backendfreshsense.inventory.application.dto.CatalogItemResponse;
import com.acme.backendfreshsense.inventory.application.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Catálogo general de alimentos: el usuario elige de aquí para agregar productos
 * a su inventario sin escribirlos a mano (con opción manual como respaldo).
 * Solo lectura; los datos los siembra {@code CatalogDataInitializer}.
 */
@Tag(name = "Catalog", description = "Catálogo general de alimentos para agregar al inventario")
@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Operation(summary = "Buscar en el catálogo de alimentos",
            description = "Lista los alimentos del catálogo general, ordenados por nombre. " +
                          "Filtros opcionales: búsqueda parcial por nombre y categoría exacta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado del catálogo (puede ser vacío)"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping
    public List<CatalogItemResponse> search(
            @Parameter(description = "Búsqueda parcial por nombre", example = "manza")
            @RequestParam(required = false) String search,
            @Parameter(description = "Categoría exacta", example = "Frutas")
            @RequestParam(required = false) String category) {
        return catalogService.search(search, category);
    }
}
