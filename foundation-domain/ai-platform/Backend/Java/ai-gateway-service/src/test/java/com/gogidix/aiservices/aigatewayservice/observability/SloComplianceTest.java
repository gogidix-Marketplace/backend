package com.gogidix.aiservices.aigatewayservice.observability;

import com.gogidix.aiservices.aigatewayservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aigatewayservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aigatewayservice.infrastructure.metrics.GatewayMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SLO compliance tests for financial-grade certification.
 * Tests that service meets observability requirements and SLO thresholds.
 */
@DisplayName("SLO Compliance Tests")
class SloComplianceTest {

    private GatewayMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator reportGenerator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new GatewayMetrics(meterRegistry);
        thresholdValidator = new ThresholdValidator(metrics);
        reportGenerator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }

    @Nested
    @DisplayName("SLO Latency Tests")
    class SloLatencyTests {

        @Test
        @DisplayName("Should meet P95 latency SLO of 500ms")
        void shouldMeetP95LatencySLO() {
            // Record latencies within SLO threshold
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            for (int i = 0; i < 100; i++) {
                timer.record(300 + (i % 200), TimeUnit.MILLISECONDS);
            }

            double p95Latency = metrics.getGatewayLatencyP95();

            assertThat(p95Latency).isLessThanOrEqualTo(500.0);
        }

        @Test
        @DisplayName("Should meet P99 latency SLO of 1000ms")
        void shouldMeetP99LatencySLO() {
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            for (int i = 0; i < 100; i++) {
                timer.record(300 + (i % 500), TimeUnit.MILLISECONDS);
            }

            double p99Latency = metrics.getGatewayLatencyP99();

            assertThat(p99Latency).isLessThanOrEqualTo(1000.0);
        }

        @Test
        @DisplayName("Should meet routing latency SLO of 100ms at P95")
        void shouldMeetRoutingLatencySLO() {
            Timer timer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
            for (int i = 0; i < 50; i++) {
                timer.record(50 + (i % 40), TimeUnit.MILLISECONDS);
            }

            double p95RoutingLatency = metrics.getRoutingLatencyP95();

            assertThat(p95RoutingLatency).isLessThanOrEqualTo(100.0);
        }

        @Test
        @DisplayName("Should meet filter execution latency SLO of 50ms at P95")
        void shouldMeetFilterLatencySLO() {
            Timer timer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
            for (int i = 0; i < 50; i++) {
                timer.record(20 + (i % 25), TimeUnit.MILLISECONDS);
            }

            double p95FilterLatency = metrics.getFilterLatencyP95();

            assertThat(p95FilterLatency).isLessThanOrEqualTo(50.0);
        }

        @Test
        @DisplayName("Should track latency percentiles correctly")
        void shouldTrackLatencyPercentilesCorrectly() {
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            timer.record(100, TimeUnit.MILLISECONDS);
            timer.record(200, TimeUnit.MILLISECONDS);
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(500, TimeUnit.MILLISECONDS);

            double p95 = metrics.getGatewayLatencyP95();
            double p99 = metrics.getGatewayLatencyP99();

            assertThat(p95).isGreaterThan(0);
            assertThat(p99).isGreaterThan(0);
            assertThat(p99).isGreaterThanOrEqualTo(p95);
        }
    }

    @Nested
    @DisplayName("Error Rate Tests")
    class ErrorRateTests {

        @Test
        @DisplayName("Should meet error rate SLO of 1%")
        void shouldMeetErrorRateSLO() {
            Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
            Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");

            // 1000 requests with 5 failures = 0.5% error rate
            totalCounter.increment(1000);
            failureCounter.increment(5);

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isLessThanOrEqualTo(0.01);
        }

        @Test
        @DisplayName("Should calculate error rate correctly with no failures")
        void shouldCalculateErrorRateCorrectlyWithNoFailures() {
            Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");

            totalCounter.increment(1000);
            // No failures recorded

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle error rate calculation with zero requests")
        void shouldHandleErrorRateCalculationWithZeroRequests() {
            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should detect error rate SLO violation")
        void shouldDetectErrorRateSLOViolation() {
            Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
            Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");

            // 100 requests with 20 failures = 20% error rate (exceeds 1% SLO)
            totalCounter.increment(100);
            failureCounter.increment(20);

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation.isPassed()).isFalse();
            assertThat(validation.getActualValue()).isGreaterThan(validation.getThreshold());
        }
    }

    @Nested
    @DisplayName("SLO Threshold Tests")
    class SloThresholdTests {

        @Test
        @DisplayName("Should enforce all SLO thresholds simultaneously")
        void shouldEnforceAllSLOThresholdsSimultaneously() {
            recordCompliantMetrics();

            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.isCompliant()).isTrue();
            assertThat(report.getFailedCheckCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should report non-compliance when any SLO threshold is violated")
        void shouldReportNonComplianceWhenAnySLOThresholdIsViolated() {
            // Record high latency that violates P95 SLO
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            for (int i = 0; i < 50; i++) {
                timer.record(600, TimeUnit.MILLISECONDS);
            }

            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.isCompliant()).isFalse();
            assertThat(report.getFailedCheckCount()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should include all threshold types in compliance report")
        void shouldIncludeAllThresholdTypesInComplianceReport() {
            recordCompliantMetrics();

            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report.getCheckCount()).isEqualTo(5); // P95, P99, Routing, Filter, Error Rate
        }
    }

    @Nested
    @DisplayName("Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @DisplayName("Should collect gateway request metrics")
        void shouldCollectGatewayRequestMetrics() {
            metrics.incrementRequestTotal();
            metrics.incrementRequestSuccess();

            Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
            Counter successCounter = meterRegistry.counter("ai.gateway.requests.success", "service", "ai-gateway");

            assertThat(totalCounter.count()).isGreaterThan(0);
            assertThat(successCounter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should collect gateway failure metrics")
        void shouldCollectGatewayFailureMetrics() {
            metrics.incrementRequestFailure();

            Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");

            assertThat(failureCounter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should collect circuit breaker metrics")
        void shouldCollectCircuitBreakerMetrics() {
            metrics.incrementCircuitBreakerTripped();

            Counter circuitCounter = meterRegistry.counter("ai.gateway.circuit.tripped", "service", "ai-gateway");

            assertThat(circuitCounter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should collect rate limit metrics")
        void shouldCollectRateLimitMetrics() {
            metrics.incrementRateLimitExceeded();

            Counter rateLimitCounter = meterRegistry.counter("ai.gateway.ratelimit.exceeded", "service", "ai-gateway");

            assertThat(rateLimitCounter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should collect route lifecycle metrics")
        void shouldCollectRouteLifecycleMetrics() {
            metrics.incrementRouteCreated();
            metrics.incrementRouteDeleted();

            Counter createdCounter = meterRegistry.counter("ai.gateway.routes.created", "service", "ai-gateway");
            Counter deletedCounter = meterRegistry.counter("ai.gateway.routes.deleted", "service", "ai-gateway");

            assertThat(createdCounter.count()).isGreaterThan(0);
            assertThat(deletedCounter.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Metrics Tag Tests")
    class MetricsTagTests {

        @Test
        @DisplayName("Should include service tag in all metrics")
        void shouldIncludeServiceTagInAllMetrics() {
            metrics.incrementRequestTotal();

            Counter counter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTag("service")).isEqualTo("ai-gateway");
        }

        @Test
        @DisplayName("Should support tenant-specific metric tags")
        void shouldSupportTenantSpecificMetricTags() {
            metrics.recordRequest("tenant-1", "route-1", true, 100);

            var counter = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("tenant", "tenant-1")
                    .counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTag("tenant")).isEqualTo("tenant-1");
        }

        @Test
        @DisplayName("Should support route-specific metric tags")
        void shouldSupportRouteSpecificMetricTags() {
            metrics.recordRequest("tenant-1", "route-abc", true, 100);

            var counter = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("route", "route-abc")
                    .counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTag("route")).isEqualTo("route-abc");
        }

        @Test
        @DisplayName("Should support status tags in metrics")
        void shouldSupportStatusTagsInMetrics() {
            metrics.recordRequest("tenant-1", "route-1", false, 100);

            var failureCounter = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("status", "failure")
                    .counter();

            assertThat(failureCounter).isNotNull();
            assertThat(failureCounter.count()).isGreaterThan(0);
        }
    }

    private void recordCompliantMetrics() {
        // Gateway latency within SLO
        Timer gatewayTimer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
        for (int i = 0; i < 100; i++) {
            gatewayTimer.record(300 + (i % 150), TimeUnit.MILLISECONDS);
        }

        // Routing latency within SLO
        Timer routingTimer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
        for (int i = 0; i < 50; i++) {
            routingTimer.record(50 + (i % 40), TimeUnit.MILLISECONDS);
        }

        // Filter latency within SLO
        Timer filterTimer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
        for (int i = 0; i < 50; i++) {
            filterTimer.record(20 + (i % 25), TimeUnit.MILLISECONDS);
        }

        // Requests with acceptable error rate
        Counter totalCounter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
        Counter failureCounter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");
        totalCounter.increment(1000);
        failureCounter.increment(5);
    }
}
