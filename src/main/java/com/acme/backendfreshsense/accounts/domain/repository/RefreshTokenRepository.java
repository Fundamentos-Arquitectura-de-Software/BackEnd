package com.acme.backendfreshsense.accounts.domain.repository;

import com.acme.backendfreshsense.accounts.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    void revokeByToken(String token);
    void revokeAllByUserId(Long userId);
}
