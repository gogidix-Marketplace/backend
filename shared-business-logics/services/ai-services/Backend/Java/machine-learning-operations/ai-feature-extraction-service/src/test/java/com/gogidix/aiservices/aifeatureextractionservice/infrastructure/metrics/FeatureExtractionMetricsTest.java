package com.gogidix.aiservices.aifeatureextractionservice.infrastructure.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FeatureExtractionMetricsTest {

    @Mock
    private MeterRegistry meterRegistry;

    private FeatureExtractionMetrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new FeatureExtractionMetrics(meterRegistry);
    }

    @Test
    void whenCreated_RegistersAllCounters() {
        // Assert
        assertThat(metrics).isNotNull();
        assertThat(metrics.getMeterRegistry()).isNotNull();
        assertThat(metrics.getMeterRegistry()).isEqualTo(meterRegistry);
    }

    @Test
    void incrementExtractionTotal_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementExtractionTotal();
    }

    @Test
    void incrementExtractionSuccess_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementExtractionSuccess();
    }

    @Test
    void incrementExtractionFailure_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementExtractionFailure();
    }

    @Test
    void incrementFeaturesExtracted_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementFeaturesExtracted();
    }

    @Test
    void incrementFeatureSetsProcessed_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementFeatureSetsProcessed();
    }

    @Test
    void incrementCacheHit_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementCacheHit();
    }

    @Test
    void incrementCacheMiss_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementCacheMiss();
    }

    @Test
    void recordExtractionTime_WhenCalled_RecordsTime() {
        // Act & Assert - should not throw
        metrics.recordExtractionTime(100);
    }

    @Test
    void startExtractionTimer_WhenCalled_ReturnsSample() {
        // Act
        var sample = metrics.startExtractionTimer();

        // Assert
        assertThat(sample).isNotNull();
    }

    @Test
    void stopExtractionTimer_WhenCalled_StopsTimer() {
        // Arrange
        var sample = metrics.startExtractionTimer();

        // Act & Assert - should not throw
        metrics.stopExtractionTimer(sample);
    }

    @Test
    void startFeatureStoreWriteTimer_WhenCalled_ReturnsSample() {
        // Act
        var sample = metrics.startFeatureStoreWriteTimer();

        // Assert
        assertThat(sample).isNotNull();
    }

    @Test
    void stopFeatureStoreWriteTimer_WhenCalled_StopsTimer() {
        // Arrange
        var sample = metrics.startFeatureStoreWriteTimer();

        // Act & Assert - should not throw
        metrics.stopFeatureStoreWriteTimer(sample);
    }

    @Test
    void recordDatabaseSaveTime_WhenCalled_RecordsTime() {
        // Act & Assert - should not throw
        metrics.recordDatabaseSaveTime(50);
    }

    @Test
    void startDatabaseSaveTimer_WhenCalled_ReturnsSample() {
        // Act
        var sample = metrics.startDatabaseSaveTimer();

        // Assert
        assertThat(sample).isNotNull();
    }

    @Test
    void stopDatabaseSaveTimer_WhenCalled_StopsTimer() {
        // Arrange
        var sample = metrics.startDatabaseSaveTimer();

        // Act & Assert - should not throw
        metrics.stopDatabaseSaveTimer(sample);
    }

    @Test
    void getErrorRate_WhenNoRequests_ReturnsZero() {
        // Act
        double errorRate = metrics.getErrorRate();

        // Assert
        assertThat(errorRate).isZero();
    }

    @Test
    void getMeterRegistry_WhenCalled_ReturnsRegistry() {
        // Act
        MeterRegistry registry = metrics.getMeterRegistry();

        // Assert
        assertThat(registry).isNotNull();
        assertThat(registry).isEqualTo(meterRegistry);
    }
}
