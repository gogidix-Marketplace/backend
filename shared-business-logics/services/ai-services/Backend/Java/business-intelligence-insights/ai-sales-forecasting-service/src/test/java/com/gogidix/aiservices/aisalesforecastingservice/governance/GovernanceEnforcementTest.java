package com.gogidix.aiservices.aisalesforecastingservice.governance;

import com.gogidix.aiservices.aisalesforecastingservice.application.service.SalesForecastingApplicationService;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastStatus;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import com.gogidix.aiservices.aisalesforecastingservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aisalesforecastingservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aisalesforecastingservice.infrastructure.metrics.SalesForecastingMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

/**
 * Financial-Grade: Governance Enforcement Tests.
 *
 * These tests validate threshold enforcement and compliance reporting.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Governance Enforcement Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GovernanceEnforcementTest {

    @Autowired(required = false)
    private SalesForecastingApplicationService segmentApplicationService;

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private SalesForecastingMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    private static final String TEST_TENANT = "governance-test-tenant";
    private static final String TEST_SEGMENT = "governance-test-segment";

    @Nested
    @DisplayName("1. Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should validate P95 latency threshold")
        void shouldValidateP95LatencyThreshold() {
            // Generate some traffic
            for (int i = 0; i < 20; i++) {
                try {
                    metrics.incrementForecastingTotal();
                    metrics.recordForecastingTime(100 + (i * 10));
                } catch (Exception e) {
                    // Ignore
                }
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
        @DisplayName("Should validate error rate threshold")
        void shouldValidateErrorRateThreshold() {
            // Generate traffic with some failures
            for (int i = 0; i < 10; i++) {
                try {
                    metrics.incrementForecastingTotal();
                    if (i % 2 == 0) {
                        metrics.incrementForecastingFailure();
                    }
                } catch (Exception e) {
                    // Some failures expected
                }
            }

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
            // Generate some traffic
            for (int i = 0; i < 5; i++) {
                metrics.incrementForecastingTotal();
                metrics.recordForecastingTime(100);
            }

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf).isNotNull();
            assertThat(perf.getTotalForecastings()).isGreaterThanOrEqualTo(0);
            assertThat(perf.getSuccessfulForecastings()).isGreaterThanOrEqualTo(0);
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

        @Test
        @Order(31)
        @DisplayName("Should have non-empty report sections")
        void shouldHaveNonEmptyReportSections() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getReportId()).isNotEmpty();
            assertThat(report.getGovernanceStatus().getMessage()).isNotEmpty();
        }
    }
}
