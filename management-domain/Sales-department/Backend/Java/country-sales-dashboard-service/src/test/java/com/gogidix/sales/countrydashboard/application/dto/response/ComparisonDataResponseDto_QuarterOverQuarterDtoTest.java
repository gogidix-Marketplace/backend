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
class ComparisonDataResponseDto_QuarterOverQuarterDtoTest {

        @Test
    void testBuilder() {
        ComparisonDataResponseDto.QuarterOverQuarterDto dto = ComparisonDataResponseDto.QuarterOverQuarterDto.builder()
                        .currentQuarterRevenue(BigDecimal.TEN)
            .previousQuarterRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentQuarterDeals(42)
            .previousQuarterDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCurrentQuarterRevenue());
        assertEquals(BigDecimal.TEN, dto.getPreviousQuarterRevenue());
        assertEquals(BigDecimal.TEN, dto.getGrowthPercentage());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(42, dto.getCurrentQuarterDeals());
        assertEquals(42, dto.getPreviousQuarterDeals());
        assertEquals(BigDecimal.TEN, dto.getDealsGrowthPercentage());
    }

    @Test
    void testSettersAndGetters() {
        ComparisonDataResponseDto.QuarterOverQuarterDto dto = new ComparisonDataResponseDto.QuarterOverQuarterDto();
        dto.setCurrentQuarterRevenue(BigDecimal.ONE);
        dto.setPreviousQuarterRevenue(BigDecimal.ONE);
        dto.setGrowthPercentage(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setCurrentQuarterDeals(99);
        dto.setPreviousQuarterDeals(99);
        dto.setDealsGrowthPercentage(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getCurrentQuarterRevenue());
        assertEquals(BigDecimal.ONE, dto.getPreviousQuarterRevenue());
        assertEquals(BigDecimal.ONE, dto.getGrowthPercentage());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(99, dto.getCurrentQuarterDeals());
        assertEquals(99, dto.getPreviousQuarterDeals());
        assertEquals(BigDecimal.ONE, dto.getDealsGrowthPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        ComparisonDataResponseDto.QuarterOverQuarterDto dto1 = ComparisonDataResponseDto.QuarterOverQuarterDto.builder()
                        .currentQuarterRevenue(BigDecimal.TEN)
            .previousQuarterRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentQuarterDeals(42)
            .previousQuarterDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ComparisonDataResponseDto.QuarterOverQuarterDto dto2 = ComparisonDataResponseDto.QuarterOverQuarterDto.builder()
                        .currentQuarterRevenue(BigDecimal.TEN)
            .previousQuarterRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentQuarterDeals(42)
            .previousQuarterDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComparisonDataResponseDto.QuarterOverQuarterDto dto = ComparisonDataResponseDto.QuarterOverQuarterDto.builder()
                        .currentQuarterRevenue(BigDecimal.TEN)
            .previousQuarterRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentQuarterDeals(42)
            .previousQuarterDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}