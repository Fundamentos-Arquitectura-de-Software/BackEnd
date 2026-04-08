package com.acme.backendfreshsense.inventory.interfaces.rest.resources;

public record ProductRequest(
        String name,
        String description,
        String category,
        Integer quantity,
        String imageUrl
) {}
