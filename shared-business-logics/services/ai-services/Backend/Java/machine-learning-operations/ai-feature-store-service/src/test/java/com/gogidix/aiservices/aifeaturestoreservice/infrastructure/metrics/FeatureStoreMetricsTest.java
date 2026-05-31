package com.gogidix.aiservices.aifeaturestoreservice.infrastructure.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FeatureStoreMetricsTest {

    @Mock
    private MeterRegistry meterRegistry;

    private FeatureStoreMetrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new FeatureStoreMetrics(meterRegistry);
    }

    @Test
    void whenCreated_RegistersAllCounters() {
        // Assert
        assertThat(metrics).isNotNull();
        assertThat(metrics.getMeterRegistry()).isNotNull();
        assertThat(metrics.getMeterRegistry()).isEqualTo(meterRegistry);
    }

    @Test
    void incrementStoreTotal_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementStoreTotal();
    }

    @Test
    void incrementStoreSuccess_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementStoreSuccess();
    }

    @Test
    void incrementStoreFailure_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementStoreFailure();
    }

    @Test
    void incrementFeaturesStored_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementFeaturesStored();
    }

    @Test
    void incrementFeaturesRetrieved_WhenCalled_IncrementsCounter() {
        // Act & Assert - should not throw
        metrics.incrementFeaturesRetrieved();
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
    void recordStoreTime_WhenCalled_RecordsTime() {
        // Act & Assert - should not throw
        metrics.recordStoreTime(100);
    }

    @Test
    void startStoreTimer_WhenCalled_ReturnsSample() {
        // Act
        var sample = metrics.startStoreTimer();

        // Assert
        assertThat(sample).isNotNull();
    }

    @Test
    void stopStoreTimer_WhenCalled_StopsTimer() {
        // Arrange
        var sample = metrics.startStoreTimer();

        // Act & Assert - should not throw
        metrics.stopStoreTimer(sample);
    }

    @Test
    void startRetrieveTimer_WhenCalled_ReturnsSample() {
        // Act
        var sample = metrics.startRetrieveTimer();

        // Assert
        assertThat(sample).isNotNull();
    }

    @Test
    void stopRetrieveTimer_WhenCalled_StopsTimer() {
        // Arrange
        var sample = metrics.startRetrieveTimer();

        // Act & Assert - should not throw
        metrics.stopRetrieveTimer(sample);
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
