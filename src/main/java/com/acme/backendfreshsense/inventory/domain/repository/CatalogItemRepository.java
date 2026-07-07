package com.acme.backendfreshsense.inventory.domain.repository;

import com.acme.backendfreshsense.inventory.domain.model.entities.CatalogItem;

import java.util.List;

public interface CatalogItemRepository {

    /**
     * Busca en el catálogo. Ambos filtros son opcionales (null = sin filtro):
     * {@code name} busca por coincidencia parcial sin distinguir mayúsculas;
     * {@code category} filtra por categoría exacta.
     */
    List<CatalogItem> search(String name, String category);

    boolean existsByName(String name);

    CatalogItem save(CatalogItem item);
}
