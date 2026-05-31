package com.gogidix.infrastructure.database;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Infrastructure Database Service Integration Tests")
class InfrastructureDatabaseServiceIntegrationTest {

    @Test
    @DisplayName("Smoke test")
    void smokeTest() {
        assertThat(true).isTrue();
    }
}
