package com.gogidix.aiservices.airecommendationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Recommendation Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class RecommendationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter recommendationTotalCounter;
    private final Counter recommendationSuccessCounter;
    private final Counter recommendationFailureCounter;
    private final Counter similarItemsCounter;
    private final Counter trendingItemsCounter;
    private final Counter interactionTrackedCounter;

    // Timers
    private final Timer recommendationTimer;
    private final Timer mlPredictionTimer;
    private final Timer databaseSaveTimer;

    public RecommendationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.recommendationTotalCounter = Counter.builder("recommendation.total")
                .description("Total number of recommendation requests")
                .tag("service", "ai-recommendation")
                .register(meterRegistry);

        this.recommendationSuccessCounter = Counter.builder("recommendation.success")
                .description("Number of successful recommendation requests")
                .tag("service", "ai-recommendation")
                .register(meterRegistry);

        this.recommendationFailureCounter = Counter.builder("recommendation.failure")
                .description("Number of failed recommendation requests")
                .tag("service", "ai-recommendation")
                .register(meterRegistry);

        this.similarItemsCounter = Counter.builder("recommendation.similar.total")
                .description("Number of similar items requests")
                .tag("service", "ai-recommendation")
                .register(meterRegistry);

        this.trendingItemsCounter = Counter.builder("recommendation.trending.total")
                .description("Number of trending items requests")
                .tag("service", "ai-recommendation")
                .register(meterRegistry);

        this.interactionTrackedCounter = Counter.builder("recommendation.interaction.total")
                .description("Number of interactions tracked")
                .tag("service", "ai-recommendation")
                .register(meterRegistry);

        // Initialize timers
        this.recommendationTimer = Timer.builder("recommendation.duration")
                .description("Recommendation processing time")
                .tag("service", "ai-recommendation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.mlPredictionTimer = Timer.builder("recommendation.ml.prediction.duration")
                .description("ML model prediction time")
                .tag("service", "ai-recommendation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("recommendation.database.save.duration")
                .description("Database save operation time")
                .tag("service", "ai-recommendation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementRecommendationTotal() {
        recommendationTotalCounter.increment();
    }

    public void incrementRecommendationSuccess() {
        recommendationSuccessCounter.increment();
    }

    public void incrementRecommendationFailure() {
        recommendationFailureCounter.increment();
    }

    public void incrementSimilarItems() {
        similarItemsCounter.increment();
    }

    public void incrementTrendingItems() {
        trendingItemsCounter.increment();
    }

    public void incrementInteractionTracked() {
        interactionTrackedCounter.increment();
    }

    // Timer methods
    public void recordRecommendationTime(long durationMs) {
        recommendationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startRecommendationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRecommendationTimer(Timer.Sample sample) {
        sample.stop(recommendationTimer);
    }

    public Timer.Sample startMlPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopMlPredictionTimer(Timer.Sample sample) {
        sample.stop(mlPredictionTimer);
    }

    public void recordDatabaseSaveTime(long durationMs) {
        databaseSaveTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDatabaseSaveTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDatabaseSaveTimer(Timer.Sample sample) {
        sample.stop(databaseSaveTimer);
    }

    // SLO compliance methods
    public double getRecommendationLatencyP95() {
        return recommendationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getRecommendationLatencyP99() {
        return recommendationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getMlPredictionLatencyP95() {
        return mlPredictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
