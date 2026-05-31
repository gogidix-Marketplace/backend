package com.gogidix.aiservices.aifeaturestoreservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Feature Store Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class FeatureStoreMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter storeTotalCounter;
    private final Counter storeSuccessCounter;
    private final Counter storeFailureCounter;
    private final Counter featuresStoredCounter;
    private final Counter featuresRetrievedCounter;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;

    // Timers
    private final Timer storeTimer;
    private final Timer retrieveTimer;
    private final Timer databaseSaveTimer;

    public FeatureStoreMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.storeTotalCounter = Counter.builder("ai.feature.store.total")
                .description("Total number of feature store requests")
                .tag("service", "ai-feature-store")
                .register(meterRegistry);

        this.storeSuccessCounter = Counter.builder("ai.feature.store.success")
                .description("Number of successful feature store requests")
                .tag("service", "ai-feature-store")
                .register(meterRegistry);

        this.storeFailureCounter = Counter.builder("ai.feature.store.failure")
                .description("Number of failed feature store requests")
                .tag("service", "ai-feature-store")
                .register(meterRegistry);

        this.featuresStoredCounter = Counter.builder("ai.feature.stored")
                .description("Number of features stored")
                .tag("service", "ai-feature-store")
                .register(meterRegistry);

        this.featuresRetrievedCounter = Counter.builder("ai.feature.retrieved")
                .description("Number of features retrieved")
                .tag("service", "ai-feature-store")
                .register(meterRegistry);

        this.cacheHitCounter = Counter.builder("ai.feature.store.cache.hit")
                .description("Number of cache hits")
                .tag("service", "ai-feature-store")
                .register(meterRegistry);

        this.cacheMissCounter = Counter.builder("ai.feature.store.cache.miss")
                .description("Number of cache misses")
                .tag("service", "ai-feature-store")
                .register(meterRegistry);

        // Initialize timers
        this.storeTimer = Timer.builder("ai.feature.store.duration")
                .description("Feature store operation time")
                .tag("service", "ai-feature-store")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.retrieveTimer = Timer.builder("ai.feature.retrieve.duration")
                .description("Feature retrieve operation time")
                .tag("service", "ai-feature-store")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("ai.feature.store.database.save.duration")
                .description("Database save operation time")
                .tag("service", "ai-feature-store")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementStoreTotal() {
        storeTotalCounter.increment();
    }

    public void incrementStoreSuccess() {
        storeSuccessCounter.increment();
    }

    public void incrementStoreFailure() {
        storeFailureCounter.increment();
    }

    public void incrementFeaturesStored() {
        featuresStoredCounter.increment();
    }

    public void incrementFeaturesRetrieved() {
        featuresRetrievedCounter.increment();
    }

    public void incrementCacheHit() {
        cacheHitCounter.increment();
    }

    public void incrementCacheMiss() {
        cacheMissCounter.increment();
    }

    // Timer methods
    public void recordStoreTime(long durationMs) {
        storeTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startStoreTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopStoreTimer(Timer.Sample sample) {
        sample.stop(storeTimer);
    }

    public Timer.Sample startRetrieveTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRetrieveTimer(Timer.Sample sample) {
        sample.stop(retrieveTimer);
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
    public double getStoreLatencyP95() {
        return storeTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getStoreLatencyP99() {
        return storeTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) storeTotalCounter.count();
        long failures = (long) storeFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
