package com.acme.backendfreshsense.reports.domain.repository;

import com.acme.backendfreshsense.reports.domain.model.History;

import java.util.List;

public interface HistoryRepository {

    List<History> findAll();

    List<History> findByUserId(Long userId);

    History save(History history);
}