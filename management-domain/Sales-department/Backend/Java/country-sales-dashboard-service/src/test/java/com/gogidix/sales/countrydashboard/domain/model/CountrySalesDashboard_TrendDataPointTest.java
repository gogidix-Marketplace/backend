package com.gogidix.sales.countrydashboard.domain.model;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
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
class CountrySalesDashboard_TrendDataPointTest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.TrendDataPoint dto = CountrySalesDashboard.TrendDataPoint.builder()
                        .period("test-period")
            .date(LocalDate.of(2025,1,15))
            .revenue(null)
            .deals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .newCustomers(42)
            .growthRate(BigDecimal.TEN)
            .territory("test-territory")
            .build();
        assertNotNull(dto);
        assertEquals("test-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,1,15), dto.getDate());
        assertEquals(42, dto.getDeals());
        assertEquals(BigDecimal.TEN, dto.getWinRate());
        assertEquals(42, dto.getNewCustomers());
        assertEquals(BigDecimal.TEN, dto.getGrowthRate());
        assertEquals("test-territory", dto.getTerritory());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.TrendDataPoint dto = new CountrySalesDashboard.TrendDataPoint();
        dto.setPeriod("val-period");
        dto.setDate(LocalDate.of(2025,6,1));
        dto.setDeals(99);
        dto.setWinRate(BigDecimal.ONE);
        dto.setNewCustomers(99);
        dto.setGrowthRate(BigDecimal.ONE);
        dto.setTerritory("val-territory");
        assertEquals("val-period", dto.getPeriod());
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
        assertEquals(99, dto.getDeals());
        assertEquals(BigDecimal.ONE, dto.getWinRate());
        assertEquals(99, dto.getNewCustomers());
        assertEquals(BigDecimal.ONE, dto.getGrowthRate());
        assertEquals("val-territory", dto.getTerritory());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.TrendDataPoint dto1 = CountrySalesDashboard.TrendDataPoint.builder()
                        .period("test-period")
            .date(LocalDate.of(2025,1,15))
            .revenue(null)
            .deals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .newCustomers(42)
            .growthRate(BigDecimal.TEN)
            .territory("test-territory")
            .build();
        CountrySalesDashboard.TrendDataPoint dto2 = CountrySalesDashboard.TrendDataPoint.builder()
                        .period("test-period")
            .date(LocalDate.of(2025,1,15))
            .revenue(null)
            .deals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .newCustomers(42)
            .growthRate(BigDecimal.TEN)
            .territory("test-territory")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySalesDashboard.TrendDataPoint dto = CountrySalesDashboard.TrendDataPoint.builder()
                        .period("test-period")
            .date(LocalDate.of(2025,1,15))
            .revenue(null)
            .deals(42)
            .winRate(BigDecimal.TEN)
            .averageDealSize(null)
            .newCustomers(42)
            .growthRate(BigDecimal.TEN)
            .territory("test-territory")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}