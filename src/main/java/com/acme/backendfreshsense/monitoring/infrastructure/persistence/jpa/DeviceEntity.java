package com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, unique = true)
    private String deviceId;

    private String name;

    @Column(name = "secret_key", nullable = false)
    private String secretKey;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    /** Código de emparejamiento de un solo uso (claiming). Null tras canjearse. */
    @Column(name = "pairing_code", unique = true, length = 12)
    private String pairingCode;

    @Column(name = "pairing_expires_at")
    private LocalDateTime pairingExpiresAt;
}
