package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.RegionalSummaryDto;
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
class RegionalSummaryDtoTest {

        @Test
    void testBuilder() {
        RegionalSummaryDto dto = RegionalSummaryDto.builder()
                        .id("test-id")
            .regionCode("test-regionCode")
            .regionName("test-regionName")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .revenue(BigDecimal.TEN)
            .expenses(BigDecimal.TEN)
            .profit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orderCount(42L)
            .customerCount(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .marketPenetration(BigDecimal.TEN)
            .customerSatisfactionScore(BigDecimal.TEN)
            .countries(Collections.emptyList())
            .topPerformingProducts(Collections.emptyList())
            .trends(null)
            .demographics(null)
            .baseCurrency("test-baseCurrency")
            .status("test-status")
            .notes("test-notes")
            .calculatedMetrics(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-regionCode", dto.getRegionCode());
        assertEquals("test-regionName", dto.getRegionName());
        assertEquals("test-periodId", dto.getPeriodId());
        assertEquals(BigDecimal.TEN, dto.getRevenue());
        assertEquals(BigDecimal.TEN, dto.getExpenses());
        assertEquals(BigDecimal.TEN, dto.getProfit());
        assertEquals(BigDecimal.TEN, dto.getProfitMargin());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals(42L, dto.getOrderCount());
        assertEquals(42L, dto.getCustomerCount());
        assertEquals(42L, dto.getNewCustomers());
        assertEquals(42L, dto.getChurnedCustomers());
        assertEquals(BigDecimal.TEN, dto.getMarketPenetration());
        assertEquals(BigDecimal.TEN, dto.getCustomerSatisfactionScore());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        RegionalSummaryDto dto = new RegionalSummaryDto();
        dto.setId("val-id");
        dto.setRegionCode("val-regionCode");
        dto.setRegionName("val-regionName");
        dto.setPeriodId("val-periodId");
        dto.setRevenue(BigDecimal.ONE);
        dto.setExpenses(BigDecimal.ONE);
        dto.setProfit(BigDecimal.ONE);
        dto.setProfitMargin(BigDecimal.ONE);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setMarketPenetration(BigDecimal.ONE);
        dto.setCustomerSatisfactionScore(BigDecimal.ONE);
        dto.setBaseCurrency("val-baseCurrency");
        dto.setStatus("val-status");
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-regionCode", dto.getRegionCode());
        assertEquals("val-regionName", dto.getRegionName());
        assertEquals("val-periodId", dto.getPeriodId());
        assertEquals(BigDecimal.ONE, dto.getRevenue());
        assertEquals(BigDecimal.ONE, dto.getExpenses());
        assertEquals(BigDecimal.ONE, dto.getProfit());
        assertEquals(BigDecimal.ONE, dto.getProfitMargin());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getMarketPenetration());
        assertEquals(BigDecimal.ONE, dto.getCustomerSatisfactionScore());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalSummaryDto dto1 = RegionalSummaryDto.builder()
                        .id("test-id")
            .regionCode("test-regionCode")
            .regionName("test-regionName")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .revenue(BigDecimal.TEN)
            .expenses(BigDecimal.TEN)
            .profit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orderCount(42L)
            .customerCount(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .marketPenetration(BigDecimal.TEN)
            .customerSatisfactionScore(BigDecimal.TEN)
            .countries(Collections.emptyList())
            .topPerformingProducts(Collections.emptyList())
            .trends(null)
            .demographics(null)
            .baseCurrency("test-baseCurrency")
            .status("test-status")
            .notes("test-notes")
            .calculatedMetrics(null)
            .build();
        RegionalSummaryDto dto2 = RegionalSummaryDto.builder()
                        .id("test-id")
            .regionCode("test-regionCode")
            .regionName("test-regionName")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .revenue(BigDecimal.TEN)
            .expenses(BigDecimal.TEN)
            .profit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orderCount(42L)
            .customerCount(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .marketPenetration(BigDecimal.TEN)
            .customerSatisfactionScore(BigDecimal.TEN)
            .countries(Collections.emptyList())
            .topPerformingProducts(Collections.emptyList())
            .trends(null)
            .demographics(null)
            .baseCurrency("test-baseCurrency")
            .status("test-status")
            .notes("test-notes")
            .calculatedMetrics(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalSummaryDto dto = RegionalSummaryDto.builder()
                        .id("test-id")
            .regionCode("test-regionCode")
            .regionName("test-regionName")
            .periodId("test-periodId")
            .startDate(LocalDateTime.of(2025,1,15,10,0))
            .endDate(LocalDateTime.of(2025,1,15,10,0))
            .revenue(BigDecimal.TEN)
            .expenses(BigDecimal.TEN)
            .profit(BigDecimal.TEN)
            .profitMargin(BigDecimal.TEN)
            .growthRate(BigDecimal.TEN)
            .orderCount(42L)
            .customerCount(42L)
            .newCustomers(42L)
            .churnedCustomers(42L)
            .marketPenetration(BigDecimal.TEN)
            .customerSatisfactionScore(BigDecimal.TEN)
            .countries(Collections.emptyList())
            .topPerformingProducts(Collections.emptyList())
            .trends(null)
            .demographics(null)
            .baseCurrency("test-baseCurrency")
            .status("test-status")
            .notes("test-notes")
            .calculatedMetrics(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}