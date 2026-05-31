package com.gogidix.finance.globalfinancedashboard.application.dto.response;

import com.gogidix.finance.globalfinancedashboard.application.dto.response.DashboardViewResponseDto;
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
class DashboardViewResponseDto_WidgetConfigDtoTest {

        @Test
    void testBuilder() {
        DashboardViewResponseDto.WidgetConfigDto dto = DashboardViewResponseDto.WidgetConfigDto.builder()
                        .widgetId("test-widgetId")
            .type("test-type")
            .title("test-title")
            .positionX(42)
            .positionY(42)
            .width(42)
            .height(42)
            .config(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-widgetId", dto.getWidgetId());
        assertEquals("test-type", dto.getType());
        assertEquals("test-title", dto.getTitle());
        assertEquals(42, dto.getPositionX());
        assertEquals(42, dto.getPositionY());
        assertEquals(42, dto.getWidth());
        assertEquals(42, dto.getHeight());
    }

    @Test
    void testSettersAndGetters() {
        DashboardViewResponseDto.WidgetConfigDto dto = new DashboardViewResponseDto.WidgetConfigDto();
        dto.setWidgetId("val-widgetId");
        dto.setType("val-type");
        dto.setTitle("val-title");
        dto.setPositionX(99);
        dto.setPositionY(99);
        dto.setWidth(99);
        dto.setHeight(99);
        assertEquals("val-widgetId", dto.getWidgetId());
        assertEquals("val-type", dto.getType());
        assertEquals("val-title", dto.getTitle());
        assertEquals(99, dto.getPositionX());
        assertEquals(99, dto.getPositionY());
        assertEquals(99, dto.getWidth());
        assertEquals(99, dto.getHeight());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardViewResponseDto.WidgetConfigDto dto1 = DashboardViewResponseDto.WidgetConfigDto.builder()
                        .widgetId("test-widgetId")
            .type("test-type")
            .title("test-title")
            .positionX(42)
            .positionY(42)
            .width(42)
            .height(42)
            .config(Collections.emptyMap())
            .build();
        DashboardViewResponseDto.WidgetConfigDto dto2 = DashboardViewResponseDto.WidgetConfigDto.builder()
                        .widgetId("test-widgetId")
            .type("test-type")
            .title("test-title")
            .positionX(42)
            .positionY(42)
            .width(42)
            .height(42)
            .config(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardViewResponseDto.WidgetConfigDto dto = DashboardViewResponseDto.WidgetConfigDto.builder()
                        .widgetId("test-widgetId")
            .type("test-type")
            .title("test-title")
            .positionX(42)
            .positionY(42)
            .width(42)
            .height(42)
            .config(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}