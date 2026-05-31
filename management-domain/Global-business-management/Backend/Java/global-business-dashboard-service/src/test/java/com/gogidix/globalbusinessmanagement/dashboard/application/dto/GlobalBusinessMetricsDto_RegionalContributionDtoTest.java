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
class GlobalBusinessMetricsDto_RegionalContributionDtoTest {

        @Test
    void testBuilder() {
        GlobalBusinessMetricsDto.RegionalContributionDto dto = GlobalBusinessMetricsDto.RegionalContributionDto.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orders(42L)
            .customers(42L)
            .profitMargin(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-regionName", dto.getRegionName());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(BigDecimal.TEN, dto.getRevenueContribution());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(42L, dto.getOrders());
        assertEquals(42L, dto.getCustomers());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
    }

    @Test
    void testSettersAndGetters() {
        GlobalBusinessMetricsDto.RegionalContributionDto dto = new GlobalBusinessMetricsDto.RegionalContributionDto();
        dto.setRegionCode("val-regionCode");
        dto.setRegionName("val-regionName");
        dto.setRevenue(BigDecimal.ONE);
        dto.setRevenueContribution(BigDecimal.ONE);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setProfitMargin(BigDecimal.ONE);
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-regionName", dto.getRegionName());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getRevenueContribution());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getProfitMargin());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalBusinessMetricsDto.RegionalContributionDto dto1 = GlobalBusinessMetricsDto.RegionalContributionDto.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orders(42L)
            .customers(42L)
            .profitMargin(BigDecimal.TEN)
            .build();
        GlobalBusinessMetricsDto.RegionalContributionDto dto2 = GlobalBusinessMetricsDto.RegionalContributionDto.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orders(42L)
            .customers(42L)
            .profitMargin(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalBusinessMetricsDto.RegionalContributionDto dto = GlobalBusinessMetricsDto.RegionalContributionDto.builder()
                        .regionCode("test-regionCode")
            .regionName("test-regionName")
            .revenue(BigDecimal.TEN)
            .revenueContribution(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orders(42L)
            .customers(42L)
            .profitMargin(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}