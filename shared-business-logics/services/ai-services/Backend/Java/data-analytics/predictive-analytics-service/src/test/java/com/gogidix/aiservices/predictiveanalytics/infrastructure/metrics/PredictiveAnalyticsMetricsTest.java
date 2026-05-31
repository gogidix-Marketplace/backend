package com.gogidix.aiservices.predictiveanalytics.infrastructure.metrics;

import org.junit.jupiter.api.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class PredictiveAnalyticsMetricsTest {
    private PredictiveAnalyticsMetrics m;

    @BeforeEach
    void setup() {
        m = new PredictiveAnalyticsMetrics(new SimpleMeterRegistry());
    }

    @Test
    void counters() {
        m.incrementForecastGenerationTotal();
        m.incrementForecastGenerationSuccess();
        m.incrementForecastGenerationFailure();
        m.incrementModelTraining();
        m.incrementPrediction();
    }

    @Test
    void records() {
        m.recordForecastGenerationTime(100);
        m.recordModelTrainingTime(100);
        m.recordPredictionTime(100);
        m.recordDataPreprocessingTime(100);
    }

    @Test
    void timers() {
        var s0 = m.startForecastGenerationTimer();
        m.stopForecastGenerationTimer(s0);
        var s1 = m.startModelTrainingTimer();
        m.stopModelTrainingTimer(s1);
        var s2 = m.startPredictionTimer();
        m.stopPredictionTimer(s2);
        var s3 = m.startDataPreprocessingTimer();
        m.stopDataPreprocessingTimer(s3);
        
    }

    @Test
    void slos() {
        assertThat(m.getForecastGenerationLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getForecastGenerationLatencyP99()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getPredictionLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getMeterRegistry()).isNotNull();
    }
}
