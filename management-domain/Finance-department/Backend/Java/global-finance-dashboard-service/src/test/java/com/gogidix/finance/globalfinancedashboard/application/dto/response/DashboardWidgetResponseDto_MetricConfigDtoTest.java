package com.gogidix.finance.globalfinancedashboard.application.dto.response;

import com.gogidix.finance.globalfinancedashboard.application.dto.response.DashboardWidgetResponseDto;
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
class DashboardWidgetResponseDto_MetricConfigDtoTest {

        @Test
    void testBuilder() {
        DashboardWidgetResponseDto.MetricConfigDto dto = DashboardWidgetResponseDto.MetricConfigDto.builder()
                        .metricType("test-metricType")
            .aggregationType("test-aggregationType")
            .metricFields(Collections.emptyList())
            .groupByField("test-groupByField")
            .timeField("test-timeField")
            .timeGrain("test-timeGrain")
            .filterExpression("test-filterExpression")
            .aliases(Collections.emptyMap())
            .calculations(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-metricType", dto.getMetricType());
        assertEquals("test-aggregationType", dto.getAggregationType());
        assertEquals("test-groupByField", dto.getGroupByField());
        assertEquals("test-timeField", dto.getTimeField());
        assertEquals("test-timeGrain", dto.getTimeGrain());
        assertEquals("test-filterExpression", dto.getFilterExpression());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidgetResponseDto.MetricConfigDto dto = new DashboardWidgetResponseDto.MetricConfigDto();
        dto.setMetricType("val-metricType");
        dto.setAggregationType("val-aggregationType");
        dto.setGroupByField("val-groupByField");
        dto.setTimeField("val-timeField");
        dto.setTimeGrain("val-timeGrain");
        dto.setFilterExpression("val-filterExpression");
        assertEquals("val-metricType", dto.getMetricType());
        assertEquals("val-aggregationType", dto.getAggregationType());
        assertEquals("val-groupByField", dto.getGroupByField());
        assertEquals("val-timeField", dto.getTimeField());
        assertEquals("val-timeGrain", dto.getTimeGrain());
        assertEquals("val-filterExpression", dto.getFilterExpression());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidgetResponseDto.MetricConfigDto dto1 = DashboardWidgetResponseDto.MetricConfigDto.builder()
                        .metricType("test-metricType")
            .aggregationType("test-aggregationType")
            .metricFields(Collections.emptyList())
            .groupByField("test-groupByField")
            .timeField("test-timeField")
            .timeGrain("test-timeGrain")
            .filterExpression("test-filterExpression")
            .aliases(Collections.emptyMap())
            .calculations(Collections.emptyList())
            .build();
        DashboardWidgetResponseDto.MetricConfigDto dto2 = DashboardWidgetResponseDto.MetricConfigDto.builder()
                        .metricType("test-metricType")
            .aggregationType("test-aggregationType")
            .metricFields(Collections.emptyList())
            .groupByField("test-groupByField")
            .timeField("test-timeField")
            .timeGrain("test-timeGrain")
            .filterExpression("test-filterExpression")
            .aliases(Collections.emptyMap())
            .calculations(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidgetResponseDto.MetricConfigDto dto = DashboardWidgetResponseDto.MetricConfigDto.builder()
                        .metricType("test-metricType")
            .aggregationType("test-aggregationType")
            .metricFields(Collections.emptyList())
            .groupByField("test-groupByField")
            .timeField("test-timeField")
            .timeGrain("test-timeGrain")
            .filterExpression("test-filterExpression")
            .aliases(Collections.emptyMap())
            .calculations(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}