package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Supply Chain Optimization Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class SupplyChainMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter optimizationTotalCounter;
    private final Counter optimizationSuccessCounter;
    private final Counter optimizationFailureCounter;
    private final Counter recommendationsGeneratedCounter;
    private final Counter costSavingsCounter;

    // Timers
    private final Timer optimizationTimer;
    private final Timer aiPredictionTimer;
    private final Timer recommendationTimer;

    public SupplyChainMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters with ai.supply.chain prefix
        this.optimizationTotalCounter = Counter.builder("ai.supply.chain.optimization.total")
                .description("Total number of optimization requests")
                .tag("service", "supply-chain-optimization")
                .register(meterRegistry);

        this.optimizationSuccessCounter = Counter.builder("ai.supply.chain.optimization.success")
                .description("Number of successful optimization requests")
                .tag("service", "supply-chain-optimization")
                .register(meterRegistry);

        this.optimizationFailureCounter = Counter.builder("ai.supply.chain.optimization.failure")
                .description("Number of failed optimization requests")
                .tag("service", "supply-chain-optimization")
                .register(meterRegistry);

        this.recommendationsGeneratedCounter = Counter.builder("ai.supply.chain.recommendations.generated")
                .description("Number of recommendations generated")
                .tag("service", "supply-chain-optimization")
                .register(meterRegistry);

        this.costSavingsCounter = Counter.builder("ai.supply.chain.cost.savings")
                .description("Accumulated cost savings from optimizations")
                .tag("service", "supply-chain-optimization")
                .register(meterRegistry);

        // Initialize timers
        this.optimizationTimer = Timer.builder("ai.supply.chain.optimization.duration")
                .description("Optimization processing time")
                .tag("service", "supply-chain-optimization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.aiPredictionTimer = Timer.builder("ai.supply.chain.prediction.duration")
                .description("AI model prediction time for optimization")
                .tag("service", "supply-chain-optimization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.recommendationTimer = Timer.builder("ai.supply.chain.recommendation.duration")
                .description("Recommendation generation time")
                .tag("service", "supply-chain-optimization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementOptimizationTotal() {
        optimizationTotalCounter.increment();
    }

    public void incrementOptimizationSuccess() {
        optimizationSuccessCounter.increment();
    }

    public void incrementOptimizationFailure() {
        optimizationFailureCounter.increment();
    }

    public void incrementRecommendationsGenerated() {
        recommendationsGeneratedCounter.increment();
    }

    public void recordCostSavings(double amount) {
        costSavingsCounter.increment(amount);
    }

    // Timer methods
    public void recordOptimizationTime(long durationMs) {
        optimizationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startOptimizationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOptimizationTimer(Timer.Sample sample) {
        sample.stop(optimizationTimer);
    }

    public Timer.Sample startAiPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAiPredictionTimer(Timer.Sample sample) {
        sample.stop(aiPredictionTimer);
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

    // SLO compliance methods
    public double getOptimizationLatencyP95() {
        return optimizationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getOptimizationLatencyP99() {
        return optimizationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getAiPredictionLatencyP95() {
        return aiPredictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
