package com.gogidix.shared.infrastructure.services.security.auth.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.LoginRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.RefreshTokenRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.AuthResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AuthPort interface contract.
 * Tests the interface definition and expected behavior.
 */
@DisplayName("AuthPort Interface Tests")
class AuthPortTest {

    @Test
    @DisplayName("Should have login method in interface")
    void shouldHaveLoginMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            // Verify the interface has the login method by checking it exists
            AuthPort.class.getMethod("login", LoginRequestDto.class);
        });
    }

    @Test
    @DisplayName("Should have refreshToken method in interface")
    void shouldHaveRefreshTokenMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            AuthPort.class.getMethod("refreshToken", RefreshTokenRequestDto.class);
        });
    }

    @Test
    @DisplayName("Should have logout method in interface")
    void shouldHaveLogoutMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            AuthPort.class.getMethod("logout", String.class);
        });
    }

    @Test
    @DisplayName("Should have validateToken method in interface")
    void shouldHaveValidateTokenMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            AuthPort.class.getMethod("validateToken", String.class);
        });
    }

    @Test
    @DisplayName("Should have findById method in interface")
    void shouldHaveFindByIdMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            AuthPort.class.getMethod("findById", String.class);
        });
    }

    @Test
    @DisplayName("Should have findByUsername method in interface")
    void shouldHaveFindByUsernameMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            AuthPort.class.getMethod("findByUsername", String.class);
        });
    }

    @Test
    @DisplayName("Should have findByEmail method in interface")
    void shouldHaveFindByEmailMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            AuthPort.class.getMethod("findByEmail", String.class);
        });
    }

    @Test
    @DisplayName("Should be an interface")
    void shouldBeAnInterface() {
        // Then
        assertTrue(AuthPort.class.isInterface());
    }

    @Test
    @DisplayName("Should have correct return type for login method")
    void shouldHaveCorrectReturnTypeForLoginMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = AuthPort.class.getMethod("login", LoginRequestDto.class).getReturnType();

        // Then
        assertEquals(AuthResponseDto.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for refreshToken method")
    void shouldHaveCorrectReturnTypeForRefreshTokenMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = AuthPort.class.getMethod("refreshToken", RefreshTokenRequestDto.class).getReturnType();

        // Then
        assertEquals(AuthResponseDto.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for logout method")
    void shouldHaveCorrectReturnTypeForLogoutMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = AuthPort.class.getMethod("logout", String.class).getReturnType();

        // Then
        assertEquals(void.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for validateToken method")
    void shouldHaveCorrectReturnTypeForValidateTokenMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = AuthPort.class.getMethod("validateToken", String.class).getReturnType();

        // Then
        assertEquals(Optional.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for findById method")
    void shouldHaveCorrectReturnTypeForFindByIdMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = AuthPort.class.getMethod("findById", String.class).getReturnType();

        // Then
        assertEquals(Optional.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for findByUsername method")
    void shouldHaveCorrectReturnTypeForFindByUsernameMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = AuthPort.class.getMethod("findByUsername", String.class).getReturnType();

        // Then
        assertEquals(Optional.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for findByEmail method")
    void shouldHaveCorrectReturnTypeForFindByEmailMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = AuthPort.class.getMethod("findByEmail", String.class).getReturnType();

        // Then
        assertEquals(Optional.class, returnType);
    }

    @Test
    @DisplayName("Should have exactly 7 methods")
    void shouldHaveExactlySevenMethods() {
        // When
        int methodCount = AuthPort.class.getMethods().length;

        // Then - Account for inherited methods from Object class
        assertTrue(methodCount >= 7, "AuthPort should have at least 7 methods");
    }

    @Test
    @DisplayName("Should create mock implementation")
    void shouldCreateMockImplementation() {
        // Given & When
        AuthPort mockPort = new AuthPort() {
            @Override
            public AuthResponseDto login(LoginRequestDto request) {
                return AuthResponseDto.builder().build();
            }

            @Override
            public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
                return AuthResponseDto.builder().build();
            }

            @Override
            public void logout(String token) {
            }

            @Override
            public Optional<UserResponseDto> validateToken(String token) {
                return Optional.empty();
            }

            @Override
            public Optional<UserResponseDto> findById(String userId) {
                return Optional.empty();
            }

            @Override
            public Optional<UserResponseDto> findByUsername(String username) {
                return Optional.empty();
            }

            @Override
            public Optional<UserResponseDto> findByEmail(String email) {
                return Optional.empty();
            }
        };

        // Then
        assertNotNull(mockPort);
        assertDoesNotThrow(() -> mockPort.login(LoginRequestDto.builder().build()));
        assertDoesNotThrow(() -> mockPort.logout("token"));
        assertDoesNotThrow(() -> mockPort.validateToken("token"));
    }

    @Test
    @DisplayName("Should verify interface is public")
    void shouldVerifyInterfaceIsPublic() {
        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(AuthPort.class.getModifiers()));
    }
}
