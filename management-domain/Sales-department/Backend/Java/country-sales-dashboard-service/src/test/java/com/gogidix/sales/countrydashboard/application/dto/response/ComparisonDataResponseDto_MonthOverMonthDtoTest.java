package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.ComparisonDataResponseDto;
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
class ComparisonDataResponseDto_MonthOverMonthDtoTest {

        @Test
    void testBuilder() {
        ComparisonDataResponseDto.MonthOverMonthDto dto = ComparisonDataResponseDto.MonthOverMonthDto.builder()
                        .currentMonthRevenue(BigDecimal.TEN)
            .previousMonthRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentMonthDeals(42)
            .previousMonthDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCurrentMonthRevenue());
        assertEquals(BigDecimal.TEN, dto.getPreviousMonthRevenue());
        assertEquals(BigDecimal.TEN, dto.getGrowthPercentage());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(42, dto.getCurrentMonthDeals());
        assertEquals(42, dto.getPreviousMonthDeals());
        assertEquals(BigDecimal.TEN, dto.getDealsGrowthPercentage());
    }

    @Test
    void testSettersAndGetters() {
        ComparisonDataResponseDto.MonthOverMonthDto dto = new ComparisonDataResponseDto.MonthOverMonthDto();
        dto.setCurrentMonthRevenue(BigDecimal.ONE);
        dto.setPreviousMonthRevenue(BigDecimal.ONE);
        dto.setGrowthPercentage(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setCurrentMonthDeals(99);
        dto.setPreviousMonthDeals(99);
        dto.setDealsGrowthPercentage(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getCurrentMonthRevenue());
        assertEquals(BigDecimal.ONE, dto.getPreviousMonthRevenue());
        assertEquals(BigDecimal.ONE, dto.getGrowthPercentage());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(99, dto.getCurrentMonthDeals());
        assertEquals(99, dto.getPreviousMonthDeals());
        assertEquals(BigDecimal.ONE, dto.getDealsGrowthPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        ComparisonDataResponseDto.MonthOverMonthDto dto1 = ComparisonDataResponseDto.MonthOverMonthDto.builder()
                        .currentMonthRevenue(BigDecimal.TEN)
            .previousMonthRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentMonthDeals(42)
            .previousMonthDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ComparisonDataResponseDto.MonthOverMonthDto dto2 = ComparisonDataResponseDto.MonthOverMonthDto.builder()
                        .currentMonthRevenue(BigDecimal.TEN)
            .previousMonthRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentMonthDeals(42)
            .previousMonthDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComparisonDataResponseDto.MonthOverMonthDto dto = ComparisonDataResponseDto.MonthOverMonthDto.builder()
                        .currentMonthRevenue(BigDecimal.TEN)
            .previousMonthRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentMonthDeals(42)
            .previousMonthDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}