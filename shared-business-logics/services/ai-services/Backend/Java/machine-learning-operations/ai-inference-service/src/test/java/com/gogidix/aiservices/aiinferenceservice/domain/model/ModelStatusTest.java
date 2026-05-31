package com.gogidix.aiservices.aiinferenceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ModelStatus enum.
 */
@DisplayName("ModelStatus Tests")
class ModelStatusTest {

    @Test
    @DisplayName("Should have correct status values")
    void shouldHaveCorrectStatusValues() {
        assertEquals(4, ModelStatus.values().length);
    }
}
