package com.gogidix.aiservices.anomalydetectionservice.governance;

import com.gogidix.aiservices.anomalydetectionservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.anomalydetectionservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.anomalydetectionservice.infrastructure.metrics.AnomalyDetectionMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Financial-Grade Governance Tests for Anomaly Detection Service.
 */
@DisplayName("Anomaly Detection Governance Tests")
class GovernanceComplianceTest {

    private AnomalyDetectionMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        metrics = new AnomalyDetectionMetrics(meterRegistry);
        thresholdValidator = new ThresholdValidator(metrics);
        reportGenerator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }

    @Test
    @DisplayName("Should validate all thresholds successfully")
    void shouldValidateAllThresholds() {
        ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

        assertThat(report).isNotNull();
        assertThat(report.getCheckCount()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should return healthy status when all thresholds pass")
    void shouldReturnHealthyStatusWhenAllThresholdsPass() {
        ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

        assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
    }

    @Test
    @DisplayName("Should generate governance report")
    void shouldGenerateGovernanceReport() {
        ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

        assertThat(report).isNotNull();
        assertThat(report.getReportId()).isNotNull();
        assertThat(report.getReportId()).startsWith("GOV-");
    }

    @Test
    @DisplayName("Should include SLO compliance section")
    void shouldIncludeSloComplianceSection() {
        ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

        assertThat(report.getSloCompliance()).isNotNull();
    }

    @Test
    @DisplayName("Should include performance metrics section")
    void shouldIncludePerformanceMetricsSection() {
        ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

        assertThat(report.getPerformanceMetrics()).isNotNull();
    }
}
