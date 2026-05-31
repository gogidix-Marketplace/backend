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
class DashboardWidgetResponseDto_WidgetSizeDtoTest {

        @Test
    void testBuilder() {
        DashboardWidgetResponseDto.WidgetSizeDto dto = DashboardWidgetResponseDto.WidgetSizeDto.builder()
                        .name("test-name")
            .width(42)
            .height(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals(42, dto.getWidth());
        assertEquals(42, dto.getHeight());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidgetResponseDto.WidgetSizeDto dto = new DashboardWidgetResponseDto.WidgetSizeDto();
        dto.setName("val-name");
        dto.setWidth(99);
        dto.setHeight(99);
        assertEquals("val-name", dto.getName());
        assertEquals(99, dto.getWidth());
        assertEquals(99, dto.getHeight());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidgetResponseDto.WidgetSizeDto dto1 = DashboardWidgetResponseDto.WidgetSizeDto.builder()
                        .name("test-name")
            .width(42)
            .height(42)
            .build();
        DashboardWidgetResponseDto.WidgetSizeDto dto2 = DashboardWidgetResponseDto.WidgetSizeDto.builder()
                        .name("test-name")
            .width(42)
            .height(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidgetResponseDto.WidgetSizeDto dto = DashboardWidgetResponseDto.WidgetSizeDto.builder()
                        .name("test-name")
            .width(42)
            .height(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}