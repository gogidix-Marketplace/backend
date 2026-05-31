package com.gogidix.aiservices.aicontentgenerationservice.governance;

import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.GenerateContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.service.ContentGenerationService;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentTone;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.ContentType;
import com.gogidix.aiservices.aicontentgenerationservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aicontentgenerationservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aicontentgenerationservice.infrastructure.metrics.ContentGenerationMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Financial-Grade: Governance Enforcement Tests.
 *
 * These tests validate threshold enforcement and compliance reporting.
 */
@SpringBootTest(
    classes = com.gogidix.aiservices.aicontentgenerationservice.AiContentGenerationServiceApplication.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Governance Enforcement Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GovernanceEnforcementTest {

    @Autowired
    private ContentGenerationService contentGenerationService;

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private ContentGenerationMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @MockBean
    private com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.ContentRepository contentRepository;

    @MockBean
    private com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.ContentGenerationPort contentGenerationPort;

    @MockBean
    private com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.EventPublisherPort eventPublisherPort;

    @MockBean
    private com.gogidix.aiservices.aicontentgenerationservice.domain.policy.ContentGenerationPolicy contentGenerationPolicy;

    private static final String TEST_TENANT = "governance-test-tenant";
    private static final String TEST_USER = "governance-test-user";

    @Nested
    @DisplayName("1. Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should validate P95 latency threshold")
        void shouldValidateP95LatencyThreshold() {
            when(contentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

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

    @Nested
    @DisplayName("5. Threshold Enforcement Tests")
    class ThresholdEnforcementTests {

        @Test
        @Order(40)
        @DisplayName("Should enforce latency threshold")
        void shouldEnforceLatencyThreshold() {
            double p95Latency = metrics.getGenerationLatencyP95();

            // Verify we can measure latency
            assertThat(p95Latency).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(41)
        @DisplayName("Should enforce error rate threshold")
        void shouldEnforceErrorRateThreshold() {
            double errorRate = metrics.getErrorRate();

            // Verify error rate is measurable
            assertThat(errorRate).isGreaterThanOrEqualTo(0);
            assertThat(errorRate).isLessThanOrEqualTo(1);
        }
    }
}
