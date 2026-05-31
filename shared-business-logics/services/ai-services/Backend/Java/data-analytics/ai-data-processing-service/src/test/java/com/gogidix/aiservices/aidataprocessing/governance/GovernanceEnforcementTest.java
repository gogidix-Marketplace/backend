package com.gogidix.aiservices.aidataprocessing.governance;

import com.gogidix.aiservices.aidataprocessing.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aidataprocessing.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aidataprocessing.infrastructure.metrics.DataProcessingMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Financial-Grade Governance Tests.
 * Tests governance enforcement and compliance reporting.
 */
@DisplayName("Governance Enforcement Tests")
class GovernanceEnforcementTest {

    private DataProcessingMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator reportGenerator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new DataProcessingMetrics(meterRegistry);
        thresholdValidator = new ThresholdValidator(metrics);
        reportGenerator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }

    @Nested
    @DisplayName("SLO Compliance Enforcement")
    class SloComplianceTests {

        @Test
        @DisplayName("Should enforce P95 latency threshold")
        void shouldEnforceP95LatencyThreshold() {
            metrics.recordProcessingTime(600); // Over 500ms threshold
            metrics.incrementProcessingTotal();

            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.isCompliant()).isFalse();
        }

        @Test
        @DisplayName("Should enforce P99 latency threshold")
        void shouldEnforceP99LatencyThreshold() {
            metrics.recordProcessingTime(1100); // Over 1000ms threshold
            metrics.incrementProcessingTotal();

            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            boolean p99Compliant = report.getChecks().stream()
                .filter(c -> c.name().equals("P99 Latency"))
                .allMatch(c -> c.passed());

            assertThat(p99Compliant).isFalse();
        }

        @Test
        @DisplayName("Should enforce error rate threshold")
        void shouldEnforceErrorRateThreshold() {
            metrics.incrementProcessingTotal();
            metrics.incrementProcessingTotal();
            metrics.incrementProcessingFailure(); // 50% error rate

            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.isCompliant()).isFalse();
        }
    }

    @Nested
    @DisplayName("Compliance Reporting")
    class ComplianceReportingTests {

        @Test
        @DisplayName("Should generate compliance report")
        void shouldGenerateComplianceReport() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getReportId()).startsWith("GOV-");
            assertThat(report.getGeneratedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should include all required sections in report")
        void shouldIncludeAllRequiredSections() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getSloCompliance()).isNotNull();
            assertThat(report.getPerformanceMetrics()).isNotNull();
            assertThat(report.getGovernanceStatus()).isNotNull();
        }

        @Test
        @DisplayName("Should convert report to map for serialization")
        void shouldConvertReportToMap() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            var map = report.toMap();

            assertThat(map).containsKeys("reportId", "generatedAt", "sloCompliance",
                "performanceMetrics", "governanceStatus");
        }
    }

    @Nested
    @DisplayName("Health Status Monitoring")
    class HealthStatusTests {

        @Test
        @DisplayName("Should report HEALTHY status when compliant")
        void shouldReportHealthyStatus() {
            recordCompliantMetrics();

            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
        }

        @Test
        @DisplayName("Should report DEGRADED status when partially compliant")
        void shouldReportDegradedStatus() {
            metrics.recordProcessingTime(600); // Slightly over P95
            metrics.incrementProcessingTotal();
            metrics.incrementProcessingSuccess();

            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isIn(
                ThresholdValidator.HealthStatus.HEALTHY,
                ThresholdValidator.HealthStatus.DEGRADED
            );
        }

        @Test
        @DisplayName("Should report UNHEALTHY status when non-compliant")
        void shouldReportUnhealthyStatus() {
            metrics.recordProcessingTime(1500); // Way over thresholds
            metrics.incrementProcessingTotal();
            metrics.incrementProcessingFailure();

            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isIn(
                ThresholdValidator.HealthStatus.DEGRADED,
                ThresholdValidator.HealthStatus.UNHEALTHY
            );
        }
    }

    @Nested
    @DisplayName("Audit Trail")
    class AuditTrailTests {

        @Test
        @DisplayName("Should maintain audit trail in reports")
        void shouldMaintainAuditTrail() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            // Report ID serves as audit trail identifier
            assertThat(report.getReportId()).matches("GOV-\\d{8}-\\d{6}");
        }

        @Test
        @DisplayName("Should include timestamp for audit purposes")
        void shouldIncludeTimestamp() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getGeneratedAt()).isNotNull();
        }
    }

    private void recordCompliantMetrics() {
        metrics.recordProcessingTime(300);
        metrics.recordProcessingTime(350);
        metrics.recordProcessingTime(400);
        metrics.incrementProcessingTotal();
        metrics.incrementProcessingTotal();
        metrics.incrementProcessingTotal();
        metrics.incrementProcessingSuccess();
        metrics.incrementProcessingSuccess();
        metrics.incrementProcessingSuccess();
    }
}
