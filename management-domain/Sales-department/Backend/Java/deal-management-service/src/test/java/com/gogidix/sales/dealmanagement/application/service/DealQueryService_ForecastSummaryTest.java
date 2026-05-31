package com.gogidix.sales.dealmanagement.application.service;

import com.gogidix.sales.dealmanagement.application.service.DealQueryService;
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
class DealQueryService_ForecastSummaryTest {

        @Test
    void testBuilder() {
        DealQueryService.ForecastSummary dto = DealQueryService.ForecastSummary.builder()
                        .totalPipeline(BigDecimal.TEN)
            .weightedForecast(BigDecimal.TEN)
            .bestCase(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .openDeals(42)
            .wonDeals(42)
            .wonAmount(BigDecimal.TEN)
            .winRate(null)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getTotalPipeline());
        assertEquals(BigDecimal.TEN, dto.getWeightedForecast());
        assertEquals(BigDecimal.TEN, dto.getBestCase());
        assertEquals(BigDecimal.TEN, dto.getWorstCase());
        assertEquals(42, dto.getOpenDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(BigDecimal.TEN, dto.getWonAmount());
    }

    @Test
    void testBuilderWithValues() {
        DealQueryService.ForecastSummary dto = DealQueryService.ForecastSummary.builder()
            .totalPipeline(BigDecimal.ONE)
            .weightedForecast(BigDecimal.ONE)
            .bestCase(BigDecimal.ONE)
            .worstCase(BigDecimal.ONE)
            .openDeals(99)
            .wonDeals(99)
            .wonAmount(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        DealQueryService.ForecastSummary dto1 = DealQueryService.ForecastSummary.builder()
                        .totalPipeline(BigDecimal.TEN)
            .weightedForecast(BigDecimal.TEN)
            .bestCase(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .openDeals(42)
            .wonDeals(42)
            .wonAmount(BigDecimal.TEN)
            .winRate(null)
            .build();
        DealQueryService.ForecastSummary dto2 = DealQueryService.ForecastSummary.builder()
                        .totalPipeline(BigDecimal.TEN)
            .weightedForecast(BigDecimal.TEN)
            .bestCase(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .openDeals(42)
            .wonDeals(42)
            .wonAmount(BigDecimal.TEN)
            .winRate(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DealQueryService.ForecastSummary dto = DealQueryService.ForecastSummary.builder()
                        .totalPipeline(BigDecimal.TEN)
            .weightedForecast(BigDecimal.TEN)
            .bestCase(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .openDeals(42)
            .wonDeals(42)
            .wonAmount(BigDecimal.TEN)
            .winRate(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}