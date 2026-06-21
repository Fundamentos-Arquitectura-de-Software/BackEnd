package com.acme.backendfreshsense.reports.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryJpaRepository extends JpaRepository<HistoryJpaEntity, Long> {
    List<HistoryJpaEntity> findByUserId(Long userId);
}