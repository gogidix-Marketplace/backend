package com.gogidix.analytics.metrics;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled("Requires Spring context with DataSource/Redis")
class MetricsAggregationServiceTest {

    @Test
    void contextLoads() {
    }
}
