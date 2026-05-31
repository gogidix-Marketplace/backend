package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.GlobalBusinessMetrics;
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
class GlobalBusinessMetrics_MarketTrendsTest {

        @Test
    void testBuilder() {
        GlobalBusinessMetrics.MarketTrends dto = GlobalBusinessMetrics.MarketTrends.builder()
                        .marketShare(BigDecimal.TEN)
            .marketGrowthRate(BigDecimal.TEN)
            .competitorCount(42)
            .industryAverageMargin(BigDecimal.TEN)
            .trendDirection("test-trendDirection")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getMarketShare());
        assertEquals(BigDecimal.TEN, dto.getMarketGrowthRate());
        assertEquals(42, dto.getCompetitorCount());
        assertEquals(BigDecimal.TEN, dto.getIndustryAverageMargin());
        assertEquals("test-trendDirection", dto.getTrendDirection());
    }

    @Test
    void testSettersAndGetters() {
        GlobalBusinessMetrics.MarketTrends dto = new GlobalBusinessMetrics.MarketTrends();
        dto.setMarketShare(BigDecimal.ONE);
        dto.setMarketGrowthRate(BigDecimal.ONE);
        dto.setCompetitorCount(99);
        dto.setIndustryAverageMargin(BigDecimal.ONE);
        dto.setTrendDirection("val-trendDirection");
        assertEquals(BigDecimal.ONE, dto.getMarketShare());
        assertEquals(BigDecimal.ONE, dto.getMarketGrowthRate());
        assertEquals(99, dto.getCompetitorCount());
        assertEquals(BigDecimal.ONE, dto.getIndustryAverageMargin());
        assertEquals("val-trendDirection", dto.getTrendDirection());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalBusinessMetrics.MarketTrends dto1 = GlobalBusinessMetrics.MarketTrends.builder()
                        .marketShare(BigDecimal.TEN)
            .marketGrowthRate(BigDecimal.TEN)
            .competitorCount(42)
            .industryAverageMargin(BigDecimal.TEN)
            .trendDirection("test-trendDirection")
            .build();
        GlobalBusinessMetrics.MarketTrends dto2 = GlobalBusinessMetrics.MarketTrends.builder()
                        .marketShare(BigDecimal.TEN)
            .marketGrowthRate(BigDecimal.TEN)
            .competitorCount(42)
            .industryAverageMargin(BigDecimal.TEN)
            .trendDirection("test-trendDirection")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalBusinessMetrics.MarketTrends dto = GlobalBusinessMetrics.MarketTrends.builder()
                        .marketShare(BigDecimal.TEN)
            .marketGrowthRate(BigDecimal.TEN)
            .competitorCount(42)
            .industryAverageMargin(BigDecimal.TEN)
            .trendDirection("test-trendDirection")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}