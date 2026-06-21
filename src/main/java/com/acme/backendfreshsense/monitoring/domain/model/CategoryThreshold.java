package com.acme.backendfreshsense.monitoring.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Umbral recomendado de temperatura y humedad para una categoría de alimentos.
 * Datos de referencia (informativos): rangos óptimos de conservación.
 */
@Schema(description = "Umbral recomendado de temperatura/humedad por categoría de alimento")
public record CategoryThreshold(
        @Schema(description = "Categoría de alimento", example = "Frutas")
        String category,

        @Schema(description = "Temperatura mínima recomendada (°C)", example = "2")
        double tempMin,

        @Schema(description = "Temperatura máxima recomendada (°C)", example = "8")
        double tempMax,

        @Schema(description = "Humedad mínima recomendada (%)", example = "85")
        double humidityMin,

        @Schema(description = "Humedad máxima recomendada (%)", example = "95")
        double humidityMax
) {}
