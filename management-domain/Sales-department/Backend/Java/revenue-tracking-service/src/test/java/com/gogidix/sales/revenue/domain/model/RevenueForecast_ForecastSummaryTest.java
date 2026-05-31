package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.domain.model.RevenueForecast;
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
class RevenueForecast_ForecastSummaryTest {

        @Test
    void testBuilder() {
        RevenueForecast.ForecastSummary dto = RevenueForecast.ForecastSummary.builder()
                        .category("test-category")
            .categoryId("test-categoryId")
            .pessimisticTotal(BigDecimal.TEN)
            .realisticTotal(BigDecimal.TEN)
            .optimisticTotal(BigDecimal.TEN)
            .weightedTotal(BigDecimal.TEN)
            .currency("test-currency")
            .entryCount(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-categoryId", dto.getCategoryId());
        assertEquals(BigDecimal.TEN, dto.getPessimisticTotal());
        assertEquals(BigDecimal.TEN, dto.getRealisticTotal());
        assertEquals(BigDecimal.TEN, dto.getOptimisticTotal());
        assertEquals(BigDecimal.TEN, dto.getWeightedTotal());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(42, dto.getEntryCount());
    }

    @Test
    void testSettersAndGetters() {
        RevenueForecast.ForecastSummary dto = new RevenueForecast.ForecastSummary();
        dto.setCategory("val-category");
        dto.setCategoryId("val-categoryId");
        dto.setPessimisticTotal(BigDecimal.ONE);
        dto.setRealisticTotal(BigDecimal.ONE);
        dto.setOptimisticTotal(BigDecimal.ONE);
        dto.setWeightedTotal(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setEntryCount(99);
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-categoryId", dto.getCategoryId());
        assertEquals(BigDecimal.ONE, dto.getPessimisticTotal());
        assertEquals(BigDecimal.ONE, dto.getRealisticTotal());
        assertEquals(BigDecimal.ONE, dto.getOptimisticTotal());
        assertEquals(BigDecimal.ONE, dto.getWeightedTotal());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(99, dto.getEntryCount());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueForecast.ForecastSummary dto1 = RevenueForecast.ForecastSummary.builder()
                        .category("test-category")
            .categoryId("test-categoryId")
            .pessimisticTotal(BigDecimal.TEN)
            .realisticTotal(BigDecimal.TEN)
            .optimisticTotal(BigDecimal.TEN)
            .weightedTotal(BigDecimal.TEN)
            .currency("test-currency")
            .entryCount(42)
            .build();
        RevenueForecast.ForecastSummary dto2 = RevenueForecast.ForecastSummary.builder()
                        .category("test-category")
            .categoryId("test-categoryId")
            .pessimisticTotal(BigDecimal.TEN)
            .realisticTotal(BigDecimal.TEN)
            .optimisticTotal(BigDecimal.TEN)
            .weightedTotal(BigDecimal.TEN)
            .currency("test-currency")
            .entryCount(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueForecast.ForecastSummary dto = RevenueForecast.ForecastSummary.builder()
                        .category("test-category")
            .categoryId("test-categoryId")
            .pessimisticTotal(BigDecimal.TEN)
            .realisticTotal(BigDecimal.TEN)
            .optimisticTotal(BigDecimal.TEN)
            .weightedTotal(BigDecimal.TEN)
            .currency("test-currency")
            .entryCount(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}