package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.CountryDashboardResponseDto;
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
class CountryDashboardResponseDto_MetricsDtoTest {

        @Test
    void testBuilder() {
        CountryDashboardResponseDto.MetricsDto dto = CountryDashboardResponseDto.MetricsDto.builder()
                        .totalRevenue(BigDecimal.TEN)
            .currency("test-currency")
            .targetRevenue(BigDecimal.TEN)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(BigDecimal.TEN)
            .weightedPipeline(BigDecimal.TEN)
            .opportunitiesInPipeline(42)
            .newCustomers(42)
            .churnedCustomers(42)
            .retentionRate(BigDecimal.TEN)
            .npsScore(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .activeSalesReps(42)
            .revenuePerRep(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getTotalRevenue());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getTargetRevenue());
        assertEquals(BigDecimal.TEN, dto.getAchievementPercentage());
        assertEquals(42, dto.getTotalDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(42, dto.getLostDeals());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals(BigDecimal.TEN, dto.getAverageDealSize());
        assertEquals(BigDecimal.TEN, dto.getWeightedPipeline());
        assertEquals(42, dto.getOpportunitiesInPipeline());
        assertEquals(42, dto.getNewCustomers());
        assertEquals(42, dto.getChurnedCustomers());
        assertEquals(BigDecimal.TEN, dto.getRetentionRate());
        assertEquals(BigDecimal.TEN, dto.getNpsScore());
        assertEquals(BigDecimal.TEN, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.TEN, dto.getMonthOverMonthGrowth());
        assertEquals(BigDecimal.TEN, dto.getQuarterOverQuarterGrowth());
        assertEquals(42, dto.getActiveSalesReps());
        assertEquals(BigDecimal.TEN, dto.getRevenuePerRep());
    }

    @Test
    void testSettersAndGetters() {
        CountryDashboardResponseDto.MetricsDto dto = new CountryDashboardResponseDto.MetricsDto();
        dto.setTotalRevenue(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setTargetRevenue(BigDecimal.ONE);
        dto.setAchievementPercentage(BigDecimal.ONE);
        dto.setTotalDeals(99);
        dto.setWonDeals(99);
        dto.setLostDeals(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setAverageDealSize(BigDecimal.ONE);
        dto.setWeightedPipeline(BigDecimal.ONE);
        dto.setOpportunitiesInPipeline(99);
        dto.setNewCustomers(99);
        dto.setChurnedCustomers(99);
        dto.setRetentionRate(BigDecimal.ONE);
        dto.setNpsScore(BigDecimal.ONE);
        dto.setYearOverYearGrowth(BigDecimal.ONE);
        dto.setMonthOverMonthGrowth(BigDecimal.ONE);
        dto.setQuarterOverQuarterGrowth(BigDecimal.ONE);
        dto.setActiveSalesReps(99);
        dto.setRevenuePerRep(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getTargetRevenue());
        assertEquals(BigDecimal.ONE, dto.getAchievementPercentage());
        assertEquals(99, dto.getTotalDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(99, dto.getLostDeals());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(BigDecimal.ONE, dto.getAverageDealSize());
        assertEquals(BigDecimal.ONE, dto.getWeightedPipeline());
        assertEquals(99, dto.getOpportunitiesInPipeline());
        assertEquals(99, dto.getNewCustomers());
        assertEquals(99, dto.getChurnedCustomers());
        assertEquals(BigDecimal.ONE, dto.getRetentionRate());
        assertEquals(BigDecimal.ONE, dto.getNpsScore());
        assertEquals(BigDecimal.ONE, dto.getYearOverYearGrowth());
        assertEquals(BigDecimal.ONE, dto.getMonthOverMonthGrowth());
        assertEquals(BigDecimal.ONE, dto.getQuarterOverQuarterGrowth());
        assertEquals(99, dto.getActiveSalesReps());
        assertEquals(BigDecimal.ONE, dto.getRevenuePerRep());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardResponseDto.MetricsDto dto1 = CountryDashboardResponseDto.MetricsDto.builder()
                        .totalRevenue(BigDecimal.TEN)
            .currency("test-currency")
            .targetRevenue(BigDecimal.TEN)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(BigDecimal.TEN)
            .weightedPipeline(BigDecimal.TEN)
            .opportunitiesInPipeline(42)
            .newCustomers(42)
            .churnedCustomers(42)
            .retentionRate(BigDecimal.TEN)
            .npsScore(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .activeSalesReps(42)
            .revenuePerRep(BigDecimal.TEN)
            .build();
        CountryDashboardResponseDto.MetricsDto dto2 = CountryDashboardResponseDto.MetricsDto.builder()
                        .totalRevenue(BigDecimal.TEN)
            .currency("test-currency")
            .targetRevenue(BigDecimal.TEN)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(BigDecimal.TEN)
            .weightedPipeline(BigDecimal.TEN)
            .opportunitiesInPipeline(42)
            .newCustomers(42)
            .churnedCustomers(42)
            .retentionRate(BigDecimal.TEN)
            .npsScore(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .activeSalesReps(42)
            .revenuePerRep(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryDashboardResponseDto.MetricsDto dto = CountryDashboardResponseDto.MetricsDto.builder()
                        .totalRevenue(BigDecimal.TEN)
            .currency("test-currency")
            .targetRevenue(BigDecimal.TEN)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(BigDecimal.TEN)
            .weightedPipeline(BigDecimal.TEN)
            .opportunitiesInPipeline(42)
            .newCustomers(42)
            .churnedCustomers(42)
            .retentionRate(BigDecimal.TEN)
            .npsScore(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .monthOverMonthGrowth(BigDecimal.TEN)
            .quarterOverQuarterGrowth(BigDecimal.TEN)
            .activeSalesReps(42)
            .revenuePerRep(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}