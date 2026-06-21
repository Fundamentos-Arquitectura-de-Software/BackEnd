package com.acme.backendfreshsense.monitoring.domain.repository;

import com.acme.backendfreshsense.monitoring.domain.model.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    Device save(Device device);
    List<Device> findByUserId(Long userId);
    Optional<Device> findByDeviceId(String deviceId);
    boolean existsByDeviceId(String deviceId);
}
