package com.gogidix.aiservices.aichurnpredictionservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ChurnPredictionMetrics.
 * Tests all counter and timer methods for SLO monitoring.
 */
@DisplayName("ChurnPredictionMetrics Tests")
class ChurnPredictionMetricsTest {

    private MeterRegistry meterRegistry;
    private ChurnPredictionMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new ChurnPredictionMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("Counter Increment Methods")
    class CounterIncrementTests {

        @Test
        @DisplayName("Should increment segmentation total counter")
        void shouldIncrementPredictionTotal() {
            metrics.incrementPredictionTotal();

            Counter counter = meterRegistry.counter("segmentation.total", "service", "ai-churn-predictionation");
            assertThat(counter).isNotNull();
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segmentation success counter")
        void shouldIncrementPredictionSuccess() {
            metrics.incrementPredictionSuccess();

            Counter counter = meterRegistry.counter("segmentation.success", "service", "ai-churn-predictionation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segmentation failure counter")
        void shouldIncrementPredictionFailure() {
            metrics.incrementPredictionFailure();

            Counter counter = meterRegistry.counter("segmentation.failure", "service", "ai-churn-predictionation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment created counter")
        void shouldIncrementPredictionCreated() {
            metrics.incrementPredictionCreated();

            Counter counter = meterRegistry.counter("segment.created", "service", "ai-churn-predictionation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment updated counter")
        void shouldIncrementPredictionUpdated() {
            metrics.incrementPredictionUpdated();

            Counter counter = meterRegistry.counter("segment.updated", "service", "ai-churn-predictionation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment deleted counter")
        void shouldIncrementPredictionDeleted() {
            metrics.incrementPredictionDeleted();

            Counter counter = meterRegistry.counter("segment.deleted", "service", "ai-churn-predictionation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer analyzed counter")
        void shouldIncrementCustomerAnalyzed() {
            metrics.incrementCustomerAnalyzed();

            Counter counter = meterRegistry.counter("customer.analyzed", "service", "ai-churn-predictionation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer added to segment counter")
        void shouldIncrementModelAddedToPrediction() {
            metrics.incrementModelAddedToPrediction();

            Counter counter = meterRegistry.counter("customer.added.to.segment", "service", "ai-churn-predictionation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer removed from segment counter")
        void shouldIncrementCustomerRemovedFromPrediction() {
            metrics.incrementCustomerRemovedFromPrediction();

            Counter counter = meterRegistry.counter("customer.removed.from.segment", "service", "ai-churn-predictionation");
            assertThat(counter.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Timer Recording Methods")
    class TimerRecordingTests {

        @Test
        @DisplayName("Should record segmentation time in milliseconds")
        void shouldRecordPredictionTime() {
            metrics.recordPredictionTime(250);

            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-churn-predictionation");
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isEqualTo(1);
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(250);
        }

        @Test
        @DisplayName("Should start and stop segmentation timer")
        void shouldStartAndStopPredictionTimer() {
            Timer.Sample sample = metrics.startPredictionTimer();
            assertThat(sample).isNotNull();

            metrics.stopPredictionTimer(sample);

            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-churn-predictionation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment analysis timer")
        void shouldStartAndStopPredictionAnalysisTimer() {
            Timer.Sample sample = metrics.startPredictionAnalysisTimer();
            assertThat(sample).isNotNull();

            metrics.stopPredictionAnalysisTimer(sample);

            Timer timer = meterRegistry.timer("segment.analysis.duration", "service", "ai-churn-predictionation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment creation timer")
        void shouldStartAndStopPredictionCreationTimer() {
            Timer.Sample sample = metrics.startPredictionCreationTimer();
            assertThat(sample).isNotNull();

            metrics.stopPredictionCreationTimer(sample);

            Timer timer = meterRegistry.timer("segment.creation.duration", "service", "ai-churn-predictionation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment update timer")
        void shouldStartAndStopPredictionUpdateTimer() {
            Timer.Sample sample = metrics.startPredictionUpdateTimer();
            assertThat(sample).isNotNull();

            metrics.stopPredictionUpdateTimer(sample);

            Timer timer = meterRegistry.timer("segment.update.duration", "service", "ai-churn-predictionation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop customer processing timer")
        void shouldStartAndStopCustomerProcessingTimer() {
            Timer.Sample sample = metrics.startCustomerProcessingTimer();
            assertThat(sample).isNotNull();

            metrics.stopCustomerProcessingTimer(sample);

            Timer timer = meterRegistry.timer("customer.processing.duration", "service", "ai-churn-predictionation");
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("SLO Compliance Methods")
    class SloComplianceTests {

        @Test
        @DisplayName("Should get P95 latency from timer")
        void shouldGetP95Latency() {
            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-churn-predictionation");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);

            double p95 = metrics.getPredictionLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P99 latency from timer")
        void shouldGetP99Latency() {
            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-churn-predictionation");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(800, TimeUnit.MILLISECONDS);

            double p99 = metrics.getPredictionLatencyP99();

            assertThat(p99).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get segment analysis P95 latency")
        void shouldGetPredictionAnalysisLatencyP95() {
            Timer timer = meterRegistry.timer("segment.analysis.duration", "service", "ai-churn-predictionation");
            timer.record(100, TimeUnit.MILLISECONDS);
            timer.record(150, TimeUnit.MILLISECONDS);

            double p95 = metrics.getPredictionAnalysisLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should calculate error rate correctly")
        void shouldCalculateErrorRate() {
            meterRegistry.counter("segmentation.total", "service", "ai-churn-predictionation").increment(100);
            meterRegistry.counter("segmentation.failure", "service", "ai-churn-predictionation").increment(10);

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isEqualTo(0.1);
        }

        @Test
        @DisplayName("Should return zero error rate when no requests")
        void shouldReturnZeroErrorRateWhenNoRequests() {
            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("Meter Registry Access")
    class MeterRegistryTests {

        @Test
        @DisplayName("Should return meter registry instance")
        void shouldReturnMeterRegistry() {
            assertThat(metrics.getMeterRegistry()).isNotNull();
            assertThat(metrics.getMeterRegistry()).isEqualTo(meterRegistry);
        }
    }
}
