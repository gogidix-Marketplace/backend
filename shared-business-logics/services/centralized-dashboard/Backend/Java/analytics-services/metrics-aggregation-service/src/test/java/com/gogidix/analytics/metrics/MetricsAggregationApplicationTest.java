package com.gogidix.analytics.metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricsAggregationApplicationTest {

    @Test
    void main_doesNotThrow() {
        assertDoesNotThrow(() -> {
            String[] args = {};
        });
    }

    @Test
    void applicationClass_exists() {
        assertNotNull(MetricsAggregationApplication.class);
    }

    @Test
    void applicationHasMainMethod() throws NoSuchMethodException {
        assertNotNull(MetricsAggregationApplication.class.getMethod("main", String[].class));
    }
}
