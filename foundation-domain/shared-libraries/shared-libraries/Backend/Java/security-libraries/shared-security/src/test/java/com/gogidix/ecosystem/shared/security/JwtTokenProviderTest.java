package com.gogidix.ecosystem.shared.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for JwtTokenProvider.
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JWT Token Provider Tests")
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private UserPrincipal userPrincipal;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        
        // Set test values using reflection
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", 
            "test-secret-key-that-is-at-least-256-bits-long-for-testing-purposes");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInSeconds", 3600);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtRefreshExpirationInSeconds", 7200);

        // Create test user principal
        userPrincipal = UserPrincipal.create(
            1L, 
            "testuser", 
            "test@example.com", 
            "password",
            Arrays.asList("USER", "ADMIN"),
            true
        );

        authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal, 
            null, 
            userPrincipal.getAuthorities()
        );
    }

    @Test
    @DisplayName("Should generate valid access token")
    void shouldGenerateValidAccessToken() {
        // When
        String token = jwtTokenProvider.generateToken(authentication);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("Should generate valid refresh token")
    void shouldGenerateValidRefreshToken() {
        // When
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Then
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken).isNotEmpty();
        assertThat(jwtTokenProvider.validateToken(refreshToken)).isTrue();
        assertThat(jwtTokenProvider.isRefreshToken(refreshToken)).isTrue();
    }

    @Test
    @DisplayName("Should extract user ID from token")
    void shouldExtractUserIdFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        String userId = jwtTokenProvider.getUserIdFromToken(token);

        // Then
        assertThat(userId).isEqualTo("1");
    }

    @Test
    @DisplayName("Should extract username from token")
    void shouldExtractUsernameFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        String username = jwtTokenProvider.getUsernameFromToken(token);

        // Then
        assertThat(username).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should extract authorities from token")
    void shouldExtractAuthoritiesFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        List<String> authorities = jwtTokenProvider.getAuthoritiesFromToken(token);

        // Then
        assertThat(authorities).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    @DisplayName("Should get expiration date from token")
    void shouldGetExpirationDateFromToken() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        Date expirationDate = jwtTokenProvider.getExpirationDateFromToken(token);

        // Then
        assertThat(expirationDate).isAfter(new Date());
    }

    @Test
    @DisplayName("Should validate correct token")
    void shouldValidateCorrectToken() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should reject invalid token")
    void shouldRejectInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should reject null token")
    void shouldRejectNullToken() {
        // When
        boolean isValid = jwtTokenProvider.validateToken(null);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should reject empty token")
    void shouldRejectEmptyToken() {
        // When
        boolean isValid = jwtTokenProvider.validateToken("");

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should check if token is expired")
    void shouldCheckIfTokenIsExpired() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        boolean isExpired = jwtTokenProvider.isTokenExpired(token);

        // Then
        assertThat(isExpired).isFalse();
    }

    @Test
    @DisplayName("Should identify refresh token")
    void shouldIdentifyRefreshToken() {
        // Given
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication);

        // When & Then
        assertThat(jwtTokenProvider.isRefreshToken(refreshToken)).isTrue();
        assertThat(jwtTokenProvider.isRefreshToken(accessToken)).isFalse();
    }

    @Test
    @DisplayName("Should refresh token successfully")
    void shouldRefreshTokenSuccessfully() throws InterruptedException {
        // Given
        String originalToken = jwtTokenProvider.generateToken(authentication);
        
        // Wait to ensure different timestamp
        Thread.sleep(1);

        // When
        String refreshedToken = jwtTokenProvider.refreshToken(originalToken);

        // Then
        assertThat(refreshedToken).isNotNull();
        assertThat(jwtTokenProvider.validateToken(refreshedToken)).isTrue();
        assertThat(jwtTokenProvider.getUserIdFromToken(refreshedToken)).isEqualTo("1");
        
        // Verify that the refresh actually created a new expiration time
        Date originalExpiration = jwtTokenProvider.getExpirationDateFromToken(originalToken);
        Date refreshedExpiration = jwtTokenProvider.getExpirationDateFromToken(refreshedToken);
        assertThat(refreshedExpiration).isAfterOrEqualTo(originalExpiration);
    }

    @Test
    @DisplayName("Should fail to refresh invalid token")
    void shouldFailToRefreshInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        assertThatThrownBy(() -> jwtTokenProvider.refreshToken(invalidToken))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Token refresh failed");
    }

    @Test
    @DisplayName("Should check if token can be refreshed")
    void shouldCheckIfTokenCanBeRefreshed() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);

        // When
        boolean canBeRefreshed = jwtTokenProvider.canTokenBeRefreshed(token);

        // Then
        assertThat(canBeRefreshed).isTrue();
    }

    @Test
    @DisplayName("Should handle token with different secret")
    void shouldHandleTokenWithDifferentSecret() {
        // Given
        String token = jwtTokenProvider.generateToken(authentication);
        
        // Change the secret
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "different-secret-key");

        // When
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should handle malformed token gracefully")
    void shouldHandleMalformedTokenGracefully() {
        // Given
        String malformedToken = "malformed.token";

        // When & Then
        assertThat(jwtTokenProvider.validateToken(malformedToken)).isFalse();
        assertThat(jwtTokenProvider.isRefreshToken(malformedToken)).isFalse();
    }
}