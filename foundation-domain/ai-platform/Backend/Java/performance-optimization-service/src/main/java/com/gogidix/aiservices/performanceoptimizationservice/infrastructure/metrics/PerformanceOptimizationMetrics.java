package com.gogidix.aiservices.performanceoptimizationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Performance Optimization Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class PerformanceOptimizationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter requestsTotalCounter;
    private final Counter requestsSuccessCounter;
    private final Counter requestsFailureCounter;
    private final Counter bottlenecksDetectedCounter;
    private final Counter recommendationsGeneratedCounter;
    private final Counter optimizationsAppliedCounter;

    // Timers
    private final Timer analysisLatencyTimer;
    private final Timer bottleneckDetectionTimer;
    private final Timer optimizationTimer;

    public PerformanceOptimizationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.requestsTotalCounter = Counter.builder("ai.performance.requests.total")
                .description("Total number of performance optimization requests")
                .tag("service", "ai-performance")
                .register(meterRegistry);

        this.requestsSuccessCounter = Counter.builder("ai.performance.requests.success")
                .description("Number of successful performance optimization requests")
                .tag("service", "ai-performance")
                .register(meterRegistry);

        this.requestsFailureCounter = Counter.builder("ai.performance.requests.failure")
                .description("Number of failed performance optimization requests")
                .tag("service", "ai-performance")
                .register(meterRegistry);

        this.bottlenecksDetectedCounter = Counter.builder("ai.performance.bottlenecks.detected")
                .description("Number of performance bottlenecks detected")
                .tag("service", "ai-performance")
                .register(meterRegistry);

        this.recommendationsGeneratedCounter = Counter.builder("ai.performance.recommendations.generated")
                .description("Number of optimization recommendations generated")
                .tag("service", "ai-performance")
                .register(meterRegistry);

        this.optimizationsAppliedCounter = Counter.builder("ai.performance.optimizations.applied")
                .description("Number of optimizations applied")
                .tag("service", "ai-performance")
                .register(meterRegistry);

        // Initialize timers
        this.analysisLatencyTimer = Timer.builder("ai.performance.analysis.duration")
                .description("Performance analysis processing time")
                .tag("service", "ai-performance")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.bottleneckDetectionTimer = Timer.builder("ai.performance.bottleneck.detection.duration")
                .description("Bottleneck detection time")
                .tag("service", "ai-performance")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.optimizationTimer = Timer.builder("ai.performance.optimization.duration")
                .description("Optimization application time")
                .tag("service", "ai-performance")
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

    public void incrementBottlenecksDetected() {
        bottlenecksDetectedCounter.increment();
    }

    public void incrementRecommendationsGenerated() {
        recommendationsGeneratedCounter.increment();
    }

    public void incrementOptimizationsApplied() {
        optimizationsAppliedCounter.increment();
    }

    // Timer methods
    public void recordAnalysisTime(long durationMs) {
        analysisLatencyTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAnalysisTimer(Timer.Sample sample) {
        sample.stop(analysisLatencyTimer);
    }

    public Timer.Sample startBottleneckDetectionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopBottleneckDetectionTimer(Timer.Sample sample) {
        sample.stop(bottleneckDetectionTimer);
    }

    public Timer.Sample startOptimizationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOptimizationTimer(Timer.Sample sample) {
        sample.stop(optimizationTimer);
    }

    // SLO compliance methods
    public double getAnalysisLatencyP95() {
        return analysisLatencyTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAnalysisLatencyP99() {
        return analysisLatencyTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getBottleneckDetectionLatencyP95() {
        return bottleneckDetectionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
