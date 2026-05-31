package com.gogidix.aiservices.aimodeltrainingservice.infrastructure.governance;

import com.gogidix.aiservices.aimodeltrainingservice.infrastructure.metrics.ModelTrainingMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThresholdValidatorTest {

    @Mock
    private ModelTrainingMetrics metrics;

    private ThresholdValidator thresholdValidator;

    @BeforeEach
    void setUp() {
        thresholdValidator = new ThresholdValidator(metrics);
    }

    @Test
    void validateAllThresholds_WhenAllWithinBounds_ReturnsCompliantReport() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(400.0);
        when(metrics.getTrainingLatencyP99()).thenReturn(800.0);
        when(metrics.getErrorRate()).thenReturn(0.005);

        // Act
        var report = thresholdValidator.validateAllThresholds();

        // Assert
        assertThat(report.isCompliant()).isTrue();
        assertThat(report.getCheckCount()).isEqualTo(4);
        assertThat(report.getPassedCheckCount()).isEqualTo(4);
        assertThat(report.getFailedCheckCount()).isEqualTo(0);
    }

    @Test
    void validateAllThresholds_WhenP95Exceeded_ReturnsNonCompliantReport() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(600.0);
        when(metrics.getTrainingLatencyP99()).thenReturn(800.0);
        when(metrics.getErrorRate()).thenReturn(0.005);

        // Act
        var report = thresholdValidator.validateAllThresholds();

        // Assert
        assertThat(report.isCompliant()).isFalse();
        assertThat(report.getFailedCheckCount()).isGreaterThan(0);
    }

    @Test
    void validateAllThresholds_WhenErrorRateExceeded_ReturnsNonCompliantReport() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(400.0);
        when(metrics.getTrainingLatencyP99()).thenReturn(800.0);
        when(metrics.getErrorRate()).thenReturn(0.02);

        // Act
        var report = thresholdValidator.validateAllThresholds();

        // Assert
        assertThat(report.isCompliant()).isFalse();
    }

    @Test
    void validateThreshold_WhenP95LatencyValid_ReturnsPassedValidation() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(400.0);

        // Act
        var validation = thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

        // Assert
        assertThat(validation.isPassed()).isTrue();
        assertThat(validation.getActualValue()).isEqualTo(400.0);
        assertThat(validation.getThreshold()).isEqualTo(500.0);
    }

    @Test
    void validateThreshold_WhenP95LatencyInvalid_ReturnsFailedValidation() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(600.0);

        // Act
        var validation = thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

        // Assert
        assertThat(validation.isPassed()).isFalse();
    }

    @Test
    void isDegraded_WhenCompliant_ReturnsFalse() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(400.0);
        when(metrics.getTrainingLatencyP99()).thenReturn(800.0);
        when(metrics.getErrorRate()).thenReturn(0.005);

        // Act
        boolean degraded = thresholdValidator.isDegraded();

        // Assert
        assertThat(degraded).isFalse();
    }

    @Test
    void isDegraded_WhenNonCompliant_ReturnsTrue() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(600.0);
        when(metrics.getTrainingLatencyP99()).thenReturn(800.0);
        when(metrics.getErrorRate()).thenReturn(0.005);

        // Act
        boolean degraded = thresholdValidator.isDegraded();

        // Assert
        assertThat(degraded).isTrue();
    }

    @Test
    void getHealthStatus_WhenAllCompliant_ReturnsHealthy() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(400.0);
        when(metrics.getTrainingLatencyP99()).thenReturn(800.0);
        when(metrics.getErrorRate()).thenReturn(0.005);

        // Act
        var health = thresholdValidator.getHealthStatus();

        // Assert
        assertThat(health).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
    }

    @Test
    void getHealthStatus_WhenMostlyCompliant_ReturnsDegraded() {
        // Arrange
        when(metrics.getTrainingLatencyP95()).thenReturn(600.0);
        when(metrics.getTrainingLatencyP99()).thenReturn(800.0);
        when(metrics.getErrorRate()).thenReturn(0.005);

        // Act
        var health = thresholdValidator.getHealthStatus();

        // Assert
        assertThat(health).isEqualTo(ThresholdValidator.HealthStatus.DEGRADED);
    }
}
