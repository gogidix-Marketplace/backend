package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics;
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
class CountryMetrics_ProductLineMetricsTest {

        @Test
    void testBuilder() {
        CountryMetrics.ProductLineMetrics dto = CountryMetrics.ProductLineMetrics.builder()
                        .productLineCode("test-productLineCode")
            .productLineName("test-productLineName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .productCount(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-productLineCode", dto.getProductLineCode());
        assertEquals("test-productLineName", dto.getProductLineName());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(BigDecimal.TEN, dto.getRevenueContribution());
        assertEquals(42L, dto.getUnitsSold());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
        assertEquals(42, dto.getProductCount());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetrics.ProductLineMetrics dto = new CountryMetrics.ProductLineMetrics();
        dto.setProductLineCode("val-productLineCode");
        dto.setProductLineName("val-productLineName");
        dto.setRevenue(BigDecimal.ONE);
        dto.setRevenueContribution(BigDecimal.ONE);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setProfitMargin(BigDecimal.ONE);
        dto.setProductCount(99);
        assertEquals("val-productLineCode", dto.getProductLineCode());
        assertEquals("val-productLineName", dto.getProductLineName());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getRevenueContribution());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getProfitMargin());
        assertEquals(99, dto.getProductCount());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetrics.ProductLineMetrics dto1 = CountryMetrics.ProductLineMetrics.builder()
                        .productLineCode("test-productLineCode")
            .productLineName("test-productLineName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .productCount(42)
            .build();
        CountryMetrics.ProductLineMetrics dto2 = CountryMetrics.ProductLineMetrics.builder()
                        .productLineCode("test-productLineCode")
            .productLineName("test-productLineName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .productCount(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetrics.ProductLineMetrics dto = CountryMetrics.ProductLineMetrics.builder()
                        .productLineCode("test-productLineCode")
            .productLineName("test-productLineName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .productCount(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}