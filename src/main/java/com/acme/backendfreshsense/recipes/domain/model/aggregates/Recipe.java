package com.acme.backendfreshsense.recipes.domain.model.aggregates;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer rating;
    private String level;
    private String type;
    private String time;

    @Builder.Default
    private List<String> ingredients = new ArrayList<>();

    @Builder.Default
    private List<String> steps = new ArrayList<>();
}
