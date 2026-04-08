package com.acme.backendfreshsense.reports.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryJpaRepository extends JpaRepository<HistoryJpaEntity, Long> {
}