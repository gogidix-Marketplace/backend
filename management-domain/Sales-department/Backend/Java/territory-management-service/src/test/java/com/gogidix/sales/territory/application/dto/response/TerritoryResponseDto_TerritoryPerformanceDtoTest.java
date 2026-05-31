package com.gogidix.sales.territory.application.dto.response;

import com.gogidix.sales.territory.application.dto.response.TerritoryResponseDto;
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
class TerritoryResponseDto_TerritoryPerformanceDtoTest {

        @Test
    void testBuilder() {
        TerritoryResponseDto.TerritoryPerformanceDto dto = TerritoryResponseDto.TerritoryPerformanceDto.builder()
                        .currentSales(BigDecimal.TEN)
            .quota(BigDecimal.TEN)
            .quotaAttainment(BigDecimal.TEN)
            .accountsCount(42)
            .dealsCount(42)
            .activeLeads(42)
            .winRate(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .periodStart("test-periodStart")
            .periodEnd("test-periodEnd")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCurrentSales());
        assertEquals(BigDecimal.TEN, dto.getQuota());
        assertEquals(BigDecimal.TEN, dto.getQuotaAttainment());
        assertEquals(42, dto.getAccountsCount());
        assertEquals(42, dto.getDealsCount());
        assertEquals(42, dto.getActiveLeads());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals("test-periodStart", dto.getPeriodStart());
        assertEquals("test-periodEnd", dto.getPeriodEnd());
    }

    @Test
    void testBuilderWithValues() {
        TerritoryResponseDto.TerritoryPerformanceDto dto = TerritoryResponseDto.TerritoryPerformanceDto.builder()
            .currentSales(BigDecimal.ONE)
            .quota(BigDecimal.ONE)
            .quotaAttainment(BigDecimal.ONE)
            .accountsCount(99)
            .dealsCount(99)
            .activeLeads(99)
            .winRate(BigDecimal.ONE)
            .periodStart("val-periodStart")
            .periodEnd("val-periodEnd")
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        TerritoryResponseDto.TerritoryPerformanceDto dto1 = TerritoryResponseDto.TerritoryPerformanceDto.builder()
                        .currentSales(BigDecimal.TEN)
            .quota(BigDecimal.TEN)
            .quotaAttainment(BigDecimal.TEN)
            .accountsCount(42)
            .dealsCount(42)
            .activeLeads(42)
            .winRate(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .periodStart("test-periodStart")
            .periodEnd("test-periodEnd")
            .build();
        TerritoryResponseDto.TerritoryPerformanceDto dto2 = TerritoryResponseDto.TerritoryPerformanceDto.builder()
                        .currentSales(BigDecimal.TEN)
            .quota(BigDecimal.TEN)
            .quotaAttainment(BigDecimal.TEN)
            .accountsCount(42)
            .dealsCount(42)
            .activeLeads(42)
            .winRate(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .periodStart("test-periodStart")
            .periodEnd("test-periodEnd")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TerritoryResponseDto.TerritoryPerformanceDto dto = TerritoryResponseDto.TerritoryPerformanceDto.builder()
                        .currentSales(BigDecimal.TEN)
            .quota(BigDecimal.TEN)
            .quotaAttainment(BigDecimal.TEN)
            .accountsCount(42)
            .dealsCount(42)
            .activeLeads(42)
            .winRate(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .periodStart("test-periodStart")
            .periodEnd("test-periodEnd")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}