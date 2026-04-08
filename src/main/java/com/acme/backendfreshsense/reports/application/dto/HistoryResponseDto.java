package com.acme.backendfreshsense.reports.application.dto;

import java.time.LocalDateTime;

public record HistoryResponseDto(
        Long id,
        Long productId,
        String productName,
        String category,
        String action,
        Integer quantity,
        LocalDateTime date
) { }
