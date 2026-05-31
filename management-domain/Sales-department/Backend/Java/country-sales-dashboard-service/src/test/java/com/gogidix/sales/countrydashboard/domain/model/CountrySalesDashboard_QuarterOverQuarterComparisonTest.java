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
class CountrySalesDashboard_QuarterOverQuarterComparisonTest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.QuarterOverQuarterComparison dto = CountrySalesDashboard.QuarterOverQuarterComparison.builder()
                        .currentQuarterRevenue(null)
            .previousQuarterRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
            .currentQuarterDeals(42)
            .previousQuarterDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getGrowthPercentage());
        assertEquals(42, dto.getCurrentQuarterDeals());
        assertEquals(42, dto.getPreviousQuarterDeals());
        assertEquals(BigDecimal.TEN, dto.getDealsGrowthPercentage());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.QuarterOverQuarterComparison dto = new CountrySalesDashboard.QuarterOverQuarterComparison();
        dto.setGrowthPercentage(BigDecimal.ONE);
        dto.setCurrentQuarterDeals(99);
        dto.setPreviousQuarterDeals(99);
        dto.setDealsGrowthPercentage(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getGrowthPercentage());
        assertEquals(99, dto.getCurrentQuarterDeals());
        assertEquals(99, dto.getPreviousQuarterDeals());
        assertEquals(BigDecimal.ONE, dto.getDealsGrowthPercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.QuarterOverQuarterComparison dto1 = CountrySalesDashboard.QuarterOverQuarterComparison.builder()
                        .currentQuarterRevenue(null)
            .previousQuarterRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
            .currentQuarterDeals(42)
            .previousQuarterDeals(42)
            .dealsGrowthPercentage(BigDecimal.TEN)
            .comparisonDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CountrySalesDashboard.QuarterOverQuarterComparison dto2 = CountrySalesDashboard.QuarterOverQuarterComparison.builder()
                        .currentQuarterRevenue(null)
            .previousQuarterRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
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
        CountrySalesDashboard.QuarterOverQuarterComparison dto = CountrySalesDashboard.QuarterOverQuarterComparison.builder()
                        .currentQuarterRevenue(null)
            .previousQuarterRevenue(null)
            .growthPercentage(BigDecimal.TEN)
            .variance(null)
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