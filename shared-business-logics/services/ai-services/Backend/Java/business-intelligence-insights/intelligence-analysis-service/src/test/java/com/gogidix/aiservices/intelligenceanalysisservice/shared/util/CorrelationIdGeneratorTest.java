package com.gogidix.aiservices.intelligenceanalysisservice.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CorrelationIdGenerator.
 */
@DisplayName("CorrelationIdGenerator Tests")
class CorrelationIdGeneratorTest {

    @Test
    @DisplayName("Should generate unique correlation IDs")
    void shouldGenerateUniqueCorrelationIds() {
        String id1 = CorrelationIdGenerator.generate();
        String id2 = CorrelationIdGenerator.generate();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("Should generate 32-character hex string")
    void shouldGenerate32CharHexString() {
        String id = CorrelationIdGenerator.generate();

        assertEquals(32, id.length());
        assertTrue(id.matches("^[a-fA-F0-9]{32}$"));
    }

    @Test
    @DisplayName("Should generate with prefix")
    void shouldGenerateWithPrefix() {
        String id = CorrelationIdGenerator.generateWithPrefix("TEST");

        assertTrue(id.startsWith("TEST_"));
        assertEquals(37, id.length()); // 4 (prefix) + 1 (_) + 32 (uuid)
    }

    @Test
    @DisplayName("Should validate valid correlation ID")
    void shouldValidateValidCorrelationId() {
        String validId = "a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4";

        assertTrue(CorrelationIdGenerator.isValid(validId));
    }

    @Test
    @DisplayName("Should reject invalid correlation ID")
    void shouldRejectInvalidCorrelationId() {
        assertFalse(CorrelationIdGenerator.isValid(null));
        assertFalse(CorrelationIdGenerator.isValid(""));
        assertFalse(CorrelationIdGenerator.isValid("invalid"));
        assertFalse(CorrelationIdGenerator.isValid("g1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4")); // contains 'g'
        assertFalse(CorrelationIdGenerator.isValid("a1b2c3d4e5f6")); // too short
    }

    @Test
    @DisplayName("Should throw when instantiating utility class")
    void shouldThrowWhenInstantiating() {
        Exception exception = assertThrows(Exception.class,
                () -> {
                    var constructor = CorrelationIdGenerator.class.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    constructor.newInstance();
                });
        // Reflection wraps the exception, so check the cause
        assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
    }
}
