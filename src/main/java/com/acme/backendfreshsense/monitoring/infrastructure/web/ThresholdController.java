package com.acme.backendfreshsense.monitoring.infrastructure.web;

import com.acme.backendfreshsense.monitoring.domain.model.CategoryThreshold;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Umbrales de temperatura/humedad recomendados por categoría de alimento.
 * Información de referencia (no funcional): se expone para mostrar en la app/informe
 * los rangos óptimos de conservación. No se usa todavía para clasificar lecturas.
 */
@Tag(name = "Thresholds", description = "Umbrales recomendados de temperatura/humedad por categoría (referencia)")
@RestController
@RequestMapping("/api/thresholds")
public class ThresholdController {

    // Rangos de referencia para conservación en refrigeración.
    private static final List<CategoryThreshold> THRESHOLDS = List.of(
            new CategoryThreshold("Frutas",     2,  8, 85, 95),
            new CategoryThreshold("Verduras",   0,  6, 90, 98),
            new CategoryThreshold("Lácteos",    1,  5, 70, 85),
            new CategoryThreshold("Carnes",    -1,  4, 75, 85),
            new CategoryThreshold("Proteínas",  0,  4, 75, 85),
            new CategoryThreshold("Panadería", 16, 24, 50, 65),
            new CategoryThreshold("Snacks",    16, 24, 40, 55)
    );

    @Operation(summary = "Listar umbrales recomendados por categoría",
            description = "Devuelve los rangos óptimos de temperatura y humedad por categoría de alimento (datos de referencia).")
    @GetMapping
    public List<CategoryThreshold> getThresholds() {
        return THRESHOLDS;
    }
}
