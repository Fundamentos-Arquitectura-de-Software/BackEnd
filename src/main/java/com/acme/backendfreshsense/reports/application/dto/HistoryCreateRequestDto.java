package com.acme.backendfreshsense.reports.application.dto;

public record HistoryCreateRequestDto(
        Long productId,
        String productName,
        String category,
        String action,
        Integer quantity
) { }