package com.acme.alertsservice.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alert")
@Getter
@Setter
@NoArgsConstructor
public class AlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String title;
    private String message;
    private String severity;
    private String source;
    private String state;

    @Column(name = "time_ago")
    private String timeAgo;
}
