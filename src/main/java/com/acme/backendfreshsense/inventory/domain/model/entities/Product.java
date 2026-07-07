package com.acme.backendfreshsense.inventory.domain.model.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private Long id;
    private Long userId;
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private String imageUrl;
    private LocalDate expirationDate;
    private LocalDateTime createdAt;
}
