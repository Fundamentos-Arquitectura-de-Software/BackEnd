package com.acme.backendfreshsense.monitoring.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.monitoring.domain.model.Device;
import com.acme.backendfreshsense.monitoring.domain.repository.DeviceRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.DeviceEntity;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.DeviceJpaRepository;

import java.util.List;
import java.util.Optional;

public class DeviceRepositoryAdapter implements DeviceRepository {

    private final DeviceJpaRepository jpa;

    public DeviceRepositoryAdapter(DeviceJpaRepository jpa) {
        this.jpa = jpa;
    }

    private static Device toDomain(DeviceEntity e) {
        return Device.builder()
                .id(e.getId())
                .deviceId(e.getDeviceId())
                .name(e.getName())
                .secretKey(e.getSecretKey())
                .userId(e.getUserId())
                .registeredAt(e.getRegisteredAt())
                .pairingCode(e.getPairingCode())
                .pairingExpiresAt(e.getPairingExpiresAt())
                .build();
    }

    private static DeviceEntity toEntity(Device d) {
        return DeviceEntity.builder()
                .id(d.getId())
                .deviceId(d.getDeviceId())
                .name(d.getName())
                .secretKey(d.getSecretKey())
                .userId(d.getUserId())
                .registeredAt(d.getRegisteredAt())
                .pairingCode(d.getPairingCode())
                .pairingExpiresAt(d.getPairingExpiresAt())
                .build();
    }

    @Override
    public Device save(Device device) {
        return toDomain(jpa.save(toEntity(device)));
    }

    @Override
    public List<Device> findByUserId(Long userId) {
        return jpa.findByUserId(userId).stream().map(DeviceRepositoryAdapter::toDomain).toList();
    }

    @Override
    public Optional<Device> findByDeviceId(String deviceId) {
        return jpa.findByDeviceId(deviceId).map(DeviceRepositoryAdapter::toDomain);
    }

    @Override
    public Optional<Device> findByPairingCode(String pairingCode) {
        return jpa.findByPairingCode(pairingCode).map(DeviceRepositoryAdapter::toDomain);
    }

    @Override
    public boolean existsByDeviceId(String deviceId) {
        return jpa.existsByDeviceId(deviceId);
    }
}
