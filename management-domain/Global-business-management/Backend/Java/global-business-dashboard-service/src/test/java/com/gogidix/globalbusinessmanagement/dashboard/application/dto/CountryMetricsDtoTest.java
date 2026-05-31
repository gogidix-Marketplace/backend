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
class CountryMetricsDtoTest {

        @Test
    void testBuilder() {
        CountryMetricsDto dto = CountryMetricsDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .revenue(BigDecimal.TEN)
            .expenses(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .profitBeforeTax(BigDecimal.TEN)
            .profitAfterTax(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .orderCount(42L)
            .customerCount(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .returningCustomers(42L)
            .averageOrderValue(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .marketSize(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .competitiveIndex(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .productLines(Collections.emptyList())
            .topCities(Collections.emptyList())
            .salesChannels(null)
            .customerMetrics(null)
            .operationalMetrics(null)
            .economicIndicators(null)
            .baseCurrency("test-baseCurrency")
            .status("test-status")
            .dataSource("test-dataSource")
            .dataQuality(null)
            .calculatedMetrics(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-periodId", dto.getPeriodId());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(BigDecimal.TEN, dto.getExpenses());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertEquals(BigDecimal.TEN, dto.getProfitBeforeTax());
        assertEquals(BigDecimal.TEN, dto.getProfitAfterTax());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
        assertEquals(42L, dto.getOrderCount());
        assertEquals(42L, dto.getCustomerCount());
        assertEquals(42L, dto.getNewCustomers());
        assertEquals(42L, dto.getChurnedCustomers());
        assertEquals(42L, dto.getReturningCustomers());
        assertEquals(BigDecimal.TEN, dto.getAverageOrderValue());
        assertEquals(BigDecimal.TEN, dto.getCustomerLifetimeValue());
        assertEquals(BigDecimal.TEN, dto.getCustomerAcquisitionCost());
        assertEquals(BigDecimal.TEN, dto.getMarketShare());
        assertEquals(BigDecimal.TEN, dto.getMarketSize());
        assertEquals(BigDecimal.TEN, dto.getMarketPenetration());
        assertEquals(BigDecimal.TEN, dto.getCompetitiveIndex());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getYearOverYearGrowth());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-dataSource", dto.getDataSource());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetricsDto dto = new CountryMetricsDto();
        dto.setId("val-id");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setRegionCode("val-regionCode");
        dto.setPeriodId("val-periodId");
        dto.setRevenue(BigDecimal.ONE);
        dto.setExpenses(BigDecimal.ONE);
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setProfitBeforeTax(BigDecimal.ONE);
        dto.setProfitAfterTax(BigDecimal.ONE);
        dto.setProfitMargin(BigDecimal.ONE);
        dto.setAverageOrderValue(BigDecimal.ONE);
        dto.setCustomerLifetimeValue(BigDecimal.ONE);
        dto.setCustomerAcquisitionCost(BigDecimal.ONE);
        dto.setMarketShare(BigDecimal.ONE);
        dto.setMarketSize(BigDecimal.ONE);
        dto.setMarketPenetration(BigDecimal.ONE);
        dto.setCompetitiveIndex(BigDecimal.ONE);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setYearOverYearGrowth(BigDecimal.ONE);
        dto.setBaseCurrency("val-baseCurrency");
        dto.setStatus("val-status");
        dto.setDataSource("val-dataSource");
        assertEquals("val-id", dto.getId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-periodId", dto.getPeriodId());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getExpenses());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertEquals(BigDecimal.ONE, dto.getProfitBeforeTax());
        assertEquals(BigDecimal.ONE, dto.getProfitAfterTax());
        assertEquals(BigDecimal.ONE, dto.getProfitMargin());
        assertEquals(BigDecimal.ONE, dto.getAverageOrderValue());
        assertEquals(BigDecimal.ONE, dto.getCustomerLifetimeValue());
        assertEquals(BigDecimal.ONE, dto.getCustomerAcquisitionCost());
        assertEquals(BigDecimal.ONE, dto.getMarketShare());
        assertEquals(BigDecimal.ONE, dto.getMarketSize());
        assertEquals(BigDecimal.ONE, dto.getMarketPenetration());
        assertEquals(BigDecimal.ONE, dto.getCompetitiveIndex());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getYearOverYearGrowth());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-dataSource", dto.getDataSource());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetricsDto dto1 = CountryMetricsDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .revenue(BigDecimal.TEN)
            .expenses(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .profitBeforeTax(BigDecimal.TEN)
            .profitAfterTax(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .orderCount(42L)
            .customerCount(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .returningCustomers(42L)
            .averageOrderValue(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .marketSize(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .competitiveIndex(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .productLines(Collections.emptyList())
            .topCities(Collections.emptyList())
            .salesChannels(null)
            .customerMetrics(null)
            .operationalMetrics(null)
            .economicIndicators(null)
            .baseCurrency("test-baseCurrency")
            .status("test-status")
            .dataSource("test-dataSource")
            .dataQuality(null)
            .calculatedMetrics(null)
            .build();
        CountryMetricsDto dto2 = CountryMetricsDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .revenue(BigDecimal.TEN)
            .expenses(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .profitBeforeTax(BigDecimal.TEN)
            .profitAfterTax(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .orderCount(42L)
            .customerCount(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .returningCustomers(42L)
            .averageOrderValue(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .marketSize(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .competitiveIndex(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .productLines(Collections.emptyList())
            .topCities(Collections.emptyList())
            .salesChannels(null)
            .customerMetrics(null)
            .operationalMetrics(null)
            .economicIndicators(null)
            .baseCurrency("test-baseCurrency")
            .status("test-status")
            .dataSource("test-dataSource")
            .dataQuality(null)
            .calculatedMetrics(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetricsDto dto = CountryMetricsDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .regionCode("test-regionCode")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .revenue(BigDecimal.TEN)
            .expenses(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .profitBeforeTax(BigDecimal.TEN)
            .profitAfterTax(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .orderCount(42L)
            .customerCount(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .returningCustomers(42L)
            .averageOrderValue(BigDecimal.TEN)
            .customerLifetimeValue(BigDecimal.TEN)
            .customerAcquisitionCost(BigDecimal.TEN)
            .marketShare(BigDecimal.TEN)
            .marketSize(BigDecimal.TEN)
            .marketPenetration(BigDecimal.TEN)
            .competitiveIndex(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .yearOverYearGrowth(BigDecimal.TEN)
            .productLines(Collections.emptyList())
            .topCities(Collections.emptyList())
            .salesChannels(null)
            .customerMetrics(null)
            .operationalMetrics(null)
            .economicIndicators(null)
            .baseCurrency("test-baseCurrency")
            .status("test-status")
            .dataSource("test-dataSource")
            .dataQuality(null)
            .calculatedMetrics(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}