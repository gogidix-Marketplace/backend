package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * JWT Provider for token generation and validation.
 * <p>
 * Handles JWT token creation, validation, and claims extraction.
 */
@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret:my-secret-key-for-multi-tenant-saas-application-with-at-least-256-bits}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param userId the user ID
     * @param roles the user roles
     * @param tenantId the tenant ID
     * @return the JWT token
     */
    public String generateToken(String userId, List<String> roles, String tenantId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userId)
                .claim("roles", roles)
                .claim("tenantId", tenantId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Generates a refresh token.
     *
     * @param userId the user ID
     * @return the refresh token
     */
    public String generateRefreshToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (jwtExpirationMs * 7)); // 7 days

        return Jwts.builder()
                .setSubject(userId)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates the JWT token.
     *
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID
     */
    public String getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Extracts tenant ID from the JWT token.
     *
     * @param token the JWT token
     * @return the tenant ID
     */
    public String getTenantId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("tenantId", String.class);
    }

    /**
     * Extracts roles from the JWT token.
     *
     * @param token the JWT token
     * @return the list of roles
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (List<String>) claims.get("roles");
    }

    /**
     * Extracts expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    /**
     * Checks if the token is expired.
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    /**
     * Generates an access token.
     *
     * @param userId the user ID
     * @param tenantId the tenant ID
     * @param roles the user roles
     * @return the access token
     */
    public String generateAccessToken(String userId, String tenantId, Set<String> roles) {
        return generateToken(userId, List.copyOf(roles), tenantId);
    }
}
