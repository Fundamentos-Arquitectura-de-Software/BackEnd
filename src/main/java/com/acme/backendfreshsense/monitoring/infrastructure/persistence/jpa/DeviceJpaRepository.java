package com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceJpaRepository extends JpaRepository<DeviceEntity, Long> {
    List<DeviceEntity> findByUserId(Long userId);
    Optional<DeviceEntity> findByDeviceId(String deviceId);
    Optional<DeviceEntity> findByPairingCode(String pairingCode);
    boolean existsByDeviceId(String deviceId);
}
