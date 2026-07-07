package com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Nullable: los productos previos a esta feature no tienen fecha (sin semáforo por vencimiento).
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
