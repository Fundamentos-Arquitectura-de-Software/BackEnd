package com.acme.backendfreshsense.inventory.interfaces.rest.resources;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String category,
        Integer quantity,
        String imageUrl
) {}
