package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model.Widget;
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
class Widget_WidgetConfigTest {

        @Test
    void testBuilder() {
        Widget.WidgetConfig dto = Widget.WidgetConfig.builder()
                        .chartType("test-chartType")
            .refreshInterval(42)
            .autoRefresh(true)
            .showLegend(true)
            .showGrid(true)
            .showLabels(true)
            .showTooltip(true)
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .groupBy("test-groupBy")
            .aggregateBy("test-aggregateBy")
            .timeGranularity("test-timeGranularity")
            .limit(42)
            .sortBy("test-sortBy")
            .sortOrder("test-sortOrder")
            .customConfig(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-chartType", dto.getChartType());
        assertEquals(42, dto.getRefreshInterval());
        assertTrue(dto.getAutoRefresh());
        assertTrue(dto.getShowLegend());
        assertTrue(dto.getShowGrid());
        assertTrue(dto.getShowLabels());
        assertTrue(dto.getShowTooltip());
        assertEquals("test-xAxis", dto.getXAxis());
        assertEquals("test-yAxis", dto.getYAxis());
        assertEquals("test-groupBy", dto.getGroupBy());
        assertEquals("test-aggregateBy", dto.getAggregateBy());
        assertEquals("test-timeGranularity", dto.getTimeGranularity());
        assertEquals(42, dto.getLimit());
        assertEquals("test-sortBy", dto.getSortBy());
        assertEquals("test-sortOrder", dto.getSortOrder());
    }

    @Test
    void testSettersAndGetters() {
        Widget.WidgetConfig dto = new Widget.WidgetConfig();
        dto.setChartType("val-chartType");
        dto.setRefreshInterval(99);
        dto.setAutoRefresh(true);
        dto.setShowLegend(true);
        dto.setShowGrid(true);
        dto.setShowLabels(true);
        dto.setShowTooltip(true);
        dto.setXAxis("val-xAxis");
        dto.setYAxis("val-yAxis");
        dto.setGroupBy("val-groupBy");
        dto.setAggregateBy("val-aggregateBy");
        dto.setTimeGranularity("val-timeGranularity");
        dto.setLimit(99);
        dto.setSortBy("val-sortBy");
        dto.setSortOrder("val-sortOrder");
        assertEquals("val-chartType", dto.getChartType());
        assertEquals(99, dto.getRefreshInterval());
        assertTrue(dto.getAutoRefresh());
        assertTrue(dto.getShowLegend());
        assertTrue(dto.getShowGrid());
        assertTrue(dto.getShowLabels());
        assertTrue(dto.getShowTooltip());
        assertEquals("val-xAxis", dto.getXAxis());
        assertEquals("val-yAxis", dto.getYAxis());
        assertEquals("val-groupBy", dto.getGroupBy());
        assertEquals("val-aggregateBy", dto.getAggregateBy());
        assertEquals("val-timeGranularity", dto.getTimeGranularity());
        assertEquals(99, dto.getLimit());
        assertEquals("val-sortBy", dto.getSortBy());
        assertEquals("val-sortOrder", dto.getSortOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        Widget.WidgetConfig dto1 = Widget.WidgetConfig.builder()
                        .chartType("test-chartType")
            .refreshInterval(42)
            .autoRefresh(true)
            .showLegend(true)
            .showGrid(true)
            .showLabels(true)
            .showTooltip(true)
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .groupBy("test-groupBy")
            .aggregateBy("test-aggregateBy")
            .timeGranularity("test-timeGranularity")
            .limit(42)
            .sortBy("test-sortBy")
            .sortOrder("test-sortOrder")
            .customConfig(Collections.emptyMap())
            .build();
        Widget.WidgetConfig dto2 = Widget.WidgetConfig.builder()
                        .chartType("test-chartType")
            .refreshInterval(42)
            .autoRefresh(true)
            .showLegend(true)
            .showGrid(true)
            .showLabels(true)
            .showTooltip(true)
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .groupBy("test-groupBy")
            .aggregateBy("test-aggregateBy")
            .timeGranularity("test-timeGranularity")
            .limit(42)
            .sortBy("test-sortBy")
            .sortOrder("test-sortOrder")
            .customConfig(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Widget.WidgetConfig dto = Widget.WidgetConfig.builder()
                        .chartType("test-chartType")
            .refreshInterval(42)
            .autoRefresh(true)
            .showLegend(true)
            .showGrid(true)
            .showLabels(true)
            .showTooltip(true)
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .groupBy("test-groupBy")
            .aggregateBy("test-aggregateBy")
            .timeGranularity("test-timeGranularity")
            .limit(42)
            .sortBy("test-sortBy")
            .sortOrder("test-sortOrder")
            .customConfig(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}