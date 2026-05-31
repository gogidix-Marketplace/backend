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
class CountrySalesDashboard_CountryMetricsTest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.CountryMetrics dto = CountrySalesDashboard.CountryMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .newCustomers(42)
            .churnedCustomers(42)
            .retentionRate(BigDecimal.TEN)
            .npsScore(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .activeSalesReps(42)
            .revenuePerRep(null)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAchievementPercentage());
        assertEquals(42, dto.getTotalDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(42, dto.getLostDeals());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals(42, dto.getOpportunitiesInPipeline());
        assertEquals(42, dto.getNewCustomers());
        assertEquals(42, dto.getChurnedCustomers());
        assertEquals(BigDecimal.TEN, dto.getRetentionRate());
        assertEquals(BigDecimal.TEN, dto.getNpsScore());
        assertEquals(BigDecimal.TEN, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.TEN, dto.getMonthOverMonthGrowth());
        assertEquals(BigDecimal.TEN, dto.getQuarterOverQuarterGrowth());
        assertEquals(42, dto.getActiveSalesReps());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.CountryMetrics dto = new CountrySalesDashboard.CountryMetrics();
        dto.setAchievementPercentage(BigDecimal.ONE);
        dto.setTotalDeals(99);
        dto.setWonDeals(99);
        dto.setLostDeals(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setOpportunitiesInPipeline(99);
        dto.setNewCustomers(99);
        dto.setChurnedCustomers(99);
        dto.setRetentionRate(BigDecimal.ONE);
        dto.setNpsScore(BigDecimal.ONE);
        dto.setYearOverYearGrowth(BigDecimal.ONE);
        dto.setMonthOverMonthGrowth(BigDecimal.ONE);
        dto.setQuarterOverQuarterGrowth(BigDecimal.ONE);
        dto.setActiveSalesReps(99);
        assertEquals(BigDecimal.ONE, dto.getAchievementPercentage());
        assertEquals(99, dto.getTotalDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(99, dto.getLostDeals());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(99, dto.getOpportunitiesInPipeline());
        assertEquals(99, dto.getNewCustomers());
        assertEquals(99, dto.getChurnedCustomers());
        assertEquals(BigDecimal.ONE, dto.getRetentionRate());
        assertEquals(BigDecimal.ONE, dto.getNpsScore());
        assertEquals(BigDecimal.ONE, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.ONE, dto.getMonthOverMonthGrowth());
        assertEquals(BigDecimal.ONE, dto.getQuarterOverQuarterGrowth());
        assertEquals(99, dto.getActiveSalesReps());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.CountryMetrics dto1 = CountrySalesDashboard.CountryMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .newCustomers(42)
            .churnedCustomers(42)
            .retentionRate(BigDecimal.TEN)
            .npsScore(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .activeSalesReps(42)
            .revenuePerRep(null)
            .build();
        CountrySalesDashboard.CountryMetrics dto2 = CountrySalesDashboard.CountryMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .newCustomers(42)
            .churnedCustomers(42)
            .retentionRate(BigDecimal.TEN)
            .npsScore(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .activeSalesReps(42)
            .revenuePerRep(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySalesDashboard.CountryMetrics dto = CountrySalesDashboard.CountryMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .newCustomers(42)
            .churnedCustomers(42)
            .retentionRate(BigDecimal.TEN)
            .npsScore(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .activeSalesReps(42)
            .revenuePerRep(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}