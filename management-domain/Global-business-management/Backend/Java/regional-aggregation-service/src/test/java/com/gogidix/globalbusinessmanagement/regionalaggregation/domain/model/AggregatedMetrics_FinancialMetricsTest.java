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
class AggregatedMetrics_FinancialMetricsTest {

        @Test
    void testBuilder() {
        AggregatedMetrics.FinancialMetrics dto = AggregatedMetrics.FinancialMetrics.builder()
                        .totalRevenue(BigDecimal.TEN)
            .revenuePerCustomer(BigDecimal.TEN)
            .revenuePerOrder(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .netProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .operatingMargin(BigDecimal.TEN)
            .ebitda(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .currencyAdjustment(BigDecimal.TEN)
            .revenueGrowthRate(BigDecimal.TEN)
            .profitGrowthRate(BigDecimal.TEN)
            .expenseGrowthRate(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getTotalRevenue());
        assertEquals(BigDecimal.TEN, dto.getRevenuePerCustomer());
        assertEquals(BigDecimal.TEN, dto.getRevenuePerOrder());
        assertEquals(BigDecimal.TEN, dto.getTotalExpenses());
        assertEquals(BigDecimal.TEN, dto.getGrossProfit());
        assertEquals(BigDecimal.TEN, dto.getNetProfit());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
        assertEquals(BigDecimal.TEN, dto.getOperatingMargin());
        assertEquals(BigDecimal.TEN, dto.getEbitda());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertEquals(BigDecimal.TEN, dto.getCurrencyAdjustment());
        assertEquals(BigDecimal.TEN, dto.getRevenueGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getProfitGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getExpenseGrowthRate());
    }

    @Test
    void testSettersAndGetters() {
        AggregatedMetrics.FinancialMetrics dto = new AggregatedMetrics.FinancialMetrics();
        dto.setTotalRevenue(BigDecimal.ONE);
        dto.setRevenuePerCustomer(BigDecimal.ONE);
        dto.setRevenuePerOrder(BigDecimal.ONE);
        dto.setTotalExpenses(BigDecimal.ONE);
        dto.setGrossProfit(BigDecimal.ONE);
        dto.setNetProfit(BigDecimal.ONE);
        dto.setProfitMargin(BigDecimal.ONE);
        dto.setOperatingMargin(BigDecimal.ONE);
        dto.setEbitda(BigDecimal.ONE);
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setCurrencyAdjustment(BigDecimal.ONE);
        dto.setRevenueGrowthRate(BigDecimal.ONE);
        dto.setProfitGrowthRate(BigDecimal.ONE);
        dto.setExpenseGrowthRate(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getTotalRevenue());
        assertEquals(BigDecimal.ONE, dto.getRevenuePerCustomer());
        assertEquals(BigDecimal.ONE, dto.getRevenuePerOrder());
        assertEquals(BigDecimal.ONE, dto.getTotalExpenses());
        assertEquals(BigDecimal.ONE, dto.getGrossProfit());
        assertEquals(BigDecimal.ONE, dto.getNetProfit());
        assertEquals(BigDecimal.ONE, dto.getProfitMargin());
        assertEquals(BigDecimal.ONE, dto.getOperatingMargin());
        assertEquals(BigDecimal.ONE, dto.getEbitda());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertEquals(BigDecimal.ONE, dto.getCurrencyAdjustment());
        assertEquals(BigDecimal.ONE, dto.getRevenueGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getProfitGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getExpenseGrowthRate());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregatedMetrics.FinancialMetrics dto1 = AggregatedMetrics.FinancialMetrics.builder()
                        .totalRevenue(BigDecimal.TEN)
            .revenuePerCustomer(BigDecimal.TEN)
            .revenuePerOrder(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .netProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .operatingMargin(BigDecimal.TEN)
            .ebitda(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .currencyAdjustment(BigDecimal.TEN)
            .revenueGrowthRate(BigDecimal.TEN)
            .profitGrowthRate(BigDecimal.TEN)
            .expenseGrowthRate(BigDecimal.TEN)
            .build();
        AggregatedMetrics.FinancialMetrics dto2 = AggregatedMetrics.FinancialMetrics.builder()
                        .totalRevenue(BigDecimal.TEN)
            .revenuePerCustomer(BigDecimal.TEN)
            .revenuePerOrder(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .netProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .operatingMargin(BigDecimal.TEN)
            .ebitda(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .currencyAdjustment(BigDecimal.TEN)
            .revenueGrowthRate(BigDecimal.TEN)
            .profitGrowthRate(BigDecimal.TEN)
            .expenseGrowthRate(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregatedMetrics.FinancialMetrics dto = AggregatedMetrics.FinancialMetrics.builder()
                        .totalRevenue(BigDecimal.TEN)
            .revenuePerCustomer(BigDecimal.TEN)
            .revenuePerOrder(BigDecimal.TEN)
            .totalExpenses(BigDecimal.TEN)
            .grossProfit(BigDecimal.TEN)
            .netProfit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .operatingMargin(BigDecimal.TEN)
            .ebitda(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .currencyAdjustment(BigDecimal.TEN)
            .revenueGrowthRate(BigDecimal.TEN)
            .profitGrowthRate(BigDecimal.TEN)
            .expenseGrowthRate(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}