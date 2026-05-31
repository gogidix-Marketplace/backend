package com.gogidix.sales.countrydashboard.domain.model;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.valueobject.TimePeriod;
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
class CountrySalesDashboard_DashboardConfigurationTest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.DashboardConfiguration dto = CountrySalesDashboard.DashboardConfiguration.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledTerritories(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .defaultPeriod(TimePeriod.PeriodType.DAILY)
            .comparisonMode(null)
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .maxTerritoryRankings(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRefreshIntervalMinutes());
        assertTrue(dto.getAutoRefresh());
        assertTrue(dto.getShowTargets());
        assertTrue(dto.getShowForecasts());
        assertEquals("test-defaultView", dto.getDefaultView());
        assertEquals(42, dto.getMaxTerritoryRankings());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.DashboardConfiguration dto = new CountrySalesDashboard.DashboardConfiguration();
        dto.setRefreshIntervalMinutes(99);
        dto.setAutoRefresh(true);
        dto.setShowTargets(true);
        dto.setShowForecasts(true);
        dto.setDefaultView("val-defaultView");
        dto.setMaxTerritoryRankings(99);
        assertEquals(99, dto.getRefreshIntervalMinutes());
        assertTrue(dto.getAutoRefresh());
        assertTrue(dto.getShowTargets());
        assertTrue(dto.getShowForecasts());
        assertEquals("val-defaultView", dto.getDefaultView());
        assertEquals(99, dto.getMaxTerritoryRankings());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.DashboardConfiguration dto1 = CountrySalesDashboard.DashboardConfiguration.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledTerritories(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .defaultPeriod(TimePeriod.PeriodType.DAILY)
            .comparisonMode(null)
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .maxTerritoryRankings(42)
            .build();
        CountrySalesDashboard.DashboardConfiguration dto2 = CountrySalesDashboard.DashboardConfiguration.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledTerritories(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .defaultPeriod(TimePeriod.PeriodType.DAILY)
            .comparisonMode(null)
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .maxTerritoryRankings(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySalesDashboard.DashboardConfiguration dto = CountrySalesDashboard.DashboardConfiguration.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledTerritories(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .defaultPeriod(TimePeriod.PeriodType.DAILY)
            .comparisonMode(null)
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .maxTerritoryRankings(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}