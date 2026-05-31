package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.GlobalBusinessMetricsDto;
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
class GlobalBusinessMetricsDtoTest {

        @Test
    void testBuilder() {
        GlobalBusinessMetricsDto dto = GlobalBusinessMetricsDto.builder()
                        .id("test-id")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .netProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .totalOrders(42L)
            .activeCustomers(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .averageOrderValue(BigDecimal.TEN)
            .totalTransactions(42L)
            .conversionRate(BigDecimal.TEN)
            .cartAbandonmentRate(BigDecimal.TEN)
            .baseCurrency("test-baseCurrency")
            .regionalBreakdown(Collections.emptyMap())
            .productCategoryPerformance(Collections.emptyMap())
            .marketTrends(null)
            .operationalMetrics(null)
            .status("test-status")
            .dataSource("test-dataSource")
            .version(42)
            .calculatedMetrics(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-periodId", dto.getPeriodId());
        assertEquals(BigDecimal.TEN, dto.getTotalRevenue());
        assertEquals(BigDecimal.TEN, dto.getTotalExpenses());
        assertEquals(BigDecimal.TEN, dto.getGrossProfit());
        assertEquals(BigDecimal.TEN, dto.getNetProfit());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
        assertEquals(42L, dto.getTotalOrders());
        assertEquals(42L, dto.getActiveCustomers());
        assertEquals(42L, dto.getNewCustomers());
        assertEquals(42L, dto.getChurnedCustomers());
        assertEquals(BigDecimal.TEN, dto.getCustomerRetentionRate());
        assertEquals(BigDecimal.TEN, dto.getAverageOrderValue());
        assertEquals(42L, dto.getTotalTransactions());
        assertEquals(BigDecimal.TEN, dto.getConversionRate());
        assertEquals(BigDecimal.TEN, dto.getCartAbandonmentRate());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals(42, dto.getVersion());
    }

    @Test
    void testSettersAndGetters() {
        GlobalBusinessMetricsDto dto = new GlobalBusinessMetricsDto();
        dto.setId("val-id");
        dto.setPeriodId("val-periodId");
        dto.setTotalRevenue(BigDecimal.ONE);
        dto.setTotalExpenses(BigDecimal.ONE);
        dto.setGrossProfit(BigDecimal.ONE);
        dto.setNetProfit(BigDecimal.ONE);
        dto.setProfitMargin(BigDecimal.ONE);
        dto.setCustomerRetentionRate(BigDecimal.ONE);
        dto.setAverageOrderValue(BigDecimal.ONE);
        dto.setConversionRate(BigDecimal.ONE);
        dto.setCartAbandonmentRate(BigDecimal.ONE);
        dto.setBaseCurrency("val-baseCurrency");
        dto.setStatus("val-status");
        dto.setDataSource("val-dataSource");
        dto.setVersion(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-periodId", dto.getPeriodId());
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
        assertEquals(BigDecimal.ONE, dto.getTotalExpenses());
        assertEquals(BigDecimal.ONE, dto.getGrossProfit());
        assertEquals(BigDecimal.ONE, dto.getNetProfit());
        assertEquals(BigDecimal.ONE, dto.getProfitMargin());
        assertEquals(BigDecimal.ONE, dto.getCustomerRetentionRate());
        assertEquals(BigDecimal.ONE, dto.getAverageOrderValue());
        assertEquals(BigDecimal.ONE, dto.getConversionRate());
        assertEquals(BigDecimal.ONE, dto.getCartAbandonmentRate());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals(99, dto.getVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalBusinessMetricsDto dto1 = GlobalBusinessMetricsDto.builder()
                        .id("test-id")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .netProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .totalOrders(42L)
            .activeCustomers(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .averageOrderValue(BigDecimal.TEN)
            .totalTransactions(42L)
            .conversionRate(BigDecimal.TEN)
            .cartAbandonmentRate(BigDecimal.TEN)
            .baseCurrency("test-baseCurrency")
            .regionalBreakdown(Collections.emptyMap())
            .productCategoryPerformance(Collections.emptyMap())
            .marketTrends(null)
            .operationalMetrics(null)
            .status("test-status")
            .dataSource("test-dataSource")
            .version(42)
            .calculatedMetrics(null)
            .build();
        GlobalBusinessMetricsDto dto2 = GlobalBusinessMetricsDto.builder()
                        .id("test-id")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .netProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .totalOrders(42L)
            .activeCustomers(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .averageOrderValue(BigDecimal.TEN)
            .totalTransactions(42L)
            .conversionRate(BigDecimal.TEN)
            .cartAbandonmentRate(BigDecimal.TEN)
            .baseCurrency("test-baseCurrency")
            .regionalBreakdown(Collections.emptyMap())
            .productCategoryPerformance(Collections.emptyMap())
            .marketTrends(null)
            .operationalMetrics(null)
            .status("test-status")
            .dataSource("test-dataSource")
            .version(42)
            .calculatedMetrics(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalBusinessMetricsDto dto = GlobalBusinessMetricsDto.builder()
                        .id("test-id")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .totalRevenue(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .netProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .totalOrders(42L)
            .activeCustomers(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .customerRetentionRate(BigDecimal.TEN)
            .averageOrderValue(BigDecimal.TEN)
            .totalTransactions(42L)
            .conversionRate(BigDecimal.TEN)
            .cartAbandonmentRate(BigDecimal.TEN)
            .baseCurrency("test-baseCurrency")
            .regionalBreakdown(Collections.emptyMap())
            .productCategoryPerformance(Collections.emptyMap())
            .marketTrends(null)
            .operationalMetrics(null)
            .status("test-status")
            .dataSource("test-dataSource")
            .version(42)
            .calculatedMetrics(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}