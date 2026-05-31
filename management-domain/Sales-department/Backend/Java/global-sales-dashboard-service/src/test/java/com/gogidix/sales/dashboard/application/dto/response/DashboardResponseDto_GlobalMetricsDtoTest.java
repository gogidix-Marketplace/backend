package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_GlobalMetricsDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.GlobalMetricsDto dto = DashboardResponseDto.GlobalMetricsDto.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(null)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(null)
            .averageDealSize(null)
            .activeSalesReps(42)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .yearOverYearGrowth(null)
            .monthOverMonthGrowth(null)
            .quarterOverQuarterGrowth(null)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getTotalDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(42, dto.getActiveSalesReps());
        assertEquals(42, dto.getOpportunitiesInPipeline());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.GlobalMetricsDto dto = new DashboardResponseDto.GlobalMetricsDto();
        dto.setTotalDeals(99);
        dto.setWonDeals(99);
        dto.setActiveSalesReps(99);
        dto.setOpportunitiesInPipeline(99);
        assertEquals(99, dto.getTotalDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(99, dto.getActiveSalesReps());
        assertEquals(99, dto.getOpportunitiesInPipeline());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.GlobalMetricsDto dto1 = DashboardResponseDto.GlobalMetricsDto.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(null)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(null)
            .averageDealSize(null)
            .activeSalesReps(42)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .yearOverYearGrowth(null)
            .monthOverMonthGrowth(null)
            .quarterOverQuarterGrowth(null)
            .build();
        DashboardResponseDto.GlobalMetricsDto dto2 = DashboardResponseDto.GlobalMetricsDto.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(null)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(null)
            .averageDealSize(null)
            .activeSalesReps(42)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .yearOverYearGrowth(null)
            .monthOverMonthGrowth(null)
            .quarterOverQuarterGrowth(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.GlobalMetricsDto dto = DashboardResponseDto.GlobalMetricsDto.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(null)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(null)
            .averageDealSize(null)
            .activeSalesReps(42)
            .weightedPipeline(null)
            .opportunitiesInPipeline(42)
            .yearOverYearGrowth(null)
            .monthOverMonthGrowth(null)
            .quarterOverQuarterGrowth(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}