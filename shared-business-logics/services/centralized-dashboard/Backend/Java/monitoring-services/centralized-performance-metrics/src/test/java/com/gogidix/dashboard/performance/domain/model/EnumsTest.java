package com.gogidix.dashboard.performance.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    @Test
    void metricType_values() {
        assertEquals(20, MetricType.values().length);
        assertNotNull(MetricType.valueOf("CPU_USAGE"));
        assertNotNull(MetricType.valueOf("RESPONSE_TIME"));
        assertNotNull(MetricType.valueOf("THROUGHPUT"));
        assertNotNull(MetricType.valueOf("ERROR_RATE"));
        assertNotNull(MetricType.valueOf("AVAILABILITY"));
    }

    @Test
    void metricType_displayName() {
        assertEquals("CPU Usage", MetricType.CPU_USAGE.getDisplayName());
        assertEquals("Response Time", MetricType.RESPONSE_TIME.getDisplayName());
    }

    @Test
    void metricType_unit() {
        assertEquals("ms", MetricType.RESPONSE_TIME.getUnit());
        assertEquals("%", MetricType.CPU_USAGE.getUnit());
    }

    @Test
    void metricType_higherBetter() {
        assertTrue(MetricType.THROUGHPUT.isHigherBetter());
        assertFalse(MetricType.RESPONSE_TIME.isHigherBetter());
        assertTrue(MetricType.AVAILABILITY.isHigherBetter());
    }

    @Test
    void metricType_criticalMetric() {
        assertTrue(MetricType.RESPONSE_TIME.isCriticalMetric());
        assertTrue(MetricType.ERROR_RATE.isCriticalMetric());
        assertFalse(MetricType.CPU_USAGE.isCriticalMetric());
    }

    @Test
    void metricType_defaultThresholds() {
        assertTrue(MetricType.CPU_USAGE.getDefaultWarningThreshold() > 0);
        assertTrue(MetricType.CPU_USAGE.getDefaultCriticalThreshold() > 0);
        assertTrue(MetricType.THROUGHPUT.getDefaultWarningThreshold() > 0);
    }

    @Test
    void performanceStatus_values() {
        assertEquals(4, PerformanceStatus.values().length);
        assertEquals("Optimal", PerformanceStatus.OPTIMAL.getDisplayName());
        assertEquals("Normal", PerformanceStatus.NORMAL.getDisplayName());
        assertEquals("Warning", PerformanceStatus.WARNING.getDisplayName());
        assertEquals("Critical", PerformanceStatus.CRITICAL.getDisplayName());
    }

    @Test
    void performanceStatus_descriptions() {
        assertNotNull(PerformanceStatus.OPTIMAL.getDescription());
        assertNotNull(PerformanceStatus.CRITICAL.getDescription());
    }

    @Test
    void alertPriority_values() {
        assertEquals(4, AlertPriority.values().length);
        assertTrue(AlertPriority.P1.requiresImmediateResponse());
        assertFalse(AlertPriority.P2.requiresImmediateResponse());
        assertFalse(AlertPriority.P4.requiresImmediateResponse());
    }

    @Test
    void alertPriority_descriptions() {
        assertNotNull(AlertPriority.P1.getDescription());
        assertNotNull(AlertPriority.P4.getDescription());
    }

    @Test
    void performanceTrend_values() {
        assertEquals(3, PerformanceTrend.values().length);
        assertNotNull(PerformanceTrend.IMPROVING.getDescription());
        assertNotNull(PerformanceTrend.DEGRADING.getDescription());
        assertNotNull(PerformanceTrend.STABLE.getDescription());
    }

    @Test
    void anomalySeverity_values() {
        assertEquals(5, AnomalySeverity.values().length);
        assertTrue(AnomalySeverity.P0.isCritical());
        assertTrue(AnomalySeverity.P1.isCritical());
        assertFalse(AnomalySeverity.P2.isCritical());
        assertFalse(AnomalySeverity.P3.isCritical());
    }

    @Test
    void anomalySeverity_descriptions() {
        assertNotNull(AnomalySeverity.P0.getDescription());
        assertNotNull(AnomalySeverity.P4.getDescription());
    }
}
