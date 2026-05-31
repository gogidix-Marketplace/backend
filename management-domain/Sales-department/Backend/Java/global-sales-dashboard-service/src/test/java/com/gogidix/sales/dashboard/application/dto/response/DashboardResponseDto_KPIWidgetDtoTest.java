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
class DashboardResponseDto_KPIWidgetDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.KPIWidgetDto dto = DashboardResponseDto.KPIWidgetDto.builder()
                        .widgetId("test-widgetId")
            .title("test-title")
            .description("test-description")
            .type(null)
            .category(null)
            .value(null)
            .displayValue("test-displayValue")
            .trendInfo(null)
            .targetInfo(null)
            .row(42)
            .column(42)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-widgetId", dto.getWidgetId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-displayValue", dto.getDisplayValue());
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.KPIWidgetDto dto = new DashboardResponseDto.KPIWidgetDto();
        dto.setWidgetId("val-widgetId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setDisplayValue("val-displayValue");
        dto.setRow(99);
        dto.setColumn(99);
        dto.setIsActive(true);
        assertEquals("val-widgetId", dto.getWidgetId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-displayValue", dto.getDisplayValue());
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.KPIWidgetDto dto1 = DashboardResponseDto.KPIWidgetDto.builder()
                        .widgetId("test-widgetId")
            .title("test-title")
            .description("test-description")
            .type(null)
            .category(null)
            .value(null)
            .displayValue("test-displayValue")
            .trendInfo(null)
            .targetInfo(null)
            .row(42)
            .column(42)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        DashboardResponseDto.KPIWidgetDto dto2 = DashboardResponseDto.KPIWidgetDto.builder()
                        .widgetId("test-widgetId")
            .title("test-title")
            .description("test-description")
            .type(null)
            .category(null)
            .value(null)
            .displayValue("test-displayValue")
            .trendInfo(null)
            .targetInfo(null)
            .row(42)
            .column(42)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.KPIWidgetDto dto = DashboardResponseDto.KPIWidgetDto.builder()
                        .widgetId("test-widgetId")
            .title("test-title")
            .description("test-description")
            .type(null)
            .category(null)
            .value(null)
            .displayValue("test-displayValue")
            .trendInfo(null)
            .targetInfo(null)
            .row(42)
            .column(42)
            .isActive(true)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}