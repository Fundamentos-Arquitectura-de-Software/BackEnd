package com.acme.backendfreshsense.monitoring.application.service;

import com.acme.backendfreshsense.monitoring.application.dto.ClaimResponse;
import com.acme.backendfreshsense.monitoring.application.dto.DeviceRegistrationResponse;
import com.acme.backendfreshsense.monitoring.application.dto.DeviceResponse;
import com.acme.backendfreshsense.monitoring.application.dto.RegisterDeviceRequest;
import com.acme.backendfreshsense.monitoring.domain.model.Device;
import com.acme.backendfreshsense.monitoring.domain.repository.DeviceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Transactional
public class DeviceService {

    /** Alfabeto del código de emparejamiento (sin caracteres ambiguos: 0/O, 1/I). */
    private static final String CODE_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private static final int CODE_TTL_MINUTES = 10;

    private final DeviceRepository deviceRepository;
    private final SecureRandom random = new SecureRandom();

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Registra un dispositivo del usuario. Genera una secretKey oculta y un
     * CÓDIGO DE EMPAREJAMIENTO temporal de un solo uso, que es lo único que se
     * muestra en la app. El Edge canjea ese código por la secretKey vía {@link #claim}.
     */
    public DeviceRegistrationResponse register(Long userId, RegisterDeviceRequest request) {
        if (deviceRepository.existsByDeviceId(request.deviceId())) {
            throw new IllegalStateException("El dispositivo ya está registrado");
        }
        String secretKey = UUID.randomUUID().toString().replace("-", "");
        String pairingCode = generatePairingCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(CODE_TTL_MINUTES);

        Device device = Device.builder()
                .deviceId(request.deviceId())
                .name(request.name())
                .secretKey(secretKey)
                .userId(userId)
                .registeredAt(LocalDateTime.now())
                .pairingCode(pairingCode)
                .pairingExpiresAt(expiresAt)
                .build();
        Device saved = deviceRepository.save(device);

        return new DeviceRegistrationResponse(saved.getDeviceId(), saved.getName(),
                saved.getPairingCode(), saved.getPairingExpiresAt());
    }

    /**
     * Canjea un código de emparejamiento por la clave del dispositivo (lo llama el Edge).
     * El código es de un solo uso: se invalida al canjearse. 400 si es inválido o expiró.
     */
    public ClaimResponse claim(String code) {
        Device device = deviceRepository.findByPairingCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Código de emparejamiento inválido"));

        if (device.getPairingExpiresAt() == null || device.getPairingExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Código de emparejamiento expirado");
        }

        // Un solo uso: invalidar el código tras el canje.
        device.setPairingCode(null);
        device.setPairingExpiresAt(null);
        deviceRepository.save(device);

        return new ClaimResponse(device.getDeviceId(), device.getSecretKey());
    }

    public List<DeviceResponse> listByUser(Long userId) {
        return deviceRepository.findByUserId(userId).stream()
                // No reexponer el secretKey al listar.
                .map(d -> new DeviceResponse(d.getId(), d.getDeviceId(), d.getName(), null, d.getRegisteredAt()))
                .toList();
    }

    /**
     * Valida la clave del dispositivo (cabecera X-Device-Key) y devuelve el dispositivo.
     * @return el dispositivo si la clave coincide; {@code null} si no existe o la clave es inválida.
     */
    public Device authenticate(String deviceId, String secretKey) {
        if (deviceId == null || secretKey == null) return null;
        return deviceRepository.findByDeviceId(deviceId)
                .filter(d -> secretKey.equals(d.getSecretKey()))
                .orElse(null);
    }

    private String generatePairingCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CODE_ALPHABET.charAt(random.nextInt(CODE_ALPHABET.length())));
        }
        return sb.toString();
    }
}
