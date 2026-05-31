package com.gogidix.dashboard.gateway.chart.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSeriesDataTest {

    @Test
    void builderCreatesInstance() {
        LocalDateTime ts = LocalDateTime.of(2025, 1, 15, 10, 30);
        TimeSeriesData data = TimeSeriesData.builder()
                .id(1L)
                .metricName("cpu_usage")
                .tenantId("tenant-1")
                .timestamp(ts)
                .value(75.5)
                .labels("host=server1")
                .source("prometheus")
                .createdAt(LocalDateTime.now())
                .build();

        assertEquals(1L, data.getId());
        assertEquals("cpu_usage", data.getMetricName());
        assertEquals("tenant-1", data.getTenantId());
        assertEquals(ts, data.getTimestamp());
        assertEquals(75.5, data.getValue());
        assertEquals("host=server1", data.getLabels());
        assertEquals("prometheus", data.getSource());
    }

    @Test
    void noArgsConstructorCreatesInstance() {
        TimeSeriesData data = new TimeSeriesData();
        assertNotNull(data);
        assertNull(data.getId());
        assertNull(data.getMetricName());
    }

    @Test
    void allArgsConstructorCreatesInstance() {
        LocalDateTime now = LocalDateTime.now();
        TimeSeriesData data = new TimeSeriesData(1L, "mem", "t1", now, 50.0, "lbl", "src", now);
        assertEquals(1L, data.getId());
        assertEquals("mem", data.getMetricName());
        assertEquals(50.0, data.getValue());
    }

    @Test
    void settersWork() {
        TimeSeriesData data = new TimeSeriesData();
        data.setId(10L);
        data.setMetricName("disk_io");
        data.setTenantId("t2");
        LocalDateTime now = LocalDateTime.now();
        data.setTimestamp(now);
        data.setValue(99.9);
        data.setLabels("region=us");
        data.setSource("collectd");
        data.setCreatedAt(now);

        assertEquals(10L, data.getId());
        assertEquals("disk_io", data.getMetricName());
        assertEquals("t2", data.getTenantId());
        assertEquals(now, data.getTimestamp());
        assertEquals(99.9, data.getValue());
        assertEquals("region=us", data.getLabels());
        assertEquals("collectd", data.getSource());
    }

    @Test
    void equalsAndHashCode() {
        TimeSeriesData d1 = TimeSeriesData.builder().metricName("cpu").build();
        TimeSeriesData d2 = TimeSeriesData.builder().metricName("cpu").build();
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void toStringContainsFields() {
        TimeSeriesData data = TimeSeriesData.builder().metricName("cpu_usage").build();
        String str = data.toString();
        assertNotNull(str);
        assertTrue(str.contains("cpu_usage"));
    }
}
