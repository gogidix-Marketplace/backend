package com.gogidix.finance.forecasting.application.dto.response;

import com.gogidix.finance.forecasting.application.dto.response.ForecastMetricsDto;
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
class ForecastMetricsDtoTest {

        @Test
    void testBuilder() {
        ForecastMetricsDto dto = ForecastMetricsDto.builder()
                        .metricId("test-metricId")
            .forecastId("test-forecastId")
            .metricName("test-metricName")
            .metricCode("test-metricCode")
            .category("test-category")
            .subcategory("test-subcategory")
            .amount(BigDecimal.TEN)
            .previousAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .period("test-period")
            .periodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .periodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .metricType(ForecastMetricsDto.MetricTypeDto.REVENUE)
            .unit("test-unit")
            .weight(BigDecimal.TEN)
            .confidenceLevel(42)
            .dataSource("test-dataSource")
            .notes("test-notes")
            .sortOrder(42)
            .isCalculated(true)
            .calculationFormula("test-calculationFormula")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-metricId", dto.getMetricId());
        assertEquals("test-forecastId", dto.getForecastId());
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals("test-metricCode", dto.getMetricCode());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-subcategory", dto.getSubcategory());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getPreviousAmount());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals("test-period", dto.getPeriod());
        assertEquals(ForecastMetricsDto.MetricTypeDto.REVENUE, dto.getMetricType());
        assertEquals("test-unit", dto.getUnit());
        assertEquals(BigDecimal.TEN, dto.getWeight());
        assertEquals(42, dto.getConfidenceLevel());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(42, dto.getSortOrder());
        assertTrue(dto.getIsCalculated());
        assertEquals("test-calculationFormula", dto.getCalculationFormula());
    }

    @Test
    void testSettersAndGetters() {
        ForecastMetricsDto dto = new ForecastMetricsDto();
        dto.setMetricId("val-metricId");
        dto.setForecastId("val-forecastId");
        dto.setMetricName("val-metricName");
        dto.setMetricCode("val-metricCode");
        dto.setCategory("val-category");
        dto.setSubcategory("val-subcategory");
        dto.setAmount(BigDecimal.ONE);
        dto.setPreviousAmount(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setPeriod("val-period");
        dto.setMetricType(ForecastMetricsDto.MetricTypeDto.REVENUE);
        dto.setUnit("val-unit");
        dto.setWeight(BigDecimal.ONE);
        dto.setConfidenceLevel(99);
        dto.setDataSource("val-dataSource");
        dto.setNotes("val-notes");
        dto.setSortOrder(99);
        dto.setIsCalculated(true);
        dto.setCalculationFormula("val-calculationFormula");
        assertEquals("val-metricId", dto.getMetricId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals("val-metricCode", dto.getMetricCode());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-subcategory", dto.getSubcategory());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getPreviousAmount());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals("val-period", dto.getPeriod());
        assertEquals(ForecastMetricsDto.MetricTypeDto.REVENUE, dto.getMetricType());
        assertEquals("val-unit", dto.getUnit());
        assertEquals(BigDecimal.ONE, dto.getWeight());
        assertEquals(99, dto.getConfidenceLevel());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(99, dto.getSortOrder());
        assertTrue(dto.getIsCalculated());
        assertEquals("val-calculationFormula", dto.getCalculationFormula());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastMetricsDto dto1 = ForecastMetricsDto.builder()
                        .metricId("test-metricId")
            .forecastId("test-forecastId")
            .metricName("test-metricName")
            .metricCode("test-metricCode")
            .category("test-category")
            .subcategory("test-subcategory")
            .amount(BigDecimal.TEN)
            .previousAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .period("test-period")
            .periodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .periodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .metricType(ForecastMetricsDto.MetricTypeDto.REVENUE)
            .unit("test-unit")
            .weight(BigDecimal.TEN)
            .confidenceLevel(42)
            .dataSource("test-dataSource")
            .notes("test-notes")
            .sortOrder(42)
            .isCalculated(true)
            .calculationFormula("test-calculationFormula")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ForecastMetricsDto dto2 = ForecastMetricsDto.builder()
                        .metricId("test-metricId")
            .forecastId("test-forecastId")
            .metricName("test-metricName")
            .metricCode("test-metricCode")
            .category("test-category")
            .subcategory("test-subcategory")
            .amount(BigDecimal.TEN)
            .previousAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .period("test-period")
            .periodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .periodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .metricType(ForecastMetricsDto.MetricTypeDto.REVENUE)
            .unit("test-unit")
            .weight(BigDecimal.TEN)
            .confidenceLevel(42)
            .dataSource("test-dataSource")
            .notes("test-notes")
            .sortOrder(42)
            .isCalculated(true)
            .calculationFormula("test-calculationFormula")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ForecastMetricsDto dto = ForecastMetricsDto.builder()
                        .metricId("test-metricId")
            .forecastId("test-forecastId")
            .metricName("test-metricName")
            .metricCode("test-metricCode")
            .category("test-category")
            .subcategory("test-subcategory")
            .amount(BigDecimal.TEN)
            .previousAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .period("test-period")
            .periodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .periodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .metricType(ForecastMetricsDto.MetricTypeDto.REVENUE)
            .unit("test-unit")
            .weight(BigDecimal.TEN)
            .confidenceLevel(42)
            .dataSource("test-dataSource")
            .notes("test-notes")
            .sortOrder(42)
            .isCalculated(true)
            .calculationFormula("test-calculationFormula")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}