package com.acme.backendfreshsense.monitoring.domain.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Dispositivo IoT (p. ej. ESP32) registrado por un usuario.
 * El {@code secretKey} se valida en la cabecera X-Device-Key del endpoint Edge.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    private Long id;
    private String deviceId;
    private String name;
    private String secretKey;
    private Long userId;
    private LocalDateTime registeredAt;

    /** Código de emparejamiento de un solo uso (claiming). Null una vez canjeado. */
    private String pairingCode;
    /** Vencimiento del código de emparejamiento. */
    private LocalDateTime pairingExpiresAt;
}
