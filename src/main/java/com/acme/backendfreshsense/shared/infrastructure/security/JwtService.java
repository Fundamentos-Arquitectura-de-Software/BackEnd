package com.acme.backendfreshsense.shared.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtService(
            @Value("${authorization.jwt.secret}") String secret,
            @Value("${authorization.jwt.access.expiration.minutes:15}") int accessMinutes,
            @Value("${authorization.jwt.refresh.expiration.days:7}") int refreshDays) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = (long) accessMinutes * 60 * 1000;
        this.refreshExpirationMs = (long) refreshDays * 24 * 60 * 60 * 1000;
    }

    /** Access token corto (15 min por defecto) con claims userId y role. */
    public String generateAccessToken(String email, Long userId, String role) {
        return Jwts.builder()
                .subject(email)
                .claims(Map.of("userId", userId, "role", role, "type", "access"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(key)
                .compact();
    }

    /** Refresh token largo (7 días por defecto), solo subject y type. */
    public String generateRefreshToken(String email, Long userId) {
        return Jwts.builder()
                .subject(email)
                .claims(Map.of("userId", userId, "type", "refresh"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(key)
                .compact();
    }

    /** @deprecated Usar generateAccessToken. Mantenido por compatibilidad. */
    @Deprecated
    public String generateToken(String email, Long userId, String role) {
        return generateAccessToken(email, userId, role);
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        Object userId = parseClaims(token).get("userId");
        if (userId instanceof Integer) return ((Integer) userId).longValue();
        if (userId instanceof Long) return (Long) userId;
        return null;
    }

    public String extractRole(String token) {
        return (String) parseClaims(token).get("role");
    }

    public String extractType(String token) {
        return (String) parseClaims(token).get("type");
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
