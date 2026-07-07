package com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "catalog_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 300)
    private String description;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "image_url", length = 500)
    private String imageUrl;
}
