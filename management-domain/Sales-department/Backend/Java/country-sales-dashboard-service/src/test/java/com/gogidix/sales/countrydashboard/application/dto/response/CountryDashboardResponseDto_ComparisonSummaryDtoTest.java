package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.CountryDashboardResponseDto;
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
class CountryDashboardResponseDto_ComparisonSummaryDtoTest {

        @Test
    void testBuilder() {
        CountryDashboardResponseDto.ComparisonSummaryDto dto = CountryDashboardResponseDto.ComparisonSummaryDto.builder()
                        .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .trend("test-trend")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.TEN, dto.getMonthOverMonthGrowth());
        assertEquals(BigDecimal.TEN, dto.getQuarterOverQuarterGrowth());
        assertEquals("test-trend", dto.getTrend());
    }

    @Test
    void testSettersAndGetters() {
        CountryDashboardResponseDto.ComparisonSummaryDto dto = new CountryDashboardResponseDto.ComparisonSummaryDto();
        dto.setYearOverYearGrowth(BigDecimal.ONE);
        dto.setMonthOverMonthGrowth(BigDecimal.ONE);
        dto.setQuarterOverQuarterGrowth(BigDecimal.ONE);
        dto.setTrend("val-trend");
        assertEquals(BigDecimal.ONE, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.ONE, dto.getMonthOverMonthGrowth());
        assertEquals(BigDecimal.ONE, dto.getQuarterOverQuarterGrowth());
        assertEquals("val-trend", dto.getTrend());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardResponseDto.ComparisonSummaryDto dto1 = CountryDashboardResponseDto.ComparisonSummaryDto.builder()
                        .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .trend("test-trend")
            .build();
        CountryDashboardResponseDto.ComparisonSummaryDto dto2 = CountryDashboardResponseDto.ComparisonSummaryDto.builder()
                        .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .trend("test-trend")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryDashboardResponseDto.ComparisonSummaryDto dto = CountryDashboardResponseDto.ComparisonSummaryDto.builder()
                        .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .trend("test-trend")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}