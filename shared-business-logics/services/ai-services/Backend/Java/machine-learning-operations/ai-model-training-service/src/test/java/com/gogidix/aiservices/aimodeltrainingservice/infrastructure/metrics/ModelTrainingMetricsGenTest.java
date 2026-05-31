package com.gogidix.aiservices.aimodeltrainingservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ModelTrainingMetricsGenTest {

    private ModelTrainingMetrics metrics;

    @BeforeEach
    void setup() {
        metrics = new ModelTrainingMetrics(new SimpleMeterRegistry());
    }

    @Test
    void incrementTrainingTotal() {
        metrics.incrementTrainingTotal();
    }

    @Test
    void incrementTrainingSuccess() {
        metrics.incrementTrainingSuccess();
    }

    @Test
    void incrementTrainingFailure() {
        metrics.incrementTrainingFailure();
    }

    @Test
    void incrementModelsTrained() {
        metrics.incrementModelsTrained();
    }

    @Test
    void incrementTrainingJobsCompleted() {
        metrics.incrementTrainingJobsCompleted();
    }

    @Test
    void incrementTrainingJobsFailed() {
        metrics.incrementTrainingJobsFailed();
    }

    @Test
    void incrementHyperparameterTuning() {
        metrics.incrementHyperparameterTuning();
    }

    @Test
    void recordTrainingTime() {
        metrics.recordTrainingTime(100);
    }

    @Test
    void recordModelSaveTime() {
        metrics.recordModelSaveTime(50);
    }

    @Test
    void stopTrainingTimer() {
        var sample = metrics.startTrainingTimer();
        metrics.stopTrainingTimer(sample);
    }

    @Test
    void stopModelEvaluationTimer() {
        var sample = metrics.startModelEvaluationTimer();
        metrics.stopModelEvaluationTimer(sample);
    }

    @Test
    void stopModelSaveTimer() {
        var sample = metrics.startModelSaveTimer();
        metrics.stopModelSaveTimer(sample);
    }

    @Test
    void sloMethods() {
        assertThat(metrics.getTrainingLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getTrainingLatencyP99()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getErrorRate()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void getMeterRegistry() {
        assertThat(metrics.getMeterRegistry()).isNotNull();
    }
}
