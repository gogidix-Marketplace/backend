package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AuthenticationException
 */
@DisplayName("AuthenticationException Tests")
class AuthenticationExceptionTest {

    @Test
    @DisplayName("Test constructor with message only")
    void testConstructorWithMessage() {
        AuthenticationException exception = new AuthenticationException("Invalid credentials");

        assertEquals("Invalid credentials", exception.getMessage());
        assertEquals("AUTHENTICATION_ERROR", exception.getErrorCode());
        assertEquals(401, exception.getHttpStatus());
        assertEquals("SECURITY", exception.getCategory());
    }

    @Test
    @DisplayName("Test constructor with message and error code")
    void testConstructorWithMessageAndErrorCode() {
        AuthenticationException exception = new AuthenticationException(
            "Token expired"
        );

        assertEquals("Token expired", exception.getMessage());
        assertEquals("AUTHENTICATION_ERROR", exception.getErrorCode());
        assertEquals(401, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message and cause")
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("JWT validation failed");
        AuthenticationException exception = new AuthenticationException(
            "Authentication failed",
            cause
        );

        assertEquals("Authentication failed", exception.getMessage());
        assertEquals("AUTHENTICATION_ERROR", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test is not critical")
    void testIsNotCritical() {
        AuthenticationException exception = new AuthenticationException("Auth failed");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test is not retryable")
    void testIsNotRetryable() {
        AuthenticationException exception = new AuthenticationException("Auth failed");
        assertFalse(exception.isRetryable());
    }

    @Test
    @DisplayName("Test can add context")
    void testCanAddContext() {
        AuthenticationException exception = new AuthenticationException("Auth failed");
        exception.addContext("attemptCount", 3);
        exception.addContext("lastAttempt", "2024-01-01T10:00:00");

        assertEquals(2, exception.getContext().size());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        AuthenticationException caught = assertThrows(AuthenticationException.class, () -> {
            throw new AuthenticationException("Invalid token");
        });

        assertEquals("Invalid token", caught.getMessage());
        assertEquals(401, caught.getHttpStatus());
    }

    @Test
    @DisplayName("Test inherits from BaseException")
    void testInheritsFromBaseException() {
        AuthenticationException exception = new AuthenticationException("Auth failed");
        assertTrue(exception instanceof BaseException);
    }
}
