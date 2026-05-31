package com.gogidix.aiservices.aigatewayservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Gateway Service.
 * Tracks key performance indicators for SLO monitoring.
 * Metrics prefix: ai.gateway
 */
@Component
public class GatewayMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter requestTotalCounter;
    private final Counter requestSuccessCounter;
    private final Counter requestFailureCounter;
    private final Counter routeCreatedCounter;
    private final Counter routeDeletedCounter;
    private final Counter circuitBreakerTrippedCounter;
    private final Counter rateLimitExceededCounter;

    // Timers
    private final Timer gatewayLatencyTimer;
    private final Timer routingTimer;
    private final Timer filterExecutionTimer;
    private final Timer circuitBreakerTimer;

    public GatewayMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters with ai.gateway prefix
        this.requestTotalCounter = Counter.builder("ai.gateway.requests.total")
                .description("Total number of gateway requests")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.requestSuccessCounter = Counter.builder("ai.gateway.requests.success")
                .description("Number of successful gateway requests")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.requestFailureCounter = Counter.builder("ai.gateway.requests.failure")
                .description("Number of failed gateway requests")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.routeCreatedCounter = Counter.builder("ai.gateway.routes.created")
                .description("Number of routes created")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.routeDeletedCounter = Counter.builder("ai.gateway.routes.deleted")
                .description("Number of routes deleted")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.circuitBreakerTrippedCounter = Counter.builder("ai.gateway.circuit.tripped")
                .description("Number of circuit breaker trips")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.rateLimitExceededCounter = Counter.builder("ai.gateway.ratelimit.exceeded")
                .description("Number of rate limit violations")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        // Initialize timers with percentile publishing
        this.gatewayLatencyTimer = Timer.builder("ai.gateway.latency")
                .description("Gateway request processing time")
                .tag("service", "ai-gateway")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.routingTimer = Timer.builder("ai.gateway.routing.duration")
                .description("Route lookup and matching time")
                .tag("service", "ai-gateway")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.filterExecutionTimer = Timer.builder("ai.gateway.filter.duration")
                .description("Filter execution time")
                .tag("service", "ai-gateway")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.circuitBreakerTimer = Timer.builder("ai.gateway.circuit.duration")
                .description("Circuit breaker state check time")
                .tag("service", "ai-gateway")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementRequestTotal() {
        requestTotalCounter.increment();
    }

    public void incrementRequestSuccess() {
        requestSuccessCounter.increment();
    }

    public void incrementRequestFailure() {
        requestFailureCounter.increment();
    }

    public void incrementRouteCreated() {
        routeCreatedCounter.increment();
    }

    public void incrementRouteDeleted() {
        routeDeletedCounter.increment();
    }

    public void incrementCircuitBreakerTripped() {
        circuitBreakerTrippedCounter.increment();
    }

    public void incrementRateLimitExceeded() {
        rateLimitExceededCounter.increment();
    }

    // Timer methods
    public void recordGatewayLatency(long durationMs) {
        gatewayLatencyTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startGatewayTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopGatewayTimer(Timer.Sample sample) {
        sample.stop(gatewayLatencyTimer);
    }

    public void recordRoutingTime(long durationMs) {
        routingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startRoutingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRoutingTimer(Timer.Sample sample) {
        sample.stop(routingTimer);
    }

    public void recordFilterExecutionTime(long durationMs) {
        filterExecutionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startFilterTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopFilterTimer(Timer.Sample sample) {
        sample.stop(filterExecutionTimer);
    }

    public void recordCircuitBreakerTime(long durationMs) {
        circuitBreakerTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    // SLO compliance methods
    public double getGatewayLatencyP95() {
        return gatewayLatencyTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getGatewayLatencyP99() {
        return gatewayLatencyTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getRoutingLatencyP95() {
        return routingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getFilterLatencyP95() {
        return filterExecutionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) requestTotalCounter.count();
        long failures = (long) requestFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }

    /**
     * Record a request with tenant and route tags
     */
    public void recordRequest(String tenantId, String routeId, boolean success, long durationMs) {
        Counter.builder("ai.gateway.requests.tagged")
                .tag("service", "ai-gateway")
                .tag("tenant", tenantId)
                .tag("route", routeId)
                .tag("status", success ? "success" : "failure")
                .register(meterRegistry)
                .increment();

        Timer.builder("ai.gateway.latency.tagged")
                .tag("service", "ai-gateway")
                .tag("tenant", tenantId)
                .tag("route", routeId)
                .register(meterRegistry)
                .record(durationMs, TimeUnit.MILLISECONDS);
    }
}
