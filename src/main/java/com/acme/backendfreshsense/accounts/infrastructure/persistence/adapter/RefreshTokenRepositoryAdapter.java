package com.acme.backendfreshsense.accounts.infrastructure.persistence.adapter;

import com.acme.backendfreshsense.accounts.domain.model.RefreshToken;
import com.acme.backendfreshsense.accounts.domain.repository.RefreshTokenRepository;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa.RefreshTokenEntity;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa.RefreshTokenJpaRepository;

import java.util.Optional;

public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpa;

    public RefreshTokenRepositoryAdapter(RefreshTokenJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public RefreshToken save(RefreshToken token) {
        RefreshTokenEntity entity = new RefreshTokenEntity(token.getToken(), token.getUserId(), token.getExpiresAt());
        RefreshTokenEntity saved = jpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpa.findByToken(token).map(this::toDomain);
    }

    @Override
    public void revokeByToken(String token) {
        jpa.revokeByToken(token);
    }

    @Override
    public void revokeAllByUserId(Long userId) {
        jpa.revokeAllByUserId(userId);
    }

    private RefreshToken toDomain(RefreshTokenEntity e) {
        RefreshToken rt = new RefreshToken(e.getToken(), e.getUserId(), e.getExpiresAt());
        rt.setId(e.getId());
        rt.setRevoked(e.isRevoked());
        return rt;
    }
}
