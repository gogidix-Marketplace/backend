package com.gogidix.aiservices.timeseriesforecasting.infrastructure.metrics;

import org.junit.jupiter.api.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class TimeSeriesForecastingMetricsTest {
    private TimeSeriesForecastingMetrics m;

    @BeforeEach
    void setup() {
        m = new TimeSeriesForecastingMetrics(new SimpleMeterRegistry());
    }

    @Test
    void counters() {
        m.incrementForecastingTotal();
        m.incrementForecastingSuccess();
        m.incrementForecastingFailure();
        m.incrementModelTraining();
        m.incrementSeasonalityAnalysis();
    }

    @Test
    void records() {
        m.recordForecastingTime(100);
        m.recordModelTrainingTime(100);
        m.recordSeasonalityAnalysisTime(100);
        m.recordDataPreprocessingTime(100);
    }

    @Test
    void timers() {
        var s0 = m.startForecastingTimer();
        m.stopForecastingTimer(s0);
        var s1 = m.startModelTrainingTimer();
        m.stopModelTrainingTimer(s1);
        var s2 = m.startSeasonalityAnalysisTimer();
        m.stopSeasonalityAnalysisTimer(s2);
        var s3 = m.startDataPreprocessingTimer();
        m.stopDataPreprocessingTimer(s3);
        
    }

    @Test
    void slos() {
        assertThat(m.getForecastingLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getForecastingLatencyP99()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getModelTrainingLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getMeterRegistry()).isNotNull();
    }
}
