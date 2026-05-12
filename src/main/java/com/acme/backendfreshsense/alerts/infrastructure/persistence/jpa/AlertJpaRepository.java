package com.acme.backendfreshsense.alerts.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertJpaRepository extends JpaRepository<AlertEntity, Long> {
}
