package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.out.TokenRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TokenRepositoryAdapter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TokenRepositoryAdapter Tests")
class TokenRepositoryAdapterTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private TokenRepositoryAdapter tokenRepositoryAdapter;

    private AuthToken testToken;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        testToken = AuthToken.builder()
                .id("token-123")
                .userId("user-123")
                .token("jwt-access-token")
                .refreshToken("refresh-token-uuid")
                .tokenType("Bearer")
                .issuedAt(now)
                .expiresAt(now.plusHours(1))
                .refreshExpiresAt(now.plusDays(7))
                .revoked(false)
                .build();
    }

    @Test
    @DisplayName("Should implement TokenRepositoryPort")
    void shouldImplementTokenRepositoryPort() {
        // Then
        assertTrue(tokenRepositoryAdapter instanceof TokenRepositoryPort);
    }

    @Test
    @DisplayName("Should save token successfully")
    void shouldSaveTokenSuccessfully() {
        // Given
        when(tokenRepository.save(any(AuthToken.class))).thenReturn(testToken);

        // When
        AuthToken result = tokenRepositoryAdapter.save(testToken);

        // Then
        assertNotNull(result);
        assertEquals(testToken, result);
        verify(tokenRepository).save(testToken);
    }

    @Test
    @DisplayName("Should find token by token value")
    void shouldFindTokenByTokenValue() {
        // Given
        String tokenValue = "jwt-access-token";
        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(testToken));

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findByToken(tokenValue);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testToken, result.get());
        verify(tokenRepository).findByToken(tokenValue);
    }

    @Test
    @DisplayName("Should return empty when token not found by value")
    void shouldReturnEmptyWhenTokenNotFoundByValue() {
        // Given
        String tokenValue = "non-existent-token";
        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findByToken(tokenValue);

        // Then
        assertFalse(result.isPresent());
        verify(tokenRepository).findByToken(tokenValue);
    }

    @Test
    @DisplayName("Should find token by refresh token value")
    void shouldFindTokenByRefreshTokenValue() {
        // Given
        String refreshToken = "refresh-token-uuid";
        when(tokenRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.of(testToken));

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findByRefreshToken(refreshToken);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testToken, result.get());
        verify(tokenRepository).findByRefreshToken(refreshToken);
    }

    @Test
    @DisplayName("Should return empty when refresh token not found")
    void shouldReturnEmptyWhenRefreshTokenNotFound() {
        // Given
        String refreshToken = "non-existent-refresh-token";
        when(tokenRepository.findByRefreshToken(refreshToken)).thenReturn(Optional.empty());

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findByRefreshToken(refreshToken);

        // Then
        assertFalse(result.isPresent());
        verify(tokenRepository).findByRefreshToken(refreshToken);
    }

    @Test
    @DisplayName("Should find valid token by value")
    void shouldFindValidTokenByValue() {
        // Given
        String tokenValue = "jwt-access-token";
        when(tokenRepository.findByTokenAndRevokedFalseAndExpiresAtAfter(eq(tokenValue), any(LocalDateTime.class)))
                .thenReturn(Optional.of(testToken));

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findValidByToken(tokenValue);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testToken, result.get());
        verify(tokenRepository).findByTokenAndRevokedFalseAndExpiresAtAfter(eq(tokenValue), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should return empty when valid token not found")
    void shouldReturnEmptyWhenValidTokenNotFound() {
        // Given
        String tokenValue = "non-existent-token";
        when(tokenRepository.findByTokenAndRevokedFalseAndExpiresAtAfter(eq(tokenValue), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findValidByToken(tokenValue);

        // Then
        assertFalse(result.isPresent());
        verify(tokenRepository).findByTokenAndRevokedFalseAndExpiresAtAfter(eq(tokenValue), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should revoke all user tokens")
    void shouldRevokeAllUserTokens() {
        // Given
        String userId = "user-123";

        // When
        tokenRepositoryAdapter.revokeAllUserTokens(userId);

        // Then
        verify(mongoTemplate).updateMulti(any(), any(), eq(AuthToken.class));
    }

    @Test
    @DisplayName("Should revoke all tokens for different users")
    void shouldRevokeAllTokensForDifferentUsers() {
        // Given
        String user1 = "user-1";
        String user2 = "user-2";

        // When
        tokenRepositoryAdapter.revokeAllUserTokens(user1);
        tokenRepositoryAdapter.revokeAllUserTokens(user2);

        // Then
        verify(mongoTemplate, times(2)).updateMulti(any(), any(), eq(AuthToken.class));
    }

    @Test
    @DisplayName("Should delete token")
    void shouldDeleteToken() {
        // When
        tokenRepositoryAdapter.delete(testToken);

        // Then
        verify(tokenRepository).delete(testToken);
    }

    @Test
    @DisplayName("Should handle deleting null token")
    void shouldHandleDeletingNullToken() {
        // When
        tokenRepositoryAdapter.delete(null);

        // Then
        verify(tokenRepository).delete(null);
    }

    @Test
    @DisplayName("Should delete expired tokens")
    void shouldDeleteExpiredTokens() {
        // When
        tokenRepositoryAdapter.deleteExpiredTokens();

        // Then
        verify(tokenRepository).deleteByExpiresAtBefore(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should forward save call to repository")
    void shouldForwardSaveCallToRepository() {
        // Given
        when(tokenRepository.save(any(AuthToken.class))).thenReturn(testToken);

        // When
        tokenRepositoryAdapter.save(testToken);

        // Then
        verify(tokenRepository, times(1)).save(testToken);
    }

    @Test
    @DisplayName("Should handle saving null token")
    void shouldHandleSavingNullToken() {
        // Given
        when(tokenRepository.save(any())).thenReturn(null);

        // When
        AuthToken result = tokenRepositoryAdapter.save(null);

        // Then
        assertNull(result);
        verify(tokenRepository).save(null);
    }

    @Test
    @DisplayName("Should handle finding by null token value")
    void shouldHandleFindingByNullTokenValue() {
        // Given
        when(tokenRepository.findByToken(null)).thenReturn(Optional.empty());

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findByToken(null);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should handle finding by null refresh token value")
    void shouldHandleFindingByNullRefreshTokenValue() {
        // Given
        when(tokenRepository.findByRefreshToken(null)).thenReturn(Optional.empty());

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findByRefreshToken(null);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should handle empty token string")
    void shouldHandleEmptyTokenString() {
        // Given
        when(tokenRepository.findByToken("")).thenReturn(Optional.empty());

        // When
        Optional<AuthToken> result = tokenRepositoryAdapter.findByToken("");

        // Then
        assertFalse(result.isPresent());
        verify(tokenRepository).findByToken("");
    }

    @Test
    @DisplayName("Should verify adapter is component")
    void shouldVerifyAdapterIsComponent() {
        // Then
        assertTrue(TokenRepositoryAdapter.class.isAnnotationPresent(org.springframework.stereotype.Component.class));
    }

    @Test
    @DisplayName("Should verify adapter has constructor accepting dependencies")
    void shouldVerifyAdapterHasRequiredArgsConstructorAnnotation() throws NoSuchMethodException {
        assertNotNull(TokenRepositoryAdapter.class.getDeclaredConstructor(TokenRepository.class, MongoTemplate.class));
    }

    @Test
    @DisplayName("Should handle multiple deleteExpiredTokens calls")
    void shouldHandleMultipleDeleteExpiredTokensCalls() {
        // When
        tokenRepositoryAdapter.deleteExpiredTokens();
        tokenRepositoryAdapter.deleteExpiredTokens();
        tokenRepositoryAdapter.deleteExpiredTokens();

        // Then
        verify(tokenRepository, times(3)).deleteByExpiresAtBefore(any(LocalDateTime.class));
    }
}
