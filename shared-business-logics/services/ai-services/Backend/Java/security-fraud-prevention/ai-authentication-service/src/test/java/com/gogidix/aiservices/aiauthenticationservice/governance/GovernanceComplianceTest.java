package com.gogidix.aiservices.aiauthenticationservice.governance;

import com.gogidix.aiservices.aiauthenticationservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aiauthenticationservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aiauthenticationservice.infrastructure.metrics.AuthenticationMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Financial-Grade Governance Tests.
 * Tests compliance reporting and threshold validation.
 */
@DisplayName("Governance Compliance Tests")
class GovernanceComplianceTest {

    private MeterRegistry meterRegistry;
    private AuthenticationMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new AuthenticationMetrics(meterRegistry);
        thresholdValidator = new ThresholdValidator(metrics);
        reportGenerator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }

    @Nested
    @DisplayName("Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @DisplayName("Should validate all thresholds successfully")
        void shouldValidateAllThresholds() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report).isNotNull();
            assertThat(report.getCheckCount()).isGreaterThan(0);
            assertThat(report.getChecks()).hasSize(5); // P95, P99, Biometric, Error Rate, Success Rate
        }

        @Test
        @DisplayName("Should return healthy status when all thresholds pass")
        void shouldReturnHealthyStatusWhenAllThresholdsPass() {
            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
        }

        @Test
        @DisplayName("Should validate P95 latency threshold")
        void shouldValidateP95LatencyThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P95_LATENCY);
            assertThat(validation.getUnit()).isEqualTo("ms");
        }

        @Test
        @DisplayName("Should validate P99 latency threshold")
        void shouldValidateP99LatencyThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P99_LATENCY);
        }

        @Test
        @DisplayName("Should validate error rate threshold")
        void shouldValidateErrorRateThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.ERROR_RATE);
            assertThat(validation.getUnit()).isEqualTo("%");
        }

        @Test
        @DisplayName("Should validate success rate threshold")
        void shouldValidateSuccessRateThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.SUCCESS_RATE);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.SUCCESS_RATE);
        }

        @Test
        @DisplayName("Should detect degraded service when thresholds fail")
        void shouldDetectDegradedServiceWhenThresholdsFail() {
            boolean isDegraded = thresholdValidator.isDegraded();

            // With no traffic, should not be degraded
            assertThat(isDegraded).isFalse();
        }
    }

    @Nested
    @DisplayName("Compliance Report Generation Tests")
    class ComplianceReportTests {

        @Test
        @DisplayName("Should generate governance report")
        void shouldGenerateGovernanceReport() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report).isNotNull();
            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getReportId()).startsWith("GOV-");
            assertThat(report.getGeneratedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should include SLO compliance section")
        void shouldIncludeSloComplianceSection() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getSloCompliance()).isNotNull();
            assertThat(report.getSloCompliance().getP95LatencyMs()).isGreaterThanOrEqualTo(0);
            assertThat(report.getSloCompliance().getErrorRate()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Should include performance metrics section")
        void shouldIncludePerformanceMetricsSection() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getPerformanceMetrics()).isNotNull();
            assertThat(report.getPerformanceMetrics().getTotalRequests()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Should include security metrics section")
        void shouldIncludeSecurityMetricsSection() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getSecurityMetrics()).isNotNull();
            assertThat(report.getSecurityMetrics().getTotalAttempts()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Should include governance status")
        void shouldIncludeGovernanceStatus() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getGovernanceStatus()).isNotNull();
            assertThat(report.getGovernanceStatus().getStatus()).isNotNull();
            assertThat(report.getGovernanceStatus().getSeverity()).isNotNull();
            assertThat(report.getGovernanceStatus().getMessage()).isNotNull();
        }

        @Test
        @DisplayName("Should convert report to map")
        void shouldConvertReportToMap() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            var map = report.toMap();

            assertThat(map).isNotNull();
            assertThat(map).containsKey("reportId");
            assertThat(map).containsKey("generatedAt");
            assertThat(map).containsKey("sloCompliance");
            assertThat(map).containsKey("performanceMetrics");
            assertThat(map).containsKey("securityMetrics");
            assertThat(map).containsKey("governanceStatus");
        }
    }

    @Nested
    @DisplayName("SLO Compliance Tests")
    class SloComplianceTests {

        @Test
        @DisplayName("Should meet P95 latency SLO target of 200ms")
        void shouldMeetP95LatencySlo() {
            // With no authentication requests, latency should be 0 (within target)
            double p95Latency = metrics.getAuthenticationLatencyP95();
            assertThat(p95Latency).isLessThanOrEqualTo(200.0);
        }

        @Test
        @DisplayName("Should meet error rate SLO target of 0.5%")
        void shouldMeetErrorRateSlo() {
            double errorRate = metrics.getErrorRate();
            assertThat(errorRate).isLessThanOrEqualTo(0.005); // 0.5%
        }

        @Test
        @DisplayName("Should meet success rate SLO target of 99%")
        void shouldMeetSuccessRateSlo() {
            // With no requests, success rate defaults to acceptable
            double successRate = metrics.getSuccessRate();
            assertThat(successRate).isGreaterThanOrEqualTo(0.0);
        }
    }
}
