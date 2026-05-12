package com.acme.backendfreshsense.inventory.application.dto;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String category,
        Integer quantity,
        String imageUrl
) {}
