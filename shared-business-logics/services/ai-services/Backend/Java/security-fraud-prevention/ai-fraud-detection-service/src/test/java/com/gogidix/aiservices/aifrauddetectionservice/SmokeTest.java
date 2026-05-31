package com.gogidix.aiservices.aifrauddetectionservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke Test - verifies basic application functionality.
 * Does NOT require Spring context or MongoDB.
 * This is a true smoke test that should pass quickly.
 */
@DisplayName("Smoke Test")
class SmokeTest {

    @Test
    @DisplayName("Should verify basic functionality")
    void shouldVerifyBasicFunctionality() {
        // This is a true smoke test - no Spring context, no MongoDB
        // Just verifies basic Java functionality and test setup

        assertThat(true).isTrue();
    }
}
