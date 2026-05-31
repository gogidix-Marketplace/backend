package com.gogidix.dashboard.performance.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SLAComplianceTest {

    @Test
    void constructor_setsFields() {
        SLACompliance sla = new SLACompliance("order", "payment", 99.5, 3, Collections.emptyList());
        assertEquals("order", sla.getDomain());
        assertEquals("payment", sla.getService());
        assertEquals(99.5, sla.getCompliancePercentage());
        assertEquals(3, sla.getTotalViolations());
        assertTrue(sla.getViolations().isEmpty());
    }

    @Test
    void getViolations_returnsList() {
        List<PerformanceMetric> violations = List.of();
        SLACompliance sla = new SLACompliance("d", "s", 100.0, 0, violations);
        assertNotNull(sla.getViolations());
    }
}
