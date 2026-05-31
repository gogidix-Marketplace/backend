package com.gogidix.shared.infrastructure.services.security.auth.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TokenRepositoryPort interface contract.
 * Tests the interface definition and expected behavior.
 */
@DisplayName("TokenRepositoryPort Interface Tests")
class TokenRepositoryPortTest {

    @Test
    @DisplayName("Should have save method in interface")
    void shouldHaveSaveMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            TokenRepositoryPort.class.getMethod("save", AuthToken.class);
        });
    }

    @Test
    @DisplayName("Should have findByToken method in interface")
    void shouldHaveFindByTokenMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            TokenRepositoryPort.class.getMethod("findByToken", String.class);
        });
    }

    @Test
    @DisplayName("Should have findByRefreshToken method in interface")
    void shouldHaveFindByRefreshTokenMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            TokenRepositoryPort.class.getMethod("findByRefreshToken", String.class);
        });
    }

    @Test
    @DisplayName("Should have findValidByToken method in interface")
    void shouldHaveFindValidByTokenMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            TokenRepositoryPort.class.getMethod("findValidByToken", String.class);
        });
    }

    @Test
    @DisplayName("Should have revokeAllUserTokens method in interface")
    void shouldHaveRevokeAllUserTokensMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            TokenRepositoryPort.class.getMethod("revokeAllUserTokens", String.class);
        });
    }

    @Test
    @DisplayName("Should have delete method in interface")
    void shouldHaveDeleteMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            TokenRepositoryPort.class.getMethod("delete", AuthToken.class);
        });
    }

    @Test
    @DisplayName("Should have deleteExpiredTokens method in interface")
    void shouldHaveDeleteExpiredTokensMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            TokenRepositoryPort.class.getMethod("deleteExpiredTokens");
        });
    }

    @Test
    @DisplayName("Should be an interface")
    void shouldBeAnInterface() {
        // Then
        assertTrue(TokenRepositoryPort.class.isInterface());
    }

    @Test
    @DisplayName("Should have correct return type for save method")
    void shouldHaveCorrectReturnTypeForSaveMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = TokenRepositoryPort.class.getMethod("save", AuthToken.class).getReturnType();

        // Then
        assertEquals(AuthToken.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for findByToken method")
    void shouldHaveCorrectReturnTypeForFindByTokenMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = TokenRepositoryPort.class.getMethod("findByToken", String.class).getReturnType();

        // Then
        assertEquals(Optional.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for findByRefreshToken method")
    void shouldHaveCorrectReturnTypeForFindByRefreshTokenMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = TokenRepositoryPort.class.getMethod("findByRefreshToken", String.class).getReturnType();

        // Then
        assertEquals(Optional.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for findValidByToken method")
    void shouldHaveCorrectReturnTypeForFindValidByTokenMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = TokenRepositoryPort.class.getMethod("findValidByToken", String.class).getReturnType();

        // Then
        assertEquals(Optional.class, returnType);
    }

    @Test
    @DisplayName("Should have void return type for revokeAllUserTokens method")
    void shouldHaveVoidReturnTypeForRevokeAllUserTokensMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = TokenRepositoryPort.class.getMethod("revokeAllUserTokens", String.class).getReturnType();

        // Then
        assertEquals(void.class, returnType);
    }

    @Test
    @DisplayName("Should have void return type for delete method")
    void shouldHaveVoidReturnTypeForDeleteMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = TokenRepositoryPort.class.getMethod("delete", AuthToken.class).getReturnType();

        // Then
        assertEquals(void.class, returnType);
    }

    @Test
    @DisplayName("Should have void return type for deleteExpiredTokens method")
    void shouldHaveVoidReturnTypeForDeleteExpiredTokensMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = TokenRepositoryPort.class.getMethod("deleteExpiredTokens").getReturnType();

        // Then
        assertEquals(void.class, returnType);
    }

    @Test
    @DisplayName("Should create mock implementation")
    void shouldCreateMockImplementation() {
        // Given & When
        TokenRepositoryPort mockPort = new TokenRepositoryPort() {
            @Override
            public AuthToken save(AuthToken token) {
                return token;
            }

            @Override
            public Optional<AuthToken> findByToken(String token) {
                return Optional.empty();
            }

            @Override
            public Optional<AuthToken> findByRefreshToken(String refreshToken) {
                return Optional.empty();
            }

            @Override
            public Optional<AuthToken> findValidByToken(String token) {
                return Optional.empty();
            }

            @Override
            public void revokeAllUserTokens(String userId) {
            }

            @Override
            public void delete(AuthToken token) {
            }

            @Override
            public void deleteExpiredTokens() {
            }
        };

        // Then
        assertNotNull(mockPort);
        assertDoesNotThrow(() -> mockPort.deleteExpiredTokens());
        assertDoesNotThrow(() -> mockPort.revokeAllUserTokens("user-123"));
    }

    @Test
    @DisplayName("Should verify interface is public")
    void shouldVerifyInterfaceIsPublic() {
        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(TokenRepositoryPort.class.getModifiers()));
    }

    @Test
    @DisplayName("Should have exactly 7 methods")
    void shouldHaveExactlySevenMethods() {
        // When
        int methodCount = TokenRepositoryPort.class.getDeclaredMethods().length;

        // Then
        assertEquals(7, methodCount, "TokenRepositoryPort should have exactly 7 methods");
    }

    @Test
    @DisplayName("Should verify deleteExpiredTokens takes no parameters")
    void shouldVerifyDeleteExpiredTokensTakesNoParameters() throws NoSuchMethodException {
        // When
        int parameterCount = TokenRepositoryPort.class.getMethod("deleteExpiredTokens").getParameterCount();

        // Then
        assertEquals(0, parameterCount);
    }
}
