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
class DashboardResponseDto_DashboardTypeDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.DashboardTypeDto dto = DashboardResponseDto.DashboardTypeDto.builder()
                        .name("test-name")
            .value("test-value")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-value", dto.getValue());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.DashboardTypeDto dto = new DashboardResponseDto.DashboardTypeDto();
        dto.setName("val-name");
        dto.setValue("val-value");
        assertEquals("val-name", dto.getName());
        assertEquals("val-value", dto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.DashboardTypeDto dto1 = DashboardResponseDto.DashboardTypeDto.builder()
                        .name("test-name")
            .value("test-value")
            .build();
        DashboardResponseDto.DashboardTypeDto dto2 = DashboardResponseDto.DashboardTypeDto.builder()
                        .name("test-name")
            .value("test-value")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.DashboardTypeDto dto = DashboardResponseDto.DashboardTypeDto.builder()
                        .name("test-name")
            .value("test-value")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}