package com.gogidix.aiservices.aifrauddetectionservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for AiFraudDetectionServiceApplication.
 * Tests the main Spring Boot application class.
 */
@DisplayName("Financial-Grade: Application Main Class Tests")
class AiFraudDetectionServiceApplicationTest {

    @Test
    @DisplayName("Should instantiate application class")
    void shouldInstantiateApplicationClass() {
        AiFraudDetectionServiceApplication application = new AiFraudDetectionServiceApplication();

        assertThat(application).isNotNull();
    }

    @Test
    @DisplayName("Should have main method")
    void shouldHaveMainMethod() throws Exception {
        // Verify main method exists and can be called (it will start Spring context)
        // We don't actually call it to avoid starting the full context
        var mainMethod = AiFraudDetectionServiceApplication.class.getMethod("main", String[].class);

        assertThat(mainMethod).isNotNull();
        assertThat(mainMethod.getReturnType()).isEqualTo(void.class);
        assertThat(mainMethod.getParameterCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should have public constructor")
    void shouldHavePublicConstructor() {
        var constructors = AiFraudDetectionServiceApplication.class.getDeclaredConstructors();

        assertThat(constructors).hasSize(1);
        assertThat(constructors[0].getParameterCount()).isEqualTo(0);
    }
}
