package com.acme.recipesservice.domain.model;

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
