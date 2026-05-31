package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
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
class GlobalSalesDashboard_GlobalMetricsTest {

        @Test
    void testBuilder() {
        GlobalSalesDashboard.GlobalMetrics dto = GlobalSalesDashboard.GlobalMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .activeSalesReps(42)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAchievementPercentage());
        assertEquals(42, dto.getTotalDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals(42, dto.getActiveSalesReps());
        assertEquals(42, dto.getOpportunitiesInPipeline());
        assertEquals(BigDecimal.TEN, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.TEN, dto.getMonthOverMonthGrowth());
        assertEquals(BigDecimal.TEN, dto.getQuarterOverQuarterGrowth());
    }

    @Test
    void testSettersAndGetters() {
        GlobalSalesDashboard.GlobalMetrics dto = new GlobalSalesDashboard.GlobalMetrics();
        dto.setAchievementPercentage(BigDecimal.ONE);
        dto.setTotalDeals(99);
        dto.setWonDeals(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setActiveSalesReps(99);
        dto.setOpportunitiesInPipeline(99);
        dto.setYearOverYearGrowth(BigDecimal.ONE);
        dto.setMonthOverMonthGrowth(BigDecimal.ONE);
        dto.setQuarterOverQuarterGrowth(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getAchievementPercentage());
        assertEquals(99, dto.getTotalDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(99, dto.getActiveSalesReps());
        assertEquals(99, dto.getOpportunitiesInPipeline());
        assertEquals(BigDecimal.ONE, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.ONE, dto.getMonthOverMonthGrowth());
        assertEquals(BigDecimal.ONE, dto.getQuarterOverQuarterGrowth());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalSalesDashboard.GlobalMetrics dto1 = GlobalSalesDashboard.GlobalMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .activeSalesReps(42)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .build();
        GlobalSalesDashboard.GlobalMetrics dto2 = GlobalSalesDashboard.GlobalMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .activeSalesReps(42)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalSalesDashboard.GlobalMetrics dto = GlobalSalesDashboard.GlobalMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .activeSalesReps(42)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}