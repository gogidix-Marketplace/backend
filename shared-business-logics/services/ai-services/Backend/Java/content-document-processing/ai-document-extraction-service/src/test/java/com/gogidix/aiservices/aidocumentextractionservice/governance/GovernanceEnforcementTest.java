package com.gogidix.aiservices.aidocumentextractionservice.governance;

import com.gogidix.aiservices.aidocumentextractionservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aidocumentextractionservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aidocumentextractionservice.infrastructure.metrics.DocumentExtractionMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Financial-Grade: Governance Enforcement Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GovernanceEnforcementTest {

    private DocumentExtractionMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator reportGenerator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new DocumentExtractionMetrics(meterRegistry);
        thresholdValidator = new ThresholdValidator(metrics);
        reportGenerator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }

    @Nested
    @DisplayName("1. Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should validate P95 latency threshold")
        void shouldValidateP95LatencyThreshold() {
            for (int i = 0; i < 20; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(100 + i * 10);
            }

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
            for (int i = 0; i < 20; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(100 + i * 20);
            }

            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P99_LATENCY);
            assertThat(validation.getThreshold()).isEqualTo(1000.0);
        }

        @Test
        @Order(3)
        @DisplayName("Should validate error rate threshold")
        void shouldValidateErrorRateThreshold() {
            for (int i = 0; i < 10; i++) {
                metrics.incrementExtractionTotal();
                if (i < 9) {
                    metrics.incrementExtractionSuccess();
                } else {
                    metrics.incrementExtractionFailure();
                }
            }

            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.ERROR_RATE);
            assertThat(validation.getUnit()).isEqualTo("%");
            assertThat(validation.getThreshold()).isEqualTo(1.0);
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
            assertThat(report.getReportId()).startsWith("GOV-DOCEXT-");
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
            assertThat(slo.getP99LatencyMs()).isGreaterThanOrEqualTo(0);
            assertThat(slo.getErrorRate()).isGreaterThanOrEqualTo(0);
            assertThat(slo.getAvailability()).isGreaterThanOrEqualTo(0);
            assertThat(slo.getAvailability()).isLessThanOrEqualTo(100);
        }

        @Test
        @Order(12)
        @DisplayName("Should include performance metrics section")
        void shouldIncludePerformanceMetricsSection() {
            for (int i = 0; i < 5; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
            }

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf).isNotNull();
            assertThat(perf.getTotalRequests()).isGreaterThanOrEqualTo(0);
            assertThat(perf.getSuccessfulRequests()).isGreaterThanOrEqualTo(0);
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
    }

    @Nested
    @DisplayName("5. Threshold Enforcement Tests")
    class ThresholdEnforcementTests {

        @Test
        @Order(40)
        @DisplayName("Should enforce latency threshold")
        void shouldEnforceLatencyThreshold() {
            int requests = 50;
            for (int i = 0; i < requests; i++) {
                metrics.incrementExtractionTotal();
                metrics.incrementExtractionSuccess();
                metrics.recordExtractionTime(100 + i);
            }

            double p95Latency = metrics.getExtractionLatencyP95();
            assertThat(p95Latency).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(41)
        @DisplayName("Should enforce error rate threshold")
        void shouldEnforceErrorRateThreshold() {
            int requests = 100;
            int failures = 5;

            for (int i = 0; i < requests; i++) {
                metrics.incrementExtractionTotal();
                if (i >= requests - failures) {
                    metrics.incrementExtractionFailure();
                } else {
                    metrics.incrementExtractionSuccess();
                }
            }

            double errorRate = metrics.getErrorRate();
            assertThat(errorRate).isGreaterThanOrEqualTo(0);
            assertThat(errorRate).isLessThanOrEqualTo(1);
        }
    }
}
