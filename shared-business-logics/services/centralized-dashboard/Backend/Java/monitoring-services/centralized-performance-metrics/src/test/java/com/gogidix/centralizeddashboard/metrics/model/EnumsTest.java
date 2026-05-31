package com.gogidix.centralizeddashboard.metrics.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthStatusTest {

    @Test
    void allValuesExist() {
        HealthStatus[] values = HealthStatus.values();
        assertEquals(7, values.length);
    }

    @Test
    void valueOf() {
        assertEquals(HealthStatus.UP, HealthStatus.valueOf("UP"));
        assertEquals(HealthStatus.DOWN, HealthStatus.valueOf("DOWN"));
        assertEquals(HealthStatus.DEGRADED, HealthStatus.valueOf("DEGRADED"));
        assertEquals(HealthStatus.UNKNOWN, HealthStatus.valueOf("UNKNOWN"));
        assertEquals(HealthStatus.OUT_OF_SERVICE, HealthStatus.valueOf("OUT_OF_SERVICE"));
        assertEquals(HealthStatus.WARNING, HealthStatus.valueOf("WARNING"));
        assertEquals(HealthStatus.CIRCUIT_OPEN, HealthStatus.valueOf("CIRCUIT_OPEN"));
    }
}

class MetricTypeTest {

    @Test
    void allValuesExist() {
        MetricType[] values = MetricType.values();
        assertEquals(7, values.length);
    }

    @Test
    void valueOf() {
        assertEquals(MetricType.COUNTER, MetricType.valueOf("COUNTER"));
        assertEquals(MetricType.GAUGE, MetricType.valueOf("GAUGE"));
        assertEquals(MetricType.HISTOGRAM, MetricType.valueOf("HISTOGRAM"));
        assertEquals(MetricType.SUMMARY, MetricType.valueOf("SUMMARY"));
        assertEquals(MetricType.TIMER, MetricType.valueOf("TIMER"));
        assertEquals(MetricType.METER, MetricType.valueOf("METER"));
        assertEquals(MetricType.TASK, MetricType.valueOf("TASK"));
    }
}
