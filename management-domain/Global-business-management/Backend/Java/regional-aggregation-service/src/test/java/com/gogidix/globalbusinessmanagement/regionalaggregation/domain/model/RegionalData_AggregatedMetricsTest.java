package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.RegionalData;
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
class RegionalData_AggregatedMetricsTest {

        @Test
    void testBuilder() {
        RegionalData.AggregatedMetrics dto = RegionalData.AggregatedMetrics.builder()
                        .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .totalProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .totalOrders(42L)
            .totalCustomers(42L)
            .newCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .averageOrderValue(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getTotalRevenue());
        assertEquals(BigDecimal.TEN, dto.getTotalExpenses());
        assertEquals(BigDecimal.TEN, dto.getTotalProfit());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
        assertEquals(42L, dto.getTotalOrders());
        assertEquals(42L, dto.getTotalCustomers());
        assertEquals(42L, dto.getNewCustomers());
        assertEquals(BigDecimal.TEN, dto.getCustomerRetentionRate());
        assertEquals(BigDecimal.TEN, dto.getAverageOrderValue());
        assertEquals(BigDecimal.TEN, dto.getMarketPenetration());
        assertEquals(BigDecimal.TEN, dto.getCustomerAcquisitionCost());
        assertEquals(BigDecimal.TEN, dto.getCustomerLifetimeValue());
    }

    @Test
    void testSettersAndGetters() {
        RegionalData.AggregatedMetrics dto = new RegionalData.AggregatedMetrics();
        dto.setTotalRevenue(BigDecimal.ONE);
        dto.setTotalExpenses(BigDecimal.ONE);
        dto.setTotalProfit(BigDecimal.ONE);
        dto.setProfitMargin(BigDecimal.ONE);
        dto.setCustomerRetentionRate(BigDecimal.ONE);
        dto.setAverageOrderValue(BigDecimal.ONE);
        dto.setMarketPenetration(BigDecimal.ONE);
        dto.setCustomerAcquisitionCost(BigDecimal.ONE);
        dto.setCustomerLifetimeValue(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
        assertEquals(BigDecimal.ONE, dto.getTotalExpenses());
        assertEquals(BigDecimal.ONE, dto.getTotalProfit());
        assertEquals(BigDecimal.ONE, dto.getProfitMargin());
        assertEquals(BigDecimal.ONE, dto.getCustomerRetentionRate());
        assertEquals(BigDecimal.ONE, dto.getAverageOrderValue());
        assertEquals(BigDecimal.ONE, dto.getMarketPenetration());
        assertEquals(BigDecimal.ONE, dto.getCustomerAcquisitionCost());
        assertEquals(BigDecimal.ONE, dto.getCustomerLifetimeValue());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalData.AggregatedMetrics dto1 = RegionalData.AggregatedMetrics.builder()
                        .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .totalProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .totalOrders(42L)
            .totalCustomers(42L)
            .newCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .averageOrderValue(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .build();
        RegionalData.AggregatedMetrics dto2 = RegionalData.AggregatedMetrics.builder()
                        .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .totalProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .totalOrders(42L)
            .totalCustomers(42L)
            .newCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .averageOrderValue(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalData.AggregatedMetrics dto = RegionalData.AggregatedMetrics.builder()
                        .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .totalProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .totalOrders(42L)
            .totalCustomers(42L)
            .newCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .averageOrderValue(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}