package com.acme.backendfreshsense.recipes.interfaces.rest.resources;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeResource {

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
