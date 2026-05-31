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
class DashboardWidgetResponseDto_WidgetLocationDtoTest {

        @Test
    void testBuilder() {
        DashboardWidgetResponseDto.WidgetLocationDto dto = DashboardWidgetResponseDto.WidgetLocationDto.builder()
                        .row(42)
            .column(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidgetResponseDto.WidgetLocationDto dto = new DashboardWidgetResponseDto.WidgetLocationDto();
        dto.setRow(99);
        dto.setColumn(99);
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidgetResponseDto.WidgetLocationDto dto1 = DashboardWidgetResponseDto.WidgetLocationDto.builder()
                        .row(42)
            .column(42)
            .build();
        DashboardWidgetResponseDto.WidgetLocationDto dto2 = DashboardWidgetResponseDto.WidgetLocationDto.builder()
                        .row(42)
            .column(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidgetResponseDto.WidgetLocationDto dto = DashboardWidgetResponseDto.WidgetLocationDto.builder()
                        .row(42)
            .column(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}