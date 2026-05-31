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
class RegionalSummary_CountryMetricSummaryTest {

        @Test
    void testBuilder() {
        RegionalSummary.CountryMetricSummary dto = RegionalSummary.CountryMetricSummary.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orders(42L)
            .customers(42L)
            .profitMargin(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(BigDecimal.TEN, dto.getRevenueContribution());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(42L, dto.getOrders());
        assertEquals(42L, dto.getCustomers());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
        assertEquals(BigDecimal.TEN, dto.getMarketShare());
    }

    @Test
    void testSettersAndGetters() {
        RegionalSummary.CountryMetricSummary dto = new RegionalSummary.CountryMetricSummary();
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setRevenue(BigDecimal.ONE);
        dto.setRevenueContribution(BigDecimal.ONE);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setProfitMargin(BigDecimal.ONE);
        dto.setMarketShare(BigDecimal.ONE);
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getRevenueContribution());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getProfitMargin());
        assertEquals(BigDecimal.ONE, dto.getMarketShare());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalSummary.CountryMetricSummary dto1 = RegionalSummary.CountryMetricSummary.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orders(42L)
            .customers(42L)
            .profitMargin(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .build();
        RegionalSummary.CountryMetricSummary dto2 = RegionalSummary.CountryMetricSummary.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orders(42L)
            .customers(42L)
            .profitMargin(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalSummary.CountryMetricSummary dto = RegionalSummary.CountryMetricSummary.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orders(42L)
            .customers(42L)
            .profitMargin(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}