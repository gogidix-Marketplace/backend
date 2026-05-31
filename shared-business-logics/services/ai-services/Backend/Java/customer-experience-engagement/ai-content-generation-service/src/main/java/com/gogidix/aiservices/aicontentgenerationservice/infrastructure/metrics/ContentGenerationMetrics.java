package com.gogidix.aiservices.aicontentgenerationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Content Generation Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class ContentGenerationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter generationTotalCounter;
    private final Counter generationSuccessCounter;
    private final Counter generationFailureCounter;
    private final Counter contentGeneratedCounter;
    private final Counter contentOptimizedCounter;
    private final Counter templateUsedCounter;

    // Timers
    private final Timer generationTimer;
    private final Timer aiGenerationTimer;
    private final Timer optimizationTimer;

    public ContentGenerationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.generationTotalCounter = Counter.builder("ai.content.generation.total")
                .description("Total number of content generation requests")
                .tag("service", "ai-content-generation")
                .register(meterRegistry);

        this.generationSuccessCounter = Counter.builder("ai.content.generation.success")
                .description("Number of successful content generation requests")
                .tag("service", "ai-content-generation")
                .register(meterRegistry);

        this.generationFailureCounter = Counter.builder("ai.content.generation.failure")
                .description("Number of failed content generation requests")
                .tag("service", "ai-content-generation")
                .register(meterRegistry);

        this.contentGeneratedCounter = Counter.builder("ai.content.generation.generated")
                .description("Number of content items generated")
                .tag("service", "ai-content-generation")
                .register(meterRegistry);

        this.contentOptimizedCounter = Counter.builder("ai.content.generation.optimized")
                .description("Number of content items optimized")
                .tag("service", "ai-content-generation")
                .register(meterRegistry);

        this.templateUsedCounter = Counter.builder("ai.content.generation.template.used")
                .description("Number of times templates were used")
                .tag("service", "ai-content-generation")
                .register(meterRegistry);

        // Initialize timers
        this.generationTimer = Timer.builder("ai.content.generation.duration")
                .description("Content generation processing time")
                .tag("service", "ai-content-generation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.aiGenerationTimer = Timer.builder("ai.content.generation.ai.duration")
                .description("AI content generation time")
                .tag("service", "ai-content-generation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.optimizationTimer = Timer.builder("ai.content.generation.optimization.duration")
                .description("Content optimization time")
                .tag("service", "ai-content-generation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementGenerationTotal() {
        generationTotalCounter.increment();
    }

    public void incrementGenerationSuccess() {
        generationSuccessCounter.increment();
    }

    public void incrementGenerationFailure() {
        generationFailureCounter.increment();
    }

    public void incrementContentGenerated() {
        contentGeneratedCounter.increment();
    }

    public void incrementContentOptimized() {
        contentOptimizedCounter.increment();
    }

    public void incrementTemplateUsed() {
        templateUsedCounter.increment();
    }

    // Timer methods
    public void recordGenerationTime(long durationMs) {
        generationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopGenerationTimer(Timer.Sample sample) {
        sample.stop(generationTimer);
    }

    public Timer.Sample startAiGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAiGenerationTimer(Timer.Sample sample) {
        sample.stop(aiGenerationTimer);
    }

    public Timer.Sample startOptimizationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOptimizationTimer(Timer.Sample sample) {
        sample.stop(optimizationTimer);
    }

    // SLO compliance methods
    public double getGenerationLatencyP95() {
        return generationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getGenerationLatencyP99() {
        return generationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getAiGenerationLatencyP95() {
        return aiGenerationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) generationTotalCounter.count();
        long failures = (long) generationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
