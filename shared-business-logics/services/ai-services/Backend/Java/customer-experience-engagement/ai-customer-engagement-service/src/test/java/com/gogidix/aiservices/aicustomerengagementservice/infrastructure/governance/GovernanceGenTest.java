package com.gogidix.aiservices.aicustomerengagementservice.infrastructure.governance;

import com.gogidix.aiservices.aicustomerengagementservice.infrastructure.metrics.CustomerEngagementMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class GovernanceGenTest {
    private CustomerEngagementMetrics metrics;
    private ThresholdValidator validator;
    private ComplianceReportGenerator generator;

    @BeforeEach
    void setup() {
        metrics = new CustomerEngagementMetrics(new SimpleMeterRegistry());
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
    void isDegraded() { assertThat(validator.isDegraded()).isFalse(); }

    @Test
    void healthStatus() { assertThat(validator.getHealthStatus()).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY); }

    @Test
    void validate_P95_LATENCY() {
        var r = validator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);
        assertThat(r).isNotNull();
    }

    @Test
    void validate_P99_LATENCY() {
        var r = validator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);
        assertThat(r).isNotNull();
    }

    @Test
    void validate_ML_PREDICTION_LATENCY() {
        var r = validator.validateThreshold(ThresholdValidator.ThresholdType.ML_PREDICTION_LATENCY);
        assertThat(r).isNotNull();
    }

    @Test
    void validate_ERROR_RATE() {
        var r = validator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);
        assertThat(r).isNotNull();
    }


    @Test
    void generateReport() {
        var report = generator.generateReport();
        assertThat(report.getReportId()).startsWith("GOV-");
        assertThat(report.getSloCompliance()).isNotNull();
    }

    @Test
    void reportToMap() {
        var map = generator.generateReport().toMap();
        assertThat(map).containsKey("reportId");
    }

    @Test
    void checkResultVariance() {
        var cr = new ThresholdValidator.ComplianceReport();
        cr.addCheck("t", 600, 500, false, "ms");
        var ch = cr.getChecks().get(0);
        assertThat(ch.getVariance()).isEqualTo(100.0);
    }

    @Test
    void enums() {
        assertThat(ThresholdValidator.ThresholdType.values()).hasSize(4);
        assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3);
    }
}
