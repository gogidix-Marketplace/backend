package com.gogidix.aiservices.aigatewayservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Gateway Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class AiGatewayMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter routeRequestTotalCounter;
    private final Counter routeRequestSuccessCounter;
    private final Counter routeRequestFailureCounter;
    private final Counter circuitBreakerTrippedCounter;
    private final Counter routeCreatedCounter;
    private final Counter routeUpdatedCounter;
    private final Counter routeDeletedCounter;

    // Timers
    private final Timer routeRequestTimer;
    private final Timer routeCreationTimer;
    private final Timer routeUpdateTimer;
    private final Timer circuitBreakerResetTimer;

    public AiGatewayMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.routeRequestTotalCounter = Counter.builder("gateway.route.request.total")
                .description("Total number of gateway route requests")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.routeRequestSuccessCounter = Counter.builder("gateway.route.request.success")
                .description("Number of successful gateway route requests")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.routeRequestFailureCounter = Counter.builder("gateway.route.request.failure")
                .description("Number of failed gateway route requests")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.circuitBreakerTrippedCounter = Counter.builder("gateway.circuitbreaker.tripped")
                .description("Number of times circuit breaker was tripped")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.routeCreatedCounter = Counter.builder("gateway.route.created")
                .description("Number of routes created")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.routeUpdatedCounter = Counter.builder("gateway.route.updated")
                .description("Number of routes updated")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        this.routeDeletedCounter = Counter.builder("gateway.route.deleted")
                .description("Number of routes deleted")
                .tag("service", "ai-gateway")
                .register(meterRegistry);

        // Initialize timers
        this.routeRequestTimer = Timer.builder("gateway.route.request.duration")
                .description("Gateway route request processing time")
                .tag("service", "ai-gateway")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.routeCreationTimer = Timer.builder("gateway.route.creation.duration")
                .description("Route creation operation time")
                .tag("service", "ai-gateway")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.routeUpdateTimer = Timer.builder("gateway.route.update.duration")
                .description("Route update operation time")
                .tag("service", "ai-gateway")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.circuitBreakerResetTimer = Timer.builder("gateway.circuitbreaker.reset.duration")
                .description("Circuit breaker reset operation time")
                .tag("service", "ai-gateway")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementRouteRequestTotal() {
        routeRequestTotalCounter.increment();
    }

    public void incrementRouteRequestSuccess() {
        routeRequestSuccessCounter.increment();
    }

    public void incrementRouteRequestFailure() {
        routeRequestFailureCounter.increment();
    }

    public void incrementCircuitBreakerTripped() {
        circuitBreakerTrippedCounter.increment();
    }

    public void incrementRouteCreated() {
        routeCreatedCounter.increment();
    }

    public void incrementRouteUpdated() {
        routeUpdatedCounter.increment();
    }

    public void incrementRouteDeleted() {
        routeDeletedCounter.increment();
    }

    // Timer methods
    public void recordRouteRequestTime(long durationMs) {
        routeRequestTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startRouteRequestTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRouteRequestTimer(Timer.Sample sample) {
        sample.stop(routeRequestTimer);
    }

    public void recordRouteCreationTime(long durationMs) {
        routeCreationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startRouteCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRouteCreationTimer(Timer.Sample sample) {
        sample.stop(routeCreationTimer);
    }

    public void recordRouteUpdateTime(long durationMs) {
        routeUpdateTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startRouteUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRouteUpdateTimer(Timer.Sample sample) {
        sample.stop(routeUpdateTimer);
    }

    public void recordCircuitBreakerResetTime(long durationMs) {
        circuitBreakerResetTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    // SLO compliance methods
    public double getRouteRequestLatencyP95() {
        return routeRequestTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getRouteRequestLatencyP99() {
        return routeRequestTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getRouteCreationLatencyP95() {
        return routeCreationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getRouteUpdateLatencyP95() {
        return routeUpdateTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) routeRequestTotalCounter.count();
        long failures = (long) routeRequestFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public double getSuccessRate() {
        long total = (long) routeRequestTotalCounter.count();
        long successes = (long) routeRequestSuccessCounter.count();
        return total > 0 ? (double) successes / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
