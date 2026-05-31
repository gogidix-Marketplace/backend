package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_DashboardConfigurationDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.DashboardConfigurationDto dto = DashboardResponseDto.DashboardConfigurationDto.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledRegions(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .dateRange("test-dateRange")
            .comparisonMode("test-comparisonMode")
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRefreshIntervalMinutes());
        assertTrue(dto.getAutoRefresh());
        assertEquals("test-dateRange", dto.getDateRange());
        assertEquals("test-comparisonMode", dto.getComparisonMode());
        assertTrue(dto.getShowTargets());
        assertTrue(dto.getShowForecasts());
        assertEquals("test-defaultView", dto.getDefaultView());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.DashboardConfigurationDto dto = new DashboardResponseDto.DashboardConfigurationDto();
        dto.setRefreshIntervalMinutes(99);
        dto.setAutoRefresh(true);
        dto.setDateRange("val-dateRange");
        dto.setComparisonMode("val-comparisonMode");
        dto.setShowTargets(true);
        dto.setShowForecasts(true);
        dto.setDefaultView("val-defaultView");
        assertEquals(99, dto.getRefreshIntervalMinutes());
        assertTrue(dto.getAutoRefresh());
        assertEquals("val-dateRange", dto.getDateRange());
        assertEquals("val-comparisonMode", dto.getComparisonMode());
        assertTrue(dto.getShowTargets());
        assertTrue(dto.getShowForecasts());
        assertEquals("val-defaultView", dto.getDefaultView());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.DashboardConfigurationDto dto1 = DashboardResponseDto.DashboardConfigurationDto.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledRegions(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .dateRange("test-dateRange")
            .comparisonMode("test-comparisonMode")
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .build();
        DashboardResponseDto.DashboardConfigurationDto dto2 = DashboardResponseDto.DashboardConfigurationDto.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledRegions(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .dateRange("test-dateRange")
            .comparisonMode("test-comparisonMode")
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.DashboardConfigurationDto dto = DashboardResponseDto.DashboardConfigurationDto.builder()
                        .refreshIntervalMinutes(42)
            .autoRefresh(true)
            .enabledRegions(Collections.emptyList())
            .enabledMetrics(Collections.emptyList())
            .dateRange("test-dateRange")
            .comparisonMode("test-comparisonMode")
            .showTargets(true)
            .showForecasts(true)
            .defaultView("test-defaultView")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}