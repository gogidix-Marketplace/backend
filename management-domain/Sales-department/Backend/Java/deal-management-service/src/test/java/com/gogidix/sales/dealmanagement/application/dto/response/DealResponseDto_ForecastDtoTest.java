package com.gogidix.sales.dealmanagement.application.dto.response;

import com.gogidix.sales.dealmanagement.application.dto.response.DealResponseDto;
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
class DealResponseDto_ForecastDtoTest {

        @Test
    void testBuilder() {
        DealResponseDto.ForecastDto dto = DealResponseDto.ForecastDto.builder()
                        .period("test-period")
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
        assertEquals("test-period", dto.getPeriod());
        assertEquals(BigDecimal.TEN, dto.getTotalPipeline());
        assertEquals(BigDecimal.TEN, dto.getWeightedForecast());
        assertEquals(BigDecimal.TEN, dto.getBestCase());
        assertEquals(BigDecimal.TEN, dto.getWorstCase());
        assertEquals(42, dto.getOpenDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(BigDecimal.TEN, dto.getWonAmount());
    }

    @Test
    void testSettersAndGetters() {
        DealResponseDto.ForecastDto dto = new DealResponseDto.ForecastDto();
        dto.setPeriod("val-period");
        dto.setTotalPipeline(BigDecimal.ONE);
        dto.setWeightedForecast(BigDecimal.ONE);
        dto.setBestCase(BigDecimal.ONE);
        dto.setWorstCase(BigDecimal.ONE);
        dto.setOpenDeals(99);
        dto.setWonDeals(99);
        dto.setWonAmount(BigDecimal.ONE);
        assertEquals("val-period", dto.getPeriod());
        assertEquals(BigDecimal.ONE, dto.getTotalPipeline());
        assertEquals(BigDecimal.ONE, dto.getWeightedForecast());
        assertEquals(BigDecimal.ONE, dto.getBestCase());
        assertEquals(BigDecimal.ONE, dto.getWorstCase());
        assertEquals(99, dto.getOpenDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(BigDecimal.ONE, dto.getWonAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        DealResponseDto.ForecastDto dto1 = DealResponseDto.ForecastDto.builder()
                        .period("test-period")
            .totalPipeline(BigDecimal.TEN)
            .weightedForecast(BigDecimal.TEN)
            .bestCase(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .openDeals(42)
            .wonDeals(42)
            .wonAmount(BigDecimal.TEN)
            .winRate(null)
            .build();
        DealResponseDto.ForecastDto dto2 = DealResponseDto.ForecastDto.builder()
                        .period("test-period")
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
        DealResponseDto.ForecastDto dto = DealResponseDto.ForecastDto.builder()
                        .period("test-period")
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