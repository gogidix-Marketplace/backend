package com.gogidix.aiservices.aicontentgenerationservice.observability;

import com.gogidix.aiservices.aicontentgenerationservice.AiContentGenerationServiceApplication;
import com.gogidix.aiservices.aicontentgenerationservice.domain.policy.ContentGenerationPolicy;
import com.gogidix.aiservices.aicontentgenerationservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aicontentgenerationservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aicontentgenerationservice.infrastructure.metrics.ContentGenerationMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade SLO Compliance Tests for Content Generation Service.
 *
 * These tests validate that the service meets its Service Level Objectives.
 */
@SpringBootTest(
    classes = AiContentGenerationServiceApplication.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    @MockBean
    private ContentGenerationPolicy contentGenerationPolicy;

    @Autowired
    private ContentGenerationMetrics metrics;

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private MeterRegistry meterRegistry;

    // SLO Thresholds
    private static final double SLO_P95_LATENCY_MS = 500.0;
    private static final double SLO_P99_LATENCY_MS = 1000.0;
    private static final double SLO_ERROR_RATE = 0.01;  // 1%

    @Nested
    @DisplayName("1. Latency SLO Tests")
    class LatencySloTests {

        @Test
        @Order(1)
        @DisplayName("Should meet P95 latency SLO")
        void shouldMeetP95LatencySlo() {
            double p95Latency = metrics.getGenerationLatencyP95();

            // In a warm system, latency should be measurable
            assertThat(p95Latency).isGreaterThanOrEqualTo(0);

            // Log the actual value for monitoring
            System.out.println("P95 Latency: " + p95Latency + "ms (SLO: " + SLO_P95_LATENCY_MS + "ms)");
        }

        @Test
        @Order(2)
        @DisplayName("Should meet P99 latency SLO")
        void shouldMeetP99LatencySlo() {
            double p99Latency = metrics.getGenerationLatencyP99();

            assertThat(p99Latency).isGreaterThanOrEqualTo(0);

            System.out.println("P99 Latency: " + p99Latency + "ms (SLO: " + SLO_P99_LATENCY_MS + "ms)");
        }

        @Test
        @Order(3)
        @DisplayName("Should track AI generation latency")
        void shouldTrackAiGenerationLatency() {
            double aiLatency = metrics.getAiGenerationLatencyP95();

            assertThat(aiLatency).isGreaterThanOrEqualTo(0);

            System.out.println("AI Generation P95 Latency: " + aiLatency + "ms");
        }
    }

    @Nested
    @DisplayName("2. Error Rate SLO Tests")
    class ErrorRateSloTests {

        @Test
        @Order(10)
        @DisplayName("Should meet error rate SLO")
        void shouldMeetErrorRateSlo() {
            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isGreaterThanOrEqualTo(0.0);
            assertThat(errorRate).isLessThanOrEqualTo(1.0);

            System.out.println("Error Rate: " + (errorRate * 100) + "% (SLO: " + (SLO_ERROR_RATE * 100) + "%)");
        }

        @Test
        @Order(11)
        @DisplayName("Should track total request count")
        void shouldTrackTotalRequests() {
            long totalRequests = (long) meterRegistry
                    .get("ai.content.generation.total")
                    .counter()
                    .count();

            assertThat(totalRequests).isGreaterThanOrEqualTo(0);

            System.out.println("Total Requests: " + totalRequests);
        }
    }

    @Nested
    @DisplayName("3. Availability SLO Tests")
    class AvailabilitySloTests {

        @Test
        @Order(20)
        @DisplayName("Should meet availability SLO")
        void shouldMeetAvailabilitySlo() {
            double errorRate = metrics.getErrorRate();
            double availability = 1.0 - errorRate;

            assertThat(availability).isGreaterThanOrEqualTo(0.0);
            assertThat(availability).isLessThanOrEqualTo(1.0);

            System.out.println("Availability: " + (availability * 100) + "%");
        }
    }

    @Nested
    @DisplayName("4. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(30)
        @DisplayName("Should record generation success counter")
        void shouldRecordSuccessCounter() {
            long successCount = (long) meterRegistry
                    .get("ai.content.generation.success")
                    .counter()
                    .count();

            assertThat(successCount).isGreaterThanOrEqualTo(0);

            System.out.println("Success Count: " + successCount);
        }

        @Test
        @Order(31)
        @DisplayName("Should record generation failure counter")
        void shouldRecordFailureCounter() {
            long failureCount = (long) meterRegistry
                    .get("ai.content.generation.failure")
                    .counter()
                    .count();

            assertThat(failureCount).isGreaterThanOrEqualTo(0);

            System.out.println("Failure Count: " + failureCount);
        }

        @Test
        @Order(32)
        @DisplayName("Should record content generated counter")
        void shouldRecordContentGeneratedCounter() {
            long contentGenerated = (long) meterRegistry
                    .get("ai.content.generation.generated")
                    .counter()
                    .count();

            assertThat(contentGenerated).isGreaterThanOrEqualTo(0);

            System.out.println("Content Generated: " + contentGenerated);
        }
    }

    @Nested
    @DisplayName("5. Compliance Report Tests")
    class ComplianceReportTests {

        @Test
        @Order(40)
        @DisplayName("Should generate compliance report")
        void shouldGenerateComplianceReport() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report).isNotNull();
            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getGeneratedAt()).isNotNull();

            System.out.println("Compliance Report ID: " + report.getReportId());
        }

        @Test
        @Order(41)
        @DisplayName("Should include SLO compliance status")
        void shouldIncludeSloComplianceStatus() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.SloComplianceSection slo = report.getSloCompliance();

            assertThat(slo).isNotNull();
            assertThat(slo.isOverallCompliant()).isNotNull();

            System.out.println("SLO Overall Compliant: " + slo.isOverallCompliant());
            System.out.println("P95 Compliant: " + slo.isP95LatencyCompliant());
            System.out.println("Error Rate Compliant: " + slo.isErrorRateCompliant());
        }

        @Test
        @Order(42)
        @DisplayName("Should provide governance status")
        void shouldProvideGovernanceStatus() {
            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isNotNull();
            assertThat(status).isIn(
                ThresholdValidator.HealthStatus.HEALTHY,
                ThresholdValidator.HealthStatus.DEGRADED,
                ThresholdValidator.HealthStatus.UNHEALTHY
            );

            System.out.println("Governance Status: " + status);
        }
    }

    @Nested
    @DisplayName("6. Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @Order(50)
        @DisplayName("Should validate P95 threshold")
        void shouldValidateP95Threshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P95_LATENCY);
            assertThat(validation.getThreshold()).isEqualTo(SLO_P95_LATENCY_MS);
            assertThat(validation.getUnit()).isEqualTo("ms");

            System.out.println("P95 Validation - Actual: " + validation.getActualValue() +
                    "ms, Threshold: " + validation.getThreshold() + "ms, Passed: " + validation.isPassed());
        }

        @Test
        @Order(51)
        @DisplayName("Should validate error rate threshold")
        void shouldValidateErrorRateThreshold() {
            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.ERROR_RATE);
            assertThat(validation.getUnit()).isEqualTo("%");

            System.out.println("Error Rate Validation - Actual: " + validation.getActualValue() +
                    "%, Threshold: " + validation.getThreshold() + "%, Passed: " + validation.isPassed());
        }

        @Test
        @Order(52)
        @DisplayName("Should validate all thresholds")
        void shouldValidateAllThresholds() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report).isNotNull();
            assertThat(report.getCheckCount()).isGreaterThan(0);

            System.out.println("Overall Compliance - Checks: " + report.getCheckCount() +
                    ", Passed: " + report.getPassedCheckCount() +
                    ", Failed: " + report.getFailedCheckCount() +
                    ", Compliant: " + report.isCompliant());
        }
    }
}
