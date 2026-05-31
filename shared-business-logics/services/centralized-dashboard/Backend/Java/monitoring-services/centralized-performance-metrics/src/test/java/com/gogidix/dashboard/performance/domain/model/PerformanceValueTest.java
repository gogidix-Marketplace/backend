package com.gogidix.dashboard.performance.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceValueTest {

    @Test
    void of_withValueAndUnit() {
        PerformanceValue pv = PerformanceValue.of(42.5, "ms");
        assertEquals(42.5, pv.getValue());
        assertEquals("ms", pv.getUnit());
    }

    @Test
    void of_withValueOnly() {
        PerformanceValue pv = PerformanceValue.of(100.0);
        assertEquals(100.0, pv.getValue());
        assertEquals("", pv.getUnit());
    }

    @Test
    void constructor_nullUnit_throwsException() {
        assertThrows(NullPointerException.class, () -> new PerformanceValue(10.0, null));
    }

    @Test
    void constructor_nanValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new PerformanceValue(Double.NaN, "ms"));
    }

    @Test
    void constructor_infiniteValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new PerformanceValue(Double.POSITIVE_INFINITY, "ms"));
    }

    @Test
    void toString_returnsValueAndUnit() {
        PerformanceValue pv = PerformanceValue.of(42.5, "ms");
        assertEquals("42.5 ms", pv.toString());
    }

    @Test
    void equals_sameValueAndUnit() {
        PerformanceValue pv1 = PerformanceValue.of(10.0, "ms");
        PerformanceValue pv2 = PerformanceValue.of(10.0, "ms");
        assertEquals(pv1, pv2);
    }

    @Test
    void hashCode_sameValueAndUnit() {
        PerformanceValue pv1 = PerformanceValue.of(10.0, "ms");
        PerformanceValue pv2 = PerformanceValue.of(10.0, "ms");
        assertEquals(pv1.hashCode(), pv2.hashCode());
    }

    @Test
    void zeroValue() {
        PerformanceValue pv = PerformanceValue.of(0.0, "%");
        assertEquals(0.0, pv.getValue());
    }

    @Test
    void negativeValue() {
        PerformanceValue pv = PerformanceValue.of(-5.0, "ms");
        assertEquals(-5.0, pv.getValue());
    }
}
