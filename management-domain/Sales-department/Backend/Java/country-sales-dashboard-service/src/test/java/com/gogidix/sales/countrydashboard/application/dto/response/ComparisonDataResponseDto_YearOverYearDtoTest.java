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
class ComparisonDataResponseDto_YearOverYearDtoTest {

        @Test
    void testBuilder() {
        ComparisonDataResponseDto.YearOverYearDto dto = ComparisonDataResponseDto.YearOverYearDto.builder()
                        .currentYearRevenue(BigDecimal.TEN)
            .previousYearRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentYearDeals(42)
            .previousYearDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCurrentYearRevenue());
        assertEquals(BigDecimal.TEN, dto.getPreviousYearRevenue());
        assertEquals(BigDecimal.TEN, dto.getGrowthPercentage());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(42, dto.getCurrentYearDeals());
        assertEquals(42, dto.getPreviousYearDeals());
        assertEquals(BigDecimal.TEN, dto.getDealsGrowthPercentage());
    }

    @Test
    void testSettersAndGetters() {
        ComparisonDataResponseDto.YearOverYearDto dto = new ComparisonDataResponseDto.YearOverYearDto();
        dto.setCurrentYearRevenue(BigDecimal.ONE);
        dto.setPreviousYearRevenue(BigDecimal.ONE);
        dto.setGrowthPercentage(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setCurrentYearDeals(99);
        dto.setPreviousYearDeals(99);
        dto.setDealsGrowthPercentage(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getCurrentYearRevenue());
        assertEquals(BigDecimal.ONE, dto.getPreviousYearRevenue());
        assertEquals(BigDecimal.ONE, dto.getGrowthPercentage());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(99, dto.getCurrentYearDeals());
        assertEquals(99, dto.getPreviousYearDeals());
        assertEquals(BigDecimal.ONE, dto.getDealsGrowthPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        ComparisonDataResponseDto.YearOverYearDto dto1 = ComparisonDataResponseDto.YearOverYearDto.builder()
                        .currentYearRevenue(BigDecimal.TEN)
            .previousYearRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentYearDeals(42)
            .previousYearDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ComparisonDataResponseDto.YearOverYearDto dto2 = ComparisonDataResponseDto.YearOverYearDto.builder()
                        .currentYearRevenue(BigDecimal.TEN)
            .previousYearRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentYearDeals(42)
            .previousYearDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComparisonDataResponseDto.YearOverYearDto dto = ComparisonDataResponseDto.YearOverYearDto.builder()
                        .currentYearRevenue(BigDecimal.TEN)
            .previousYearRevenue(BigDecimal.TEN)
            .growthPercentage(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .currentYearDeals(42)
            .previousYearDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}