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
class DashboardResponseDto_RegionalMetricDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.RegionalMetricDto dto = DashboardResponseDto.RegionalMetricDto.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(null)
            .target(null)
            .achievementPercentage(null)
            .deals(42)
            .growthRate(null)
            .rank(42)
            .currency("test-currency")
            .build();
        assertNotNull(dto);
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-regionName", dto.getRegionName());
        assertEquals(42, dto.getDeals());
        assertEquals(42, dto.getRank());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.RegionalMetricDto dto = new DashboardResponseDto.RegionalMetricDto();
        dto.setRegionCode("val-regionCode");
        dto.setRegionName("val-regionName");
        dto.setDeals(99);
        dto.setRank(99);
        dto.setCurrency("val-currency");
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-regionName", dto.getRegionName());
        assertEquals(99, dto.getDeals());
        assertEquals(99, dto.getRank());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.RegionalMetricDto dto1 = DashboardResponseDto.RegionalMetricDto.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(null)
            .target(null)
            .achievementPercentage(null)
            .deals(42)
            .growthRate(null)
            .rank(42)
            .currency("test-currency")
            .build();
        DashboardResponseDto.RegionalMetricDto dto2 = DashboardResponseDto.RegionalMetricDto.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(null)
            .target(null)
            .achievementPercentage(null)
            .deals(42)
            .growthRate(null)
            .rank(42)
            .currency("test-currency")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.RegionalMetricDto dto = DashboardResponseDto.RegionalMetricDto.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(null)
            .target(null)
            .achievementPercentage(null)
            .deals(42)
            .growthRate(null)
            .rank(42)
            .currency("test-currency")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}