package com.acme.backendfreshsense.reports.domain.repository;

import com.acme.backendfreshsense.reports.domain.model.History;

import java.util.List;

public interface HistoryRepository {

    List<History> findAll();

    History save(History history);
}