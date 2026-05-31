package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary;
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
class RegionalSummary_RegionalTrendsTest {

        @Test
    void testBuilder() {
        RegionalSummary.RegionalTrends dto = RegionalSummary.RegionalTrends.builder()
                        .revenueTrend(BigDecimal.TEN)
            .revenueTrendDirection("test-revenueTrendDirection")
            .customerTrend(BigDecimal.TEN)
            .customerTrendDirection("test-customerTrendDirection")
            .orderTrend(BigDecimal.TEN)
            .orderTrendDirection("test-orderTrendDirection")
            .topGrowthDriver("test-topGrowthDriver")
            .topRiskFactor("test-topRiskFactor")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getRevenueTrend());
        assertEquals("test-revenueTrendDirection", dto.getRevenueTrendDirection());
        assertEquals(BigDecimal.TEN, dto.getCustomerTrend());
        assertEquals("test-customerTrendDirection", dto.getCustomerTrendDirection());
        assertEquals(BigDecimal.TEN, dto.getOrderTrend());
        assertEquals("test-orderTrendDirection", dto.getOrderTrendDirection());
        assertEquals("test-topGrowthDriver", dto.getTopGrowthDriver());
        assertEquals("test-topRiskFactor", dto.getTopRiskFactor());
    }

    @Test
    void testSettersAndGetters() {
        RegionalSummary.RegionalTrends dto = new RegionalSummary.RegionalTrends();
        dto.setRevenueTrend(BigDecimal.ONE);
        dto.setRevenueTrendDirection("val-revenueTrendDirection");
        dto.setCustomerTrend(BigDecimal.ONE);
        dto.setCustomerTrendDirection("val-customerTrendDirection");
        dto.setOrderTrend(BigDecimal.ONE);
        dto.setOrderTrendDirection("val-orderTrendDirection");
        dto.setTopGrowthDriver("val-topGrowthDriver");
        dto.setTopRiskFactor("val-topRiskFactor");
        assertEquals(BigDecimal.ONE, dto.getRevenueTrend());
        assertEquals("val-revenueTrendDirection", dto.getRevenueTrendDirection());
        assertEquals(BigDecimal.ONE, dto.getCustomerTrend());
        assertEquals("val-customerTrendDirection", dto.getCustomerTrendDirection());
        assertEquals(BigDecimal.ONE, dto.getOrderTrend());
        assertEquals("val-orderTrendDirection", dto.getOrderTrendDirection());
        assertEquals("val-topGrowthDriver", dto.getTopGrowthDriver());
        assertEquals("val-topRiskFactor", dto.getTopRiskFactor());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalSummary.RegionalTrends dto1 = RegionalSummary.RegionalTrends.builder()
                        .revenueTrend(BigDecimal.TEN)
            .revenueTrendDirection("test-revenueTrendDirection")
            .customerTrend(BigDecimal.TEN)
            .customerTrendDirection("test-customerTrendDirection")
            .orderTrend(BigDecimal.TEN)
            .orderTrendDirection("test-orderTrendDirection")
            .topGrowthDriver("test-topGrowthDriver")
            .topRiskFactor("test-topRiskFactor")
            .build();
        RegionalSummary.RegionalTrends dto2 = RegionalSummary.RegionalTrends.builder()
                        .revenueTrend(BigDecimal.TEN)
            .revenueTrendDirection("test-revenueTrendDirection")
            .customerTrend(BigDecimal.TEN)
            .customerTrendDirection("test-customerTrendDirection")
            .orderTrend(BigDecimal.TEN)
            .orderTrendDirection("test-orderTrendDirection")
            .topGrowthDriver("test-topGrowthDriver")
            .topRiskFactor("test-topRiskFactor")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalSummary.RegionalTrends dto = RegionalSummary.RegionalTrends.builder()
                        .revenueTrend(BigDecimal.TEN)
            .revenueTrendDirection("test-revenueTrendDirection")
            .customerTrend(BigDecimal.TEN)
            .customerTrendDirection("test-customerTrendDirection")
            .orderTrend(BigDecimal.TEN)
            .orderTrendDirection("test-orderTrendDirection")
            .topGrowthDriver("test-topGrowthDriver")
            .topRiskFactor("test-topRiskFactor")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}