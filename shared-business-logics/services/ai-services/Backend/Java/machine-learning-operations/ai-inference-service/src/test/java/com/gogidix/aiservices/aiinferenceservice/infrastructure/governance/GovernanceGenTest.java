package com.gogidix.aiservices.aiinferenceservice.infrastructure.governance;

import com.gogidix.aiservices.aiinferenceservice.infrastructure.metrics.InferenceMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class GovernanceGenTest {

    private InferenceMetrics metrics;
    private ThresholdValidator validator;
    private ComplianceReportGenerator generator;

    @BeforeEach
    void setup() {
        metrics = new InferenceMetrics(new SimpleMeterRegistry());
        validator = new ThresholdValidator(metrics);
        generator = new ComplianceReportGenerator(metrics, validator);
    }

    @Test
    void validateAll_compliant() {
        var report = validator.validateAllThresholds();
        assertThat(report.isCompliant()).isTrue();
        assertThat(report.getCheckCount()).isEqualTo(4);
    }

    @Test
    void isDegraded() {
        assertThat(validator.isDegraded()).isFalse();
    }

    @Test
    void healthStatus() {
        assertThat(validator.getHealthStatus()).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
    }

    @Test
    void validate_P95_LATENCY() {
        var result = validator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);
        assertThat(result).isNotNull();
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void validate_P99_LATENCY() {
        var result = validator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);
        assertThat(result).isNotNull();
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void validate_PREDICTION_LATENCY() {
        var result = validator.validateThreshold(ThresholdValidator.ThresholdType.PREDICTION_LATENCY);
        assertThat(result).isNotNull();
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void validate_ERROR_RATE() {
        var result = validator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);
        assertThat(result).isNotNull();
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void generateReport() {
        var report = generator.generateReport();
        assertThat(report.getReportId()).startsWith("GOV-");
        assertThat(report.getSloCompliance()).isNotNull();
        assertThat(report.getPerformanceMetrics()).isNotNull();
        assertThat(report.getGovernanceStatus()).isNotNull();
    }

    @Test
    void reportToMap() {
        var report = generator.generateReport();
        var map = report.toMap();
        assertThat(map).containsKey("reportId");
    }

    @Test
    void checkResultVariance() {
        var cr = new ThresholdValidator.ComplianceReport();
        cr.addCheck("t", 600, 500, false, "ms");
        var check = cr.getChecks().get(0);
        assertThat(check.getVariance()).isEqualTo(100.0);
        assertThat(check.getVariancePercent()).isEqualTo(20.0);
    }

    @Test
    void enums() {
        assertThat(ThresholdValidator.ThresholdType.values()).hasSize(4);
        assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3);
    }
}
