package com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.governance;

import com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.metrics.ProductRecommendationMetrics;
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

    private ProductRecommendationMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator generator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new ProductRecommendationMetrics(meterRegistry);
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
            Timer segmentationTimer = meterRegistry.timer("segmentation.duration", "service", "ai-product-recommendationation");
            segmentationTimer.record(350, TimeUnit.MILLISECONDS);
            segmentationTimer.record(400, TimeUnit.MILLISECONDS);
            segmentationTimer.record(300, TimeUnit.MILLISECONDS);

            Counter totalCounter = meterRegistry.counter("segmentation.total", "service", "ai-product-recommendationation");
            Counter failureCounter = meterRegistry.counter("segmentation.failure", "service", "ai-product-recommendationation");
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
            Counter totalCounter = meterRegistry.counter("segmentation.total", "service", "ai-product-recommendationation");
            Counter successCounter = meterRegistry.counter("segmentation.success", "service", "ai-product-recommendationation");
            Counter failureCounter = meterRegistry.counter("segmentation.failure", "service", "ai-product-recommendationation");
            Counter createdCounter = meterRegistry.counter("segment.created", "service", "ai-product-recommendationation");
            Counter updatedCounter = meterRegistry.counter("segment.updated", "service", "ai-product-recommendationation");
            Counter analyzedCounter = meterRegistry.counter("customer.analyzed", "service", "ai-product-recommendationation");

            totalCounter.increment(5000);
            successCounter.increment(4750);
            failureCounter.increment(250);
            createdCounter.increment(100);
            updatedCounter.increment(50);
            analyzedCounter.increment(50000);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf.getTotalRecommendations()).isEqualTo(5000L);
            assertThat(perf.getSuccessfulRecommendations()).isEqualTo(4750L);
            assertThat(perf.getFailedRecommendations()).isEqualTo(250L);
            assertThat(perf.getRecommendationsCreated()).isEqualTo(100L);
            assertThat(perf.getRecommendationsUpdated()).isEqualTo(50L);
            assertThat(perf.getProductsAnalyzed()).isEqualTo(50000L);
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
            Counter totalCounter = meterRegistry.counter("segmentation.total", "service", "ai-product-recommendationation");
            Counter failureCounter = meterRegistry.counter("segmentation.failure", "service", "ai-product-recommendationation");
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
        Counter totalCounter = meterRegistry.counter("segmentation.total", "service", "ai-product-recommendationation");
        Counter failureCounter = meterRegistry.counter("segmentation.failure", "service", "ai-product-recommendationation");
        totalCounter.increment(100);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-product-recommendationation");
        timer.record(350, TimeUnit.MILLISECONDS);
    }

    private void recordGoodMetrics() {
        Counter totalCounter = meterRegistry.counter("segmentation.total", "service", "ai-product-recommendationation");
        Counter successCounter = meterRegistry.counter("segmentation.success", "service", "ai-product-recommendationation");
        Counter failureCounter = meterRegistry.counter("segmentation.failure", "service", "ai-product-recommendationation");

        Counter createdCounter = meterRegistry.counter("segment.created", "service", "ai-product-recommendationation");
        Counter analyzedCounter = meterRegistry.counter("customer.analyzed", "service", "ai-product-recommendationation");

        totalCounter.increment(1000);
        successCounter.increment(995);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-product-recommendationation");
        timer.record(300, TimeUnit.MILLISECONDS);
        timer.record(350, TimeUnit.MILLISECONDS);
        timer.record(400, TimeUnit.MILLISECONDS);

        Timer analysisTimer = meterRegistry.timer("segment.analysis.duration", "service", "ai-product-recommendationation");
        analysisTimer.record(100, TimeUnit.MILLISECONDS);
    }
}
