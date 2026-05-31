package com.gogidix.aiservices.aifeaturestoreservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class FeatureStoreMetricsGenTest {

    private FeatureStoreMetrics metrics;

    @BeforeEach
    void setup() {
        metrics = new FeatureStoreMetrics(new SimpleMeterRegistry());
    }

    @Test
    void incrementStoreTotal() {
        metrics.incrementStoreTotal();
    }

    @Test
    void incrementStoreSuccess() {
        metrics.incrementStoreSuccess();
    }

    @Test
    void incrementStoreFailure() {
        metrics.incrementStoreFailure();
    }

    @Test
    void incrementFeaturesStored() {
        metrics.incrementFeaturesStored();
    }

    @Test
    void incrementFeaturesRetrieved() {
        metrics.incrementFeaturesRetrieved();
    }

    @Test
    void incrementCacheHit() {
        metrics.incrementCacheHit();
    }

    @Test
    void incrementCacheMiss() {
        metrics.incrementCacheMiss();
    }

    @Test
    void recordStoreTime() {
        metrics.recordStoreTime(100);
    }

    @Test
    void recordDatabaseSaveTime() {
        metrics.recordDatabaseSaveTime(50);
    }

    @Test
    void stopStoreTimer() {
        var sample = metrics.startStoreTimer();
        metrics.stopStoreTimer(sample);
    }

    @Test
    void stopRetrieveTimer() {
        var sample = metrics.startRetrieveTimer();
        metrics.stopRetrieveTimer(sample);
    }

    @Test
    void stopDatabaseSaveTimer() {
        var sample = metrics.startDatabaseSaveTimer();
        metrics.stopDatabaseSaveTimer(sample);
    }

    @Test
    void sloMethods() {
        assertThat(metrics.getStoreLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getStoreLatencyP99()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getErrorRate()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void getMeterRegistry() {
        assertThat(metrics.getMeterRegistry()).isNotNull();
    }
}
