package com.acme.backendfreshsense.challenges.application.dto;

public record LeaderboardEntryDto(
        Long userId,
        Long challengeId,
        Integer progress,
        int rank
) {}
