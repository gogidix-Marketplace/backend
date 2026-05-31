package com.gogidix.universal.tracking.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TrackingMetric domain model.
 */
class TrackingMetricTest {

    @Test
    void testCreateTrackingMetric() {
        TrackingMetric metric = TrackingMetric.builder()
            .id(UUID.randomUUID())
            .metricName("daily_page_views")
            .metricType("COUNT")
            .metricValue(1250.0)
            .metricDate(LocalDate.now())
            .tenantId("tenant-1")
            .eventType("PAGE_VIEW")
            .source("WEB")
            .count(1250L)
            .lastUpdatedAt(LocalDateTime.now())
            .build();

        assertNotNull(metric);
        assertEquals("daily_page_views", metric.getMetricName());
        assertEquals("COUNT", metric.getMetricType());
        assertEquals(1250.0, metric.getMetricValue());
        assertEquals(1250L, metric.getCount());
        assertEquals("tenant-1", metric.getTenantId());
    }

    @Test
    void testIncrementValue() {
        TrackingMetric metric = TrackingMetric.builder()
            .id(UUID.randomUUID())
            .metricName("daily_clicks")
            .metricType("SUM")
            .metricValue(100.0)
            .count(10L)
            .lastUpdatedAt(LocalDateTime.now().minusHours(1))
            .build();

        double originalValue = metric.getMetricValue();
        long originalCount = metric.getCount();

        metric.incrementValue(5.5);

        assertEquals(originalValue + 5.5, metric.getMetricValue());
        assertEquals(originalCount + 1, metric.getCount());
        assertNotNull(metric.getLastUpdatedAt());
    }

    @Test
    void testSetValue() {
        TrackingMetric metric = TrackingMetric.builder()
            .id(UUID.randomUUID())
            .metricName("avg_session_duration")
            .metricType("AVG")
            .metricValue(0.0)
            .count(0L)
            .build();

        metric.setValue(180.5, 500L);

        assertEquals(180.5, metric.getMetricValue());
        assertEquals(500L, metric.getCount());
        assertNotNull(metric.getLastUpdatedAt());
    }

    @Test
    void testIsHourlyMetric() {
        TrackingMetric hourlyMetric = TrackingMetric.builder()
            .id(UUID.randomUUID())
            .metricName("hourly_visitors")
            .metricDate(LocalDate.now())
            .metricHour(14)
            .build();

        assertTrue(hourlyMetric.isHourlyMetric());
        assertFalse(hourlyMetric.isDailyMetric());
    }

    @Test
    void testIsDailyMetric() {
        TrackingMetric dailyMetric = TrackingMetric.builder()
            .id(UUID.randomUUID())
            .metricName("daily_visitors")
            .metricDate(LocalDate.now())
            .metricHour(null)
            .build();

        assertTrue(dailyMetric.isDailyMetric());
        assertFalse(dailyMetric.isHourlyMetric());
    }

    @Test
    void testBuilderDefaults() {
        TrackingMetric metric = TrackingMetric.builder()
            .metricName("test_metric")
            .metricType("COUNT")
            .metricValue(0.0)
            .build();

        assertEquals(0L, metric.getCount());
    }

    @Test
    void testMetricWithDimensions() {
        String dimensionsJson = "{\"event_type\":\"PAGE_VIEW\",\"source\":\"WEB\",\"country\":\"US\"}";

        TrackingMetric metric = TrackingMetric.builder()
            .id(UUID.randomUUID())
            .metricName("page_views_by_country")
            .metricType("COUNT")
            .metricValue(500.0)
            .dimensions(dimensionsJson)
            .build();

        assertEquals(dimensionsJson, metric.getDimensions());
        assertNotNull(metric.getDimensions());
    }
}
