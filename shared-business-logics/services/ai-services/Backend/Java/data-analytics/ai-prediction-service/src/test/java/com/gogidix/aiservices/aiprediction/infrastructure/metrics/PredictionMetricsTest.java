package com.gogidix.aiservices.aiprediction.infrastructure.metrics;

import org.junit.jupiter.api.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class PredictionMetricsTest {
    private PredictionMetrics m;

    @BeforeEach
    void setup() {
        m = new PredictionMetrics(new SimpleMeterRegistry());
    }

    @Test
    void counters() {
        m.incrementPredictionTotal();
        m.incrementPredictionSuccess();
        m.incrementPredictionFailure();
        m.incrementBatchPrediction();
        m.incrementHighConfidencePrediction();
        m.incrementLowConfidencePrediction();
    }

    @Test
    void records() {
        m.recordPredictionTime(100);
    }

    @Test
    void timers() {
        var s0 = m.startPredictionTimer();
        m.stopPredictionTimer(s0);
        var s1 = m.startModelInferenceTimer();
        m.stopModelInferenceTimer(s1);
        var s2 = m.startBatchPredictionTimer();
        m.stopBatchPredictionTimer(s2);
        
    }

    @Test
    void slos() {
        assertThat(m.getPredictionLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getPredictionLatencyP99()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getModelInferenceLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getMeterRegistry()).isNotNull();
    }
}
