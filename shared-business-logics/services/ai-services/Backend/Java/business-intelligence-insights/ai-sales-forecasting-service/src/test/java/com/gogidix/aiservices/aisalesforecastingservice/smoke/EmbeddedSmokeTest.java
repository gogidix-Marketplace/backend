package com.gogidix.aiservices.aisalesforecastingservice.smoke;

import com.gogidix.aiservices.aisalesforecastingservice.AISalesForecastingServiceApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke tests to verify basic application loading.
 * Uses @SpringBootTest with limited classes to avoid Docker/Testcontainer dependencies.
 */
@SpringBootTest(classes = AISalesForecastingServiceApplication.class)
@DisplayName("Smoke Tests")
class SmokeTest {

    @Test
    @DisplayName("Application context should load")
    void applicationContextShouldLoad() {
        org.junit.jupiter.api.Assertions.assertNotNull(AISalesForecastingServiceApplication.class,
                "Application class should be loadable");
    }

    @Test
    @DisplayName("Application should have correct package")
    void applicationPackageShouldBeCorrect() {
        org.junit.jupiter.api.Assertions.assertEquals("com.gogidix.aiservices.aisalesforecastingservice",
                AISalesForecastingServiceApplication.class.getPackage().getName());
    }
}
