package com.gogidix.sales.territory.domain.model;

import com.gogidix.sales.territory.domain.model.Territory;
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
class Territory_TerritoryPerformanceTest {

        @Test
    void testBuilder() {
        Territory.TerritoryPerformance dto = Territory.TerritoryPerformance.builder()
                        .currentSales(BigDecimal.TEN)
            .quota(BigDecimal.TEN)
            .quotaAttainment(BigDecimal.TEN)
            .accountsCount(42)
            .dealsCount(42)
            .activeLeads(42)
            .winRate(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getCurrentSales());
        assertEquals(BigDecimal.TEN, dto.getQuota());
        assertEquals(BigDecimal.TEN, dto.getQuotaAttainment());
        assertEquals(42, dto.getAccountsCount());
        assertEquals(42, dto.getDealsCount());
        assertEquals(42, dto.getActiveLeads());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodEnd());
    }

    @Test
    void testSettersAndGetters() {
        Territory.TerritoryPerformance dto = new Territory.TerritoryPerformance();
        dto.setCurrentSales(BigDecimal.ONE);
        dto.setQuota(BigDecimal.ONE);
        dto.setQuotaAttainment(BigDecimal.ONE);
        dto.setAccountsCount(99);
        dto.setDealsCount(99);
        dto.setActiveLeads(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        assertEquals(BigDecimal.ONE, dto.getCurrentSales());
        assertEquals(BigDecimal.ONE, dto.getQuota());
        assertEquals(BigDecimal.ONE, dto.getQuotaAttainment());
        assertEquals(99, dto.getAccountsCount());
        assertEquals(99, dto.getDealsCount());
        assertEquals(99, dto.getActiveLeads());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
    }

    @Test
    void testEqualsAndHashCode() {
        Territory.TerritoryPerformance dto1 = Territory.TerritoryPerformance.builder()
                        .currentSales(BigDecimal.TEN)
            .quota(BigDecimal.TEN)
            .quotaAttainment(BigDecimal.TEN)
            .accountsCount(42)
            .dealsCount(42)
            .activeLeads(42)
            .winRate(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .build();
        Territory.TerritoryPerformance dto2 = Territory.TerritoryPerformance.builder()
                        .currentSales(BigDecimal.TEN)
            .quota(BigDecimal.TEN)
            .quotaAttainment(BigDecimal.TEN)
            .accountsCount(42)
            .dealsCount(42)
            .activeLeads(42)
            .winRate(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Territory.TerritoryPerformance dto = Territory.TerritoryPerformance.builder()
                        .currentSales(BigDecimal.TEN)
            .quota(BigDecimal.TEN)
            .quotaAttainment(BigDecimal.TEN)
            .accountsCount(42)
            .dealsCount(42)
            .activeLeads(42)
            .winRate(BigDecimal.TEN)
            .lastUpdated(Instant.parse("2025-01-15T10:00:00Z"))
            .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}