package com.acme.backendfreshsense.reports.infrastructure.persistence;

import com.acme.backendfreshsense.reports.domain.model.History;
import com.acme.backendfreshsense.reports.domain.repository.HistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistoryRepositoryAdapter implements HistoryRepository {

    private final HistoryJpaRepository jpaRepository;

    public HistoryRepositoryAdapter(HistoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<History> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public History save(History history) {
        HistoryJpaEntity entity = toEntity(history);
        HistoryJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    private History toDomain(HistoryJpaEntity e) {
        History h = new History();
        h.setId(e.getId());
        h.setProductId(e.getProductId());
        h.setProductName(e.getProductName());
        h.setCategory(e.getCategory());
        h.setAction(e.getAction());
        h.setQuantity(e.getQuantity());
        h.setDate(e.getDate());
        return h;
    }

    private HistoryJpaEntity toEntity(History h) {
        HistoryJpaEntity e = new HistoryJpaEntity();
        e.setId(h.getId());
        e.setProductId(h.getProductId());
        e.setProductName(h.getProductName());
        e.setCategory(h.getCategory());
        e.setAction(h.getAction());
        e.setQuantity(h.getQuantity());
        e.setDate(h.getDate());
        return e;
    }
}
