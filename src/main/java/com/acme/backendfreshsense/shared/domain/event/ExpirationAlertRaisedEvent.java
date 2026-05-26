package com.acme.backendfreshsense.shared.domain.event;

public class ExpirationAlertRaisedEvent extends DomainEvent {

    private final Long userId;
    private final String productName;
    private final String severity;

    public ExpirationAlertRaisedEvent(Long userId, String productName, String severity) {
        super();
        this.userId = userId;
        this.productName = productName;
        this.severity = severity;
    }

    public Long getUserId()       { return userId; }
    public String getProductName() { return productName; }
    public String getSeverity()   { return severity; }
}
