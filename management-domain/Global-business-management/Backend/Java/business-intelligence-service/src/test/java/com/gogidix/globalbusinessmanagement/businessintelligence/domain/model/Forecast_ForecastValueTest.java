package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Forecast;
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
class Forecast_ForecastValueTest {

        @Test
    void testBuilder() {
        Forecast.ForecastValue dto = Forecast.ForecastValue.builder()
                        .period("test-period")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .forecastValue(BigDecimal.TEN)
            .lowerBound(BigDecimal.TEN)
            .upperBound(BigDecimal.TEN)
            .standardError(BigDecimal.TEN)
            .predictionInterval(BigDecimal.TEN)
            .probability(BigDecimal.TEN)
            .isActual(true)
            .actualValue(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .attributes(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-period", dto.getPeriod());
        assertEquals(BigDecimal.TEN, dto.getForecastValue());
        assertEquals(BigDecimal.TEN, dto.getLowerBound());
        assertEquals(BigDecimal.TEN, dto.getUpperBound());
        assertEquals(BigDecimal.TEN, dto.getStandardError());
        assertEquals(BigDecimal.TEN, dto.getPredictionInterval());
        assertEquals(BigDecimal.TEN, dto.getProbability());
        assertTrue(dto.getIsActual());
        assertEquals(BigDecimal.TEN, dto.getActualValue());
        assertEquals(BigDecimal.TEN, dto.getAccuracy());
    }

    @Test
    void testSettersAndGetters() {
        Forecast.ForecastValue dto = new Forecast.ForecastValue();
        dto.setPeriod("val-period");
        dto.setForecastValue(BigDecimal.ONE);
        dto.setLowerBound(BigDecimal.ONE);
        dto.setUpperBound(BigDecimal.ONE);
        dto.setStandardError(BigDecimal.ONE);
        dto.setPredictionInterval(BigDecimal.ONE);
        dto.setProbability(BigDecimal.ONE);
        dto.setIsActual(true);
        dto.setActualValue(BigDecimal.ONE);
        dto.setAccuracy(BigDecimal.ONE);
        assertEquals("val-period", dto.getPeriod());
        assertEquals(BigDecimal.ONE, dto.getForecastValue());
        assertEquals(BigDecimal.ONE, dto.getLowerBound());
        assertEquals(BigDecimal.ONE, dto.getUpperBound());
        assertEquals(BigDecimal.ONE, dto.getStandardError());
        assertEquals(BigDecimal.ONE, dto.getPredictionInterval());
        assertEquals(BigDecimal.ONE, dto.getProbability());
        assertTrue(dto.getIsActual());
        assertEquals(BigDecimal.ONE, dto.getActualValue());
        assertEquals(BigDecimal.ONE, dto.getAccuracy());
    }

    @Test
    void testEqualsAndHashCode() {
        Forecast.ForecastValue dto1 = Forecast.ForecastValue.builder()
                        .period("test-period")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .forecastValue(BigDecimal.TEN)
            .lowerBound(BigDecimal.TEN)
            .upperBound(BigDecimal.TEN)
            .standardError(BigDecimal.TEN)
            .predictionInterval(BigDecimal.TEN)
            .probability(BigDecimal.TEN)
            .isActual(true)
            .actualValue(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .attributes(Collections.emptyMap())
            .build();
        Forecast.ForecastValue dto2 = Forecast.ForecastValue.builder()
                        .period("test-period")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .forecastValue(BigDecimal.TEN)
            .lowerBound(BigDecimal.TEN)
            .upperBound(BigDecimal.TEN)
            .standardError(BigDecimal.TEN)
            .predictionInterval(BigDecimal.TEN)
            .probability(BigDecimal.TEN)
            .isActual(true)
            .actualValue(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .attributes(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Forecast.ForecastValue dto = Forecast.ForecastValue.builder()
                        .period("test-period")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .forecastValue(BigDecimal.TEN)
            .lowerBound(BigDecimal.TEN)
            .upperBound(BigDecimal.TEN)
            .standardError(BigDecimal.TEN)
            .predictionInterval(BigDecimal.TEN)
            .probability(BigDecimal.TEN)
            .isActual(true)
            .actualValue(BigDecimal.TEN)
            .accuracy(BigDecimal.TEN)
            .attributes(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}