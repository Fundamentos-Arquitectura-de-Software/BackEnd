package com.acme.backendfreshsense.challenges.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    private Long id;
    private Long challengeId;
    private Long userId;
    private Integer progress;
    private String status;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
}
