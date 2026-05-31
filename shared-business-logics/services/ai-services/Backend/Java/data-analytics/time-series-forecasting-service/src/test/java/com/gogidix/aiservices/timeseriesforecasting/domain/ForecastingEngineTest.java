package com.gogidix.aiservices.timeseriesforecasting.domain;

import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class ForecastingEngineTest {
    private ForecastingEngine e;

    @BeforeEach
    void setup() { e = new ForecastingEngine(); }

    @Test
    void generateForecast() {
        var data = new ArrayList<TimeSeriesForecast.TimeSeriesDataPoint>();
        for (int i = 0; i < 30; i++) {
            data.add(new TimeSeriesForecast.TimeSeriesDataPoint(
                LocalDateTime.now().minusDays(30 - i), (double)(i * 10)));
        }
        var result = e.generateForecast(data, 10, TimeSeriesForecast.Frequency.DAILY, true);
        assertThat(result).isNotNull();
    }

    @Test
    void calculateAccuracyMetrics() {
        var data = new ArrayList<TimeSeriesForecast.TimeSeriesDataPoint>();
        for (int i = 0; i < 30; i++) {
            data.add(new TimeSeriesForecast.TimeSeriesDataPoint(
                LocalDateTime.now().minusDays(30 - i), (double)(i * 10)));
        }
        var metrics = e.calculateAccuracyMetrics(data);
        assertThat(metrics).isNotNull();
    }
}
