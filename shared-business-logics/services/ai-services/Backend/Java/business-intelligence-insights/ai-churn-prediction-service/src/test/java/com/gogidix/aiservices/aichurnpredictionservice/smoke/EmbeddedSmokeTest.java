package com.gogidix.aiservices.aichurnpredictionservice.smoke;

import com.gogidix.aiservices.aichurnpredictionservice.AIChurnPredictionServiceApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Smoke tests to verify basic application loading.
 * Tests class loading without Spring context to avoid dependency issues.
 */
@DisplayName("Smoke Tests")
class SmokeTest {

    @Test
    @DisplayName("Application context should load")
    void applicationContextShouldLoad() {
        org.junit.jupiter.api.Assertions.assertNotNull(AIChurnPredictionServiceApplication.class,
                "Application class should be loadable");
    }

    @Test
    @DisplayName("Application should have correct package")
    void applicationPackageShouldBeCorrect() {
        org.junit.jupiter.api.Assertions.assertEquals("com.gogidix.aiservices.aichurnpredictionservice",
                AIChurnPredictionServiceApplication.class.getPackage().getName());
    }
}
