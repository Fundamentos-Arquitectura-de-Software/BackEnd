package com.acme.backendfreshsense.challenges.domain.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Challenge {

    private Long id;
    private String title;
    private String description;
    private Integer rewardPts;
    private LocalDate startAt;
    private LocalDate endAt;
    private String goalType;
    private Integer goalTarget;
    private String status;
    private String bannerUrl;
}
