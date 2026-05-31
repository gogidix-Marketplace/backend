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
class GlobalBusinessMetricsDto_CategoryPerformanceDtoTest {

        @Test
    void testBuilder() {
        GlobalBusinessMetricsDto.CategoryPerformanceDto dto = GlobalBusinessMetricsDto.CategoryPerformanceDto.builder()
                        .categoryCode("test-categoryCode")
            .categoryName("test-categoryName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .unitsSold(42L)
            .averagePrice(BigDecimal.TEN)
            .productCount(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-categoryCode", dto.getCategoryCode());
        assertEquals("test-categoryName", dto.getCategoryName());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(BigDecimal.TEN, dto.getRevenueContribution());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(42L, dto.getUnitsSold());
        assertEquals(BigDecimal.TEN, dto.getAveragePrice());
        assertEquals(42, dto.getProductCount());
    }

    @Test
    void testSettersAndGetters() {
        GlobalBusinessMetricsDto.CategoryPerformanceDto dto = new GlobalBusinessMetricsDto.CategoryPerformanceDto();
        dto.setCategoryCode("val-categoryCode");
        dto.setCategoryName("val-categoryName");
        dto.setRevenue(BigDecimal.ONE);
        dto.setRevenueContribution(BigDecimal.ONE);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setAveragePrice(BigDecimal.ONE);
        dto.setProductCount(99);
        assertEquals("val-categoryCode", dto.getCategoryCode());
        assertEquals("val-categoryName", dto.getCategoryName());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getRevenueContribution());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getAveragePrice());
        assertEquals(99, dto.getProductCount());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalBusinessMetricsDto.CategoryPerformanceDto dto1 = GlobalBusinessMetricsDto.CategoryPerformanceDto.builder()
                        .categoryCode("test-categoryCode")
            .categoryName("test-categoryName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .unitsSold(42L)
            .averagePrice(BigDecimal.TEN)
            .productCount(42)
            .build();
        GlobalBusinessMetricsDto.CategoryPerformanceDto dto2 = GlobalBusinessMetricsDto.CategoryPerformanceDto.builder()
                        .categoryCode("test-categoryCode")
            .categoryName("test-categoryName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .unitsSold(42L)
            .averagePrice(BigDecimal.TEN)
            .productCount(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalBusinessMetricsDto.CategoryPerformanceDto dto = GlobalBusinessMetricsDto.CategoryPerformanceDto.builder()
                        .categoryCode("test-categoryCode")
            .categoryName("test-categoryName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .unitsSold(42L)
            .averagePrice(BigDecimal.TEN)
            .productCount(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}