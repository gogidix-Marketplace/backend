package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceUnavailableException Tests")
class ServiceUnavailableExceptionTest {

    @Test
    @DisplayName("Test constructor with message")
    void testConstructorWithMessage() {
        ServiceUnavailableException exception = new ServiceUnavailableException("Service is down for maintenance");
        assertEquals("Service is down for maintenance", exception.getMessage());
        assertEquals("SERVICE_UNAVAILABLE", exception.getErrorCode());
        assertEquals(503, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message and cause")
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Database connection pool exhausted");
        ServiceUnavailableException exception = new ServiceUnavailableException("Cannot process requests", cause);
        assertEquals("Cannot process requests", exception.getMessage());
        assertEquals("SERVICE_UNAVAILABLE", exception.getErrorCode());
        assertEquals(503, exception.getHttpStatus());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test is critical (5xx)")
    void testIsCritical() {
        ServiceUnavailableException exception = new ServiceUnavailableException("Unavailable");
        assertTrue(exception.isCritical());
    }

    @Test
    @DisplayName("Test is retryable")
    void testIsRetryable() {
        ServiceUnavailableException exception = new ServiceUnavailableException("Unavailable");
        assertTrue(exception.isRetryable());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        ServiceUnavailableException caught = assertThrows(ServiceUnavailableException.class, () -> {
            throw new ServiceUnavailableException("Temporarily unavailable");
        });
        assertEquals("Temporarily unavailable", caught.getMessage());
        assertEquals(503, caught.getHttpStatus());
    }

    @Test
    @DisplayName("Test can add context")
    void testCanAddContext() {
        ServiceUnavailableException exception = new ServiceUnavailableException("Maintenance mode");
        exception.addContext("retryAfter", "300 seconds");
        exception.addContext("reason", "scheduled maintenance");
        assertEquals(2, exception.getContext().size());
        assertEquals("300 seconds", exception.getContext().get("retryAfter"));
    }
}
