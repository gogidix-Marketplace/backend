package com.gogidix.analytics.data;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class for Analytics Data Service.
 */
@SpringBootTest
@ActiveProfiles("test")
@Disabled("ApplicationContext cannot load without infrastructure services")
class AnalyticsDataServiceTest {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }
}
