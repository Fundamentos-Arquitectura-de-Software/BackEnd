package com.acme.backendfreshsense.challenges.application.dto;

import java.time.LocalDate;

public record ChallengeDto(
        Long id,
        String title,
        String description,
        Integer rewardPts,
        LocalDate startAt,
        LocalDate endAt,
        String goalType,
        Integer goalTarget,
        String status,
        String bannerUrl
) {}
