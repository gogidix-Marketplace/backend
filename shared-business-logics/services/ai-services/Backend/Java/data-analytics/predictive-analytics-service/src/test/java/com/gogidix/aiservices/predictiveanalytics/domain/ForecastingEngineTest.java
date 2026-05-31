package com.gogidix.aiservices.predictiveanalytics.domain;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class ForecastingEngineTest {
    private ForecastingEngine e;

    @BeforeEach
    void setup() { e = new ForecastingEngine(new TrendAnalysisService()); }

    @Test
    void generateForecast() {
        var result = e.generateForecast("ds", "value", 10, Forecast.ForecastMethod.MOVING_AVERAGE);
        assertThat(result).isNotNull();
    }

    @Test
    void generateConfidenceIntervals() {
        var data = List.of(
            new Forecast.ForecastDataPoint(java.time.LocalDateTime.now(), 100.0),
            new Forecast.ForecastDataPoint(java.time.LocalDateTime.now().plusDays(1), 110.0)
        );
        var intervals = e.generateConfidenceIntervals(data, 0.95);
        assertThat(intervals).isNotNull();
    }

    @Test
    void calculateMetrics() {
        var metrics = e.calculateMetrics();
        assertThat(metrics).isNotNull();
    }

    @Test
    void fetchHistoricalData() {
        var data = e.fetchHistoricalData("ds");
        assertThat(data).isNotNull();
    }

    @Test
    void analyzeTrendsNeedsEnoughData() {
        var data = Map.<String, Object>of("data", Collections.nCopies(55, Map.of("val", 1.0)));
        var result = e.analyzeTrends(data, "val");
        assertThat(result).isNotNull();
    }

    @Test
    void forecastMethodEnum() {
        assertThat(Forecast.ForecastMethod.values()).isNotEmpty();
    }
}
