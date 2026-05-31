package com.gogidix.aiservices.aiauthenticationservice.domain.port.out;

import java.util.Set;
import java.util.UUID;

public interface TokenPolicy {
    String generateAccessToken(String userId, Set<String> roles);
    String generateRefreshToken(String userId);
    boolean validateAccessToken(String token);
    boolean validateRefreshToken(String token);
    String extractUserId(String token);
    Set<String> extractRoles(String token);
    boolean isTokenExpired(String token);
    void blacklistToken(String token);
    int getAccessTokenExpiration();
    int getRefreshTokenExpiration();
    Long extractExpiration(String token);
    Long extractIssuedAt(String token);
    String extractIssuer(String token);
}
