package com.gogidix.aiservices.aifeatureextractionservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Feature Extraction Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class FeatureExtractionMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter extractionTotalCounter;
    private final Counter extractionSuccessCounter;
    private final Counter extractionFailureCounter;
    private final Counter featuresExtractedCounter;
    private final Counter featureSetsProcessedCounter;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;

    // Timers
    private final Timer extractionTimer;
    private final Timer featureStoreWriteTimer;
    private final Timer databaseSaveTimer;

    public FeatureExtractionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.extractionTotalCounter = Counter.builder("ai.feature.extraction.total")
                .description("Total number of feature extraction requests")
                .tag("service", "ai-feature-extraction")
                .register(meterRegistry);

        this.extractionSuccessCounter = Counter.builder("ai.feature.extraction.success")
                .description("Number of successful feature extraction requests")
                .tag("service", "ai-feature-extraction")
                .register(meterRegistry);

        this.extractionFailureCounter = Counter.builder("ai.feature.extraction.failure")
                .description("Number of failed feature extraction requests")
                .tag("service", "ai-feature-extraction")
                .register(meterRegistry);

        this.featuresExtractedCounter = Counter.builder("ai.feature.extracted")
                .description("Number of features extracted")
                .tag("service", "ai-feature-extraction")
                .register(meterRegistry);

        this.featureSetsProcessedCounter = Counter.builder("ai.feature.set.processed")
                .description("Number of feature sets processed")
                .tag("service", "ai-feature-extraction")
                .register(meterRegistry);

        this.cacheHitCounter = Counter.builder("ai.feature.cache.hit")
                .description("Number of cache hits")
                .tag("service", "ai-feature-extraction")
                .register(meterRegistry);

        this.cacheMissCounter = Counter.builder("ai.feature.cache.miss")
                .description("Number of cache misses")
                .tag("service", "ai-feature-extraction")
                .register(meterRegistry);

        // Initialize timers
        this.extractionTimer = Timer.builder("ai.feature.extraction.duration")
                .description("Feature extraction processing time")
                .tag("service", "ai-feature-extraction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.featureStoreWriteTimer = Timer.builder("ai.feature.store.write.duration")
                .description("Feature store write operation time")
                .tag("service", "ai-feature-extraction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("ai.feature.database.save.duration")
                .description("Database save operation time")
                .tag("service", "ai-feature-extraction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementExtractionTotal() {
        extractionTotalCounter.increment();
    }

    public void incrementExtractionSuccess() {
        extractionSuccessCounter.increment();
    }

    public void incrementExtractionFailure() {
        extractionFailureCounter.increment();
    }

    public void incrementFeaturesExtracted() {
        featuresExtractedCounter.increment();
    }

    public void incrementFeatureSetsProcessed() {
        featureSetsProcessedCounter.increment();
    }

    public void incrementCacheHit() {
        cacheHitCounter.increment();
    }

    public void incrementCacheMiss() {
        cacheMissCounter.increment();
    }

    // Timer methods
    public void recordExtractionTime(long durationMs) {
        extractionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startExtractionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopExtractionTimer(Timer.Sample sample) {
        sample.stop(extractionTimer);
    }

    public Timer.Sample startFeatureStoreWriteTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopFeatureStoreWriteTimer(Timer.Sample sample) {
        sample.stop(featureStoreWriteTimer);
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
    public double getExtractionLatencyP95() {
        return extractionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getExtractionLatencyP99() {
        return extractionTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) extractionTotalCounter.count();
        long failures = (long) extractionFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
