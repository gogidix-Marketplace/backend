package com.gogidix.management.executive.analytics.application.dto;

import com.gogidix.management.executive.analytics.application.dto.AnalyticsQueryRequest;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AnalyticsQueryRequestTest {

        @Test
    void testBuilder() {
        AnalyticsQueryRequest dto = AnalyticsQueryRequest.builder()
                        .metricId("test-metricId")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dashboardIds(Collections.emptyList())
            .dimensions(Collections.emptyList())
            .aggregation("test-aggregation")
            .limit(42)
            .sortBy("test-sortBy")
            .sortOrder("test-sortOrder")
            .build();
        assertNotNull(dto);
        assertEquals("test-metricId", dto.getMetricId());
        assertEquals("test-aggregation", dto.getAggregation());
        assertEquals(42, dto.getLimit());
        assertEquals("test-sortBy", dto.getSortBy());
        assertEquals("test-sortOrder", dto.getSortOrder());
    }

    @Test
    void testSettersAndGetters() {
        AnalyticsQueryRequest dto = new AnalyticsQueryRequest();
        dto.setMetricId("val-metricId");
        dto.setAggregation("val-aggregation");
        dto.setLimit(99);
        dto.setSortBy("val-sortBy");
        dto.setSortOrder("val-sortOrder");
        assertEquals("val-metricId", dto.getMetricId());
        assertEquals("val-aggregation", dto.getAggregation());
        assertEquals(99, dto.getLimit());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortOrder", dto.getSortOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsQueryRequest dto1 = AnalyticsQueryRequest.builder()
                        .metricId("test-metricId")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dashboardIds(Collections.emptyList())
            .dimensions(Collections.emptyList())
            .aggregation("test-aggregation")
            .limit(42)
            .sortBy("test-sortBy")
            .sortOrder("test-sortOrder")
            .build();
        AnalyticsQueryRequest dto2 = AnalyticsQueryRequest.builder()
                        .metricId("test-metricId")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dashboardIds(Collections.emptyList())
            .dimensions(Collections.emptyList())
            .aggregation("test-aggregation")
            .limit(42)
            .sortBy("test-sortBy")
            .sortOrder("test-sortOrder")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsQueryRequest dto = AnalyticsQueryRequest.builder()
                        .metricId("test-metricId")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dashboardIds(Collections.emptyList())
            .dimensions(Collections.emptyList())
            .aggregation("test-aggregation")
            .limit(42)
            .sortBy("test-sortBy")
            .sortOrder("test-sortOrder")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}