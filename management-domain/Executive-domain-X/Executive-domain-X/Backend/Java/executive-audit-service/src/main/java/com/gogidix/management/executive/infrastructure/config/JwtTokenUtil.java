package com.gogidix.management.executive.audit.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for JWT token parsing and validation using jjwt library
 * Provides tenant context extraction and token validation for audit service
 */
@Component
public class JwtTokenUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret:ZnVja2RvZ2dpZGl4ZGV2ZWxvcG1lbnRzZWNyZXRrZXlmb3Jqd3R2ZXJpZmljYXRpb24=}")
    private String jwtSecret;

    @Value("${jwt.validity:86400000}")
    private Long jwtValidityMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractTenantId(String token) {
        try {
            return extractClaim(token, claims -> claims.get("tenant_id", String.class));
        } catch (Exception e) {
            log.warn("Failed to extract tenant_id from token: {}", e.getMessage());
            return null;
        }
    }

    public String extractUserId(String token) {
        try {
            return extractClaim(token, claims -> claims.get("user_id", String.class));
        } catch (Exception e) {
            log.warn("Failed to extract user_id from token: {}", e.getMessage());
            return null;
        }
    }

    public String extractRoles(String token) {
        try {
            return extractClaim(token, claims -> claims.get("roles", String.class));
        } catch (Exception e) {
            log.warn("Failed to extract roles from token: {}", e.getMessage());
            return null;
        }
    }

    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.warn("Failed to check token expiration: {}", e.getMessage());
            return true;
        }
    }

    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
