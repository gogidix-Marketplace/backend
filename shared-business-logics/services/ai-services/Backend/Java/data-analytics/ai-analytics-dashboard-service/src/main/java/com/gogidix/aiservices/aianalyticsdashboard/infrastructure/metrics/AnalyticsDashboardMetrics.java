package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Analytics Dashboard Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class AnalyticsDashboardMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter dashboardTotalCounter;
    private final Counter dashboardSuccessCounter;
    private final Counter dashboardFailureCounter;
    private final Counter chartRenderCounter;
    private final Counter dataFetchCounter;
    private final Counter userSessionCounter;

    // Timers
    private final Timer dashboardTimer;
    private final Timer chartRenderTimer;
    private final Timer dataFetchTimer;
    private final Timer queryExecutionTimer;

    public AnalyticsDashboardMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.dashboardTotalCounter = Counter.builder("ai.data.visualization.total")
                .description("Total number of dashboard requests")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.dashboardSuccessCounter = Counter.builder("ai.data.visualization.success")
                .description("Number of successful dashboard requests")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.dashboardFailureCounter = Counter.builder("ai.data.visualization.failure")
                .description("Number of failed dashboard requests")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.chartRenderCounter = Counter.builder("ai.data.visualization.chart.render")
                .description("Number of charts rendered")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.dataFetchCounter = Counter.builder("ai.data.visualization.data.fetch")
                .description("Number of data fetches")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        this.userSessionCounter = Counter.builder("ai.data.visualization.user.session")
                .description("Number of user sessions")
                .tag("service", "ai-analytics-dashboard")
                .register(meterRegistry);

        // Initialize timers
        this.dashboardTimer = Timer.builder("ai.data.visualization.duration")
                .description("Dashboard loading time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.chartRenderTimer = Timer.builder("ai.data.visualization.chart.duration")
                .description("Chart rendering time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.dataFetchTimer = Timer.builder("ai.data.visualization.data.duration")
                .description("Data fetch time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.queryExecutionTimer = Timer.builder("ai.data.visualization.query.duration")
                .description("Query execution time")
                .tag("service", "ai-analytics-dashboard")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementDashboardTotal() { dashboardTotalCounter.increment(); }
    public void incrementDashboardSuccess() { dashboardSuccessCounter.increment(); }
    public void incrementDashboardFailure() { dashboardFailureCounter.increment(); }
    public void incrementChartRender() { chartRenderCounter.increment(); }
    public void incrementDataFetch() { dataFetchCounter.increment(); }
    public void incrementUserSession() { userSessionCounter.increment(); }

    // Timer methods
    public void recordDashboardTime(long durationMs) { dashboardTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public Timer.Sample startDashboardTimer() { return Timer.start(meterRegistry); }
    public void stopDashboardTimer(Timer.Sample sample) { sample.stop(dashboardTimer); }

    public void recordChartRenderTime(long durationMs) { chartRenderTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public Timer.Sample startChartRenderTimer() { return Timer.start(meterRegistry); }
    public void stopChartRenderTimer(Timer.Sample sample) { sample.stop(chartRenderTimer); }

    public void recordDataFetchTime(long durationMs) { dataFetchTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public Timer.Sample startDataFetchTimer() { return Timer.start(meterRegistry); }
    public void stopDataFetchTimer(Timer.Sample sample) { sample.stop(dataFetchTimer); }

    public void recordQueryExecutionTime(long durationMs) { queryExecutionTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public Timer.Sample startQueryExecutionTimer() { return Timer.start(meterRegistry); }
    public void stopQueryExecutionTimer(Timer.Sample sample) { sample.stop(queryExecutionTimer); }

    // SLO compliance methods
    public double getDashboardLatencyP95() { return dashboardTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getDashboardLatencyP99() { return dashboardTimer.percentile(0.99, TimeUnit.MILLISECONDS); }
    public double getQueryExecutionLatencyP95() { return queryExecutionTimer.percentile(0.95, TimeUnit.MILLISECONDS); }

    public double getErrorRate() {
        long total = (long) dashboardTotalCounter.count();
        long failures = (long) dashboardFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() { return meterRegistry; }
}
