package com.gogidix.finance.forecasting.application.service;

import com.gogidix.finance.forecasting.application.service.ForecastQueryService;
import com.gogidix.finance.forecasting.domain.model.Forecast;
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
class ForecastQueryService_VarianceAnalysisTest {

        @Test
    void testBuilder() {
        ForecastQueryService.VarianceAnalysis dto = ForecastQueryService.VarianceAnalysis.builder()
                        .baseForecastId("test-baseForecastId")
            .comparisonForecastId("test-comparisonForecastId")
            .baseAmount(BigDecimal.TEN)
            .comparisonAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .baseType(Forecast.ForecastType.REVENUE)
            .comparisonType(Forecast.ForecastType.REVENUE)
            .build();
        assertNotNull(dto);
        assertEquals("test-baseForecastId", dto.getBaseForecastId());
        assertEquals("test-comparisonForecastId", dto.getComparisonForecastId());
        assertEquals(BigDecimal.TEN, dto.getBaseAmount());
        assertEquals(BigDecimal.TEN, dto.getComparisonAmount());
        assertEquals(BigDecimal.TEN, dto.getVarianceAmount());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
    }

    @Test
    void testBuilderWithValues() {
        ForecastQueryService.VarianceAnalysis dto = ForecastQueryService.VarianceAnalysis.builder()
            .baseForecastId("val-baseForecastId")
            .comparisonForecastId("val-comparisonForecastId")
            .baseAmount(BigDecimal.ONE)
            .comparisonAmount(BigDecimal.ONE)
            .varianceAmount(BigDecimal.ONE)
            .variancePercentage(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQueryService.VarianceAnalysis dto1 = ForecastQueryService.VarianceAnalysis.builder()
                        .baseForecastId("test-baseForecastId")
            .comparisonForecastId("test-comparisonForecastId")
            .baseAmount(BigDecimal.TEN)
            .comparisonAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .baseType(Forecast.ForecastType.REVENUE)
            .comparisonType(Forecast.ForecastType.REVENUE)
            .build();
        ForecastQueryService.VarianceAnalysis dto2 = ForecastQueryService.VarianceAnalysis.builder()
                        .baseForecastId("test-baseForecastId")
            .comparisonForecastId("test-comparisonForecastId")
            .baseAmount(BigDecimal.TEN)
            .comparisonAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .baseType(Forecast.ForecastType.REVENUE)
            .comparisonType(Forecast.ForecastType.REVENUE)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ForecastQueryService.VarianceAnalysis dto = ForecastQueryService.VarianceAnalysis.builder()
                        .baseForecastId("test-baseForecastId")
            .comparisonForecastId("test-comparisonForecastId")
            .baseAmount(BigDecimal.TEN)
            .comparisonAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .baseType(Forecast.ForecastType.REVENUE)
            .comparisonType(Forecast.ForecastType.REVENUE)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}