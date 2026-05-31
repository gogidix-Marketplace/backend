package com.gogidix.customersupport.globalsupportdashboard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Main application test
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.host=localhost",
        "spring.data.mongodb.port=27017",
        "spring.data.mongodb.database=test_global_support_dashboard_db"
})
class SupportDashboardServiceApplicationTests {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }
}
