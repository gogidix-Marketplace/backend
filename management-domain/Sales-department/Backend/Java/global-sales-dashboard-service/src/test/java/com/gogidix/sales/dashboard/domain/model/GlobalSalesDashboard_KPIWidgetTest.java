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
class GlobalSalesDashboard_KPIWidgetTest {

        @Test
    void testBuilder() {
        GlobalSalesDashboard.KPIWidget dto = GlobalSalesDashboard.KPIWidget.builder()
                        .widgetId("test-widgetId")
            .title("test-title")
            .type(null)
            .widgetType(null)
            .category(null)
            .description("test-description")
            .value(null)
            .format("test-format")
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .dataSource("test-dataSource")
            .position(42)
            .size(GlobalSalesDashboard.KPIWidget.WidgetSize.SMALL)
            .isVisible(true)
            .metadata(Collections.emptyMap())
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-widgetId", dto.getWidgetId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-format", dto.getFormat());
        assertEquals("test-trend", dto.getTrend());
        assertEquals(BigDecimal.TEN, dto.getTrendValue());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals(42, dto.getPosition());
        assertEquals(GlobalSalesDashboard.KPIWidget.WidgetSize.SMALL, dto.getSize());
        assertTrue(dto.getIsVisible());
    }

    @Test
    void testSettersAndGetters() {
        GlobalSalesDashboard.KPIWidget dto = new GlobalSalesDashboard.KPIWidget();
        dto.setWidgetId("val-widgetId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setFormat("val-format");
        dto.setTrend("val-trend");
        dto.setTrendValue(BigDecimal.ONE);
        dto.setDataSource("val-dataSource");
        dto.setPosition(99);
        dto.setSize(GlobalSalesDashboard.KPIWidget.WidgetSize.SMALL);
        dto.setIsVisible(true);
        assertEquals("val-widgetId", dto.getWidgetId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-format", dto.getFormat());
        assertEquals("val-trend", dto.getTrend());
        assertEquals(BigDecimal.ONE, dto.getTrendValue());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals(99, dto.getPosition());
        assertEquals(GlobalSalesDashboard.KPIWidget.WidgetSize.SMALL, dto.getSize());
        assertTrue(dto.getIsVisible());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalSalesDashboard.KPIWidget dto1 = GlobalSalesDashboard.KPIWidget.builder()
                        .widgetId("test-widgetId")
            .title("test-title")
            .type(null)
            .widgetType(null)
            .category(null)
            .description("test-description")
            .value(null)
            .format("test-format")
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .dataSource("test-dataSource")
            .position(42)
            .size(GlobalSalesDashboard.KPIWidget.WidgetSize.SMALL)
            .isVisible(true)
            .metadata(Collections.emptyMap())
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        GlobalSalesDashboard.KPIWidget dto2 = GlobalSalesDashboard.KPIWidget.builder()
                        .widgetId("test-widgetId")
            .title("test-title")
            .type(null)
            .widgetType(null)
            .category(null)
            .description("test-description")
            .value(null)
            .format("test-format")
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .dataSource("test-dataSource")
            .position(42)
            .size(GlobalSalesDashboard.KPIWidget.WidgetSize.SMALL)
            .isVisible(true)
            .metadata(Collections.emptyMap())
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalSalesDashboard.KPIWidget dto = GlobalSalesDashboard.KPIWidget.builder()
                        .widgetId("test-widgetId")
            .title("test-title")
            .type(null)
            .widgetType(null)
            .category(null)
            .description("test-description")
            .value(null)
            .format("test-format")
            .trend("test-trend")
            .trendValue(BigDecimal.TEN)
            .dataSource("test-dataSource")
            .position(42)
            .size(GlobalSalesDashboard.KPIWidget.WidgetSize.SMALL)
            .isVisible(true)
            .metadata(Collections.emptyMap())
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}