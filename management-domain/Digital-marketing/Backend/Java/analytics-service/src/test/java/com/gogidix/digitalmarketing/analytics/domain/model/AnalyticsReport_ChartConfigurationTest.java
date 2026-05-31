package com.gogidix.digitalmarketing.analytics.domain.model;

import com.gogidix.digitalmarketing.analytics.domain.model.AnalyticsReport;
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
class AnalyticsReport_ChartConfigurationTest {

        @Test
    void testBuilder() {
        AnalyticsReport.ChartConfiguration dto = AnalyticsReport.ChartConfiguration.builder()
                        .id("test-id")
            .title("test-title")
            .chartType("test-chartType")
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .series(Collections.emptyList())
            .options(Collections.emptyMap())
            .order(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-chartType", dto.getChartType());
        assertEquals("test-xAxis", dto.getXAxis());
        assertEquals("test-yAxis", dto.getYAxis());
        assertEquals(42, dto.getOrder());
    }

    @Test
    void testSettersAndGetters() {
        AnalyticsReport.ChartConfiguration dto = new AnalyticsReport.ChartConfiguration();
        dto.setId("val-id");
        dto.setTitle("val-title");
        dto.setChartType("val-chartType");
        dto.setXAxis("val-xAxis");
        dto.setYAxis("val-yAxis");
        dto.setOrder(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-chartType", dto.getChartType());
        assertEquals("val-xAxis", dto.getXAxis());
        assertEquals("val-yAxis", dto.getYAxis());
        assertEquals(99, dto.getOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReport.ChartConfiguration dto1 = AnalyticsReport.ChartConfiguration.builder()
                        .id("test-id")
            .title("test-title")
            .chartType("test-chartType")
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .series(Collections.emptyList())
            .options(Collections.emptyMap())
            .order(42)
            .build();
        AnalyticsReport.ChartConfiguration dto2 = AnalyticsReport.ChartConfiguration.builder()
                        .id("test-id")
            .title("test-title")
            .chartType("test-chartType")
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .series(Collections.emptyList())
            .options(Collections.emptyMap())
            .order(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsReport.ChartConfiguration dto = AnalyticsReport.ChartConfiguration.builder()
                        .id("test-id")
            .title("test-title")
            .chartType("test-chartType")
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .series(Collections.emptyList())
            .options(Collections.emptyMap())
            .order(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}