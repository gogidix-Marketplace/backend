package com.gogidix.aiservices.airecommendationengineservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RecommendationEngineMetrics {

    private final MeterRegistry meterRegistry;

    private final Counter recommendationTotalCounter;
    private final Counter recommendationSuccessCounter;
    private final Counter recommendationFailureCounter;
    private final Counter recommendationsGeneratedCounter;

    private final Timer recommendationTimer;
    private final Timer mlInferenceTimer;

    public RecommendationEngineMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.recommendationTotalCounter = Counter.builder("ai.recommendation.engine.total")
                .description("Total number of recommendation requests")
                .tag("service", "ai-recommendation-engine")
                .register(meterRegistry);

        this.recommendationSuccessCounter = Counter.builder("ai.recommendation.engine.success")
                .description("Number of successful recommendation requests")
                .tag("service", "ai-recommendation-engine")
                .register(meterRegistry);

        this.recommendationFailureCounter = Counter.builder("ai.recommendation.engine.failure")
                .description("Number of failed recommendation requests")
                .tag("service", "ai-recommendation-engine")
                .register(meterRegistry);

        this.recommendationsGeneratedCounter = Counter.builder("ai.recommendation.engine.generated")
                .description("Number of recommendations generated")
                .tag("service", "ai-recommendation-engine")
                .register(meterRegistry);

        this.recommendationTimer = Timer.builder("ai.recommendation.engine.duration")
                .description("Recommendation processing time")
                .tag("service", "ai-recommendation-engine")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.mlInferenceTimer = Timer.builder("ai.recommendation.engine.ml.inference.duration")
                .description("ML inference time")
                .tag("service", "ai-recommendation-engine")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementRecommendationTotal() {
        recommendationTotalCounter.increment();
    }

    public void incrementRecommendationSuccess() {
        recommendationSuccessCounter.increment();
    }

    public void incrementRecommendationFailure() {
        recommendationFailureCounter.increment();
    }

    public void incrementRecommendationsGenerated() {
        recommendationsGeneratedCounter.increment();
    }

    public void recordRecommendationTime(long durationMs) {
        recommendationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startRecommendationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRecommendationTimer(Timer.Sample sample) {
        sample.stop(recommendationTimer);
    }

    public double getRecommendationLatencyP95() {
        return recommendationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getRecommendationLatencyP99() {
        return recommendationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getMlInferenceLatencyP95() {
        return mlInferenceTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) recommendationTotalCounter.count();
        long failures = (long) recommendationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
