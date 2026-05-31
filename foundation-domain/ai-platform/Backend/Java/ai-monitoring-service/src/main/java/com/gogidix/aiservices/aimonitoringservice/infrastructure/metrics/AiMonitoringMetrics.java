package com.gogidix.aiservices.aimonitoringservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Monitoring Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class AiMonitoringMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter requestsTotalCounter;
    private final Counter requestsSuccessCounter;
    private final Counter requestsFailureCounter;
    private final Counter alertsTriggeredCounter;
    private final Counter healthChecksTotalCounter;
    private final Counter healthChecksPassedCounter;
    private final Counter healthChecksFailedCounter;

    // Timers
    private final Timer monitoringLatencyTimer;
    private final Timer healthCheckLatencyTimer;
    private final Timer alertEvaluationTimer;

    public AiMonitoringMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.requestsTotalCounter = Counter.builder("ai.monitoring.requests.total")
                .description("Total number of monitoring requests")
                .tag("service", "ai-monitoring")
                .register(meterRegistry);

        this.requestsSuccessCounter = Counter.builder("ai.monitoring.requests.success")
                .description("Number of successful monitoring requests")
                .tag("service", "ai-monitoring")
                .register(meterRegistry);

        this.requestsFailureCounter = Counter.builder("ai.monitoring.requests.failure")
                .description("Number of failed monitoring requests")
                .tag("service", "ai-monitoring")
                .register(meterRegistry);

        this.alertsTriggeredCounter = Counter.builder("ai.monitoring.alerts.triggered")
                .description("Number of alerts triggered")
                .tag("service", "ai-monitoring")
                .register(meterRegistry);

        this.healthChecksTotalCounter = Counter.builder("ai.monitoring.healthchecks.total")
                .description("Total number of health checks performed")
                .tag("service", "ai-monitoring")
                .register(meterRegistry);

        this.healthChecksPassedCounter = Counter.builder("ai.monitoring.healthchecks.passed")
                .description("Number of health checks passed")
                .tag("service", "ai-monitoring")
                .register(meterRegistry);

        this.healthChecksFailedCounter = Counter.builder("ai.monitoring.healthchecks.failed")
                .description("Number of health checks failed")
                .tag("service", "ai-monitoring")
                .register(meterRegistry);

        // Initialize timers
        this.monitoringLatencyTimer = Timer.builder("ai.monitoring.duration")
                .description("Monitoring request processing time")
                .tag("service", "ai-monitoring")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.healthCheckLatencyTimer = Timer.builder("ai.monitoring.healthcheck.duration")
                .description("Health check execution time")
                .tag("service", "ai-monitoring")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.alertEvaluationTimer = Timer.builder("ai.monitoring.alert.evaluation.duration")
                .description("Alert rule evaluation time")
                .tag("service", "ai-monitoring")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementRequestsTotal() {
        requestsTotalCounter.increment();
    }

    public void incrementRequestsSuccess() {
        requestsSuccessCounter.increment();
    }

    public void incrementRequestsFailure() {
        requestsFailureCounter.increment();
    }

    public void incrementAlertsTriggered() {
        alertsTriggeredCounter.increment();
    }

    public void incrementHealthChecksTotal() {
        healthChecksTotalCounter.increment();
    }

    public void incrementHealthChecksPassed() {
        healthChecksPassedCounter.increment();
    }

    public void incrementHealthChecksFailed() {
        healthChecksFailedCounter.increment();
    }

    // Timer methods
    public void recordMonitoringTime(long durationMs) {
        monitoringLatencyTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startMonitoringTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopMonitoringTimer(Timer.Sample sample) {
        sample.stop(monitoringLatencyTimer);
    }

    public Timer.Sample startHealthCheckTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopHealthCheckTimer(Timer.Sample sample) {
        sample.stop(healthCheckLatencyTimer);
    }

    public Timer.Sample startAlertEvaluationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAlertEvaluationTimer(Timer.Sample sample) {
        sample.stop(alertEvaluationTimer);
    }

    // SLO compliance methods
    public double getMonitoringLatencyP95() {
        return monitoringLatencyTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getMonitoringLatencyP99() {
        return monitoringLatencyTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getHealthCheckLatencyP95() {
        return healthCheckLatencyTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) requestsTotalCounter.count();
        long failures = (long) requestsFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
