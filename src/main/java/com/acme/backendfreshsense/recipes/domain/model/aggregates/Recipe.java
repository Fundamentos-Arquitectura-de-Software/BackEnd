package com.acme.backendfreshsense.recipes.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column
    private Integer rating; // 0–5

    @Column(length = 50)
    private String level;   // Easy / Intermediate / Advanced

    @Column(length = 50)
    private String type;    // Breakfast / Meals / Snacks / Desserts

    @Column(length = 50)
    private String time;    // Ej: "15 min"

    // ---- Ingredientes ----
    @ElementCollection
    @CollectionTable(
            name = "recipe_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id")
    )
    @Column(name = "ingredient", length = 300)
    @Builder.Default
    private List<String> ingredients = new ArrayList<>();

    // ---- Pasos ----
    @ElementCollection
    @CollectionTable(
            name = "recipe_steps",
            joinColumns = @JoinColumn(name = "recipe_id")
    )
    @Column(name = "step", length = 1000)
    @Builder.Default
    private List<String> steps = new ArrayList<>();
}
