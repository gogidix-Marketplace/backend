package com.gogidix.aiservices.intelligenceanalysisservice.smoke;

import com.gogidix.aiservices.intelligenceanalysisservice.AIIntelligenceAnalysisationServiceApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke tests to verify basic application loading.
 * Uses @SpringBootTest with limited classes to avoid Docker/Testcontainer dependencies.
 */
@SpringBootTest(classes = AIIntelligenceAnalysisationServiceApplication.class)
@DisplayName("Smoke Tests")
class SmokeTest {

    @Test
    @DisplayName("Application context should load")
    void applicationContextShouldLoad() {
        org.junit.jupiter.api.Assertions.assertNotNull(AIIntelligenceAnalysisationServiceApplication.class,
                "Application class should be loadable");
    }

    @Test
    @DisplayName("Application should have correct package")
    void applicationPackageShouldBeCorrect() {
        org.junit.jupiter.api.Assertions.assertEquals("com.gogidix.aiservices.intelligenceanalysisservice",
                AIIntelligenceAnalysisationServiceApplication.class.getPackage().getName());
    }
}
