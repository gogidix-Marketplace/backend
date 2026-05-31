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
class DashboardResponseDto_TrendInfoDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.TrendInfoDto dto = DashboardResponseDto.TrendInfoDto.builder()
                        .direction("test-direction")
            .value(null)
            .percentage("test-percentage")
            .isPositive(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-direction", dto.getDirection());
        assertEquals("test-percentage", dto.getPercentage());
        assertTrue(dto.getIsPositive());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.TrendInfoDto dto = new DashboardResponseDto.TrendInfoDto();
        dto.setDirection("val-direction");
        dto.setPercentage("val-percentage");
        dto.setIsPositive(true);
        assertEquals("val-direction", dto.getDirection());
        assertEquals("val-percentage", dto.getPercentage());
        assertTrue(dto.getIsPositive());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.TrendInfoDto dto1 = DashboardResponseDto.TrendInfoDto.builder()
                        .direction("test-direction")
            .value(null)
            .percentage("test-percentage")
            .isPositive(true)
            .build();
        DashboardResponseDto.TrendInfoDto dto2 = DashboardResponseDto.TrendInfoDto.builder()
                        .direction("test-direction")
            .value(null)
            .percentage("test-percentage")
            .isPositive(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.TrendInfoDto dto = DashboardResponseDto.TrendInfoDto.builder()
                        .direction("test-direction")
            .value(null)
            .percentage("test-percentage")
            .isPositive(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}