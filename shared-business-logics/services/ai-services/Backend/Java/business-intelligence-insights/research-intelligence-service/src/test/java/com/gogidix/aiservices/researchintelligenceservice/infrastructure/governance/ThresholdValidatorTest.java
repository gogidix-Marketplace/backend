package com.gogidix.aiservices.researchintelligenceservice.infrastructure.governance;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.researchintelligenceservice.infrastructure.metrics.ResearchIntelligenceMetrics; import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
class ThresholdValidatorTest {
    private ResearchIntelligenceMetrics metrics;
    private ThresholdValidator validator;
    @BeforeEach void setup() {
        metrics = mock(ResearchIntelligenceMetrics.class);
        validator = new ThresholdValidator(metrics);
    }
    @Test void validateAllCompliant() {
        when(metrics.getAnalysisLatencyP95()).thenReturn(100.0);
        when(metrics.getAnalysisLatencyP99()).thenReturn(500.0);
        when(metrics.getProjectCreationLatencyP95()).thenReturn(200.0);
        when(metrics.getErrorRate()).thenReturn(0.005);
        var report = validator.validateAllThresholds();
        assertThat(report.isCompliant()).isTrue();
        assertThat(report.getCheckCount()).isEqualTo(4);
        assertThat(report.getPassedCheckCount()).isEqualTo(4);
        assertThat(report.getFailedCheckCount()).isEqualTo(0);
    }
    @Test void validateAllNonCompliant() {
        when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);
        when(metrics.getAnalysisLatencyP99()).thenReturn(1500.0);
        when(metrics.getProjectCreationLatencyP95()).thenReturn(400.0);
        when(metrics.getErrorRate()).thenReturn(0.05);
        var report = validator.validateAllThresholds();
        assertThat(report.isCompliant()).isFalse();
        assertThat(report.getFailedCheckCount()).isEqualTo(4);
    }
    @Test void validateThresholdP95() {
        when(metrics.getAnalysisLatencyP95()).thenReturn(300.0);
        var v = validator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);
        assertThat(v.isPassed()).isTrue();
        assertThat(v.getActualValue()).isEqualTo(300.0);
    }
    @Test void validateThresholdP99Fail() {
        when(metrics.getAnalysisLatencyP99()).thenReturn(1200.0);
        var v = validator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);
        assertThat(v.isPassed()).isFalse();
    }
    @Test void validateThresholdErrorRate() {
        when(metrics.getErrorRate()).thenReturn(0.005);
        var v = validator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);
        assertThat(v.isPassed()).isTrue();
    }
    @Test void validateThresholdProjectCreation() {
        when(metrics.getProjectCreationLatencyP95()).thenReturn(250.0);
        var v = validator.validateThreshold(ThresholdValidator.ThresholdType.PROJECT_CREATION_LATENCY);
        assertThat(v.isPassed()).isTrue();
    }
    @Test void isDegradedFalse() {
        when(metrics.getAnalysisLatencyP95()).thenReturn(100.0);
        when(metrics.getAnalysisLatencyP99()).thenReturn(500.0);
        when(metrics.getProjectCreationLatencyP95()).thenReturn(200.0);
        when(metrics.getErrorRate()).thenReturn(0.005);
        assertThat(validator.isDegraded()).isFalse();
    }
    @Test void isDegradedTrue() {
        when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);
        when(metrics.getAnalysisLatencyP99()).thenReturn(1500.0);
        when(metrics.getProjectCreationLatencyP95()).thenReturn(400.0);
        when(metrics.getErrorRate()).thenReturn(0.05);
        assertThat(validator.isDegraded()).isTrue();
    }
    @Test void healthStatusHealthy() {
        when(metrics.getAnalysisLatencyP95()).thenReturn(100.0);
        when(metrics.getAnalysisLatencyP99()).thenReturn(500.0);
        when(metrics.getProjectCreationLatencyP95()).thenReturn(200.0);
        when(metrics.getErrorRate()).thenReturn(0.005);
        assertThat(validator.getHealthStatus()).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
    }
    @Test void healthStatusUnhealthy() {
        when(metrics.getAnalysisLatencyP95()).thenReturn(600.0);
        when(metrics.getAnalysisLatencyP99()).thenReturn(1500.0);
        when(metrics.getProjectCreationLatencyP95()).thenReturn(400.0);
        when(metrics.getErrorRate()).thenReturn(0.05);
        assertThat(validator.getHealthStatus()).isEqualTo(ThresholdValidator.HealthStatus.UNHEALTHY);
    }
    @Test void complianceReportCheckResult() {
        var cr = new ThresholdValidator.ComplianceReport();
        cr.addCheck("Test", 50.0, 100.0, true, "ms");
        assertThat(cr.getChecks()).hasSize(1);
        var check = cr.getChecks().get(0);
        assertThat(check.getVariance()).isEqualTo(-50.0);
        assertThat(check.getVariancePercent()).isEqualTo(-50.0);
    }
    @Test void thresholdTypeEnum() { assertThat(ThresholdValidator.ThresholdType.values()).hasSize(4); }
    @Test void healthStatusEnum() { assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3); }
}
