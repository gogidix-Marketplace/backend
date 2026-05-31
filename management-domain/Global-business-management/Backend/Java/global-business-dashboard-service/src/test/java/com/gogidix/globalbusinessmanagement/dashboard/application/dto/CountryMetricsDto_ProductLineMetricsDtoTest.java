package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.CountryMetricsDto;
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
class CountryMetricsDto_ProductLineMetricsDtoTest {

        @Test
    void testBuilder() {
        CountryMetricsDto.ProductLineMetricsDto dto = CountryMetricsDto.ProductLineMetricsDto.builder()
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
        CountryMetricsDto.ProductLineMetricsDto dto = new CountryMetricsDto.ProductLineMetricsDto();
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
        CountryMetricsDto.ProductLineMetricsDto dto1 = CountryMetricsDto.ProductLineMetricsDto.builder()
                        .productLineCode("test-productLineCode")
            .productLineName("test-productLineName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .unitsSold(42L)
            .growthRate(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .productCount(42)
            .build();
        CountryMetricsDto.ProductLineMetricsDto dto2 = CountryMetricsDto.ProductLineMetricsDto.builder()
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
        CountryMetricsDto.ProductLineMetricsDto dto = CountryMetricsDto.ProductLineMetricsDto.builder()
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