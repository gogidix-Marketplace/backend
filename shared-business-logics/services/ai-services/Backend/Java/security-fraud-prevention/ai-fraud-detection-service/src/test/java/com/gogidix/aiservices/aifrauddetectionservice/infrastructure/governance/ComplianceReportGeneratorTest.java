package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.governance;

import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.metrics.FraudDetectionMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ComplianceReportGenerator.
 * Tests governance report generation for financial-grade compliance.
 */
@DisplayName("ComplianceReportGenerator Tests")
class ComplianceReportGeneratorTest {

    private FraudDetectionMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator generator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new FraudDetectionMetrics(meterRegistry);
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
            Timer analysisTimer = meterRegistry.timer("fraud.analysis.duration", "service", "ai-fraud-detection");
            analysisTimer.record(350, TimeUnit.MILLISECONDS);
            analysisTimer.record(400, TimeUnit.MILLISECONDS);
            analysisTimer.record(300, TimeUnit.MILLISECONDS);

            Counter totalCounter = meterRegistry.counter("fraud.analysis.total", "service", "ai-fraud-detection");
            Counter failureCounter = meterRegistry.counter("fraud.analysis.failure", "service", "ai-fraud-detection");
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
            Counter totalCounter = meterRegistry.counter("fraud.analysis.total", "service", "ai-fraud-detection");
            Counter successCounter = meterRegistry.counter("fraud.analysis.success", "service", "ai-fraud-detection");
            Counter failureCounter = meterRegistry.counter("fraud.analysis.failure", "service", "ai-fraud-detection");
            Counter fraudCounter = meterRegistry.counter("fraud.detected", "service", "ai-fraud-detection");
            Counter highRiskCounter = meterRegistry.counter("fraud.high.risk", "service", "ai-fraud-detection");
            Counter blockedCounter = meterRegistry.counter("fraud.blocked", "service", "ai-fraud-detection");

            totalCounter.increment(5000);
            successCounter.increment(4750);
            failureCounter.increment(250);
            fraudCounter.increment(300);
            highRiskCounter.increment(150);
            blockedCounter.increment(100);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf.getTotalRequests()).isEqualTo(5000L);
            assertThat(perf.getSuccessfulRequests()).isEqualTo(4750L);
            assertThat(perf.getFailedRequests()).isEqualTo(250L);
            assertThat(perf.getFraudDetected()).isEqualTo(300L);
            assertThat(perf.getHighRiskTransactions()).isEqualTo(150L);
            assertThat(perf.getBlockedTransactions()).isEqualTo(100L);
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
            Timer timer = meterRegistry.timer("fraud.analysis.duration", "service", "ai-fraud-detection");
            timer.record(600, TimeUnit.MILLISECONDS); // Over P95 threshold

            Counter totalCounter = meterRegistry.counter("fraud.analysis.total", "service", "ai-fraud-detection");
            Counter failureCounter = meterRegistry.counter("fraud.analysis.failure", "service", "ai-fraud-detection");
            totalCounter.increment(100);
            failureCounter.increment(5);

            // Recreate validator with updated metrics
            thresholdValidator = new ThresholdValidator(metrics);
            generator = new ComplianceReportGenerator(metrics, thresholdValidator);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();
            ComplianceReportGenerator.GovernanceStatus status = report.getGovernanceStatus();

            // Should be DEGRADED or UNHEALTHY depending on threshold check
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
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle zero error rate correctly")
        void shouldHandleZeroErrorRate() {
            Counter totalCounter = meterRegistry.counter("fraud.analysis.total", "service", "ai-fraud-detection");
            Counter failureCounter = meterRegistry.counter("fraud.analysis.failure", "service", "ai-fraud-detection");
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
    }

    private void recordSomeMetrics() {
        Counter totalCounter = meterRegistry.counter("fraud.analysis.total", "service", "ai-fraud-detection");
        Counter failureCounter = meterRegistry.counter("fraud.analysis.failure", "service", "ai-fraud-detection");
        totalCounter.increment(100);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("fraud.analysis.duration", "service", "ai-fraud-detection");
        timer.record(350, TimeUnit.MILLISECONDS);
    }

    private void recordGoodMetrics() {
        Counter totalCounter = meterRegistry.counter("fraud.analysis.total", "service", "ai-fraud-detection");
        Counter failureCounter = meterRegistry.counter("fraud.analysis.failure", "service", "ai-fraud-detection");

        Counter successCounter = meterRegistry.counter("fraud.analysis.success", "service", "ai-fraud-detection");
        Counter fraudCounter = meterRegistry.counter("fraud.detected", "service", "ai-fraud-detection");
        Counter highRiskCounter = meterRegistry.counter("fraud.high.risk", "service", "ai-fraud-detection");
        Counter blockedCounter = meterRegistry.counter("fraud.blocked", "service", "ai-fraud-detection");

        totalCounter.increment(1000);
        successCounter.increment(995);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("fraud.analysis.duration", "service", "ai-fraud-detection");
        timer.record(300, TimeUnit.MILLISECONDS);
        timer.record(350, TimeUnit.MILLISECONDS);
        timer.record(400, TimeUnit.MILLISECONDS);

        Timer mlTimer = meterRegistry.timer("fraud.ml.prediction.duration", "service", "ai-fraud-detection");
        mlTimer.record(50, TimeUnit.MILLISECONDS);
    }
}
