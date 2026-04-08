package com.acme.backendfreshsense.reports.application.service;

import com.acme.backendfreshsense.reports.application.dto.HistoryCreateRequestDto;
import com.acme.backendfreshsense.reports.application.dto.HistoryResponseDto;
import com.acme.backendfreshsense.reports.domain.model.History;
import com.acme.backendfreshsense.reports.domain.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository repository;

    public HistoryService(HistoryRepository repository) {
        this.repository = repository;
    }

    public List<HistoryResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public HistoryResponseDto create(HistoryCreateRequestDto dto) {
        History history = new History(
                null,
                dto.productId(),
                dto.productName(),
                dto.category(),
                dto.action(),
                dto.quantity(),
                LocalDateTime.now()
        );
        History saved = repository.save(history);
        return toDto(saved);
    }

    private HistoryResponseDto toDto(History h) {
        return new HistoryResponseDto(
                h.getId(),
                h.getProductId(),
                h.getProductName(),
                h.getCategory(),
                h.getAction(),
                h.getQuantity(),
                h.getDate()
        );
    }
}
