package com.gogidix.shared.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConflictException Tests")
class ConflictExceptionTest {

    @Test
    @DisplayName("Test constructor with message")
    void testConstructorWithMessage() {
        ConflictException exception = new ConflictException("Resource already exists");
        assertEquals("Resource already exists", exception.getMessage());
        assertEquals("CONFLICT", exception.getErrorCode());
        assertEquals(409, exception.getHttpStatus());
        assertEquals("Unknown", exception.getConflictType());
        assertEquals("Unknown", exception.getConflictingResourceId());
    }

    @Test
    @DisplayName("Test constructor with conflict type and resource ID")
    void testConstructorWithTypeAndResourceId() {
        ConflictException exception = new ConflictException("DUPLICATE", "order-123");
        assertTrue(exception.getMessage().contains("DUPLICATE"));
        assertTrue(exception.getMessage().contains("order-123"));
        assertEquals("CONFLICT", exception.getErrorCode());
        assertEquals(409, exception.getHttpStatus());
        assertEquals("DUPLICATE", exception.getConflictType());
        assertEquals("order-123", exception.getConflictingResourceId());
    }

    @Test
    @DisplayName("Test constructor with message, type, and resource ID")
    void testConstructorWithAllParams() {
        ConflictException exception = new ConflictException("Custom message", "VERSION_MISMATCH", "user-456");
        assertEquals("Custom message", exception.getMessage());
        assertEquals("VERSION_MISMATCH", exception.getConflictType());
        assertEquals("user-456", exception.getConflictingResourceId());
        assertEquals(409, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test static factory duplicate")
    void testDuplicateFactory() {
        ConflictException exception = ConflictException.duplicate("Order", "order-789");
        assertEquals("DUPLICATE", exception.getConflictType());
        assertEquals("order-789", exception.getConflictingResourceId());
        assertEquals(409, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test static factory versionMismatch")
    void testVersionMismatchFactory() {
        ConflictException exception = ConflictException.versionMismatch("Product", "prod-100");
        assertTrue(exception.getMessage().contains("Version mismatch"));
        assertEquals("VERSION_MISMATCH", exception.getConflictType());
        assertEquals("prod-100", exception.getConflictingResourceId());
    }

    @Test
    @DisplayName("Test context is populated for conflict type and resource ID")
    void testContextPopulated() {
        ConflictException exception = new ConflictException("DUPLICATE", "res-1");
        assertEquals("DUPLICATE", exception.getContext().get("conflictType"));
        assertEquals("res-1", exception.getContext().get("conflictingResourceId"));
    }

    @Test
    @DisplayName("Test conflict exception is not critical (4xx)")
    void testIsNotCritical() {
        ConflictException exception = new ConflictException("Conflict");
        assertFalse(exception.isCritical());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        ConflictException caught = assertThrows(ConflictException.class, () -> {
            throw new ConflictException("Duplicate entry");
        });
        assertEquals("Duplicate entry", caught.getMessage());
    }
}
