package com.acme.backendfreshsense.monitoring.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Respuesta al registrar un dispositivo. Devuelve un CÓDIGO DE EMPAREJAMIENTO
 * (de un solo uso, temporal), NO la clave secreta. El Edge canjea ese código en
 * {@code POST /api/edge/claim} para obtener la clave — así el secreto nunca lo
 * ve ni lo copia el usuario.
 */
@Schema(description = "Datos de emparejamiento tras registrar un dispositivo")
public record DeviceRegistrationResponse(
        @Schema(description = "Identificador del dispositivo", example = "esp32-freshsense-1")
        String deviceId,

        @Schema(description = "Nombre amigable", example = "Refrigerador cocina")
        String name,

        @Schema(description = "Código de emparejamiento de un solo uso. Introdúcelo en el Edge para vincularlo.", example = "7K4Q2P")
        String pairingCode,

        @Schema(description = "Momento en que expira el código de emparejamiento (ISO 8601)")
        LocalDateTime pairingExpiresAt
) {}
