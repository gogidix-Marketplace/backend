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
class DashboardResponseDto_WidgetTypeDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.WidgetTypeDto dto = DashboardResponseDto.WidgetTypeDto.builder()
                        .name("test-name")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.WidgetTypeDto dto = new DashboardResponseDto.WidgetTypeDto();
        dto.setName("val-name");
        assertEquals("val-name", dto.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.WidgetTypeDto dto1 = DashboardResponseDto.WidgetTypeDto.builder()
                        .name("test-name")
            .build();
        DashboardResponseDto.WidgetTypeDto dto2 = DashboardResponseDto.WidgetTypeDto.builder()
                        .name("test-name")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.WidgetTypeDto dto = DashboardResponseDto.WidgetTypeDto.builder()
                        .name("test-name")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}