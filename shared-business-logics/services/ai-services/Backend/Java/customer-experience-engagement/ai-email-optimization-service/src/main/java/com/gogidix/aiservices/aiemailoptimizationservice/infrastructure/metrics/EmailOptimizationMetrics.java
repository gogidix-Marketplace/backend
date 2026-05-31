package com.gogidix.aiservices.aiemailoptimizationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class EmailOptimizationMetrics {

    private final MeterRegistry meterRegistry;

    private final Counter optimizationTotalCounter;
    private final Counter optimizationSuccessCounter;
    private final Counter optimizationFailureCounter;
    private final Counter emailsOptimizedCounter;

    private final Timer optimizationTimer;
    private final Timer aiAnalysisTimer;

    public EmailOptimizationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.optimizationTotalCounter = Counter.builder("ai.email.optimization.total")
                .description("Total number of email optimization requests")
                .tag("service", "ai-email-optimization")
                .register(meterRegistry);

        this.optimizationSuccessCounter = Counter.builder("ai.email.optimization.success")
                .description("Number of successful email optimization requests")
                .tag("service", "ai-email-optimization")
                .register(meterRegistry);

        this.optimizationFailureCounter = Counter.builder("ai.email.optimization.failure")
                .description("Number of failed email optimization requests")
                .tag("service", "ai-email-optimization")
                .register(meterRegistry);

        this.emailsOptimizedCounter = Counter.builder("ai.email.optimization.optimized")
                .description("Number of emails optimized")
                .tag("service", "ai-email-optimization")
                .register(meterRegistry);

        this.optimizationTimer = Timer.builder("ai.email.optimization.duration")
                .description("Email optimization processing time")
                .tag("service", "ai-email-optimization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.aiAnalysisTimer = Timer.builder("ai.email.optimization.ai.analysis.duration")
                .description("AI analysis time")
                .tag("service", "ai-email-optimization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementOptimizationTotal() {
        optimizationTotalCounter.increment();
    }

    public void incrementOptimizationSuccess() {
        optimizationSuccessCounter.increment();
    }

    public void incrementOptimizationFailure() {
        optimizationFailureCounter.increment();
    }

    public void incrementEmailsOptimized() {
        emailsOptimizedCounter.increment();
    }

    public void recordOptimizationTime(long durationMs) {
        optimizationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startOptimizationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOptimizationTimer(Timer.Sample sample) {
        sample.stop(optimizationTimer);
    }

    public double getOptimizationLatencyP95() {
        return optimizationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getOptimizationLatencyP99() {
        return optimizationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getAiAnalysisLatencyP95() {
        return aiAnalysisTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) optimizationTotalCounter.count();
        long failures = (long) optimizationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
