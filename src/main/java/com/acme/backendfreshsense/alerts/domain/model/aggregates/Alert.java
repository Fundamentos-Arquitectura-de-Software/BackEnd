package com.acme.backendfreshsense.alerts.domain.model.aggregates;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Alert {

    private Long id;
    private String title;
    private String message;
    private String severity;
    private String source;
    private String state;
    private String timeAgo;
    private Long userId;
}
