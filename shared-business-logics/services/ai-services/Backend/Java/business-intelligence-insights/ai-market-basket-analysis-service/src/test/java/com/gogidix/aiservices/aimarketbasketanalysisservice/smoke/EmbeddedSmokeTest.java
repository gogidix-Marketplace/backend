package com.gogidix.aiservices.aimarketbasketanalysisservice.smoke;

import com.gogidix.aiservices.aimarketbasketanalysisservice.AIMarketBasketAnalysisServiceApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke tests to verify basic application loading.
 * Uses @SpringBootTest with limited classes to avoid Docker/Testcontainer dependencies.
 */
@SpringBootTest(classes = AIMarketBasketAnalysisServiceApplication.class)
@DisplayName("Smoke Tests")
class SmokeTest {

    @Test
    @DisplayName("Application context should load")
    void applicationContextShouldLoad() {
        org.junit.jupiter.api.Assertions.assertNotNull(AIMarketBasketAnalysisServiceApplication.class,
                "Application class should be loadable");
    }

    @Test
    @DisplayName("Application should have correct package")
    void applicationPackageShouldBeCorrect() {
        org.junit.jupiter.api.Assertions.assertEquals("com.gogidix.aiservices.aimarketbasketanalysisservice",
                AIMarketBasketAnalysisServiceApplication.class.getPackage().getName());
    }
}
