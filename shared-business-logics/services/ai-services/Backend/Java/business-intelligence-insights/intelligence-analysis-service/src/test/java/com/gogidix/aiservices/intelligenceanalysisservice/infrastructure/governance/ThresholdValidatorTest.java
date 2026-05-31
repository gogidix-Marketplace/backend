package com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.governance;

import com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.metrics.IntelligenceAnalysisationMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ThresholdValidator.
 * Tests SLO threshold validation and health status calculation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ThresholdValidator Tests")
class ThresholdValidatorTest {

    @Mock
    private IntelligenceAnalysisationMetrics metrics;

    private ThresholdValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ThresholdValidator(metrics);
    }

    @Nested
    @DisplayName("Validate All Thresholds")
    class ValidateAllThresholdsTests {

        @Test
        @DisplayName("Should return compliant report when all thresholds within limits")
        void shouldReturnCompliantReportWhenAllThresholdsWithinLimits() {
            // Setup - all metrics within limits
            when(metrics.getAnalysisLatencyP95()).thenReturn(400.0);  // < 500ms
            when(metrics.getAnalysisLatencyP99()).thenReturn(800.0);  // < 1000ms
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);  // < 200ms
            when(metrics.getErrorRate()).thenReturn(0.005);  // < 1%

            ThresholdValidator.ComplianceReport report = validator.validateAllThresholds();

            assertThat(report.isCompliant()).isTrue();
            assertThat(report.getCheckCount()).isEqualTo(4);
            assertThat(report.getPassedCheckCount()).isEqualTo(4);
            assertThat(report.getFailedCheckCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return non-compliant report when P95 latency exceeds threshold")
        void shouldReturnNonCompliantWhenP95LatencyExceedsThreshold() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);  // > 500ms
            when(metrics.getAnalysisLatencyP99()).thenReturn(800.0);
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);
            when(metrics.getErrorRate()).thenReturn(0.005);

            ThresholdValidator.ComplianceReport report = validator.validateAllThresholds();

            assertThat(report.isCompliant()).isFalse();
            assertThat(report.getPassedCheckCount()).isEqualTo(3);
            assertThat(report.getFailedCheckCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return non-compliant report when P99 latency exceeds threshold")
        void shouldReturnNonCompliantWhenP99LatencyExceedsThreshold() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(400.0);
            when(metrics.getAnalysisLatencyP99()).thenReturn(1200.0);  // > 1000ms
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);
            when(metrics.getErrorRate()).thenReturn(0.005);

            ThresholdValidator.ComplianceReport report = validator.validateAllThresholds();

            assertThat(report.isCompliant()).isFalse();
            assertThat(report.getPassedCheckCount()).isEqualTo(3);
            assertThat(report.getFailedCheckCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return non-compliant report when analysis latency exceeds threshold")
        void shouldReturnNonCompliantWhenAnalysisLatencyExceedsThreshold() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(400.0);
            when(metrics.getAnalysisLatencyP99()).thenReturn(800.0);
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(250.0);  // > 200ms
            when(metrics.getErrorRate()).thenReturn(0.005);

            ThresholdValidator.ComplianceReport report = validator.validateAllThresholds();

            assertThat(report.isCompliant()).isFalse();
            assertThat(report.getPassedCheckCount()).isEqualTo(3);
            assertThat(report.getFailedCheckCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return non-compliant report when error rate exceeds threshold")
        void shouldReturnNonCompliantWhenErrorRateExceedsThreshold() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(400.0);
            when(metrics.getAnalysisLatencyP99()).thenReturn(800.0);
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);
            when(metrics.getErrorRate()).thenReturn(0.02);  // > 1%

            ThresholdValidator.ComplianceReport report = validator.validateAllThresholds();

            assertThat(report.isCompliant()).isFalse();
            assertThat(report.getPassedCheckCount()).isEqualTo(3);
            assertThat(report.getFailedCheckCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return non-compliant report when multiple thresholds exceed limits")
        void shouldReturnNonCompliantWhenMultipleThresholdsExceed() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);  // > 500ms
            when(metrics.getAnalysisLatencyP99()).thenReturn(1200.0);  // > 1000ms
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(250.0);  // > 200ms
            when(metrics.getErrorRate()).thenReturn(0.02);  // > 1%

            ThresholdValidator.ComplianceReport report = validator.validateAllThresholds();

            assertThat(report.isCompliant()).isFalse();
            assertThat(report.getPassedCheckCount()).isEqualTo(0);
            assertThat(report.getFailedCheckCount()).isEqualTo(4);
        }
    }

    @Nested
    @DisplayName("Validate Specific Threshold")
    class ValidateSpecificThresholdTests {

        @Test
        @DisplayName("Should validate P95 latency threshold")
        void shouldValidateP95Latency() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(350.0);

            ThresholdValidator.ThresholdValidation validation =
                    validator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P95_LATENCY);
            assertThat(validation.getActualValue()).isEqualTo(350.0);
            assertThat(validation.getThreshold()).isEqualTo(500.0);
            assertThat(validation.getUnit()).isEqualTo("ms");
            assertThat(validation.isPassed()).isTrue();
        }

        @Test
        @DisplayName("Should validate P99 latency threshold")
        void shouldValidateP99Latency() {
            when(metrics.getAnalysisLatencyP99()).thenReturn(850.0);

            ThresholdValidator.ThresholdValidation validation =
                    validator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);

            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P99_LATENCY);
            assertThat(validation.getActualValue()).isEqualTo(850.0);
            assertThat(validation.getThreshold()).isEqualTo(1000.0);
            assertThat(validation.isPassed()).isTrue();
        }

        @Test
        @DisplayName("Should validate segment analysis latency threshold")
        void shouldValidateAnalysisAnalysisLatency() {
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);

            ThresholdValidator.ThresholdValidation validation =
                    validator.validateThreshold(ThresholdValidator.ThresholdType.SEGMENT_ANALYSIS_LATENCY);

            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.SEGMENT_ANALYSIS_LATENCY);
            assertThat(validation.getActualValue()).isEqualTo(150.0);
            assertThat(validation.getThreshold()).isEqualTo(200.0);
            assertThat(validation.isPassed()).isTrue();
        }

        @Test
        @DisplayName("Should validate error rate threshold")
        void shouldValidateErrorRate() {
            when(metrics.getErrorRate()).thenReturn(0.008);  // 0.8%

            ThresholdValidator.ThresholdValidation validation =
                    validator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.ERROR_RATE);
            assertThat(validation.getActualValue()).isEqualTo(0.8);  // percentage
            assertThat(validation.getThreshold()).isEqualTo(1.0);  // percentage
            assertThat(validation.getUnit()).isEqualTo("%");
            assertThat(validation.isPassed()).isTrue();
        }

        @Test
        @DisplayName("Should detect failed P95 latency validation")
        void shouldDetectFailedP95Validation() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(550.0);

            ThresholdValidator.ThresholdValidation validation =
                    validator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation.isPassed()).isFalse();
            assertThat(validation.getActualValue()).isGreaterThan(validation.getThreshold());
        }
    }

    @Nested
    @DisplayName("Health Status Calculation")
    class HealthStatusTests {

        @Test
        @DisplayName("Should return HEALTHY when all thresholds pass")
        void shouldReturnHealthyWhenAllThresholdsPass() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(400.0);
            when(metrics.getAnalysisLatencyP99()).thenReturn(800.0);
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);
            when(metrics.getErrorRate()).thenReturn(0.005);

            ThresholdValidator.HealthStatus status = validator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
        }

        @Test
        @DisplayName("Should return DEGRADED when 75% or more thresholds pass")
        void shouldReturnDegradedWhen75PercentPass() {
            // 3 of 4 pass = 75%
            when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);  // fail
            when(metrics.getAnalysisLatencyP99()).thenReturn(800.0);  // pass
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);  // pass
            when(metrics.getErrorRate()).thenReturn(0.005);  // pass

            ThresholdValidator.HealthStatus status = validator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.DEGRADED);
        }

        @Test
        @DisplayName("Should return DEGRADED when more than 75% but not all pass")
        void shouldReturnDegradedWhenMoreThan75PercentPass() {
            // 3 of 4 pass = 75% -> DEGRADED
            when(metrics.getAnalysisLatencyP95()).thenReturn(400.0);  // pass
            when(metrics.getAnalysisLatencyP99()).thenReturn(1200.0);  // fail
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);  // pass
            when(metrics.getErrorRate()).thenReturn(0.005);  // pass

            ThresholdValidator.HealthStatus status = validator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.DEGRADED);
        }

        @Test
        @DisplayName("Should return UNHEALTHY when less than 75% thresholds pass")
        void shouldReturnUnhealthyWhenLessThan75PercentPass() {
            // 2 of 4 pass = 50% -> UNHEALTHY
            when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);  // fail
            when(metrics.getAnalysisLatencyP99()).thenReturn(1200.0);  // fail
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);  // pass
            when(metrics.getErrorRate()).thenReturn(0.005);  // pass

            ThresholdValidator.HealthStatus status = validator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.UNHEALTHY);
        }

        @Test
        @DisplayName("Should return UNHEALTHY when all thresholds fail")
        void shouldReturnUnhealthyWhenAllThresholdsFail() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);
            when(metrics.getAnalysisLatencyP99()).thenReturn(1200.0);
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(250.0);
            when(metrics.getErrorRate()).thenReturn(0.02);

            ThresholdValidator.HealthStatus status = validator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.UNHEALTHY);
        }
    }

    @Nested
    @DisplayName("Is Degraded Check")
    class IsDegradedTests {

        @Test
        @DisplayName("Should return false when compliant")
        void shouldReturnFalseWhenCompliant() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(400.0);
            when(metrics.getAnalysisLatencyP99()).thenReturn(800.0);
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);
            when(metrics.getErrorRate()).thenReturn(0.005);

            boolean degraded = validator.isDegraded();

            assertThat(degraded).isFalse();
        }

        @Test
        @DisplayName("Should return true when non-compliant")
        void shouldReturnTrueWhenNonCompliant() {
            when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);
            when(metrics.getAnalysisLatencyP99()).thenReturn(800.0);
            when(metrics.getAnalysisAnalysisLatencyP95()).thenReturn(150.0);
            when(metrics.getErrorRate()).thenReturn(0.005);

            boolean degraded = validator.isDegraded();

            assertThat(degraded).isTrue();
        }
    }
}
