package com.gogidix.aiservices.aiinferenceservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class InferenceMetricsGenTest {

    private InferenceMetrics metrics;

    @BeforeEach
    void setup() {
        metrics = new InferenceMetrics(new SimpleMeterRegistry());
    }

    @Test
    void incrementInferenceTotal() {
        metrics.incrementInferenceTotal();
    }

    @Test
    void incrementInferenceSuccess() {
        metrics.incrementInferenceSuccess();
    }

    @Test
    void incrementInferenceFailure() {
        metrics.incrementInferenceFailure();
    }

    @Test
    void incrementModelLoaded() {
        metrics.incrementModelLoaded();
    }

    @Test
    void incrementModelUnloaded() {
        metrics.incrementModelUnloaded();
    }

    @Test
    void incrementHighConfidence() {
        metrics.incrementHighConfidence();
    }

    @Test
    void incrementLowConfidence() {
        metrics.incrementLowConfidence();
    }

    @Test
    void recordInferenceTime() {
        metrics.recordInferenceTime(100);
    }

    @Test
    void recordModelLoadingTime() {
        metrics.recordModelLoadingTime(50);
    }

    @Test
    void recordPredictionTime() {
        metrics.recordPredictionTime(75);
    }

    @Test
    void stopInferenceTimer() {
        var sample = metrics.startInferenceTimer();
        metrics.stopInferenceTimer(sample);
    }

    @Test
    void stopModelLoadingTimer() {
        var sample = metrics.startModelLoadingTimer();
        metrics.stopModelLoadingTimer(sample);
    }

    @Test
    void stopPredictionTimer() {
        var sample = metrics.startPredictionTimer();
        metrics.stopPredictionTimer(sample);
    }

    @Test
    void sloMethods() {
        assertThat(metrics.getInferenceLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getInferenceLatencyP99()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getErrorRate()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void getMeterRegistry() {
        assertThat(metrics.getMeterRegistry()).isNotNull();
    }
}
