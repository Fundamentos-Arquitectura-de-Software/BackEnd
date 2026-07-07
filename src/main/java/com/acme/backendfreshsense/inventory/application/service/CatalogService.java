package com.acme.backendfreshsense.inventory.application.service;

import com.acme.backendfreshsense.inventory.application.dto.CatalogItemResponse;
import com.acme.backendfreshsense.inventory.domain.repository.CatalogItemRepository;

import java.util.List;

public class CatalogService {

    private final CatalogItemRepository catalogRepository;

    public CatalogService(CatalogItemRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<CatalogItemResponse> search(String name, String category) {
        return catalogRepository.search(name, category).stream()
                .map(i -> new CatalogItemResponse(i.getId(), i.getName(), i.getDescription(),
                        i.getCategory(), i.getImageUrl()))
                .toList();
    }
}
