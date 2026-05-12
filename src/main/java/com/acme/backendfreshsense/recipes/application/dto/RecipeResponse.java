package com.acme.backendfreshsense.recipes.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponse {

    private Long id;
    private String title;
    private String description;
    private String image;
    private Integer rating;
    private String level;
    private String type;
    private String time;
    private List<String> ingredients;
    private List<String> steps;
}
