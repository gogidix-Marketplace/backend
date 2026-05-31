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
class DashboardWidgetResponseDtoTest {

        @Test
    void testBuilder() {
        DashboardWidgetResponseDto dto = DashboardWidgetResponseDto.builder()
                        .id("test-id")
            .widgetId("test-widgetId")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .name("test-name")
            .description("test-description")
            .type("test-type")
            .status("test-status")
            .position(42)
            .size(null)
            .location(null)
            .dataSource(null)
            .metricConfig(null)
            .visualizationConfig(null)
            .properties(Collections.emptyMap())
            .refreshConfig(null)
            .drilldownConfig(null)
            .allowDrilldown(true)
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-widgetId", dto.getWidgetId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-dashboardId", dto.getDashboardId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-type", dto.getType());
        assertEquals("test-status", dto.getStatus());
        assertEquals(42, dto.getPosition());
        assertTrue(dto.getAllowDrilldown());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidgetResponseDto dto = new DashboardWidgetResponseDto();
        dto.setId("val-id");
        dto.setWidgetId("val-widgetId");
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setType("val-type");
        dto.setStatus("val-status");
        dto.setPosition(99);
        dto.setAllowDrilldown(true);
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-widgetId", dto.getWidgetId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-type", dto.getType());
        assertEquals("val-status", dto.getStatus());
        assertEquals(99, dto.getPosition());
        assertTrue(dto.getAllowDrilldown());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidgetResponseDto dto1 = DashboardWidgetResponseDto.builder()
                        .id("test-id")
            .widgetId("test-widgetId")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .name("test-name")
            .description("test-description")
            .type("test-type")
            .status("test-status")
            .position(42)
            .size(null)
            .location(null)
            .dataSource(null)
            .metricConfig(null)
            .visualizationConfig(null)
            .properties(Collections.emptyMap())
            .refreshConfig(null)
            .drilldownConfig(null)
            .allowDrilldown(true)
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        DashboardWidgetResponseDto dto2 = DashboardWidgetResponseDto.builder()
                        .id("test-id")
            .widgetId("test-widgetId")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .name("test-name")
            .description("test-description")
            .type("test-type")
            .status("test-status")
            .position(42)
            .size(null)
            .location(null)
            .dataSource(null)
            .metricConfig(null)
            .visualizationConfig(null)
            .properties(Collections.emptyMap())
            .refreshConfig(null)
            .drilldownConfig(null)
            .allowDrilldown(true)
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidgetResponseDto dto = DashboardWidgetResponseDto.builder()
                        .id("test-id")
            .widgetId("test-widgetId")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .name("test-name")
            .description("test-description")
            .type("test-type")
            .status("test-status")
            .position(42)
            .size(null)
            .location(null)
            .dataSource(null)
            .metricConfig(null)
            .visualizationConfig(null)
            .properties(Collections.emptyMap())
            .refreshConfig(null)
            .drilldownConfig(null)
            .allowDrilldown(true)
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastRefreshedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}