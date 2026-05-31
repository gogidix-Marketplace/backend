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
class CountrySalesDashboard_YearOverYearComparisonTest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.YearOverYearComparison dto = CountrySalesDashboard.YearOverYearComparison.builder()
                        .currentYearRevenue(null)
            .previousYearRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
            .currentYearDeals(42)
            .previousYearDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getGrowthPercentage());
        assertEquals(42, dto.getCurrentYearDeals());
        assertEquals(42, dto.getPreviousYearDeals());
        assertEquals(BigDecimal.TEN, dto.getDealsGrowthPercentage());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.YearOverYearComparison dto = new CountrySalesDashboard.YearOverYearComparison();
        dto.setGrowthPercentage(BigDecimal.ONE);
        dto.setCurrentYearDeals(99);
        dto.setPreviousYearDeals(99);
        dto.setDealsGrowthPercentage(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getGrowthPercentage());
        assertEquals(99, dto.getCurrentYearDeals());
        assertEquals(99, dto.getPreviousYearDeals());
        assertEquals(BigDecimal.ONE, dto.getDealsGrowthPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.YearOverYearComparison dto1 = CountrySalesDashboard.YearOverYearComparison.builder()
                        .currentYearRevenue(null)
            .previousYearRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
            .currentYearDeals(42)
            .previousYearDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CountrySalesDashboard.YearOverYearComparison dto2 = CountrySalesDashboard.YearOverYearComparison.builder()
                        .currentYearRevenue(null)
            .previousYearRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
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
        CountrySalesDashboard.YearOverYearComparison dto = CountrySalesDashboard.YearOverYearComparison.builder()
                        .currentYearRevenue(null)
            .previousYearRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
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