package com.gogidix.dashboard.performance.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceAnomalyTest {

    @Test
    void constructor_setsFields() {
        LocalDateTime now = LocalDateTime.now();
        PerformanceAnomaly anomaly = new PerformanceAnomaly("CPU spike", AnomalySeverity.P0, now);

        assertEquals("CPU spike", anomaly.getDescription());
        assertEquals(AnomalySeverity.P0, anomaly.getSeverity());
        assertEquals(now, anomaly.getDetectedAt());
    }

    @Test
    void constructor_nullDescription_throws() {
        assertThrows(NullPointerException.class,
                () -> new PerformanceAnomaly(null, AnomalySeverity.P1, LocalDateTime.now()));
    }

    @Test
    void constructor_nullSeverity_throws() {
        assertThrows(NullPointerException.class,
                () -> new PerformanceAnomaly("test", null, LocalDateTime.now()));
    }

    @Test
    void constructor_nullTime_throws() {
        assertThrows(NullPointerException.class,
                () -> new PerformanceAnomaly("test", AnomalySeverity.P1, null));
    }

    @Test
    void equals_sameValues() {
        LocalDateTime now = LocalDateTime.now();
        PerformanceAnomaly a1 = new PerformanceAnomaly("x", AnomalySeverity.P0, now);
        PerformanceAnomaly a2 = new PerformanceAnomaly("x", AnomalySeverity.P0, now);
        assertEquals(a1, a2);
    }

    @Test
    void hashCode_sameValues() {
        LocalDateTime now = LocalDateTime.now();
        PerformanceAnomaly a1 = new PerformanceAnomaly("x", AnomalySeverity.P0, now);
        PerformanceAnomaly a2 = new PerformanceAnomaly("x", AnomalySeverity.P0, now);
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
