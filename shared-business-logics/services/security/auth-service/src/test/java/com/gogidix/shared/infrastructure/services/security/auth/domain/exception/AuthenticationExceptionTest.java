package com.gogidix.shared.infrastructure.services.security.auth.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AuthenticationException and its subclasses.
 */
@DisplayName("Authentication Exception Tests")
class AuthenticationExceptionTest {

    @Test
    @DisplayName("Should create AuthenticationException with message")
    void shouldCreateAuthenticationExceptionWithMessage() {
        // Given
        String message = "Authentication failed";

        // When
        AuthenticationException exception = new AuthenticationException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create AuthenticationException with message and cause")
    void shouldCreateAuthenticationExceptionWithMessageAndCause() {
        // Given
        String message = "Authentication failed";
        Throwable cause = new RuntimeException("Underlying error");

        // When
        AuthenticationException exception = new AuthenticationException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Underlying error", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Should create InvalidCredentialsException with default message")
    void shouldCreateInvalidCredentialsExceptionWithDefaultMessage() {
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException();

        // Then
        assertNotNull(exception);
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    @DisplayName("Should create InvalidCredentialsException with custom message")
    void shouldCreateInvalidCredentialsExceptionWithCustomMessage() {
        // Given
        String message = "Account is disabled";

        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Should create InvalidCredentialsException as AuthenticationException")
    void shouldCreateInvalidCredentialsExceptionAsAuthenticationException() {
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException();

        // Then
        assertTrue(exception instanceof AuthenticationException);
    }

    @Test
    @DisplayName("Should create UserNotFoundException with userId")
    void shouldCreateUserNotFoundExceptionWithUserId() {
        // Given
        String userId = "user-123";

        // When
        UserNotFoundException exception = new UserNotFoundException(userId);

        // Then
        assertNotNull(exception);
        assertEquals("User not found: " + userId, exception.getMessage());
        assertEquals(userId, exception.getUserId());
    }

    @Test
    @DisplayName("Should create UserNotFoundException with field and value")
    void shouldCreateUserNotFoundExceptionWithFieldAndValue() {
        // Given
        String field = "email";
        String value = "test@example.com";

        // When
        UserNotFoundException exception = new UserNotFoundException(field, value);

        // Then
        assertNotNull(exception);
        assertEquals("User not found with " + field + ": " + value, exception.getMessage());
        assertEquals(value, exception.getUserId());
    }

    @Test
    @DisplayName("Should create UserNotFoundException as AuthenticationException")
    void shouldCreateUserNotFoundExceptionAsAuthenticationException() {
        // When
        UserNotFoundException exception = new UserNotFoundException("user-123");

        // Then
        assertTrue(exception instanceof AuthenticationException);
    }

    @Test
    @DisplayName("Should throw and catch InvalidCredentialsException")
    void shouldThrowAndCatchInvalidCredentialsException() {
        // When & Then
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> {
                    throw new InvalidCredentialsException("Invalid credentials");
                }
        );

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw and catch UserNotFoundException")
    void shouldThrowAndCatchUserNotFoundException() {
        // Given
        String userId = "user-456";

        // When & Then
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> {
                    throw new UserNotFoundException(userId);
                }
        );

        assertTrue(exception.getMessage().contains(userId));
        assertEquals(userId, exception.getUserId());
    }

    @Test
    @DisplayName("Should preserve stack trace in AuthenticationException")
    void shouldPreserveStackTraceInAuthenticationException() {
        // Given
        String message = "Test exception";

        // When
        try {
            throw new AuthenticationException(message);
        } catch (AuthenticationException e) {
            // Then
            assertNotNull(e.getStackTrace());
            assertTrue(e.getStackTrace().length > 0);
        }
    }

    @Test
    @DisplayName("Should create exception chain with cause")
    void shouldCreateExceptionChainWithCause() {
        // Given
        Throwable cause = new IllegalStateException("Invalid state");
        String message = "Authentication failed due to state";

        // When
        AuthenticationException exception = new AuthenticationException(message, cause);

        // Then
        assertSame(cause, exception.getCause());
        assertEquals(message, exception.getMessage());
        assertEquals("Invalid state", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Should handle null message in InvalidCredentialsException")
    void shouldHandleNullMessageInInvalidCredentialsException() {
        // Given
        String message = null;

        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(message);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Should handle empty message in UserNotFoundException")
    void shouldHandleEmptyMessageInUserNotFoundException() {
        // Given
        String emptyUserId = "";

        // When
        UserNotFoundException exception = new UserNotFoundException(emptyUserId);

        // Then
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(""));
        assertEquals(emptyUserId, exception.getUserId());
    }

    @Test
    @DisplayName("Should verify UserNotFoundException getUserId method")
    void shouldVerifyUserNotFoundExceptionGetUserIdMethod() {
        // Given
        String userId = "user-789";
        UserNotFoundException exception = new UserNotFoundException("username", "john.doe");

        // Then - userId should be the value when using field constructor
        assertEquals("john.doe", exception.getUserId());

        // When using userId constructor
        UserNotFoundException exception2 = new UserNotFoundException(userId);

        // Then
        assertEquals(userId, exception2.getUserId());
    }

    @Test
    @DisplayName("Should create different exception instances properly")
    void shouldCreateDifferentExceptionInstancesProperly() {
        // Given
        AuthenticationException authException = new AuthenticationException("Auth error");
        InvalidCredentialsException invalidCredsException = new InvalidCredentialsException();
        UserNotFoundException userNotFoundException = new UserNotFoundException("user-123");

        // Then - all should be different instances
        assertNotSame(authException, invalidCredsException);
        assertNotSame(authException, userNotFoundException);
        assertNotSame(invalidCredsException, userNotFoundException);

        // But InvalidCredentialsException and UserNotFoundException should be instances of AuthenticationException
        assertTrue(invalidCredsException instanceof AuthenticationException);
        assertTrue(userNotFoundException instanceof AuthenticationException);
        assertFalse(authException instanceof InvalidCredentialsException);
        assertFalse(authException instanceof UserNotFoundException);
    }

    @Test
    @DisplayName("Should verify exception equals behavior")
    void shouldVerifyExceptionEqualsBehavior() {
        // Given
        InvalidCredentialsException exception1 = new InvalidCredentialsException("Test");
        InvalidCredentialsException exception2 = new InvalidCredentialsException("Test");
        InvalidCredentialsException exception3 = new InvalidCredentialsException("Different");

        // Then - exceptions are not equal by default even with same message
        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);
        // Same instance should be equal to itself
        assertEquals(exception1, exception1);
    }
}
