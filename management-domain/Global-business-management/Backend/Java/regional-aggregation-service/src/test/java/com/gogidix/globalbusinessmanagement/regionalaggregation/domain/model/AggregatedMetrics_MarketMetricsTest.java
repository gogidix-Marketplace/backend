package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.AggregatedMetrics;
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
class AggregatedMetrics_MarketMetricsTest {

        @Test
    void testBuilder() {
        AggregatedMetrics.MarketMetrics dto = AggregatedMetrics.MarketMetrics.builder()
                        .marketShare(BigDecimal.TEN)
            .marketSize(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .marketGrowthRate(BigDecimal.TEN)
            .competitorCount(42)
            .avgCompetitorMarketShare(BigDecimal.TEN)
            .relativeMarketPosition(BigDecimal.TEN)
            .marketRank(42)
            .brandAwareness(BigDecimal.TEN)
            .customerReach(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getMarketShare());
        assertEquals(BigDecimal.TEN, dto.getMarketSize());
        assertEquals(BigDecimal.TEN, dto.getMarketPenetration());
        assertEquals(BigDecimal.TEN, dto.getMarketGrowthRate());
        assertEquals(42, dto.getCompetitorCount());
        assertEquals(BigDecimal.TEN, dto.getAvgCompetitorMarketShare());
        assertEquals(BigDecimal.TEN, dto.getRelativeMarketPosition());
        assertEquals(42, dto.getMarketRank());
        assertEquals(BigDecimal.TEN, dto.getBrandAwareness());
        assertEquals(BigDecimal.TEN, dto.getCustomerReach());
    }

    @Test
    void testSettersAndGetters() {
        AggregatedMetrics.MarketMetrics dto = new AggregatedMetrics.MarketMetrics();
        dto.setMarketShare(BigDecimal.ONE);
        dto.setMarketSize(BigDecimal.ONE);
        dto.setMarketPenetration(BigDecimal.ONE);
        dto.setMarketGrowthRate(BigDecimal.ONE);
        dto.setCompetitorCount(99);
        dto.setAvgCompetitorMarketShare(BigDecimal.ONE);
        dto.setRelativeMarketPosition(BigDecimal.ONE);
        dto.setMarketRank(99);
        dto.setBrandAwareness(BigDecimal.ONE);
        dto.setCustomerReach(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getMarketShare());
        assertEquals(BigDecimal.ONE, dto.getMarketSize());
        assertEquals(BigDecimal.ONE, dto.getMarketPenetration());
        assertEquals(BigDecimal.ONE, dto.getMarketGrowthRate());
        assertEquals(99, dto.getCompetitorCount());
        assertEquals(BigDecimal.ONE, dto.getAvgCompetitorMarketShare());
        assertEquals(BigDecimal.ONE, dto.getRelativeMarketPosition());
        assertEquals(99, dto.getMarketRank());
        assertEquals(BigDecimal.ONE, dto.getBrandAwareness());
        assertEquals(BigDecimal.ONE, dto.getCustomerReach());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregatedMetrics.MarketMetrics dto1 = AggregatedMetrics.MarketMetrics.builder()
                        .marketShare(BigDecimal.TEN)
            .marketSize(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .marketGrowthRate(BigDecimal.TEN)
            .competitorCount(42)
            .avgCompetitorMarketShare(BigDecimal.TEN)
            .relativeMarketPosition(BigDecimal.TEN)
            .marketRank(42)
            .brandAwareness(BigDecimal.TEN)
            .customerReach(BigDecimal.TEN)
            .build();
        AggregatedMetrics.MarketMetrics dto2 = AggregatedMetrics.MarketMetrics.builder()
                        .marketShare(BigDecimal.TEN)
            .marketSize(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .marketGrowthRate(BigDecimal.TEN)
            .competitorCount(42)
            .avgCompetitorMarketShare(BigDecimal.TEN)
            .relativeMarketPosition(BigDecimal.TEN)
            .marketRank(42)
            .brandAwareness(BigDecimal.TEN)
            .customerReach(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregatedMetrics.MarketMetrics dto = AggregatedMetrics.MarketMetrics.builder()
                        .marketShare(BigDecimal.TEN)
            .marketSize(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .marketGrowthRate(BigDecimal.TEN)
            .competitorCount(42)
            .avgCompetitorMarketShare(BigDecimal.TEN)
            .relativeMarketPosition(BigDecimal.TEN)
            .marketRank(42)
            .brandAwareness(BigDecimal.TEN)
            .customerReach(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}