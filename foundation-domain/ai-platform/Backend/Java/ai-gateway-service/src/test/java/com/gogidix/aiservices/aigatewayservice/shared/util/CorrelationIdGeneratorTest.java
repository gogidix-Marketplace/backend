package com.gogidix.aiservices.aigatewayservice.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CorrelationIdGenerator.
 */
@DisplayName("CorrelationIdGenerator Tests")
class CorrelationIdGeneratorTest {

    @Test
    @DisplayName("Should generate correlation ID")
    void shouldGenerateCorrelationId() {
        String id = CorrelationIdGenerator.generate();

        assertNotNull(id);
        assertFalse(id.isBlank());
    }

    @Test
    @DisplayName("Should generate correlation ID with prefix")
    void shouldGenerateCorrelationIdWithPrefix() {
        String id = CorrelationIdGenerator.generate("test");

        assertNotNull(id);
        assertTrue(id.startsWith("test_"));
    }

    @Test
    @DisplayName("Should generate unique correlation IDs")
    void shouldGenerateUniqueCorrelationIds() {
        String id1 = CorrelationIdGenerator.generate();
        String id2 = CorrelationIdGenerator.generate();

        assertNotEquals(id1, id2);
    }
}
