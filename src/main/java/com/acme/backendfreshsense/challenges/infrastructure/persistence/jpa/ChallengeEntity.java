package com.acme.backendfreshsense.challenges.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "reward_pts", nullable = false)
    private Integer rewardPts;

    @Column(name = "start_at", nullable = false)
    private LocalDate startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDate endAt;

    @Column(name = "goal_type", nullable = false, length = 50)
    private String goalType;

    @Column(name = "goal_target", nullable = false)
    private Integer goalTarget;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;
}
