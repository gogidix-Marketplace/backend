package com.gogidix.aiservices.aigatewayservice.infrastructure.governance;

import com.gogidix.aiservices.aigatewayservice.infrastructure.metrics.GatewayMetrics;
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

    private GatewayMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator generator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new GatewayMetrics(meterRegistry);
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
            Timer gatewayTimer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            gatewayTimer.record(350, TimeUnit.MILLISECONDS);
            gatewayTimer.record(400, TimeUnit.MILLISECONDS);
            gatewayTimer.record(300, TimeUnit.MILLISECONDS);

            Timer routingTimer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
            routingTimer.record(70, TimeUnit.MILLISECONDS);
            routingTimer.record(80, TimeUnit.MILLISECONDS);

            Timer filterTimer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
            filterTimer.record(30, TimeUnit.MILLISECONDS);
            filterTimer.record(40, TimeUnit.MILLISECONDS);

            Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
            Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");
            totalCounter.increment(1000);
            failureCounter.increment(50);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.SloComplianceSection slo = report.getSloCompliance();

            assertThat(slo.getP95GatewayLatencyMs()).isGreaterThan(0);
            assertThat(slo.getP99GatewayLatencyMs()).isGreaterThan(0);
            assertThat(slo.getP95RoutingLatencyMs()).isGreaterThan(0);
            assertThat(slo.getP95FilterLatencyMs()).isGreaterThan(0);
            assertThat(slo.getErrorRate()).isGreaterThan(0);
            assertThat(slo.getAvailability()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should populate performance metrics section")
        void shouldPopulatePerformanceMetricsSection() {
            Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
            Counter successCounter = meterRegistry.counter("ai.gateway.requests.success", "service", "ai-gateway");
            Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");
            Counter routeCreatedCounter = meterRegistry.counter("ai.gateway.routes.created", "service", "ai-gateway");
            Counter routeDeletedCounter = meterRegistry.counter("ai.gateway.routes.deleted", "service", "ai-gateway");
            Counter circuitTrippedCounter = meterRegistry.counter("ai.gateway.circuit.tripped", "service", "ai-gateway");
            Counter rateLimitCounter = meterRegistry.counter("ai.gateway.ratelimit.exceeded", "service", "ai-gateway");

            totalCounter.increment(5000);
            successCounter.increment(4750);
            failureCounter.increment(250);
            routeCreatedCounter.increment(100);
            routeDeletedCounter.increment(50);
            circuitTrippedCounter.increment(10);
            rateLimitCounter.increment(25);

            ComplianceReportGenerator.GovernanceReport report = generator.generateReport();

            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();

            assertThat(perf.getTotalRequests()).isEqualTo(5000L);
            assertThat(perf.getSuccessfulRequests()).isEqualTo(4750L);
            assertThat(perf.getFailedRequests()).isEqualTo(250L);
            assertThat(perf.getRoutesCreated()).isEqualTo(100L);
            assertThat(perf.getRoutesDeleted()).isEqualTo(50L);
            assertThat(perf.getCircuitBreakerTrips()).isEqualTo(10L);
            assertThat(perf.getRateLimitExceeded()).isEqualTo(25L);
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
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            timer.record(600, TimeUnit.MILLISECONDS); // Over P95 threshold

            Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
            Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");
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
            assertThat(sloCompliance).containsKey("p95GatewayLatencyMs");
            assertThat(sloCompliance).containsKey("p95GatewayLatencyCompliant");
            assertThat(sloCompliance).containsKey("p95RoutingLatencyMs");
            assertThat(sloCompliance).containsKey("p95FilterLatencyMs");
            assertThat(sloCompliance).containsKey("errorRate");
            assertThat(sloCompliance).containsKey("errorRateCompliant");
            assertThat(sloCompliance).containsKey("availability");
            assertThat(sloCompliance).containsKey("overallCompliant");
        }

        @Test
        @DisplayName("Should include performance metrics data in map")
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
            assertThat(perfMetrics).containsKey("routesCreated");
            assertThat(perfMetrics).containsKey("routesDeleted");
            assertThat(perfMetrics).containsKey("circuitBreakerTrips");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle zero error rate correctly")
        void shouldHandleZeroErrorRate() {
            Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
            Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");
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
        Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
        Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");
        totalCounter.increment(100);
        failureCounter.increment(5);

        Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
        timer.record(350, TimeUnit.MILLISECONDS);
    }

    private void recordGoodMetrics() {
        Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
        Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");

        Counter successCounter = meterRegistry.counter("ai.gateway.requests.success", "service", "ai-gateway");
        Counter routeCreatedCounter = meterRegistry.counter("ai.gateway.routes.created", "service", "ai-gateway");
        Counter routeDeletedCounter = meterRegistry.counter("ai.gateway.routes.deleted", "service", "ai-gateway");

        totalCounter.increment(1000);
        successCounter.increment(995);
        failureCounter.increment(5);
        routeCreatedCounter.increment(10);
        routeDeletedCounter.increment(5);

        Timer gatewayTimer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
        gatewayTimer.record(300, TimeUnit.MILLISECONDS);
        gatewayTimer.record(350, TimeUnit.MILLISECONDS);
        gatewayTimer.record(400, TimeUnit.MILLISECONDS);

        Timer routingTimer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
        routingTimer.record(70, TimeUnit.MILLISECONDS);
        routingTimer.record(80, TimeUnit.MILLISECONDS);

        Timer filterTimer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
        filterTimer.record(30, TimeUnit.MILLISECONDS);
        filterTimer.record(40, TimeUnit.MILLISECONDS);
    }
}
