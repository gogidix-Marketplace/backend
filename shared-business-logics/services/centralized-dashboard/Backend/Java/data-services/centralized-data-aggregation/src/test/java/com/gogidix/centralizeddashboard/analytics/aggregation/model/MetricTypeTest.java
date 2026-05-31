package com.gogidix.centralizeddashboard.analytics.aggregation.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricTypeTest {

    @Test
    void allValuesAreAccessible() {
        assertTrue(MetricType.values().length > 0);
        assertNotNull(MetricType.valueOf("COUNT"));
        assertNotNull(MetricType.valueOf("CUSTOM"));
    }

    @Test
    void allValuesHaveNames() {
        for (MetricType mt : MetricType.values()) {
            assertNotNull(mt.name());
        }
    }
}
