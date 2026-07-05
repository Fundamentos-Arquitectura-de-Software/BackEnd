package com.acme.backendfreshsense.monitoring.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resultado del canje de un código de emparejamiento. Entrega la clave secreta
 * al Edge (que la guarda localmente). No se expone a la UI del usuario.
 */
@Schema(description = "Clave del dispositivo entregada al Edge tras canjear el código")
public record ClaimResponse(
        @Schema(description = "Identificador del dispositivo", example = "esp32-freshsense-1")
        String deviceId,

        @Schema(description = "Clave secreta del dispositivo (se usa como cabecera X-Device-Key)", example = "a1b2c3...")
        String secretKey
) {}
