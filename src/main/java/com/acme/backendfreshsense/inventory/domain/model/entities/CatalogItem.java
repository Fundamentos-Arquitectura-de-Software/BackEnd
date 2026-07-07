package com.acme.backendfreshsense.inventory.domain.model.entities;

import lombok.*;

/**
 * Alimento del catálogo general: plantilla desde la que el usuario agrega
 * productos a su inventario sin escribirlos a mano. La categoría SIEMPRE es
 * una de las 7 del Edge (Frutas, Verduras, Lácteos, Carnes, Proteínas,
 * Panadería, Snacks) para que el semáforo de frescura funcione.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogItem {

    private Long id;
    private String name;
    private String description;
    private String category;
    private String imageUrl;
}
