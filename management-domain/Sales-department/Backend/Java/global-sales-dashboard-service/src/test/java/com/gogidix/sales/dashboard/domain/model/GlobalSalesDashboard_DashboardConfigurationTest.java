package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
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
class GlobalSalesDashboard_DashboardConfigurationTest {

        @Test
    void testBuilder() {
        GlobalSalesDashboard.DashboardConfiguration dto = GlobalSalesDashboard.DashboardConfiguration.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledRegions(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .dateRange("test-dateRange")
            .comparisonMode(null)
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRefreshIntervalMinutes());
        assertTrue(dto.getAutoRefresh());
        assertEquals("test-dateRange", dto.getDateRange());
        assertTrue(dto.getShowTargets());
        assertTrue(dto.getShowForecasts());
        assertEquals("test-defaultView", dto.getDefaultView());
    }

    @Test
    void testSettersAndGetters() {
        GlobalSalesDashboard.DashboardConfiguration dto = new GlobalSalesDashboard.DashboardConfiguration();
        dto.setRefreshIntervalMinutes(99);
        dto.setAutoRefresh(true);
        dto.setDateRange("val-dateRange");
        dto.setShowTargets(true);
        dto.setShowForecasts(true);
        dto.setDefaultView("val-defaultView");
        assertEquals(99, dto.getRefreshIntervalMinutes());
        assertTrue(dto.getAutoRefresh());
        assertEquals("val-dateRange", dto.getDateRange());
        assertTrue(dto.getShowTargets());
        assertTrue(dto.getShowForecasts());
        assertEquals("val-defaultView", dto.getDefaultView());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalSalesDashboard.DashboardConfiguration dto1 = GlobalSalesDashboard.DashboardConfiguration.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledRegions(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .dateRange("test-dateRange")
            .comparisonMode(null)
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .build();
        GlobalSalesDashboard.DashboardConfiguration dto2 = GlobalSalesDashboard.DashboardConfiguration.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledRegions(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .dateRange("test-dateRange")
            .comparisonMode(null)
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalSalesDashboard.DashboardConfiguration dto = GlobalSalesDashboard.DashboardConfiguration.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledRegions(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .dateRange("test-dateRange")
            .comparisonMode(null)
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}