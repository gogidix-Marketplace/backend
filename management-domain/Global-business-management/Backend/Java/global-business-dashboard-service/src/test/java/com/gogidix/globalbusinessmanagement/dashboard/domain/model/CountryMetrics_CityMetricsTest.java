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
class CountryMetrics_CityMetricsTest {

        @Test
    void testBuilder() {
        CountryMetrics.CityMetrics dto = CountryMetrics.CityMetrics.builder()
                        .cityName("test-cityName")
            .regionCode("test-regionCode")
            .revenue(BigDecimal.TEN)
            .customers(42L)
            .orders(42L)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-cityName", dto.getCityName());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(42L, dto.getCustomers());
        assertEquals(42L, dto.getOrders());
        assertEquals(BigDecimal.TEN, dto.getRevenueContribution());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
    }

    @Test
    void testSettersAndGetters() {
        CountryMetrics.CityMetrics dto = new CountryMetrics.CityMetrics();
        dto.setCityName("val-cityName");
        dto.setRegionCode("val-regionCode");
        dto.setRevenue(BigDecimal.ONE);
        dto.setRevenueContribution(BigDecimal.ONE);
        dto.setGrowthRate(BigDecimal.ONE);
        assertEquals("val-cityName", dto.getCityName());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getRevenueContribution());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryMetrics.CityMetrics dto1 = CountryMetrics.CityMetrics.builder()
                        .cityName("test-cityName")
            .regionCode("test-regionCode")
            .revenue(BigDecimal.TEN)
            .customers(42L)
            .orders(42L)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .build();
        CountryMetrics.CityMetrics dto2 = CountryMetrics.CityMetrics.builder()
                        .cityName("test-cityName")
            .regionCode("test-regionCode")
            .revenue(BigDecimal.TEN)
            .customers(42L)
            .orders(42L)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryMetrics.CityMetrics dto = CountryMetrics.CityMetrics.builder()
                        .cityName("test-cityName")
            .regionCode("test-regionCode")
            .revenue(BigDecimal.TEN)
            .customers(42L)
            .orders(42L)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}