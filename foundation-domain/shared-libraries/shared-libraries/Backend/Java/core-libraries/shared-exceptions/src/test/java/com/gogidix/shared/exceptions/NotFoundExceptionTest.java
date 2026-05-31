package com.gogidix.shared.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NotFoundException Tests")
class NotFoundExceptionTest {

    @Test
    @DisplayName("Test constructor with resource type and ID")
    void testConstructorWithTypeAndId() {
        NotFoundException exception = new NotFoundException("Order", "order-123");
        assertTrue(exception.getMessage().contains("Order"));
        assertTrue(exception.getMessage().contains("order-123"));
        assertEquals(404, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message")
    void testConstructorWithMessage() {
        NotFoundException exception = new NotFoundException("Resource not found");
        assertEquals("Resource not found", exception.getMessage());
        assertEquals(404, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test static factory of()")
    void testStaticFactoryOf() {
        NotFoundException exception = NotFoundException.of("User", "user-456");
        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains("user-456"));
        assertEquals(404, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test not found exception is not critical (4xx)")
    void testIsNotCritical() {
        NotFoundException exception = new NotFoundException("Test");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        NotFoundException caught = assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("User", "user-999");
        });
        assertEquals(404, caught.getHttpStatus());
    }

    @Test
    @DisplayName("Test is not retryable")
    void testIsNotRetryable() {
        NotFoundException exception = new NotFoundException("Test");
        assertFalse(exception.isRetryable());
    }
}
