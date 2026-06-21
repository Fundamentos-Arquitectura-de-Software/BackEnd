package com.acme.backendfreshsense.monitoring.application.service;

import com.acme.backendfreshsense.monitoring.application.dto.DeviceResponse;
import com.acme.backendfreshsense.monitoring.application.dto.RegisterDeviceRequest;
import com.acme.backendfreshsense.monitoring.domain.model.Device;
import com.acme.backendfreshsense.monitoring.domain.repository.DeviceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Transactional
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /** Registra un dispositivo del usuario y genera su secretKey (se devuelve una sola vez). */
    public DeviceResponse register(Long userId, RegisterDeviceRequest request) {
        if (deviceRepository.existsByDeviceId(request.deviceId())) {
            throw new IllegalStateException("El dispositivo ya está registrado");
        }
        String secretKey = UUID.randomUUID().toString().replace("-", "");
        Device device = Device.builder()
                .deviceId(request.deviceId())
                .name(request.name())
                .secretKey(secretKey)
                .userId(userId)
                .registeredAt(LocalDateTime.now())
                .build();
        Device saved = deviceRepository.save(device);
        // Incluye secretKey solo en la respuesta de registro.
        return new DeviceResponse(saved.getId(), saved.getDeviceId(), saved.getName(),
                saved.getSecretKey(), saved.getRegisteredAt());
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
}
