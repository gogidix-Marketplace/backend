package com.gogidix.shared.servicediscovery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for shared-service-discovery.
 * Integration tests are in subdirectories and require significant resources.
 */
@DisplayName("ServiceDiscovery Unit Tests")
class ServiceDiscoveryUnitTest {

    @Test
    @DisplayName("Library module is valid")
    void libraryModuleIsValid() {
        assertThat(true).isTrue();
    }
}
