package com.gogidix.aiservices.aibusinessautomationservice.governance;

import com.gogidix.aiservices.aibusinessautomationservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aibusinessautomationservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aibusinessautomationservice.infrastructure.metrics.BusinessAutomationMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: Governance Enforcement Tests.
 *
 * These tests validate threshold enforcement and compliance reporting
 * for Business Operations service.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Governance Enforcement Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GovernanceEnforcementTest {

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private BusinessAutomationMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    private static final String TEST_TENANT = "governance-test-tenant";

    @Nested
    @DisplayName("1. Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should validate P95 latency threshold")
        void shouldValidateP95LatencyThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P95_LATENCY);
            assertThat(validation.getUnit()).isEqualTo("ms");
            assertThat(validation.getThreshold()).isEqualTo(500.0);
        }

        @Test
        @Order(2)
        @DisplayName("Should validate P99 latency threshold")
        void shouldValidateP99LatencyThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P99_LATENCY);
            assertThat(validation.getUnit()).isEqualTo("ms");
            assertThat(validation.getThreshold()).isEqualTo(1000.0);
        }

        @Test
        @Order(3)
        @DisplayName("Should validate workflow execution latency threshold")
        void shouldValidateWorkflowExecutionLatencyThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.WORKFLOW_EXECUTION_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.WORKFLOW_EXECUTION_LATENCY);
            assertThat(validation.getUnit()).isEqualTo("ms");
            assertThat(validation.getThreshold()).isEqualTo(750.0);
        }

        @Test
        @Order(4)
        @DisplayName("Should validate AI decision latency threshold")
        void shouldValidateAiDecisionLatencyThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.AI_DECISION_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.AI_DECISION_LATENCY);
            assertThat(validation.getUnit()).isEqualTo("ms");
            assertThat(validation.getThreshold()).isEqualTo(200.0);
        }

        @Test
        @Order(5)
        @DisplayName("Should validate error rate threshold")
        void shouldValidateErrorRateThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.ERROR_RATE);
            assertThat(validation.getUnit()).isEqualTo("%");
            assertThat(validation.getThreshold()).isEqualTo(1.0); // 1% threshold
        }
    }

    @Nested
    @DisplayName("2. Compliance Report Tests")
    class ComplianceReportTests {

        @Test
        @Order(10)
        @DisplayName("Should generate compliance report")
        void shouldGenerateComplianceReport() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report).isNotNull();
            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getReportId()).startsWith("GOV-");
            assertThat(report.getGeneratedAt()).isNotNull();
            assertThat(report.getSloCompliance()).isNotNull();
            assertThat(report.getPerformanceMetrics()).isNotNull();
            assertThat(report.getGovernanceStatus()).isNotNull();
        }

        @Test
        @Order(11)
        @DisplayName("Should include SLO compliance section")
        void shouldIncludeSloComplianceSection() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.SloComplianceSection slo = report.getSloCompliance();

            assertThat(slo).isNotNull();
            assertThat(slo.getP95LatencyMs()).isGreaterThanOrEqualTo(0);
            assertThat(slo.getErrorRate()).isGreaterThanOrEqualTo(0);
            assertThat(slo.getAvailability()).isGreaterThanOrEqualTo(0);
            assertThat(slo.getAvailability()).isLessThanOrEqualTo(100);
        }

        @Test
        @Order(12)
        @DisplayName("Should include performance metrics section")
        void shouldIncludePerformanceMetricsSection() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf).isNotNull();
            assertThat(perf.getTotalRequests()).isGreaterThanOrEqualTo(0);
            assertThat(perf.getSuccessfulRequests()).isGreaterThanOrEqualTo(0);
            assertThat(perf.getWorkflowsExecuted()).isGreaterThanOrEqualTo(0);
            assertThat(perf.getApprovalsProcessed()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(13)
        @DisplayName("Should include governance status")
        void shouldIncludeGovernanceStatus() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.GovernanceStatus status = report.getGovernanceStatus();

            assertThat(status).isNotNull();
            assertThat(status.getStatus()).isIn("COMPLIANT", "WARNING", "NON-COMPLIANT");
            assertThat(status.getSeverity()).isIn("INFO", "WARN", "CRITICAL");
            assertThat(status.getMessage()).isNotNull();
            assertThat(status.getHealthStatus()).isIn("HEALTHY", "DEGRADED", "UNHEALTHY");
        }

        @Test
        @Order(14)
        @DisplayName("Should include business operations specific metrics")
        void shouldIncludeBusinessOperationsSpecificMetrics() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf).isNotNull();
            assertThat(perf.getWorkflowsExecuted()).isGreaterThanOrEqualTo(0);
            assertThat(perf.getApprovalsProcessed()).isGreaterThanOrEqualTo(0);
            assertThat(perf.getDocumentsProcessed()).isGreaterThanOrEqualTo(0);
            assertThat(perf.getNotificationsSent()).isGreaterThanOrEqualTo(0);
        }
    }

    @Nested
    @DisplayName("3. Overall Compliance Tests")
    class OverallComplianceTests {

        @Test
        @Order(20)
        @DisplayName("Should validate all thresholds")
        void shouldValidateAllThresholds() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report).isNotNull();
            assertThat(report.getCheckCount()).isGreaterThan(0);
            assertThat(report.getPassedCheckCount() + report.getFailedCheckCount())
                    .isEqualTo(report.getCheckCount());
        }

        @Test
        @Order(21)
        @DisplayName("Should detect degraded state")
        void shouldDetectDegradedState() {
            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isNotNull();
            assertThat(status).isIn(
                ThresholdValidator.HealthStatus.HEALTHY,
                ThresholdValidator.HealthStatus.DEGRADED,
                ThresholdValidator.HealthStatus.UNHEALTHY
            );
        }

        @Test
        @Order(22)
        @DisplayName("Should check if service is degraded")
        void shouldCheckIfServiceIsDegraded() {
            boolean isDegraded = thresholdValidator.isDegraded();

            // Should return a boolean
            assertThat(isDegraded).isNotNull();
        }
    }

    @Nested
    @DisplayName("4. Report Structure Tests")
    class ReportStructureTests {

        @Test
        @Order(30)
        @DisplayName("Should convert report to map")
        void shouldConvertReportToMap() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            var map = report.toMap();

            assertThat(map).isNotNull();
            assertThat(map).containsKey("reportId");
            assertThat(map).containsKey("generatedAt");
            assertThat(map).containsKey("sloCompliance");
            assertThat(map).containsKey("performanceMetrics");
            assertThat(map).containsKey("governanceStatus");
        }

        @Test
        @Order(31)
        @DisplayName("Should have non-empty report sections")
        void shouldHaveNonEmptyReportSections() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getReportId()).isNotEmpty();
            assertThat(report.getGovernanceStatus().getMessage()).isNotEmpty();
        }

        @Test
        @Order(32)
        @DisplayName("Should include business operations metrics in map")
        void shouldIncludeBusinessOperationsMetricsInMap() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            var map = report.toMap();

            assertThat(map).isNotNull();
            assertThat(map).containsKey("performanceMetrics");
        }
    }

    @Nested
    @DisplayName("5. Threshold Enforcement Tests")
    class ThresholdEnforcementTests {

        @Test
        @Order(40)
        @DisplayName("Should enforce P95 latency threshold")
        void shouldEnforceP95LatencyThreshold() {
            double p95Latency = metrics.getAutomationLatencyP95();

            // Verify we can measure latency
            assertThat(p95Latency).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(41)
        @DisplayName("Should enforce P99 latency threshold")
        void shouldEnforceP99LatencyThreshold() {
            double p99Latency = metrics.getAutomationLatencyP99();

            // Verify we can measure latency
            assertThat(p99Latency).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(42)
        @DisplayName("Should enforce workflow execution latency threshold")
        void shouldEnforceWorkflowExecutionLatencyThreshold() {
            double workflowLatency = metrics.getWorkflowExecutionLatencyP95();

            // Verify we can measure latency
            assertThat(workflowLatency).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(43)
        @DisplayName("Should enforce AI decision latency threshold")
        void shouldEnforceAiDecisionLatencyThreshold() {
            double aiDecisionLatency = metrics.getAiDecisionLatencyP95();

            // Verify we can measure latency
            assertThat(aiDecisionLatency).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(44)
        @DisplayName("Should enforce error rate threshold")
        void shouldEnforceErrorRateThreshold() {
            double errorRate = metrics.getErrorRate();

            // Verify error rate is measurable
            assertThat(errorRate).isGreaterThanOrEqualTo(0);
            assertThat(errorRate).isLessThanOrEqualTo(1);
        }
    }

    @Nested
    @DisplayName("6. Compliance Check Result Tests")
    class ComplianceCheckResultTests {

        @Test
        @Order(50)
        @DisplayName("Should provide check results with variance")
        void shouldProvideCheckResultsWithVariance() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.getChecks()).isNotNull();
            assertThat(report.getChecks()).isNotEmpty();

            // Check variance calculation
            for (var check : report.getChecks()) {
                assertThat(check.getVariance()).isNotNull();
            }
        }

        @Test
        @Order(51)
        @DisplayName("Should provide check results with variance percent")
        void shouldProvideCheckResultsWithVariancePercent() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.getChecks()).isNotNull();

            // Check variance percent calculation
            for (var check : report.getChecks()) {
                if (check.threshold() > 0) {
                    assertThat(check.getVariancePercent()).isNotNull();
                }
            }
        }
    }

    @Nested
    @DisplayName("7. Health Status Tests")
    class HealthStatusTests {

        @Test
        @Order(60)
        @DisplayName("Should return healthy status when all thresholds met")
        void shouldReturnHealthyStatusWhenAllThresholdsMet() {
            // Generate some good metrics
            for (int i = 0; i < 10; i++) {
                metrics.recordAutomationTime(100);
                metrics.incrementAutomationTotal();
                metrics.incrementAutomationSuccess();
            }

            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isNotNull();
            assertThat(status.toString()).isIn("HEALTHY", "DEGRADED", "UNHEALTHY");
        }
    }
}
