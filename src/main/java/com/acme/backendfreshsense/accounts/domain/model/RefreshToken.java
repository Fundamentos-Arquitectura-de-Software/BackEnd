package com.acme.backendfreshsense.accounts.domain.model;

import java.time.Instant;

public class RefreshToken {

    private Long id;
    private String token;
    private Long userId;
    private Instant expiresAt;
    private boolean revoked;

    public RefreshToken() {}

    public RefreshToken(String token, Long userId, Instant expiresAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

    public Long getId()         { return id; }
    public void setId(Long id)  { this.id = id; }
    public String getToken()    { return token; }
    public Long getUserId()     { return userId; }
    public Instant getExpiresAt() { return expiresAt; }
    public boolean isRevoked()  { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
}
