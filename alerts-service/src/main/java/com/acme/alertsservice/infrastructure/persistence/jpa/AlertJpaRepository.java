package com.acme.alertsservice.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertJpaRepository extends JpaRepository<AlertEntity, Long> {
}
