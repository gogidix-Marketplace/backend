package com.gogidix.sales.countrydashboard.domain.model;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
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
class CountrySalesDashboard_MonthOverMonthComparisonTest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.MonthOverMonthComparison dto = CountrySalesDashboard.MonthOverMonthComparison.builder()
                        .currentMonthRevenue(null)
            .previousMonthRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
            .currentMonthDeals(42)
            .previousMonthDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getGrowthPercentage());
        assertEquals(42, dto.getCurrentMonthDeals());
        assertEquals(42, dto.getPreviousMonthDeals());
        assertEquals(BigDecimal.TEN, dto.getDealsGrowthPercentage());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.MonthOverMonthComparison dto = new CountrySalesDashboard.MonthOverMonthComparison();
        dto.setGrowthPercentage(BigDecimal.ONE);
        dto.setCurrentMonthDeals(99);
        dto.setPreviousMonthDeals(99);
        dto.setDealsGrowthPercentage(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getGrowthPercentage());
        assertEquals(99, dto.getCurrentMonthDeals());
        assertEquals(99, dto.getPreviousMonthDeals());
        assertEquals(BigDecimal.ONE, dto.getDealsGrowthPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.MonthOverMonthComparison dto1 = CountrySalesDashboard.MonthOverMonthComparison.builder()
                        .currentMonthRevenue(null)
            .previousMonthRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
            .currentMonthDeals(42)
            .previousMonthDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CountrySalesDashboard.MonthOverMonthComparison dto2 = CountrySalesDashboard.MonthOverMonthComparison.builder()
                        .currentMonthRevenue(null)
            .previousMonthRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
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
        CountrySalesDashboard.MonthOverMonthComparison dto = CountrySalesDashboard.MonthOverMonthComparison.builder()
                        .currentMonthRevenue(null)
            .previousMonthRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
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