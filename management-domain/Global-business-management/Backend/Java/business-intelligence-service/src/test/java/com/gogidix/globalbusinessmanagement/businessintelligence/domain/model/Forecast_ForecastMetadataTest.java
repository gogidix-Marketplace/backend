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
class Forecast_ForecastMetadataTest {

        @Test
    void testBuilder() {
        Forecast.ForecastMetadata dto = Forecast.ForecastMetadata.builder()
                        .dataPointsUsed(42)
            .forecastHorizon(42)
            .frequency("test-frequency")
            .currency("test-currency")
            .unit("test-unit")
            .scale(BigDecimal.TEN)
            .dataSource("test-dataSource")
            .dataQualityScore(BigDecimal.TEN)
            .hasOutliers(true)
            .hasMissingData(true)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getDataPointsUsed());
        assertEquals(42, dto.getForecastHorizon());
        assertEquals("test-frequency", dto.getFrequency());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-unit", dto.getUnit());
        assertEquals(BigDecimal.TEN, dto.getScale());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals(BigDecimal.TEN, dto.getDataQualityScore());
        assertTrue(dto.getHasOutliers());
        assertTrue(dto.getHasMissingData());
    }

    @Test
    void testSettersAndGetters() {
        Forecast.ForecastMetadata dto = new Forecast.ForecastMetadata();
        dto.setDataPointsUsed(99);
        dto.setForecastHorizon(99);
        dto.setFrequency("val-frequency");
        dto.setCurrency("val-currency");
        dto.setUnit("val-unit");
        dto.setScale(BigDecimal.ONE);
        dto.setDataSource("val-dataSource");
        dto.setDataQualityScore(BigDecimal.ONE);
        dto.setHasOutliers(true);
        dto.setHasMissingData(true);
        assertEquals(99, dto.getDataPointsUsed());
        assertEquals(99, dto.getForecastHorizon());
        assertEquals("val-frequency", dto.getFrequency());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-unit", dto.getUnit());
        assertEquals(BigDecimal.ONE, dto.getScale());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals(BigDecimal.ONE, dto.getDataQualityScore());
        assertTrue(dto.getHasOutliers());
        assertTrue(dto.getHasMissingData());
    }

    @Test
    void testEqualsAndHashCode() {
        Forecast.ForecastMetadata dto1 = Forecast.ForecastMetadata.builder()
                        .dataPointsUsed(42)
            .forecastHorizon(42)
            .frequency("test-frequency")
            .currency("test-currency")
            .unit("test-unit")
            .scale(BigDecimal.TEN)
            .dataSource("test-dataSource")
            .dataQualityScore(BigDecimal.TEN)
            .hasOutliers(true)
            .hasMissingData(true)
            .build();
        Forecast.ForecastMetadata dto2 = Forecast.ForecastMetadata.builder()
                        .dataPointsUsed(42)
            .forecastHorizon(42)
            .frequency("test-frequency")
            .currency("test-currency")
            .unit("test-unit")
            .scale(BigDecimal.TEN)
            .dataSource("test-dataSource")
            .dataQualityScore(BigDecimal.TEN)
            .hasOutliers(true)
            .hasMissingData(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Forecast.ForecastMetadata dto = Forecast.ForecastMetadata.builder()
                        .dataPointsUsed(42)
            .forecastHorizon(42)
            .frequency("test-frequency")
            .currency("test-currency")
            .unit("test-unit")
            .scale(BigDecimal.TEN)
            .dataSource("test-dataSource")
            .dataQualityScore(BigDecimal.TEN)
            .hasOutliers(true)
            .hasMissingData(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}