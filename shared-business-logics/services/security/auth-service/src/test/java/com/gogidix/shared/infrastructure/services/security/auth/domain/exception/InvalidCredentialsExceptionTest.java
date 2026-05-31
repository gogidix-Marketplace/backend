package com.gogidix.shared.infrastructure.services.security.auth.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InvalidCredentialsException.
 */
@DisplayName("InvalidCredentialsException Tests")
class InvalidCredentialsExceptionTest {

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
        String customMessage = "Account is disabled";

        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(customMessage);

        // Then
        assertNotNull(exception);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should create InvalidCredentialsException with null message")
    void shouldCreateInvalidCredentialsExceptionWithNullMessage() {
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(null);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Should create InvalidCredentialsException with empty message")
    void shouldCreateInvalidCredentialsExceptionWithEmptyMessage() {
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException("");

        // Then
        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    @DisplayName("Should be instance of AuthenticationException")
    void shouldBeInstanceOfAuthenticationException() {
        // When
        InvalidCredentialsException exception = new InvalidCredentialsException();

        // Then
        assertTrue(exception instanceof AuthenticationException);
    }

    @Test
    @DisplayName("Should be throwable and catchable")
    void shouldBeThrowableAndCatchable() {
        // Given
        String message = "Invalid credentials provided";

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> {
            throw new InvalidCredentialsException(message);
        });
    }

    @Test
    @DisplayName("Should be catchable as AuthenticationException")
    void shouldBeCatchableAsAuthenticationException() {
        // Given
        String message = "Invalid credentials";

        // When & Then
        assertThrows(AuthenticationException.class, () -> {
            throw new InvalidCredentialsException(message);
        });
    }

    @Test
    @DisplayName("Should be catchable as RuntimeException")
    void shouldBeCatchableAsRuntimeException() {
        // Given
        String message = "Invalid credentials";

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            throw new InvalidCredentialsException(message);
        });
    }

    @Test
    @DisplayName("Should preserve stack trace")
    void shouldPreserveStackTrace() {
        // Given
        InvalidCredentialsException exception = new InvalidCredentialsException("Test");

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Should support getMessage from superclass")
    void shouldSupportGetMessageFromSuperclass() {
        // Given
        String message = "Custom error message";
        InvalidCredentialsException exception = new InvalidCredentialsException(message);

        // When
        String result = exception.getMessage();

        // Then
        assertEquals(message, result);
    }

    @Test
    @DisplayName("Should create multiple independent instances")
    void shouldCreateMultipleIndependentInstances() {
        // When
        InvalidCredentialsException exception1 = new InvalidCredentialsException("Error 1");
        InvalidCredentialsException exception2 = new InvalidCredentialsException("Error 2");

        // Then
        assertNotEquals(exception1.getMessage(), exception2.getMessage());
        assertNotSame(exception1, exception2);
    }

    @Test
    @DisplayName("Should handle message with special characters")
    void shouldHandleMessageWithSpecialCharacters() {
        // Given
        String specialMessage = "Error: <script>alert('xss')</script> & \"quotes\"";

        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(specialMessage);

        // Then
        assertEquals(specialMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should handle very long message")
    void shouldHandleVeryLongMessage() {
        // Given
        String longMessage = "A".repeat(1000);

        // When
        InvalidCredentialsException exception = new InvalidCredentialsException(longMessage);

        // Then
        assertEquals(1000, exception.getMessage().length());
    }
}
