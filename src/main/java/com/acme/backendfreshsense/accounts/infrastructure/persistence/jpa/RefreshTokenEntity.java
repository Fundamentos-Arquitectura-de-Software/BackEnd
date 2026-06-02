package com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    protected RefreshTokenEntity() {}

    public RefreshTokenEntity(String token, Long userId, Instant expiresAt) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public Long getId()           { return id; }
    public String getToken()      { return token; }
    public Long getUserId()       { return userId; }
    public Instant getExpiresAt() { return expiresAt; }
    public boolean isRevoked()    { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
}
