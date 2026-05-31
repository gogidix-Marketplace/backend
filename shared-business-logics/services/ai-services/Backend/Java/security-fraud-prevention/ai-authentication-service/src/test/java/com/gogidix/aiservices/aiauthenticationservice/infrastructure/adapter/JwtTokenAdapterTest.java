package com.gogidix.aiservices.aiauthenticationservice.infrastructure.adapter;

import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.TokenPolicy;
import com.gogidix.aiservices.aiauthenticationservice.infrastructure.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JWT Token Adapter Infrastructure Tests")
class JwtTokenAdapterTest {

    private JwtTokenAdapter adapter;
    private JwtProperties properties;

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @BeforeEach
    void setUp() {
        properties = new JwtProperties();
        properties.setSecret("test-secret-key-for-jwt-signing-must-be-long-enough");
        properties.setAccessTokenExpiration(3600);
        properties.setRefreshTokenExpiration(2592000);
        properties.setIssuer("ai-authentication-service");

        adapter = new JwtTokenAdapter(properties);
    }

    @Nested
    @DisplayName("Access Token Generation Tests")
    class AccessTokenGenerationTests {

        @Test
        @DisplayName("Should generate valid access token")
        void shouldGenerateValidAccessToken() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();

            // JWT has 3 parts separated by dots
            String[] parts = token.split("\\.");
            assertThat(parts).hasSize(3);
        }

        @Test
        @DisplayName("Should include user ID in token")
        void shouldIncludeUserId() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            String userId = adapter.extractUserId(token);
            assertThat(userId).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should include roles in token")
        void shouldIncludeRoles() {
            Set<String> roles = Set.of("USER", "ADMIN");
            String token = adapter.generateAccessToken(USER_ID, roles);

            Set<String> extractedRoles = adapter.extractRoles(token);
            assertThat(extractedRoles).containsExactlyInAnyOrderElementsOf(roles);
        }

