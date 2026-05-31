package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MetricTypeTest {

    @Test
    void businessCriticalTypes_identifiedCorrectly() {
        assertTrue(MetricType.BUSINESS_KPI.isBusinessCritical());
        assertTrue(MetricType.FINANCIAL.isBusinessCritical());
        assertTrue(MetricType.CUSTOMER.isBusinessCritical());
        assertFalse(MetricType.OPERATIONAL.isBusinessCritical());
        assertFalse(MetricType.TECHNICAL.isBusinessCritical());
    }

    @Test
    void operationalTypes_identifiedCorrectly() {
        assertTrue(MetricType.OPERATIONAL.isOperational());
        assertTrue(MetricType.TECHNICAL.isOperational());
        assertTrue(MetricType.PERFORMANCE.isOperational());
        assertFalse(MetricType.BUSINESS_KPI.isOperational());
    }

    @Test
    void allTypes_supportAlerting() {
        for (MetricType type : MetricType.values()) {
            assertTrue(type.supportsAlerting(), type.name() + " should support alerting");
        }
    }

    @Test
    void displayNames_setCorrectly() {
        assertNotNull(MetricType.BUSINESS_KPI.getDisplayName());
        assertEquals("Business KPI", MetricType.BUSINESS_KPI.getDisplayName());
    }

    @Test
    void allValuesAreAccessible() {
        assertEquals(8, MetricType.values().length);
    }
}
