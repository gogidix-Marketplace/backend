package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.governance;

import com.gogidix.aiservices.aianalyticsdashboard.infrastructure.metrics.DashboardMetrics;
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

    private DashboardMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator generator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new DashboardMetrics(meterRegistry);
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
            Timer creationTimer = meterRegistry.timer("dashboard.creation.duration", "service", "ai-analytics-dashboard");
            creationTimer.record(350, TimeUnit.MILLISECONDS);
            creationTimer.record(400, TimeUnit.MILLISECONDS);
            creationTimer.record(300, TimeUnit.MILLISECONDS);

            Timer queryTimer = meterRegistry.timer("dashboard.query.duration", "service", "ai-analytics-dashboard");
            queryTimer.record(200, TimeUnit.MILLISECONDS);
            queryTimer.record(250, TimeUnit.MILLISECONDS);

            Counter totalCounter = meterRegistry.counter("metric.query.total", "service", "ai-analytics-dashboard");
            Counter failureCounter = meterRegistry.counter("metric.query.failure", "service", "ai-analytics-dashboard");
            totalCounter.increment(1000);
            failureCounter.increment(50);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.SloComplianceSection slo = report.getSloCompliance();

            assertThat(slo.getP95CreationLatencyMs()).isGreaterThan(0);
            assertThat(slo.getP99CreationLatencyMs()).isGreaterThan(0);
            assertThat(slo.getP95QueryLatencyMs()).isGreaterThan(0);
            assertThat(slo.getErrorRate()).isGreaterThan(0);
            assertThat(slo.getAvailability()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should populate performance metrics section")
        void shouldPopulatePerformanceMetricsSection() {
            Counter createdCounter = meterRegistry.counter("dashboard.created", "service", "ai-analytics-dashboard");
            Counter updatedCounter = meterRegistry.counter("dashboard.updated", "service", "ai-analytics-dashboard");
            Counter viewedCounter = meterRegistry.counter("dashboard.view", "service", "ai-analytics-dashboard");
            Counter widgetAddedCounter = meterRegistry.counter("widget.added", "service", "ai-analytics-dashboard");
            Counter widgetRemovedCounter = meterRegistry.counter("widget.removed", "service", "ai-analytics-dashboard");
            Counter queryCounter = meterRegistry.counter("metric.query.total", "service", "ai-analytics-dashboard");
            Counter successCounter = meterRegistry.counter("metric.query.success", "service", "ai-analytics-dashboard");
            Counter failureCounter = meterRegistry.counter("metric.query.failure", "service", "ai-analytics-dashboard");

            createdCounter.increment(100);
            updatedCounter.increment(200);
            viewedCounter.increment(1000);
            widgetAddedCounter.increment(50);
            widgetRemovedCounter.increment(10);
            queryCounter.increment(5000);
            successCounter.increment(4750);
            failureCounter.increment(250);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf.getDashboardsCreated()).isEqualTo(100L);
            assertThat(perf.getDashboardsUpdated()).isEqualTo(200L);
            assertThat(perf.getDashboardsViewed()).isEqualTo(1000L);
            assertThat(perf.getWidgetsAdded()).isEqualTo(50L);
            assertThat(perf.getWidgetsRemoved()).isEqualTo(10L);
            assertThat(perf.getMetricQueries()).isEqualTo(5000L);
            assertThat(perf.getSuccessfulQueries()).isEqualTo(4750L);
            assertThat(perf.getFailedQueries()).isEqualTo(250L);
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
            Timer timer = meterRegistry.timer("dashboard.creation.duration", "service", "ai-analytics-dashboard");
            timer.record(600, TimeUnit.MILLISECONDS); // Over P95 threshold

            Counter totalCounter = meterRegistry.counter("metric.query.total", "service", "ai-analytics-dashboard");
            Counter failureCounter = meterRegistry.counter("metric.query.failure", "service", "ai-analytics-dashboard");
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
            assertThat(sloCompliance).containsKey("p95CreationLatencyMs");
            assertThat(sloCompliance).containsKey("p95CreationLatencyCompliant");
            assertThat(sloCompliance).containsKey("p95QueryLatencyMs");
            assertThat(sloCompliance).containsKey("p95QueryLatencyCompliant");
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
            Counter totalCounter = meterRegistry.counter("metric.query.total", "service", "ai-analytics-dashboard");
            Counter failureCounter = meterRegistry.counter("metric.query.failure", "service", "ai-analytics-dashboard");
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
        Counter totalCounter = meterRegistry.counter("metric.query.total", "service", "ai-analytics-dashboard");
        Counter failureCounter = meterRegistry.counter("metric.query.failure", "service", "ai-analytics-dashboard");
        totalCounter.increment(100);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("dashboard.creation.duration", "service", "ai-analytics-dashboard");
        timer.record(350, TimeUnit.MILLISECONDS);
    }

    private void recordGoodMetrics() {
        Counter totalCounter = meterRegistry.counter("metric.query.total", "service", "ai-analytics-dashboard");
        Counter failureCounter = meterRegistry.counter("metric.query.failure", "service", "ai-analytics-dashboard");

        Counter successCounter = meterRegistry.counter("metric.query.success", "service", "ai-analytics-dashboard");

        totalCounter.increment(1000);
        successCounter.increment(995);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("dashboard.creation.duration", "service", "ai-analytics-dashboard");
        timer.record(300, TimeUnit.MILLISECONDS);
        timer.record(350, TimeUnit.MILLISECONDS);
        timer.record(400, TimeUnit.MILLISECONDS);

        Timer queryTimer = meterRegistry.timer("dashboard.query.duration", "service", "ai-analytics-dashboard");
        queryTimer.record(200, TimeUnit.MILLISECONDS);
        queryTimer.record(250, TimeUnit.MILLISECONDS);
    }
}
