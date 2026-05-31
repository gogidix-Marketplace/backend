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
class DashboardResponseDto_WidgetCategoryDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.WidgetCategoryDto dto = DashboardResponseDto.WidgetCategoryDto.builder()
                        .name("test-name")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.WidgetCategoryDto dto = new DashboardResponseDto.WidgetCategoryDto();
        dto.setName("val-name");
        assertEquals("val-name", dto.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.WidgetCategoryDto dto1 = DashboardResponseDto.WidgetCategoryDto.builder()
                        .name("test-name")
            .build();
        DashboardResponseDto.WidgetCategoryDto dto2 = DashboardResponseDto.WidgetCategoryDto.builder()
                        .name("test-name")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.WidgetCategoryDto dto = DashboardResponseDto.WidgetCategoryDto.builder()
                        .name("test-name")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}