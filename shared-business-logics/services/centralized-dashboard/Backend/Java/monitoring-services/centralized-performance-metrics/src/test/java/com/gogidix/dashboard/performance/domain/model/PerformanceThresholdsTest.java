package com.gogidix.dashboard.performance.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceThresholdsTest {

    @Test
    void constructor_setsValues() {
        PerformanceThresholds t = new PerformanceThresholds(0, 100, 80, 95);
        assertEquals(0, t.getMinThreshold());
        assertEquals(100, t.getMaxThreshold());
        assertEquals(80, t.getWarningThreshold());
        assertEquals(95, t.getCriticalThreshold());
    }

    @Test
    void isWarningThresholdBreached_true() {
        PerformanceThresholds t = new PerformanceThresholds(0, 100, 80, 95);
        assertTrue(t.isWarningThresholdBreached(85));
    }

    @Test
    void isWarningThresholdBreached_false() {
        PerformanceThresholds t = new PerformanceThresholds(0, 100, 80, 95);
        assertFalse(t.isWarningThresholdBreached(70));
    }

    @Test
    void isCriticalThresholdBreached_true() {
        PerformanceThresholds t = new PerformanceThresholds(0, 100, 80, 95);
        assertTrue(t.isCriticalThresholdBreached(96));
    }

    @Test
    void isCriticalThresholdBreached_false() {
        PerformanceThresholds t = new PerformanceThresholds(0, 100, 80, 95);
        assertFalse(t.isCriticalThresholdBreached(90));
    }

    @Test
    void isOptimal_withinRange() {
        PerformanceThresholds t = new PerformanceThresholds(10, 80, 85, 95);
        assertTrue(t.isOptimal(50));
    }

    @Test
    void isOptimal_belowMin() {
        PerformanceThresholds t = new PerformanceThresholds(10, 80, 85, 95);
        assertFalse(t.isOptimal(5));
    }

    @Test
    void isOptimal_aboveMax() {
        PerformanceThresholds t = new PerformanceThresholds(10, 80, 85, 95);
        assertFalse(t.isOptimal(85));
    }

    @Test
    void equals_sameValues() {
        PerformanceThresholds t1 = new PerformanceThresholds(0, 100, 80, 95);
        PerformanceThresholds t2 = new PerformanceThresholds(0, 100, 80, 95);
        assertEquals(t1, t2);
    }

    @Test
    void equals_differentValues() {
        PerformanceThresholds t1 = new PerformanceThresholds(0, 100, 80, 95);
        PerformanceThresholds t2 = new PerformanceThresholds(0, 100, 80, 96);
        assertNotEquals(t1, t2);
    }

    @Test
    void hashCode_sameValues() {
        PerformanceThresholds t1 = new PerformanceThresholds(0, 100, 80, 95);
        PerformanceThresholds t2 = new PerformanceThresholds(0, 100, 80, 95);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