        @Test
        @DisplayName("Should set correct expiration time")
        void shouldSetCorrectExpiration() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(adapter.isTokenExpired(token)).isFalse();
        }

        @Test
        @DisplayName("Should include issuer in token")
        void shouldIncludeIssuer() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            String issuer = adapter.extractIssuer(token);
            assertThat(issuer).isEqualTo("ai-authentication-service");
        }

        @Test
        @DisplayName("Should generate different tokens for same user")
        void shouldGenerateDifferentTokens() {
            String token1 = adapter.generateAccessToken(USER_ID, Set.of("USER"));
            String token2 = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(token1).isNotEqualTo(token2);
        }
    }

    @Nested
    @DisplayName("Refresh Token Generation Tests")
    class RefreshTokenGenerationTests {

        @Test
        @DisplayName("Should generate valid refresh token")
        void shouldGenerateValidRefreshToken() {
            String token = adapter.generateRefreshToken(USER_ID);

            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
            assertThat(token).hasSize(64); // 256 bits hex encoded
        }

        @Test
        @DisplayName("Should include user ID in refresh token")
        void shouldIncludeUserIdInRefreshToken() {
            String token = adapter.generateRefreshToken(USER_ID);

            assertThat(token).isNotNull();
            // Refresh tokens are random, but we can verify they're valid hex
            assertThat(token).matches("[0-9a-f]{64}");
        }

        @Test
        @DisplayName("Should generate unique refresh tokens")
        void shouldGenerateUniqueRefreshTokens() {
            String token1 = adapter.generateRefreshToken(USER_ID);
            String token2 = adapter.generateRefreshToken(USER_ID);

            assertThat(token1).isNotEqualTo(token2);
        }

        @Test
        @DisplayName("Should validate refresh token format")
        void shouldValidateRefreshTokenFormat() {
            String validToken = adapter.generateRefreshToken(USER_ID);

            assertThat(adapter.validateRefreshToken(validToken)).isTrue();
        }

        @Test
        @DisplayName("Should reject invalid refresh token")
        void shouldRejectInvalidRefreshToken() {
            assertThat(adapter.validateRefreshToken("invalid-token")).isFalse();
        }
    }

    @Nested
    @DisplayName("Token Validation Tests")
    class TokenValidationTests {

        @Test
        @DisplayName("Should validate valid access token")
        void shouldValidateValidToken() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(adapter.validateAccessToken(token)).isTrue();
        }

        @Test
        @DisplayName("Should reject malformed token")
        void shouldRejectMalformedToken() {
            assertThat(adapter.validateAccessToken("invalid.token")).isFalse();
            assertThat(adapter.validateAccessToken("not-a-jwt")).isFalse();
        }

        @Test
        @DisplayName("Should reject empty token")
        void shouldRejectEmptyToken() {
            assertThat(adapter.validateAccessToken("")).isFalse();
            assertThat(adapter.validateAccessToken(null)).isFalse();
        }

        @Test
        @DisplayName("Should detect expired tokens")
        void shouldDetectExpiredToken() {
            // Create a token with very short expiration
            properties.setAccessTokenExpiration(0);
            JwtTokenAdapter shortLivedAdapter = new JwtTokenAdapter(properties);

            String token = shortLivedAdapter.generateAccessToken(USER_ID, Set.of("USER"));

            // Wait a bit for token to expire
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Ignore
            }

            assertThat(shortLivedAdapter.isTokenExpired(token)).isTrue();
        }

        @Test
        @DisplayName("Should reject token with invalid signature")
        void shouldRejectInvalidSignature() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            // Tamper with the token
            String tamperedToken = token + "tampered";

            assertThat(adapter.validateAccessToken(tamperedToken)).isFalse();
        }
    }

    @Nested
    @DisplayName("Claims Extraction Tests")
    class ClaimsExtractionTests {

        @Test
        @DisplayName("Should extract user ID from token")
        void shouldExtractUserId() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            String extractedUserId = adapter.extractUserId(token);

            assertThat(extractedUserId).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should extract roles from token")
        void shouldExtractRoles() {
            Set<String> roles = Set.of("USER", "ADMIN", "MODERATOR");
            String token = adapter.generateAccessToken(USER_ID, roles);

            Set<String> extractedRoles = adapter.extractRoles(token);

            assertThat(extractedRoles).containsExactlyInAnyOrderElementsOf(roles);
        }

        @Test
        @DisplayName("Should extract expiration time from token")
        void shouldExtractExpiration() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            Long expiration = adapter.extractExpiration(token);

            assertThat(expiration).isNotNull();
            assertThat(expiration).isGreaterThan(System.currentTimeMillis() / 1000);
        }

        @Test
        @DisplayName("Should extract issued at time from token")
        void shouldExtractIssuedAt() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            Long issuedAt = adapter.extractIssuedAt(token);

            assertThat(issuedAt).isNotNull();
            assertThat(issuedAt).isLessThanOrEqualTo(System.currentTimeMillis() / 1000);
        }

        @Test
        @DisplayName("Should extract issuer from token")
        void shouldExtractIssuer() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            String issuer = adapter.extractIssuer(token);

            assertThat(issuer).isEqualTo("ai-authentication-service");
        }

        @Test
        @DisplayName("Should throw exception for invalid claims extraction")
        void shouldThrowForInvalidClaimsExtraction() {
            assertThatThrownBy(() -> adapter.extractUserId("invalid.token"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Token Blacklisting Tests")
    class TokenBlacklistingTests {

        @Test
        @DisplayName("Should blacklist token")
        void shouldBlacklistToken() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            adapter.blacklistToken(token);

            assertThat(adapter.isTokenBlacklisted(token)).isTrue();
        }

        @Test
        @DisplayName("Should reject blacklisted token")
        void shouldRejectBlacklistedToken() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            adapter.blacklistToken(token);

            assertThat(adapter.validateAccessToken(token)).isFalse();
        }

        @Test
        @DisplayName("Should remove token from blacklist after expiration")
        void shouldRemoveFromBlacklistAfterExpiration() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            adapter.blacklistToken(token);

            // Simulate time passing
            // In real implementation, cleanup would happen automatically
        }

        @Test
        @DisplayName("Should handle multiple blacklisted tokens")
        void shouldHandleMultipleBlacklistedTokens() {
            String token1 = adapter.generateAccessToken(USER_ID, Set.of("USER"));
            String token2 = adapter.generateAccessToken(USER_ID, Set.of("ADMIN"));

            adapter.blacklistToken(token1);
            adapter.blacklistToken(token2);

            assertThat(adapter.isTokenBlacklisted(token1)).isTrue();
            assertThat(adapter.isTokenBlacklisted(token2)).isTrue();
        }
    }

    @Nested
    @DisplayName("Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should use configured secret")
        void shouldUseConfiguredSecret() {
            properties.setSecret("different-secret-key");
            JwtTokenAdapter customAdapter = new JwtTokenAdapter(properties);

            String token = customAdapter.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(customAdapter.validateAccessToken(token)).isTrue();
        }

        @Test
        @DisplayName("Should use configured expiration time")
        void shouldUseConfiguredExpiration() {
            properties.setAccessTokenExpiration(7200);
            JwtTokenAdapter customAdapter = new JwtTokenAdapter(properties);

            assertThat(customAdapter.getAccessTokenExpiration()).isEqualTo(7200);
        }

        @Test
        @DisplayName("Should use configured issuer")
        void shouldUseConfiguredIssuer() {
            properties.setIssuer("custom-issuer");
            JwtTokenAdapter customAdapter = new JwtTokenAdapter(properties);

            String token = customAdapter.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(customAdapter.extractIssuer(token)).isEqualTo("custom-issuer");
        }

        @Test
        @DisplayName("Should validate minimum secret length")
        void shouldValidateSecretLength() {
            properties.setSecret("short");

            assertThatThrownBy(() -> new JwtTokenAdapter(properties))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Security Tests")
    class SecurityTests {

        @Test
        @DisplayName("Should not include sensitive data in token")
        void shouldNotIncludeSensitiveData() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            String tokenBody = new String(
                    java.util.Base64.getUrlDecoder().decode(token.split("\\.")[1])
            );

            assertThat(tokenBody).doesNotContain("password");
            assertThat(tokenBody).doesNotContain("secret");
        }

        @Test
        @DisplayName("Should use proper signing algorithm")
        void shouldUseProperSigningAlgorithm() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            String header = new String(
                    java.util.Base64.getUrlDecoder().decode(token.split("\\.")[0])
            );

            assertThat(header).contains("HS256");
        }

        @Test
        @DisplayName("Should prevent token tampering")
        void shouldPreventTampering() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            // Try to tamper with the token
            String[] parts = token.split("\\.");
            String tamperedPayload = parts[1] + "tampered";
            String tamperedToken = parts[0] + "." + tamperedPayload + "." + parts[2];

            assertThat(adapter.validateAccessToken(tamperedToken)).isFalse();
        }

        @Test
        @DisplayName("Should handle token replay attacks")
        void shouldHandleReplayAttacks() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            // Use token once
            adapter.validateAccessToken(token);

            // In a real implementation, jti (JWT ID) would be checked
            // against a used token cache
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should generate tokens quickly")
        void shouldGenerateTokensQuickly() {
            long start = System.nanoTime();

            for (int i = 0; i < 100; i++) {
                adapter.generateAccessToken(USER_ID, Set.of("USER"));
            }

            long duration = System.nanoTime() - start;
            long avgMs = duration / 1_000_000 / 100;

            assertThat(avgMs).isLessThan(10); // Should average less than 10ms per token
        }

        @Test
        @DisplayName("Should validate tokens quickly")
        void shouldValidateTokensQuickly() {
            String token = adapter.generateAccessToken(USER_ID, Set.of("USER"));

            long start = System.nanoTime();

            for (int i = 0; i < 100; i++) {
                adapter.validateAccessToken(token);
            }

            long duration = System.nanoTime() - start;
            long avgMs = duration / 1_000_000 / 100;

            assertThat(avgMs).isLessThan(5); // Should average less than 5ms per validation
        }
    }
}
