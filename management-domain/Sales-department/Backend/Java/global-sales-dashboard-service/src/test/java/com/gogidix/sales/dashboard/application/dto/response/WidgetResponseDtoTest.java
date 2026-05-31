package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.WidgetResponseDto;
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
class WidgetResponseDtoTest {

        @Test
    void testBuilder() {
        WidgetResponseDto dto = WidgetResponseDto.builder()
                        .id("test-id")
            .widgetId("test-widgetId")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .title("test-title")
            .description("test-description")
            .widgetType("test-widgetType")
            .category("test-category")
            .value(null)
            .displayValue("test-displayValue")
            .trendInfo(null)
            .targetInfo(null)
            .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .isVisible(true)
            .isActive(true)
            .refreshFrequencyMinutes(42)
            .owner("test-owner")
            .tags(Collections.emptyList())
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-widgetId", dto.getWidgetId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-dashboardId", dto.getDashboardId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-widgetType", dto.getWidgetType());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-displayValue", dto.getDisplayValue());
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
        assertEquals(42, dto.getRowSpan());
        assertEquals(42, dto.getColumnSpan());
        assertTrue(dto.getIsVisible());
        assertTrue(dto.getIsActive());
        assertEquals(42, dto.getRefreshFrequencyMinutes());
        assertEquals("test-owner", dto.getOwner());
    }

    @Test
    void testSettersAndGetters() {
        WidgetResponseDto dto = new WidgetResponseDto();
        dto.setId("val-id");
        dto.setWidgetId("val-widgetId");
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setWidgetType("val-widgetType");
        dto.setCategory("val-category");
        dto.setDisplayValue("val-displayValue");
        dto.setRow(99);
        dto.setColumn(99);
        dto.setRowSpan(99);
        dto.setColumnSpan(99);
        dto.setIsVisible(true);
        dto.setIsActive(true);
        dto.setRefreshFrequencyMinutes(99);
        dto.setOwner("val-owner");
        assertEquals("val-id", dto.getId());
        assertEquals("val-widgetId", dto.getWidgetId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-widgetType", dto.getWidgetType());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-displayValue", dto.getDisplayValue());
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
        assertEquals(99, dto.getRowSpan());
        assertEquals(99, dto.getColumnSpan());
        assertTrue(dto.getIsVisible());
        assertTrue(dto.getIsActive());
        assertEquals(99, dto.getRefreshFrequencyMinutes());
        assertEquals("val-owner", dto.getOwner());
    }

    @Test
    void testEqualsAndHashCode() {
        WidgetResponseDto dto1 = WidgetResponseDto.builder()
                        .id("test-id")
            .widgetId("test-widgetId")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .title("test-title")
            .description("test-description")
            .widgetType("test-widgetType")
            .category("test-category")
            .value(null)
            .displayValue("test-displayValue")
            .trendInfo(null)
            .targetInfo(null)
            .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .isVisible(true)
            .isActive(true)
            .refreshFrequencyMinutes(42)
            .owner("test-owner")
            .tags(Collections.emptyList())
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        WidgetResponseDto dto2 = WidgetResponseDto.builder()
                        .id("test-id")
            .widgetId("test-widgetId")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .title("test-title")
            .description("test-description")
            .widgetType("test-widgetType")
            .category("test-category")
            .value(null)
            .displayValue("test-displayValue")
            .trendInfo(null)
            .targetInfo(null)
            .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .isVisible(true)
            .isActive(true)
            .refreshFrequencyMinutes(42)
            .owner("test-owner")
            .tags(Collections.emptyList())
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        WidgetResponseDto dto = WidgetResponseDto.builder()
                        .id("test-id")
            .widgetId("test-widgetId")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .title("test-title")
            .description("test-description")
            .widgetType("test-widgetType")
            .category("test-category")
            .value(null)
            .displayValue("test-displayValue")
            .trendInfo(null)
            .targetInfo(null)
            .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .isVisible(true)
            .isActive(true)
            .refreshFrequencyMinutes(42)
            .owner("test-owner")
            .tags(Collections.emptyList())
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}