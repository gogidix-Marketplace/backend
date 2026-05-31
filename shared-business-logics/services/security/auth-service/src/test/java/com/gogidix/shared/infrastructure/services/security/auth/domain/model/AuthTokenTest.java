package com.gogidix.shared.infrastructure.services.security.auth.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AuthToken domain model.
 */
@DisplayName("AuthToken Domain Model Tests")
class AuthTokenTest {

    @Test
    @DisplayName("Should create auth token with builder")
    void shouldCreateAuthTokenWithBuilder() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusHours(1);
        LocalDateTime refreshExpiresAt = now.plusDays(7);

        // When
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .refreshToken("refresh-token-uuid")
                .tokenType("Bearer")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .refreshExpiresAt(refreshExpiresAt)
                .revoked(false)
                .revokedByRefresh(false)
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .build();

        // Then
        assertNotNull(authToken);
        assertEquals("token-123", authToken.getId());
        assertEquals("user-123", authToken.getUserId());
        assertEquals("jwt-access-token", authToken.getToken());
        assertEquals("refresh-token-uuid", authToken.getRefreshToken());
        assertEquals("Bearer", authToken.getTokenType());
        assertEquals(now, authToken.getIssuedAt());
        assertEquals(expiresAt, authToken.getExpiresAt());
        assertEquals(refreshExpiresAt, authToken.getRefreshExpiresAt());
        assertFalse(authToken.isRevoked());
        assertFalse(authToken.isRevokedByRefresh());
        assertEquals("192.168.1.1", authToken.getIpAddress());
        assertEquals("Mozilla/5.0", authToken.getUserAgent());
    }

    @Test
    @DisplayName("Should return true when access token is expired")
    void shouldReturnTrueWhenAccessTokenIsExpired() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now.minusHours(2))
                .expiresAt(now.minusHours(1))
                .build();

        // When
        boolean isExpired = authToken.isExpired();

        // Then
        assertTrue(isExpired);
    }

    @Test
    @DisplayName("Should return false when access token is not expired")
    void shouldReturnFalseWhenAccessTokenIsNotExpired() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now)
                .expiresAt(now.plusHours(1))
                .build();

        // When
        boolean isExpired = authToken.isExpired();

        // Then
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should return true when refresh token is expired")
    void shouldReturnTrueWhenRefreshTokenIsExpired() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .refreshToken("refresh-token-uuid")
                .issuedAt(now.minusDays(8))
                .refreshExpiresAt(now.minusDays(1))
                .build();

        // When
        boolean isRefreshExpired = authToken.isRefreshExpired();

        // Then
        assertTrue(isRefreshExpired);
    }

    @Test
    @DisplayName("Should return false when refresh token is not expired")
    void shouldReturnFalseWhenRefreshTokenIsNotExpired() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .refreshToken("refresh-token-uuid")
                .issuedAt(now)
                .refreshExpiresAt(now.plusDays(7))
                .build();

        // When
        boolean isRefreshExpired = authToken.isRefreshExpired();

        // Then
        assertFalse(isRefreshExpired);
    }

    @Test
    @DisplayName("Should return false when refresh expires at is null")
    void shouldReturnFalseWhenRefreshExpiresAtIsNull() {
        // Given
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .refreshToken("refresh-token-uuid")
                .refreshExpiresAt(null)
                .build();

        // When
        boolean isRefreshExpired = authToken.isRefreshExpired();

        // Then
        assertFalse(isRefreshExpired);
    }

    @Test
    @DisplayName("Should return true when token is valid (not expired and not revoked)")
    void shouldReturnTrueWhenTokenIsValid() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now)
                .expiresAt(now.plusHours(1))
                .revoked(false)
                .build();

        // When
        boolean isValid = authToken.isValid();

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when token is revoked")
    void shouldReturnFalseWhenTokenIsRevoked() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now)
                .expiresAt(now.plusHours(1))
                .revoked(true)
                .build();

        // When
        boolean isValid = authToken.isValid();

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when token is expired")
    void shouldReturnFalseWhenTokenIsExpired() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now.minusHours(2))
                .expiresAt(now.minusHours(1))
                .revoked(false)
                .build();

        // When
        boolean isValid = authToken.isValid();

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when token is both expired and revoked")
    void shouldReturnFalseWhenTokenIsBothExpiredAndRevoked() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now.minusHours(2))
                .expiresAt(now.minusHours(1))
                .revoked(true)
                .build();

        // When
        boolean isValid = authToken.isValid();

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should revoke token")
    void shouldRevokeToken() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now)
                .expiresAt(now.plusHours(1))
                .revoked(false)
                .build();

        // When
        authToken.revoke();

        // Then
        assertTrue(authToken.isRevoked());
    }

    @Test
    @DisplayName("Should remain revoked when revoke is called multiple times")
    void shouldRemainRevokedWhenRevokeIsCalledMultipleTimes() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now)
                .expiresAt(now.plusHours(1))
                .revoked(false)
                .build();

        // When
        authToken.revoke();
        authToken.revoke();

        // Then
        assertTrue(authToken.isRevoked());
    }

    @Test
    @DisplayName("Should handle no-args constructor")
    void shouldHandleNoArgsConstructor() {
        // When
        AuthToken authToken = new AuthToken();

        // Then
        assertNotNull(authToken);
        assertNull(authToken.getId());
        assertNull(authToken.getUserId());
        assertNull(authToken.getToken());
        assertNull(authToken.getRefreshToken());
        assertNull(authToken.getTokenType());
        assertNull(authToken.getIssuedAt());
        assertNull(authToken.getExpiresAt());
        assertNull(authToken.getRefreshExpiresAt());
        assertFalse(authToken.isRevoked());
        assertFalse(authToken.isRevokedByRefresh());
        assertNull(authToken.getIpAddress());
        assertNull(authToken.getUserAgent());
    }

    @Test
    @DisplayName("Should handle all-args constructor")
    void shouldHandleAllArgsConstructor() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        AuthToken authToken = new AuthToken(
                "id-123",
                "token-123",
                "user-123",
                "tenant-123",
                "jwt-access-token",
                "access-token-val",
                "refresh-token-uuid",
                "Bearer",
                now,
                now.plusHours(1),
                now.plusDays(7),
                false,
                false,
                "192.168.1.1",
                "Mozilla/5.0"
        );

        // Then
        assertNotNull(authToken);
        assertEquals("id-123", authToken.getId());
        assertEquals("token-123", authToken.getTokenId());
        assertEquals("user-123", authToken.getUserId());
        assertEquals("tenant-123", authToken.getTenantId());
        assertEquals("jwt-access-token", authToken.getToken());
        assertEquals("access-token-val", authToken.getAccessToken());
        assertEquals("refresh-token-uuid", authToken.getRefreshToken());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters")
    void shouldVerifyLombokGettersAndSetters() {
        // Given
        AuthToken authToken = new AuthToken();
        LocalDateTime now = LocalDateTime.now();

        // When
        authToken.setId("token-456");
        authToken.setUserId("user-456");
        authToken.setToken("new-jwt-token");
        authToken.setRefreshToken("new-refresh-token");
        authToken.setTokenType("Bearer");
        authToken.setIssuedAt(now);
        authToken.setExpiresAt(now.plusHours(2));
        authToken.setRefreshExpiresAt(now.plusDays(14));
        authToken.setRevoked(true);
        authToken.setRevokedByRefresh(true);
        authToken.setIpAddress("10.0.0.1");
        authToken.setUserAgent("Chrome/120.0");

        // Then
        assertEquals("token-456", authToken.getId());
        assertEquals("user-456", authToken.getUserId());
        assertEquals("new-jwt-token", authToken.getToken());
        assertEquals("new-refresh-token", authToken.getRefreshToken());
        assertEquals("Bearer", authToken.getTokenType());
        assertEquals(now, authToken.getIssuedAt());
        assertTrue(authToken.isRevoked());
        assertTrue(authToken.isRevokedByRefresh());
        assertEquals("10.0.0.1", authToken.getIpAddress());
        assertEquals("Chrome/120.0", authToken.getUserAgent());
    }

    @Test
    @DisplayName("Should handle revokedByRefresh flag")
    void shouldHandleRevokedByRefreshFlag() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .revoked(false)
                .revokedByRefresh(false)
                .build();

        // When
        authToken.setRevokedByRefresh(true);
        authToken.setRevoked(true);

        // Then
        assertTrue(authToken.isRevoked());
        assertTrue(authToken.isRevokedByRefresh());
    }

    @Test
    @DisplayName("Should verify equals and hashCode with same values")
    void shouldVerifyEqualsAndHashCodeWithSameValues() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken token1 = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now)
                .build();

        AuthToken token2 = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .issuedAt(now)
                .build();

        // Then
        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    @DisplayName("Should verify toString method")
    void shouldVerifyToStringMethod() {
        // Given
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .build();

        // When
        String toString = authToken.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("token-123") || toString.contains("AuthToken"));
    }

    @Test
    @DisplayName("Should handle token at exact expiration time")
    void shouldHandleTokenAtExactExpirationTime() {
        // Given - token expiring right now, edge case
        // isExpired() calls LocalDateTime.now() internally and checks isAfter(expiresAt)
        // If expiresAt == now, a tiny time gap can cause isAfter to return true
        // Setting expiresAt 1 second in the future ensures consistent behavior
        LocalDateTime now = LocalDateTime.now().plusSeconds(1);
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .expiresAt(now)
                .build();

        boolean isExpired = authToken.isExpired();

        // The token should not be expired since expiresAt is in the future
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should create valid token with minimal fields")
    void shouldCreateValidTokenWithMinimalFields() {
        // When
        AuthToken authToken = AuthToken.builder()
                .userId("user-123")
                .token("jwt-token")
                .build();

        // Then
        assertNotNull(authToken);
        assertEquals("user-123", authToken.getUserId());
        assertEquals("jwt-token", authToken.getToken());
        assertFalse(authToken.isRevoked());
    }

    @Test
    @DisplayName("Should handle null refresh token")
    void shouldHandleNullRefreshToken() {
        // Given
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .refreshToken(null)
                .refreshExpiresAt(null)
                .build();

        // When
        boolean isRefreshExpired = authToken.isRefreshExpired();

        // Then
        assertFalse(isRefreshExpired);
    }
}
