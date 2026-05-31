package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Analytics Dashboard Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class DashboardMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter dashboardCreatedCounter;
    private final Counter dashboardUpdatedCounter;
    private final Counter dashboardDeletedCounter;
    private final Counter dashboardViewCounter;
    private final Counter widgetAddedCounter;
    private final Counter widgetRemovedCounter;
    private final Counter metricQueryCounter;
    private final Counter metricQuerySuccessCounter;
    private final Counter metricQueryFailureCounter;

    // Timers
    private final Timer dashboardCreationTimer;
    private final Timer dashboardUpdateTimer;
    private final Timer dashboardQueryTimer;
    private final Timer metricAggregationTimer;
    private final Timer widgetOperationTimer;

    public DashboardMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.dashboardCreatedCounter = Counter.builder("dashboard.created")
                .description("Total number of dashboards created")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.dashboardUpdatedCounter = Counter.builder("dashboard.updated")
                .description("Total number of dashboards updated")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.dashboardDeletedCounter = Counter.builder("dashboard.deleted")
                .description("Total number of dashboards deleted")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.dashboardViewCounter = Counter.builder("dashboard.view")
                .description("Total number of dashboard views")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.widgetAddedCounter = Counter.builder("widget.added")
                .description("Total number of widgets added")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.widgetRemovedCounter = Counter.builder("widget.removed")
                .description("Total number of widgets removed")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.metricQueryCounter = Counter.builder("metric.query.total")
                .description("Total number of metric queries")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.metricQuerySuccessCounter = Counter.builder("metric.query.success")
                .description("Number of successful metric queries")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.metricQueryFailureCounter = Counter.builder("metric.query.failure")
                .description("Number of failed metric queries")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        // Initialize timers
        this.dashboardCreationTimer = Timer.builder("dashboard.creation.duration")
                .description("Dashboard creation processing time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.dashboardUpdateTimer = Timer.builder("dashboard.update.duration")
                .description("Dashboard update processing time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.dashboardQueryTimer = Timer.builder("dashboard.query.duration")
                .description("Dashboard query processing time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.metricAggregationTimer = Timer.builder("metric.aggregation.duration")
                .description("Metric aggregation processing time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.widgetOperationTimer = Timer.builder("widget.operation.duration")
                .description("Widget operation processing time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementDashboardCreated() {
        dashboardCreatedCounter.increment();
    }

    public void incrementDashboardUpdated() {
        dashboardUpdatedCounter.increment();
    }

    public void incrementDashboardDeleted() {
        dashboardDeletedCounter.increment();
    }

    public void incrementDashboardView() {
        dashboardViewCounter.increment();
    }

    public void incrementWidgetAdded() {
        widgetAddedCounter.increment();
    }

    public void incrementWidgetRemoved() {
        widgetRemovedCounter.increment();
    }

    public void incrementMetricQuery() {
        metricQueryCounter.increment();
    }

    public void incrementMetricQuerySuccess() {
        metricQuerySuccessCounter.increment();
    }

    public void incrementMetricQueryFailure() {
        metricQueryFailureCounter.increment();
    }

    // Timer methods
    public void recordDashboardCreationTime(long durationMs) {
        dashboardCreationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDashboardCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDashboardCreationTimer(Timer.Sample sample) {
        sample.stop(dashboardCreationTimer);
    }

    public void recordDashboardUpdateTime(long durationMs) {
        dashboardUpdateTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDashboardUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDashboardUpdateTimer(Timer.Sample sample) {
        sample.stop(dashboardUpdateTimer);
    }

    public void recordDashboardQueryTime(long durationMs) {
        dashboardQueryTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDashboardQueryTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDashboardQueryTimer(Timer.Sample sample) {
        sample.stop(dashboardQueryTimer);
    }

    public void recordMetricAggregationTime(long durationMs) {
        metricAggregationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startMetricAggregationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopMetricAggregationTimer(Timer.Sample sample) {
        sample.stop(metricAggregationTimer);
    }

    public void recordWidgetOperationTime(long durationMs) {
        widgetOperationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startWidgetOperationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopWidgetOperationTimer(Timer.Sample sample) {
        sample.stop(widgetOperationTimer);
    }

    // SLO compliance methods
    public double getDashboardCreationLatencyP95() {
        return dashboardCreationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getDashboardCreationLatencyP99() {
        return dashboardCreationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getDashboardQueryLatencyP95() {
        return dashboardQueryTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getMetricAggregationLatencyP95() {
        return metricAggregationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) metricQueryCounter.count();
        long failures = (long) metricQueryFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
