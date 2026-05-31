package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.SalesAggregation;
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
class SalesAggregation_AggregatedMetricsTest {

        @Test
    void testBuilder() {
        SalesAggregation.AggregatedMetrics dto = SalesAggregation.AggregatedMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .averageDiscount(null)
            .newOpportunities(42)
            .closedOpportunities(42)
            .pipelineValue(null)
            .weightedPipeline(null)
            .conversionRate(BigDecimal.TEN)
            .salesActivities(42)
            .customerAcquisitionCost(BigDecimal.TEN)
            .customerLifetimeValue(null)
            .activeCustomers(42)
            .churnedCustomers(42)
            .churnRate(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAchievementPercentage());
        assertEquals(42, dto.getTotalDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals(42, dto.getNewOpportunities());
        assertEquals(42, dto.getClosedOpportunities());
        assertEquals(BigDecimal.TEN, dto.getConversionRate());
        assertEquals(42, dto.getSalesActivities());
        assertEquals(BigDecimal.TEN, dto.getCustomerAcquisitionCost());
        assertEquals(42, dto.getActiveCustomers());
        assertEquals(42, dto.getChurnedCustomers());
        assertEquals(BigDecimal.TEN, dto.getChurnRate());
    }

    @Test
    void testSettersAndGetters() {
        SalesAggregation.AggregatedMetrics dto = new SalesAggregation.AggregatedMetrics();
        dto.setAchievementPercentage(BigDecimal.ONE);
        dto.setTotalDeals(99);
        dto.setWonDeals(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setNewOpportunities(99);
        dto.setClosedOpportunities(99);
        dto.setConversionRate(BigDecimal.ONE);
        dto.setSalesActivities(99);
        dto.setCustomerAcquisitionCost(BigDecimal.ONE);
        dto.setActiveCustomers(99);
        dto.setChurnedCustomers(99);
        dto.setChurnRate(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getAchievementPercentage());
        assertEquals(99, dto.getTotalDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(99, dto.getNewOpportunities());
        assertEquals(99, dto.getClosedOpportunities());
        assertEquals(BigDecimal.ONE, dto.getConversionRate());
        assertEquals(99, dto.getSalesActivities());
        assertEquals(BigDecimal.ONE, dto.getCustomerAcquisitionCost());
        assertEquals(99, dto.getActiveCustomers());
        assertEquals(99, dto.getChurnedCustomers());
        assertEquals(BigDecimal.ONE, dto.getChurnRate());
    }

    @Test
    void testEqualsAndHashCode() {
        SalesAggregation.AggregatedMetrics dto1 = SalesAggregation.AggregatedMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .averageDiscount(null)
            .newOpportunities(42)
            .closedOpportunities(42)
            .pipelineValue(null)
            .weightedPipeline(null)
            .conversionRate(BigDecimal.TEN)
            .salesActivities(42)
            .customerAcquisitionCost(BigDecimal.TEN)
            .customerLifetimeValue(null)
            .activeCustomers(42)
            .churnedCustomers(42)
            .churnRate(BigDecimal.TEN)
            .build();
        SalesAggregation.AggregatedMetrics dto2 = SalesAggregation.AggregatedMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .averageDiscount(null)
            .newOpportunities(42)
            .closedOpportunities(42)
            .pipelineValue(null)
            .weightedPipeline(null)
            .conversionRate(BigDecimal.TEN)
            .salesActivities(42)
            .customerAcquisitionCost(BigDecimal.TEN)
            .customerLifetimeValue(null)
            .activeCustomers(42)
            .churnedCustomers(42)
            .churnRate(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SalesAggregation.AggregatedMetrics dto = SalesAggregation.AggregatedMetrics.builder()
                        .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(BigDecimal.TEN)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .averageDiscount(null)
            .newOpportunities(42)
            .closedOpportunities(42)
            .pipelineValue(null)
            .weightedPipeline(null)
            .conversionRate(BigDecimal.TEN)
            .salesActivities(42)
            .customerAcquisitionCost(BigDecimal.TEN)
            .customerLifetimeValue(null)
            .activeCustomers(42)
            .churnedCustomers(42)
            .churnRate(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}