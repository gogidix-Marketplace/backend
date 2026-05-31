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
class Forecast_ForecastAccuracyTest {

        @Test
    void testBuilder() {
        Forecast.ForecastAccuracy dto = Forecast.ForecastAccuracy.builder()
                        .meanAbsoluteError(BigDecimal.TEN)
            .meanSquaredError(BigDecimal.TEN)
            .rootMeanSquaredError(BigDecimal.TEN)
            .meanAbsolutePercentageError(BigDecimal.TEN)
            .symmetricMeanAbsolutePercentageError(BigDecimal.TEN)
            .theilUStatistic(BigDecimal.TEN)
            .trackingSignal(BigDecimal.TEN)
            .bias(BigDecimal.TEN)
            .accuracyRating("test-accuracyRating")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getMeanAbsoluteError());
        assertEquals(BigDecimal.TEN, dto.getMeanSquaredError());
        assertEquals(BigDecimal.TEN, dto.getRootMeanSquaredError());
        assertEquals(BigDecimal.TEN, dto.getMeanAbsolutePercentageError());
        assertEquals(BigDecimal.TEN, dto.getSymmetricMeanAbsolutePercentageError());
        assertEquals(BigDecimal.TEN, dto.getTheilUStatistic());
        assertEquals(BigDecimal.TEN, dto.getTrackingSignal());
        assertEquals(BigDecimal.TEN, dto.getBias());
        assertEquals("test-accuracyRating", dto.getAccuracyRating());
    }

    @Test
    void testSettersAndGetters() {
        Forecast.ForecastAccuracy dto = new Forecast.ForecastAccuracy();
        dto.setMeanAbsoluteError(BigDecimal.ONE);
        dto.setMeanSquaredError(BigDecimal.ONE);
        dto.setRootMeanSquaredError(BigDecimal.ONE);
        dto.setMeanAbsolutePercentageError(BigDecimal.ONE);
        dto.setSymmetricMeanAbsolutePercentageError(BigDecimal.ONE);
        dto.setTheilUStatistic(BigDecimal.ONE);
        dto.setTrackingSignal(BigDecimal.ONE);
        dto.setBias(BigDecimal.ONE);
        dto.setAccuracyRating("val-accuracyRating");
        assertEquals(BigDecimal.ONE, dto.getMeanAbsoluteError());
        assertEquals(BigDecimal.ONE, dto.getMeanSquaredError());
        assertEquals(BigDecimal.ONE, dto.getRootMeanSquaredError());
        assertEquals(BigDecimal.ONE, dto.getMeanAbsolutePercentageError());
        assertEquals(BigDecimal.ONE, dto.getSymmetricMeanAbsolutePercentageError());
        assertEquals(BigDecimal.ONE, dto.getTheilUStatistic());
        assertEquals(BigDecimal.ONE, dto.getTrackingSignal());
        assertEquals(BigDecimal.ONE, dto.getBias());
        assertEquals("val-accuracyRating", dto.getAccuracyRating());
    }

    @Test
    void testEqualsAndHashCode() {
        Forecast.ForecastAccuracy dto1 = Forecast.ForecastAccuracy.builder()
                        .meanAbsoluteError(BigDecimal.TEN)
            .meanSquaredError(BigDecimal.TEN)
            .rootMeanSquaredError(BigDecimal.TEN)
            .meanAbsolutePercentageError(BigDecimal.TEN)
            .symmetricMeanAbsolutePercentageError(BigDecimal.TEN)
            .theilUStatistic(BigDecimal.TEN)
            .trackingSignal(BigDecimal.TEN)
            .bias(BigDecimal.TEN)
            .accuracyRating("test-accuracyRating")
            .build();
        Forecast.ForecastAccuracy dto2 = Forecast.ForecastAccuracy.builder()
                        .meanAbsoluteError(BigDecimal.TEN)
            .meanSquaredError(BigDecimal.TEN)
            .rootMeanSquaredError(BigDecimal.TEN)
            .meanAbsolutePercentageError(BigDecimal.TEN)
            .symmetricMeanAbsolutePercentageError(BigDecimal.TEN)
            .theilUStatistic(BigDecimal.TEN)
            .trackingSignal(BigDecimal.TEN)
            .bias(BigDecimal.TEN)
            .accuracyRating("test-accuracyRating")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Forecast.ForecastAccuracy dto = Forecast.ForecastAccuracy.builder()
                        .meanAbsoluteError(BigDecimal.TEN)
            .meanSquaredError(BigDecimal.TEN)
            .rootMeanSquaredError(BigDecimal.TEN)
            .meanAbsolutePercentageError(BigDecimal.TEN)
            .symmetricMeanAbsolutePercentageError(BigDecimal.TEN)
            .theilUStatistic(BigDecimal.TEN)
            .trackingSignal(BigDecimal.TEN)
            .bias(BigDecimal.TEN)
            .accuracyRating("test-accuracyRating")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}