package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardWidget;
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
class DashboardWidget_VisualizationConfigTest {

        @Test
    void testBuilder() {
        DashboardWidget.VisualizationConfig dto = DashboardWidget.VisualizationConfig.builder()
                        .chartType("test-chartType")
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .colorBy("test-colorBy")
            .seriesBy("test-seriesBy")
            .showLegend(true)
            .showDataLabels(true)
            .showGridLines(true)
            .colorScheme("test-colorScheme")
            .maxDataPoints(42)
            .sortOrder("test-sortOrder")
            .limit(42)
            .customOptions(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-chartType", dto.getChartType());
        assertEquals("test-xAxis", dto.getXAxis());
        assertEquals("test-yAxis", dto.getYAxis());
        assertEquals("test-colorBy", dto.getColorBy());
        assertEquals("test-seriesBy", dto.getSeriesBy());
        assertTrue(dto.getShowLegend());
        assertTrue(dto.getShowDataLabels());
        assertTrue(dto.getShowGridLines());
        assertEquals("test-colorScheme", dto.getColorScheme());
        assertEquals(42, dto.getMaxDataPoints());
        assertEquals("test-sortOrder", dto.getSortOrder());
        assertEquals(42, dto.getLimit());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidget.VisualizationConfig dto = new DashboardWidget.VisualizationConfig();
        dto.setChartType("val-chartType");
        dto.setXAxis("val-xAxis");
        dto.setYAxis("val-yAxis");
        dto.setColorBy("val-colorBy");
        dto.setSeriesBy("val-seriesBy");
        dto.setShowLegend(true);
        dto.setShowDataLabels(true);
        dto.setShowGridLines(true);
        dto.setColorScheme("val-colorScheme");
        dto.setMaxDataPoints(99);
        dto.setSortOrder("val-sortOrder");
        dto.setLimit(99);
        assertEquals("val-chartType", dto.getChartType());
        assertEquals("val-xAxis", dto.getXAxis());
        assertEquals("val-yAxis", dto.getYAxis());
        assertEquals("val-colorBy", dto.getColorBy());
        assertEquals("val-seriesBy", dto.getSeriesBy());
        assertTrue(dto.getShowLegend());
        assertTrue(dto.getShowDataLabels());
        assertTrue(dto.getShowGridLines());
        assertEquals("val-colorScheme", dto.getColorScheme());
        assertEquals(99, dto.getMaxDataPoints());
        assertEquals("val-sortOrder", dto.getSortOrder());
        assertEquals(99, dto.getLimit());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidget.VisualizationConfig dto1 = DashboardWidget.VisualizationConfig.builder()
                        .chartType("test-chartType")
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .colorBy("test-colorBy")
            .seriesBy("test-seriesBy")
            .showLegend(true)
            .showDataLabels(true)
            .showGridLines(true)
            .colorScheme("test-colorScheme")
            .maxDataPoints(42)
            .sortOrder("test-sortOrder")
            .limit(42)
            .customOptions(Collections.emptyMap())
            .build();
        DashboardWidget.VisualizationConfig dto2 = DashboardWidget.VisualizationConfig.builder()
                        .chartType("test-chartType")
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .colorBy("test-colorBy")
            .seriesBy("test-seriesBy")
            .showLegend(true)
            .showDataLabels(true)
            .showGridLines(true)
            .colorScheme("test-colorScheme")
            .maxDataPoints(42)
            .sortOrder("test-sortOrder")
            .limit(42)
            .customOptions(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidget.VisualizationConfig dto = DashboardWidget.VisualizationConfig.builder()
                        .chartType("test-chartType")
            .xAxis("test-xAxis")
            .yAxis("test-yAxis")
            .colorBy("test-colorBy")
            .seriesBy("test-seriesBy")
            .showLegend(true)
            .showDataLabels(true)
            .showGridLines(true)
            .colorScheme("test-colorScheme")
            .maxDataPoints(42)
            .sortOrder("test-sortOrder")
            .limit(42)
            .customOptions(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}