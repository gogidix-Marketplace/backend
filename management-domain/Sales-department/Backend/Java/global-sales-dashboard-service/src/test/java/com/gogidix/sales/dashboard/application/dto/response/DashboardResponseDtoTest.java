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
class DashboardResponseDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto dto = DashboardResponseDto.builder()
                        .id("test-id")
            .dashboardId("test-dashboardId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .status(null)
            .type(null)
            .globalMetrics(null)
            .regionalMetrics(Collections.emptyList())
            .widgets(Collections.emptyList())
            .trendData(Collections.emptyList())
            .executiveSummary(null)
            .configuration(null)
            .baseCurrency("test-baseCurrency")
            .lastRefreshAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshedBy("test-lastRefreshedBy")
            .dataFreshness(null)
            .drillDownConfigs(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-dashboardId", dto.getDashboardId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals("test-lastRefreshedBy", dto.getLastRefreshedBy());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto dto = new DashboardResponseDto();
        dto.setId("val-id");
        dto.setDashboardId("val-dashboardId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setLastRefreshedBy("val-lastRefreshedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-lastRefreshedBy", dto.getLastRefreshedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto dto1 = DashboardResponseDto.builder()
                        .id("test-id")
            .dashboardId("test-dashboardId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .status(null)
            .type(null)
            .globalMetrics(null)
            .regionalMetrics(Collections.emptyList())
            .widgets(Collections.emptyList())
            .trendData(Collections.emptyList())
            .executiveSummary(null)
            .configuration(null)
            .baseCurrency("test-baseCurrency")
            .lastRefreshAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshedBy("test-lastRefreshedBy")
            .dataFreshness(null)
            .drillDownConfigs(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        DashboardResponseDto dto2 = DashboardResponseDto.builder()
                        .id("test-id")
            .dashboardId("test-dashboardId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .status(null)
            .type(null)
            .globalMetrics(null)
            .regionalMetrics(Collections.emptyList())
            .widgets(Collections.emptyList())
            .trendData(Collections.emptyList())
            .executiveSummary(null)
            .configuration(null)
            .baseCurrency("test-baseCurrency")
            .lastRefreshAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshedBy("test-lastRefreshedBy")
            .dataFreshness(null)
            .drillDownConfigs(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto dto = DashboardResponseDto.builder()
                        .id("test-id")
            .dashboardId("test-dashboardId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .status(null)
            .type(null)
            .globalMetrics(null)
            .regionalMetrics(Collections.emptyList())
            .widgets(Collections.emptyList())
            .trendData(Collections.emptyList())
            .executiveSummary(null)
            .configuration(null)
            .baseCurrency("test-baseCurrency")
            .lastRefreshAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshedBy("test-lastRefreshedBy")
            .dataFreshness(null)
            .drillDownConfigs(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}