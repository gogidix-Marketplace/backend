package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT Token Provider for AI Fraud Detection Service.
 * Handles JWT token generation, validation, and claims extraction.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${security.jwt.secret:ai-fraud-detection-secret-key-for-financial-grade-blueprint}")
    private String secret;

    @Value("${security.jwt.expiration:3600000}") // 1 hour in milliseconds
    private long expiration;

    @Value("${security.jwt.header:Authorization}")
    private String tokenHeader;

    @Value("${security.jwt.prefix:Bearer }")
    private String tokenPrefix;

    /**
     * Generates a JWT token for the given username and roles.
     *
     * @param username The username
     * @param roles    The list of roles
     * @param tenantId The tenant ID
     * @return The JWT token
     */
    public String generateToken(String username, List<String> roles, String tenantId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .claim("tenantId", tenantId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates a JWT token.
     *
     * @param token The token to validate
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
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token
     * @return The username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Extracts the roles from a JWT token.
     *
     * @param token The JWT token
     * @return The list of roles
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object rolesObj = claims.get("roles");

        if (rolesObj instanceof List) {
            return ((List<?>) rolesObj).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    /**
     * Extracts the tenant ID from a JWT token.
     *
     * @param token The JWT token
     * @return The tenant ID
     */
    public String getTenantIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("tenantId", String.class);
    }

    /**
     * Extracts the JWT token from the HTTP request.
     *
     * @param request The HTTP request
     * @return The JWT token or null if not found
     */
    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenHeader);

        if (bearerToken != null && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(tokenPrefix.length());
        }

        return null;
    }

    /**
     * Gets the signing key from the configured secret.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts claims from a JWT token.
     *
     * @param token The JWT token
     * @return The claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
