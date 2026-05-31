package com.gogidix.aiservices.aigatewayservice.infrastructure.metrics;

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
 * Unit tests for GatewayMetrics.
 * Tests all counter and timer methods for SLO monitoring.
 */
@DisplayName("GatewayMetrics Tests")
class GatewayMetricsTest {

    private MeterRegistry meterRegistry;
    private GatewayMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new GatewayMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("Counter Increment Methods")
    class CounterIncrementTests {

        @Test
        @DisplayName("Should increment request total counter")
        void shouldIncrementRequestTotal() {
            metrics.incrementRequestTotal();

            Counter counter = meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway");
            assertThat(counter).isNotNull();
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment request success counter")
        void shouldIncrementRequestSuccess() {
            metrics.incrementRequestSuccess();

            Counter counter = meterRegistry.counter("ai.gateway.requests.success", "service", "ai-gateway");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment request failure counter")
        void shouldIncrementRequestFailure() {
            metrics.incrementRequestFailure();

            Counter counter = meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment route created counter")
        void shouldIncrementRouteCreated() {
            metrics.incrementRouteCreated();

            Counter counter = meterRegistry.counter("ai.gateway.routes.created", "service", "ai-gateway");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment route deleted counter")
        void shouldIncrementRouteDeleted() {
            metrics.incrementRouteDeleted();

            Counter counter = meterRegistry.counter("ai.gateway.routes.deleted", "service", "ai-gateway");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment circuit breaker tripped counter")
        void shouldIncrementCircuitBreakerTripped() {
            metrics.incrementCircuitBreakerTripped();

            Counter counter = meterRegistry.counter("ai.gateway.circuit.tripped", "service", "ai-gateway");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment rate limit exceeded counter")
        void shouldIncrementRateLimitExceeded() {
            metrics.incrementRateLimitExceeded();

            Counter counter = meterRegistry.counter("ai.gateway.ratelimit.exceeded", "service", "ai-gateway");
            assertThat(counter.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Timer Recording Methods")
    class TimerRecordingTests {

        @Test
        @DisplayName("Should record gateway latency in milliseconds")
        void shouldRecordGatewayLatency() {
            metrics.recordGatewayLatency(250);

            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isEqualTo(1);
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(250);
        }

        @Test
        @DisplayName("Should start and stop gateway timer")
        void shouldStartAndStopGatewayTimer() {
            Timer.Sample sample = metrics.startGatewayTimer();
            assertThat(sample).isNotNull();

            metrics.stopGatewayTimer(sample);

            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record routing time in milliseconds")
        void shouldRecordRoutingTime() {
            metrics.recordRoutingTime(50);

            Timer timer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
            assertThat(timer).isNotNull();
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(50);
        }

        @Test
        @DisplayName("Should start and stop routing timer")
        void shouldStartAndStopRoutingTimer() {
            Timer.Sample sample = metrics.startRoutingTimer();
            assertThat(sample).isNotNull();

            metrics.stopRoutingTimer(sample);

            Timer timer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record filter execution time in milliseconds")
        void shouldRecordFilterExecutionTime() {
            metrics.recordFilterExecutionTime(25);

            Timer timer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
            assertThat(timer).isNotNull();
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(25);
        }

        @Test
        @DisplayName("Should start and stop filter timer")
        void shouldStartAndStopFilterTimer() {
            Timer.Sample sample = metrics.startFilterTimer();
            assertThat(sample).isNotNull();

            metrics.stopFilterTimer(sample);

            Timer timer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("SLO Compliance Methods")
    class SloComplianceTests {

        @Test
        @DisplayName("Should get P95 gateway latency from timer")
        void shouldGetP95GatewayLatency() {
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);

            double p95 = metrics.getGatewayLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P99 gateway latency from timer")
        void shouldGetP99GatewayLatency() {
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(800, TimeUnit.MILLISECONDS);

            double p99 = metrics.getGatewayLatencyP99();

            assertThat(p99).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P95 routing latency")
        void shouldGetP95RoutingLatency() {
            Timer timer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
            timer.record(40, TimeUnit.MILLISECONDS);
            timer.record(60, TimeUnit.MILLISECONDS);

            double p95 = metrics.getRoutingLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P95 filter latency")
        void shouldGetP95FilterLatency() {
            Timer timer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
            timer.record(20, TimeUnit.MILLISECONDS);
            timer.record(30, TimeUnit.MILLISECONDS);

            double p95 = metrics.getFilterLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should calculate error rate correctly")
        void shouldCalculateErrorRate() {
            meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway").increment(100);
            meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway").increment(10);

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isEqualTo(0.1);
        }

        @Test
        @DisplayName("Should return zero error rate when no requests")
        void shouldReturnZeroErrorRateWhenNoRequests() {
            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("Meter Registry Access")
    class MeterRegistryTests {

        @Test
        @DisplayName("Should return meter registry instance")
        void shouldReturnMeterRegistry() {
            assertThat(metrics.getMeterRegistry()).isNotNull();
            assertThat(metrics.getMeterRegistry()).isEqualTo(meterRegistry);
        }
    }

    @Nested
    @DisplayName("Tagged Request Recording")
    class TaggedRequestTests {

        @Test
        @DisplayName("Should record tagged request with tenant and route")
        void shouldRecordTaggedRequest() {
            metrics.recordRequest("tenant-123", "route-456", true, 150);

            Counter counter = meterRegistry.counter("ai.gateway.requests.tagged",
                    "service", "ai-gateway",
                    "tenant", "tenant-123",
                    "route", "route-456",
                    "status", "success");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record failed tagged request")
        void shouldRecordFailedTaggedRequest() {
            metrics.recordRequest("tenant-123", "route-456", false, 150);

            Counter counter = meterRegistry.counter("ai.gateway.requests.tagged",
                    "service", "ai-gateway",
                    "tenant", "tenant-123",
                    "route", "route-456",
                    "status", "failure");
            assertThat(counter.count()).isGreaterThan(0);
        }
    }
}
