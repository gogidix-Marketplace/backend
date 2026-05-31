package com.gogidix.aiservices.aifeatureextractionservice.infrastructure.governance;

import com.gogidix.aiservices.aifeatureextractionservice.infrastructure.metrics.FeatureExtractionMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class GovernanceTest {

    private FeatureExtractionMetrics metrics;
    private ThresholdValidator validator;
    private ComplianceReportGenerator generator;

    @BeforeEach
    void setup() {
        var registry = new SimpleMeterRegistry();
        metrics = new FeatureExtractionMetrics(registry);
        validator = new ThresholdValidator(metrics);
        generator = new ComplianceReportGenerator(metrics, validator);
    }

    @Test
    void thresholdValidator_allPass_withNoTraffic() {
        var report = validator.validateAllThresholds();
        assertThat(report.isCompliant()).isTrue();
        assertThat(report.getCheckCount()).isEqualTo(4);
        assertThat(report.getPassedCheckCount()).isEqualTo(4);
    }

    @Test
    void thresholdValidator_validateIndividualP95() {
        var result = validator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);
        assertThat(result.isPassed()).isTrue();
        assertThat(result.getType()).isEqualTo(ThresholdValidator.ThresholdType.P95_LATENCY);
    }

    @Test
    void thresholdValidator_validateIndividualErrorRate() {
        var result = validator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);
        assertThat(result.isPassed()).isTrue();
    }

    @Test
    void thresholdValidator_isDegraded_noTraffic() {
        assertThat(validator.isDegraded()).isFalse();
    }

    @Test
    void thresholdValidator_healthStatus_noTraffic() {
        assertThat(validator.getHealthStatus()).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
    }

    @Test
    void complianceReport_generateReport() {
        metrics.incrementExtractionTotal();
        metrics.incrementExtractionSuccess();
        var report = generator.generateReport();
        assertThat(report.getReportId()).startsWith("GOV-");
        assertThat(report.getSloCompliance()).isNotNull();
        assertThat(report.getPerformanceMetrics()).isNotNull();
        assertThat(report.getGovernanceStatus()).isNotNull();
        assertThat(report.getGeneratedAt()).isNotNull();
    }

    @Test
    void complianceReport_toMap() {
        metrics.incrementExtractionTotal();
        var report = generator.generateReport();
        var map = report.toMap();
        assertThat(map).containsKey("reportId");
        assertThat(map).containsKey("sloCompliance");
        assertThat(map).containsKey("performanceMetrics");
        assertThat(map).containsKey("governanceStatus");
    }

    @Test
    void checkResult_varianceCalculations() {
        var cr = new ThresholdValidator.ComplianceReport();
        cr.addCheck("test", 600, 500, false, "ms");
        var check = cr.getChecks().get(0);
        assertThat(check.getVariance()).isEqualTo(100.0);
        assertThat(check.getVariancePercent()).isEqualTo(20.0);
    }

    @Test
    void allThresholdTypes() {
        assertThat(ThresholdValidator.ThresholdType.values()).hasSize(4);
    }

    @Test
    void allHealthStatuses() {
        assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3);
    }
}
