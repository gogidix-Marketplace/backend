package com.gogidix.dashboard.performance.adapter.in.web.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceMetricDTOTest {

    @Test
    void noArgsConstructor() {
        PerformanceMetricDTO dto = new PerformanceMetricDTO();
        assertNull(dto.getMetricId());
        assertNull(dto.getMetricName());
    }

    @Test
    void allArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        PerformanceMetricDTO dto = new PerformanceMetricDTO("id1", "cpu", "order",
                "payment", 75.5, "%", now, "NORMAL");

        assertEquals("id1", dto.getMetricId());
        assertEquals("cpu", dto.getMetricName());
        assertEquals("order", dto.getDomain());
        assertEquals("payment", dto.getService());
        assertEquals(75.5, dto.getValue());
        assertEquals("%", dto.getUnit());
        assertEquals(now, dto.getTimestamp());
        assertEquals("NORMAL", dto.getStatus());
    }

    @Test
    void settersWork() {
        PerformanceMetricDTO dto = new PerformanceMetricDTO();
        dto.setMetricId("m1");
        dto.setMetricName("mem");
        dto.setDomain("d1");
        dto.setService("s1");
        dto.setValue(50.0);
        dto.setUnit("ms");
        LocalDateTime now = LocalDateTime.now();
        dto.setTimestamp(now);
        dto.setStatus("WARNING");

        assertEquals("m1", dto.getMetricId());
        assertEquals("mem", dto.getMetricName());
        assertEquals("d1", dto.getDomain());
        assertEquals("s1", dto.getService());
        assertEquals(50.0, dto.getValue());
        assertEquals("ms", dto.getUnit());
        assertEquals(now, dto.getTimestamp());
        assertEquals("WARNING", dto.getStatus());
    }
}
