package com.gogidix.management.executive.application.dto;

import com.gogidix.management.executive.application.dto.KpiWidgetDto;
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
class KpiWidgetDtoTest {

        @Test
    void testBuilder() {
        KpiWidgetDto dto = KpiWidgetDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .name("test-name")
            .description("test-description")
            .metricType("test-metricType")
            .dataSource("test-dataSource")
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config(Collections.emptyMap())
            .status("test-status")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-dashboardId", dto.getDashboardId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-metricType", dto.getMetricType());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals(42, dto.getPosition());
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
        assertEquals(42, dto.getWidth());
        assertEquals(42, dto.getHeight());
        assertEquals("test-status", dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        KpiWidgetDto dto = new KpiWidgetDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setDashboardId("val-dashboardId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setMetricType("val-metricType");
        dto.setDataSource("val-dataSource");
        dto.setPosition(99);
        dto.setRow(99);
        dto.setColumn(99);
        dto.setWidth(99);
        dto.setHeight(99);
        dto.setStatus("val-status");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dashboardId", dto.getDashboardId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-metricType", dto.getMetricType());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals(99, dto.getPosition());
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
        assertEquals(99, dto.getWidth());
        assertEquals(99, dto.getHeight());
        assertEquals("val-status", dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        KpiWidgetDto dto1 = KpiWidgetDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .name("test-name")
            .description("test-description")
            .metricType("test-metricType")
            .dataSource("test-dataSource")
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config(Collections.emptyMap())
            .status("test-status")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        KpiWidgetDto dto2 = KpiWidgetDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .name("test-name")
            .description("test-description")
            .metricType("test-metricType")
            .dataSource("test-dataSource")
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config(Collections.emptyMap())
            .status("test-status")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KpiWidgetDto dto = KpiWidgetDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .dashboardId("test-dashboardId")
            .name("test-name")
            .description("test-description")
            .metricType("test-metricType")
            .dataSource("test-dataSource")
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config(Collections.emptyMap())
            .status("test-status")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}