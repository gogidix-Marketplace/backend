package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AuthorizationException
 */
@DisplayName("AuthorizationException Tests")
class AuthorizationExceptionTest {

    @Test
    @DisplayName("Test constructor with message only")
    void testConstructorWithMessage() {
        AuthorizationException exception = new AuthorizationException("Access denied");

        assertEquals("Access denied", exception.getMessage());
        assertEquals("AUTHORIZATION_ERROR", exception.getErrorCode());
        assertEquals(403, exception.getHttpStatus());
        assertEquals("SECURITY", exception.getCategory());
    }

    @Test
    @DisplayName("Test constructor with message and error code")
    void testConstructorWithMessageAndErrorCode() {
        AuthorizationException exception = new AuthorizationException(
            "Insufficient permissions",
            "INSUFFICIENT_PERMISSIONS"
        );

        assertEquals("Insufficient permissions", exception.getMessage());
        assertEquals("INSUFFICIENT_PERMISSIONS", exception.getErrorCode());
        assertEquals(403, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message and required permission")
    void testConstructorWithMessageAndRequiredPermission() {
        AuthorizationException exception = new AuthorizationException(
            "Authorization failed",
            "ADMIN_ACCESS"
        );

        assertEquals("Authorization failed", exception.getMessage());
        assertEquals("AUTHORIZATION_ERROR", exception.getErrorCode());
        assertEquals("ADMIN_ACCESS", exception.getRequiredPermission());
        assertEquals(403, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test is not critical")
    void testIsNotCritical() {
        AuthorizationException exception = new AuthorizationException("Access denied");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test is not retryable")
    void testIsNotRetryable() {
        AuthorizationException exception = new AuthorizationException("Access denied");
        assertFalse(exception.isRetryable());
    }

    @Test
    @DisplayName("Test can add context")
    void testCanAddContext() {
        AuthorizationException exception = new AuthorizationException("Access denied");
        exception.addContext("requiredRole", "ADMIN");
        exception.addContext("userRole", "USER");

        assertEquals(2, exception.getContext().size());
        assertEquals("ADMIN", exception.getContext().get("requiredRole"));
        assertEquals("USER", exception.getContext().get("userRole"));
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        AuthorizationException caught = assertThrows(AuthorizationException.class, () -> {
            throw new AuthorizationException("Not authorized");
        });

        assertEquals("Not authorized", caught.getMessage());
        assertEquals(403, caught.getHttpStatus());
    }

    @Test
    @DisplayName("Test inherits from BaseException")
    void testInheritsFromBaseException() {
        AuthorizationException exception = new AuthorizationException("Access denied");
        assertTrue(exception instanceof BaseException);
    }
}
