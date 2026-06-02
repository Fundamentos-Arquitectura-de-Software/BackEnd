package com.acme.backendfreshsense.shared.domain.event;

public class UserRoleChangedEvent extends DomainEvent {

    private final Long userId;
    private final String previousRole;
    private final String newRole;

    public UserRoleChangedEvent(Long userId, String previousRole, String newRole) {
        super();
        this.userId = userId;
        this.previousRole = previousRole;
        this.newRole = newRole;
    }

    public Long getUserId()       { return userId; }
    public String getPreviousRole() { return previousRole; }
    public String getNewRole()    { return newRole; }
}
