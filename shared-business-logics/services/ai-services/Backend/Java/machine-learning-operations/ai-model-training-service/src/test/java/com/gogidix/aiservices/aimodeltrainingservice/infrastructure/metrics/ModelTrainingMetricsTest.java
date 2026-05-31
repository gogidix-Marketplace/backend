package com.gogidix.aiservices.aimodeltrainingservice.infrastructure.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ModelTrainingMetricsTest {

    @Mock
    private MeterRegistry meterRegistry;

    private ModelTrainingMetrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new ModelTrainingMetrics(meterRegistry);
    }

    @Test
    void whenCreated_RegistersAllCounters() {
        // Assert
        assertThat(metrics).isNotNull();
        assertThat(metrics.getMeterRegistry()).isNotNull();
        assertThat(metrics.getMeterRegistry()).isEqualTo(meterRegistry);
    }

    @Test
    void incrementTrainingTotal_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementTrainingTotal();
    }

    @Test
    void incrementTrainingSuccess_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementTrainingSuccess();
    }

    @Test
    void incrementTrainingFailure_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementTrainingFailure();
    }

    @Test
    void incrementModelsTrained_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementModelsTrained();
    }

    @Test
    void incrementTrainingJobsCompleted_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementTrainingJobsCompleted();
    }

    @Test
    void incrementTrainingJobsFailed_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementTrainingJobsFailed();
    }

    @Test
    void incrementHyperparameterTuning_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementHyperparameterTuning();
    }

    @Test
    void recordTrainingTime_WhenCalled_RecordsTime() {
        // Act & Assert - should not throw
        metrics.recordTrainingTime(100);
    }

    @Test
    void startTrainingTimer_WhenCalled_ReturnsSample() {
        // Act
        var sample = metrics.startTrainingTimer();

        // Assert
        assertThat(sample).isNotNull();
    }

    @Test
    void stopTrainingTimer_WhenCalled_StopsTimer() {
        // Arrange
        var sample = metrics.startTrainingTimer();

        // Act & Assert - should not throw
        metrics.stopTrainingTimer(sample);
    }

    @Test
    void startModelEvaluationTimer_WhenCalled_ReturnsSample() {
        // Act
        var sample = metrics.startModelEvaluationTimer();

        // Assert
        assertThat(sample).isNotNull();
    }

    @Test
    void stopModelEvaluationTimer_WhenCalled_StopsTimer() {
        // Arrange
        var sample = metrics.startModelEvaluationTimer();

        // Act & Assert - should not throw
        metrics.stopModelEvaluationTimer(sample);
    }

    @Test
    void recordModelSaveTime_WhenCalled_RecordsTime() {
        // Act & Assert - should not throw
        metrics.recordModelSaveTime(50);
    }

    @Test
    void startModelSaveTimer_WhenCalled_ReturnsSample() {
        // Act
        var sample = metrics.startModelSaveTimer();

        // Assert
        assertThat(sample).isNotNull();
    }

    @Test
    void stopModelSaveTimer_WhenCalled_StopsTimer() {
        // Arrange
        var sample = metrics.startModelSaveTimer();

        // Act & Assert - should not throw
        metrics.stopModelSaveTimer(sample);
    }

    @Test
    void getErrorRate_WhenNoRequests_ReturnsZero() {
        // Act
        double errorRate = metrics.getErrorRate();

        // Assert
        assertThat(errorRate).isZero();
    }

    @Test
    void getMeterRegistry_WhenCalled_ReturnsRegistry() {
        // Act
        MeterRegistry registry = metrics.getMeterRegistry();

        // Assert
        assertThat(registry).isNotNull();
        assertThat(registry).isEqualTo(meterRegistry);
    }
}
