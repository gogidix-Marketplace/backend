package com.gogidix.aiservices.aitrainingservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class TrainingMetricsGenTest {

    private TrainingMetrics metrics;

    @BeforeEach
    void setup() {
        metrics = new TrainingMetrics(new SimpleMeterRegistry());
    }

    @Test
    void incrementTrainingJobTotal() {
        metrics.incrementTrainingJobTotal();
    }

    @Test
    void incrementTrainingJobSuccess() {
        metrics.incrementTrainingJobSuccess();
    }

    @Test
    void incrementTrainingJobFailure() {
        metrics.incrementTrainingJobFailure();
    }

    @Test
    void incrementTrainingJobStarted() {
        metrics.incrementTrainingJobStarted();
    }

    @Test
    void incrementTrainingJobCompleted() {
        metrics.incrementTrainingJobCompleted();
    }

    @Test
    void incrementTrainingJobCancelled() {
        metrics.incrementTrainingJobCancelled();
    }

    @Test
    void recordTrainingJobTime() {
        metrics.recordTrainingJobTime(100);
    }

    @Test
    void recordModelTrainingTime() {
        metrics.recordModelTrainingTime(50);
    }

    @Test
    void recordFineTuningTime() {
        metrics.recordFineTuningTime(75);
    }

    @Test
    void stopTrainingJobTimer() {
        var sample = metrics.startTrainingJobTimer();
        metrics.stopTrainingJobTimer(sample);
    }

    @Test
    void stopModelTrainingTimer() {
        var sample = metrics.startModelTrainingTimer();
        metrics.stopModelTrainingTimer(sample);
    }

    @Test
    void stopFineTuningTimer() {
        var sample = metrics.startFineTuningTimer();
        metrics.stopFineTuningTimer(sample);
    }

    @Test
    void sloMethods() {
        assertThat(metrics.getTrainingJobLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getTrainingJobLatencyP99()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getErrorRate()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void getMeterRegistry() {
        assertThat(metrics.getMeterRegistry()).isNotNull();
    }
}
