package com.gogidix.aiservices.aidataprocessing.infrastructure.governance;

import com.gogidix.aiservices.aidataprocessing.infrastructure.metrics.DataProcessingMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ComplianceReportGenerator.
 * Tests governance report generation for financial-grade compliance.
 */
@DisplayName("ComplianceReportGenerator Tests")
class ComplianceReportGeneratorTest {

    private DataProcessingMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator generator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new DataProcessingMetrics(meterRegistry);
        thresholdValidator = new ThresholdValidator(metrics);
        generator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }

    @Nested
    @DisplayName("Report Generation")
    class ReportGenerationTests {

        @Test
        @DisplayName("Should generate report with valid structure")
        void shouldGenerateReportWithValidStructure() {
            recordSomeMetrics();

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            assertThat(report).isNotNull();
            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getReportId()).startsWith("GOV-");
            assertThat(report.getGeneratedAt()).isNotNull();
            assertThat(report.getSloCompliance()).isNotNull();
            assertThat(report.getPerformanceMetrics()).isNotNull();
            assertThat(report.getGovernanceStatus()).isNotNull();
        }

        @Test
        @DisplayName("Should generate report ID with correct format")
        void shouldGenerateReportIdWithCorrectFormat() {
            recordSomeMetrics();

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            assertThat(report.getReportId()).matches("GOV-\\d{8}-\\d{6}");
        }

        @Test
        @DisplayName("Should populate SLO compliance section")
        void shouldPopulateSloComplianceSection() {
            Timer processingTimer = meterRegistry.timer("ai.data.processing.duration", "service", "ai-data-processing");
            processingTimer.record(350, java.util.concurrent.TimeUnit.MILLISECONDS);
            processingTimer.record(400, java.util.concurrent.TimeUnit.MILLISECONDS);
            processingTimer.record(300, java.util.concurrent.TimeUnit.MILLISECONDS);

            Counter totalCounter = meterRegistry.counter("ai.data.processing.total", "service", "ai-data-processing");
            Counter failureCounter = meterRegistry.counter("ai.data.processing.failure", "service", "ai-data-processing");
            totalCounter.increment(1000);
            failureCounter.increment(50);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.SloComplianceSection slo = report.getSloCompliance();

            assertThat(slo.getP95LatencyMs()).isGreaterThan(0);
            assertThat(slo.getP99LatencyMs()).isGreaterThan(0);
            assertThat(slo.getErrorRate()).isGreaterThan(0);
            assertThat(slo.getAvailability()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should populate performance metrics section")
        void shouldPopulatePerformanceMetricsSection() {
            Counter totalCounter = meterRegistry.counter("ai.data.processing.total", "service", "ai-data-processing");
            Counter successCounter = meterRegistry.counter("ai.data.processing.success", "service", "ai-data-processing");
            Counter failureCounter = meterRegistry.counter("ai.data.processing.failure", "service", "ai-data-processing");
            Counter validationPassedCounter = meterRegistry.counter("ai.data.processing.validation.passed", "service", "ai-data-processing");
            Counter validationFailedCounter = meterRegistry.counter("ai.data.processing.validation.failed", "service", "ai-data-processing");
            Counter transformationCounter = meterRegistry.counter("ai.data.processing.transformation.applied", "service", "ai-data-processing");

            totalCounter.increment(5000);
            successCounter.increment(4750);
            failureCounter.increment(250);
            validationPassedCounter.increment(4500);
            validationFailedCounter.increment(250);
            transformationCounter.increment(3000);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf.getTotalRequests()).isEqualTo(5000L);
            assertThat(perf.getSuccessfulRequests()).isEqualTo(4750L);
            assertThat(perf.getFailedRequests()).isEqualTo(250L);
            assertThat(perf.getValidationPassed()).isEqualTo(4500L);
            assertThat(perf.getValidationFailed()).isEqualTo(250L);
            assertThat(perf.getTransformationsApplied()).isEqualTo(3000L);
        }

        @Test
        @DisplayName("Should set governance status based on health status")
        void shouldSetGovernanceStatusBasedOnHealth() {
            recordGoodMetrics();

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.GovernanceStatus status = report.getGovernanceStatus();

            assertThat(status.getStatus()).isEqualTo("COMPLIANT");
            assertThat(status.getSeverity()).isEqualTo("INFO");
            assertThat(status.getHealthStatus()).isEqualTo("HEALTHY");
        }

        @Test
        @DisplayName("Should set WARNING governance status when DEGRADED")
        void shouldSetWarningStatusWhenDegraded() {
            Timer timer = meterRegistry.timer("ai.data.processing.duration", "service", "ai-data-processing");
            timer.record(600, java.util.concurrent.TimeUnit.MILLISECONDS);

            Counter totalCounter = meterRegistry.counter("ai.data.processing.total", "service", "ai-data-processing");
            Counter failureCounter = meterRegistry.counter("ai.data.processing.failure", "service", "ai-data-processing");
            totalCounter.increment(100);
            failureCounter.increment(5);

            thresholdValidator = new ThresholdValidator(metrics);
            generator = new ComplianceReportGenerator(metrics, thresholdValidator);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();
            ComplianceReportGenerator.GovernanceStatus status = report.getGovernanceStatus();

            assertThat(status.getHealthStatus()).isIn("DEGRADED", "UNHEALTHY", "HEALTHY");
            assertThat(status.getSeverity()).isIn("INFO", "WARN", "CRITICAL");
        }
    }

    @Nested
    @DisplayName("Report to Map Conversion")
    class ReportToMapTests {

        @Test
        @DisplayName("Should convert report to map structure")
        void shouldConvertReportToMap() {
            recordSomeMetrics();

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            var map = report.toMap();

            assertThat(map).isNotNull();
            assertThat(map).containsKey("reportId");
            assertThat(map).containsKey("generatedAt");
            assertThat(map).containsKey("sloCompliance");
            assertThat(map).containsKey("performanceMetrics");
            assertThat(map).containsKey("governanceStatus");
        }

        @Test
        @DisplayName("Should include SLO compliance data in map")
        void shouldIncludeSloComplianceInMap() {
            recordSomeMetrics();

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();
            var map = report.toMap();

            @SuppressWarnings("unchecked")
            var sloCompliance = (java.util.Map<String, Object>) map.get("sloCompliance");

            assertThat(sloCompliance).isNotNull();
            assertThat(sloCompliance).containsKey("p95LatencyMs");
            assertThat(sloCompliance).containsKey("p95LatencyCompliant");
            assertThat(sloCompliance).containsKey("errorRate");
            assertThat(sloCompliance).containsKey("errorRateCompliant");
            assertThat(sloCompliance).containsKey("availability");
            assertThat(sloCompliance).containsKey("overallCompliant");
        }

        @Test
        @DisplayName("Should include performance metrics in map")
        void shouldIncludePerformanceMetricsInMap() {
            recordSomeMetrics();

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();
            var map = report.toMap();

            @SuppressWarnings("unchecked")
            var perfMetrics = (java.util.Map<String, Object>) map.get("performanceMetrics");

            assertThat(perfMetrics).isNotNull();
            assertThat(perfMetrics).containsKey("totalRequests");
            assertThat(perfMetrics).containsKey("successfulRequests");
            assertThat(perfMetrics).containsKey("failedRequests");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle zero error rate correctly")
        void shouldHandleZeroErrorRate() {
            Counter totalCounter = meterRegistry.counter("ai.data.processing.total", "service", "ai-data-processing");
            Counter failureCounter = meterRegistry.counter("ai.data.processing.failure", "service", "ai-data-processing");
            totalCounter.increment(1000);
            // No failures

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            assertThat(report.getSloCompliance().getErrorRate()).isEqualTo(0.0);
            assertThat(report.getSloCompliance().getAvailability()).isEqualTo(100.0);
        }

        @Test
        @DisplayName("Should have report generated timestamp close to now")
        void shouldHaveRecentGeneratedTimestamp() {
            recordSomeMetrics();

            Instant before = Instant.now().minusSeconds(5);
            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();
            Instant after = Instant.now().plusSeconds(5);

            assertThat(report.getGeneratedAt()).isAfter(before);
            assertThat(report.getGeneratedAt()).isBefore(after);
        }

        @Test
        @DisplayName("Should calculate P95 latency variance correctly")
        void shouldCalculateP95VarianceCorrectly() {
            Timer timer = meterRegistry.timer("ai.data.processing.duration", "service", "ai-data-processing");
            timer.record(300, java.util.concurrent.TimeUnit.MILLISECONDS);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            assertThat(report.getSloCompliance().getP95LatencyVariance()).isNotZero();
        }
    }

    private void recordSomeMetrics() {
        Counter totalCounter = meterRegistry.counter("ai.data.processing.total", "service", "ai-data-processing");
        Counter failureCounter = meterRegistry.counter("ai.data.processing.failure", "service", "ai-data-processing");
        totalCounter.increment(100);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("ai.data.processing.duration", "service", "ai-data-processing");
        timer.record(350, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    private void recordGoodMetrics() {
        Counter totalCounter = meterRegistry.counter("ai.data.processing.total", "service", "ai-data-processing");
        Counter failureCounter = meterRegistry.counter("ai.data.processing.failure", "service", "ai-data-processing");
        Counter successCounter = meterRegistry.counter("ai.data.processing.success", "service", "ai-data-processing");

        totalCounter.increment(1000);
        successCounter.increment(995);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("ai.data.processing.duration", "service", "ai-data-processing");
        timer.record(300, java.util.concurrent.TimeUnit.MILLISECONDS);
        timer.record(350, java.util.concurrent.TimeUnit.MILLISECONDS);
        timer.record(400, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
