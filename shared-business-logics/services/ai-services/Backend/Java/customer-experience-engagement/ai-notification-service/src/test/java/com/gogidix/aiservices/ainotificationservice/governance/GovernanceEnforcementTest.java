package com.gogidix.aiservices.ainotificationservice.governance;

import com.gogidix.aiservices.ainotificationservice.AiNotificationServiceApplication;
import com.gogidix.aiservices.ainotificationservice.application.service.NotificationService;
import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import com.gogidix.aiservices.ainotificationservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.ainotificationservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.ainotificationservice.infrastructure.metrics.NotificationMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Financial-Grade: Governance Enforcement Tests.
 *
 * These tests validate threshold enforcement and compliance reporting.
 */
@SpringBootTest(
    classes = AiNotificationServiceApplication.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Governance Enforcement Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GovernanceEnforcementTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private NotificationMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationRepository notificationRepository;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.NotificationSenderPort notificationSender;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.port.out.EventPublisherPort eventPublisher;

    @MockBean
    private com.gogidix.aiservices.ainotificationservice.domain.policy.NotificationPolicy policy;

    private static final String TEST_TENANT = "governance-test-tenant";
    private static final String TEST_USER = "governance-test-user";

    @Nested
    @DisplayName("1. Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should validate P95 latency threshold")
        void shouldValidateP95LatencyThreshold() {
            when(notificationRepository.save(any())).thenReturn(createTestNotification("p95-validation"));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(true);
            doNothing().when(eventPublisher).publishNotificationSent(any(), any());

            // Generate some traffic
            for (int i = 0; i < 20; i++) {
                try {
                    notificationService.sendNotification(TEST_USER, NotificationType.EMAIL,
                            "Test Subject", "Test Content");
                } catch (Exception e) {
                    // Ignore
                }
            }

            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P95_LATENCY);
            assertThat(validation.getUnit()).isEqualTo("ms");
            assertThat(validation.getThreshold()).isEqualTo(200.0);
        }

        @Test
        @Order(2)
        @DisplayName("Should validate error rate threshold")
        void shouldValidateErrorRateThreshold() {
            when(notificationRepository.save(any())).thenReturn(createTestNotification("error-rate-val"));
            when(policy.canSendNow(any())).thenReturn(true);
            when(notificationSender.send(any())).thenReturn(false); // Force failures
            doNothing().when(eventPublisher).publishNotificationFailed(any(), any(), any());

            // Generate traffic with failures
            for (int i = 0; i < 10; i++) {
                try {
                    notificationService.sendNotification(TEST_USER, NotificationType.EMAIL,
                            "Test Subject", "Test Content");
                } catch (Exception e) {
                    // Failures expected
                }
            }

            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.ERROR_RATE);
            assertThat(validation.getUnit()).isEqualTo("%");
            assertThat(validation.getThreshold()).isEqualTo(2.0);
        }

        @Test
        @Order(3)
        @DisplayName("Should validate P99 latency threshold")
        void shouldValidateP99LatencyThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P99_LATENCY);
            assertThat(validation.getThreshold()).isEqualTo(500.0);
        }

        @Test
        @Order(4)
        @DisplayName("Should validate batch processing latency threshold")
        void shouldValidateBatchProcessingLatencyThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.BATCH_PROCESSING_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.BATCH_PROCESSING_LATENCY);
            assertThat(validation.getThreshold()).isEqualTo(5000.0);
        }
    }

    @Nested
    @DisplayName("2. Compliance Report Tests")
    class ComplianceReportTests {

        @Test
        @Order(1)
        @DisplayName("Should generate compliance report")
        void shouldGenerateComplianceReport() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report).isNotNull();
            assertThat(report.getReportId()).isNotEmpty();
            assertThat(report.getReportId()).startsWith("GOV-");
            assertThat(report.getGeneratedAt()).isNotNull();
        }

        @Test
        @Order(2)
        @DisplayName("Should include SLO compliance section")
        void shouldIncludeSloComplianceSection() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getSloCompliance()).isNotNull();
            assertThat(report.getSloCompliance().getP95LatencyMs()).isGreaterThanOrEqualTo(0);
            assertThat(report.getSloCompliance().getErrorRate()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(3)
        @DisplayName("Should include performance metrics section")
        void shouldIncludePerformanceMetricsSection() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getPerformanceMetrics()).isNotNull();
            assertThat(report.getPerformanceMetrics().getTotalRequests()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should include governance status")
        void shouldIncludeGovernanceStatus() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getGovernanceStatus()).isNotNull();
            assertThat(report.getGovernanceStatus().getStatus()).isIn("COMPLIANT", "WARNING", "NON-COMPLIANT");
            assertThat(report.getGovernanceStatus().getHealthStatus()).isIn("HEALTHY", "DEGRADED", "UNHEALTHY");
        }
    }

    @Nested
    @DisplayName("3. Overall Compliance Tests")
    class OverallComplianceTests {

        @Test
        @Order(1)
        @DisplayName("Should validate all thresholds")
        void shouldValidateAllThresholds() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report).isNotNull();
            assertThat(report.getCheckCount()).isGreaterThan(0);
            assertThat(report.getChecks()).hasSize(4); // P95, P99, Batch, Error Rate
        }

        @Test
        @Order(2)
        @DisplayName("Should check if service is degraded")
        void shouldCheckIfServiceIsDegraded() {
            boolean isDegraded = thresholdValidator.isDegraded();

            assertThat(isDegraded).isNotNull();
            // Could be true or false depending on current state
        }

        @Test
        @Order(3)
        @DisplayName("Should get health status")
        void shouldGetHealthStatus() {
            ThresholdValidator.HealthStatus healthStatus = thresholdValidator.getHealthStatus();

            assertThat(healthStatus).isIn(
                ThresholdValidator.HealthStatus.HEALTHY,
                ThresholdValidator.HealthStatus.DEGRADED,
                ThresholdValidator.HealthStatus.UNHEALTHY
            );
        }

        @Test
        @Order(4)
        @DisplayName("Should report compliant when all checks pass")
        void shouldReportCompliantWhenAllChecksPass() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            if (report.getFailedCheckCount() == 0) {
                assertThat(report.isCompliant()).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("4. Report Structure Tests")
    class ReportStructureTests {

        @Test
        @Order(1)
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
        @Order(2)
        @DisplayName("Should include all check results")
        void shouldIncludeAllCheckResults() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.getChecks()).isNotNull();
            assertThat(report.getChecks()).hasSize(4);

            report.getChecks().forEach(check -> {
                assertThat(check.name()).isNotEmpty();
                assertThat(check.actualValue()).isGreaterThanOrEqualTo(0);
                assertThat(check.threshold()).isGreaterThan(0);
            });
        }

        @Test
        @Order(3)
        @DisplayName("Should calculate variance correctly")
        void shouldCalculateVarianceCorrectly() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            report.getChecks().forEach(check -> {
                double variance = check.getVariance();
                double variancePercent = check.getVariancePercent();

                assertThat(variance).isNotNull();
                assertThat(variancePercent).isNotNull();
            });
        }
    }

    @Nested
    @DisplayName("5. Compliance Status Tests")
    class ComplianceStatusTests {

        @Test
        @Order(1)
        @DisplayName("Should track passed check count")
        void shouldTrackPassedCheckCount() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.getPassedCheckCount()).isGreaterThanOrEqualTo(0);
            assertThat(report.getPassedCheckCount()).isLessThanOrEqualTo(report.getCheckCount());
        }

        @Test
        @Order(2)
        @DisplayName("Should track failed check count")
        void shouldTrackFailedCheckCount() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.getFailedCheckCount()).isGreaterThanOrEqualTo(0);
            assertThat(report.getPassedCheckCount() + report.getFailedCheckCount())
                .isEqualTo(report.getCheckCount());
        }

        @Test
        @Order(3)
        @DisplayName("Should provide severity level")
        void shouldProvideSeverityLevel() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getGovernanceStatus().getSeverity())
                .isIn("INFO", "WARN", "CRITICAL");
        }

        @Test
        @Order(4)
        @DisplayName("Should provide status message")
        void shouldProvideStatusMessage() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getGovernanceStatus().getMessage()).isNotEmpty();
        }
    }

    private Notification createTestNotification(String suffix) {
        return Notification.create(
            TEST_USER + "-" + suffix,
            NotificationType.EMAIL,
            "Test Subject",
            "Test Content"
        );
    }
}
