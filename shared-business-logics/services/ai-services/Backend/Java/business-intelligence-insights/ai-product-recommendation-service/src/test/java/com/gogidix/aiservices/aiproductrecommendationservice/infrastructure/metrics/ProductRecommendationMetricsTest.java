package com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.metrics;

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
 * Unit tests for ProductRecommendationMetrics.
 * Tests all counter and timer methods for SLO monitoring.
 */
@DisplayName("ProductRecommendationMetrics Tests")
class ProductRecommendationMetricsTest {

    private MeterRegistry meterRegistry;
    private ProductRecommendationMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new ProductRecommendationMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("Counter Increment Methods")
    class CounterIncrementTests {

        @Test
        @DisplayName("Should increment segmentation total counter")
        void shouldIncrementRecommendationTotal() {
            metrics.incrementRecommendationTotal();

            Counter counter = meterRegistry.counter("segmentation.total", "service", "ai-product-recommendationation");
            assertThat(counter).isNotNull();
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segmentation success counter")
        void shouldIncrementRecommendationSuccess() {
            metrics.incrementRecommendationSuccess();

            Counter counter = meterRegistry.counter("segmentation.success", "service", "ai-product-recommendationation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segmentation failure counter")
        void shouldIncrementRecommendationFailure() {
            metrics.incrementRecommendationFailure();

            Counter counter = meterRegistry.counter("segmentation.failure", "service", "ai-product-recommendationation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment created counter")
        void shouldIncrementRecommendationCreated() {
            metrics.incrementRecommendationCreated();

            Counter counter = meterRegistry.counter("segment.created", "service", "ai-product-recommendationation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment updated counter")
        void shouldIncrementRecommendationUpdated() {
            metrics.incrementRecommendationUpdated();

            Counter counter = meterRegistry.counter("segment.updated", "service", "ai-product-recommendationation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment deleted counter")
        void shouldIncrementRecommendationDeleted() {
            metrics.incrementRecommendationDeleted();

            Counter counter = meterRegistry.counter("segment.deleted", "service", "ai-product-recommendationation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer analyzed counter")
        void shouldIncrementProductAnalyzed() {
            metrics.incrementProductAnalyzed();

            Counter counter = meterRegistry.counter("customer.analyzed", "service", "ai-product-recommendationation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer added to segment counter")
        void shouldIncrementProductAddedToRecommendation() {
            metrics.incrementProductAddedToRecommendation();

            Counter counter = meterRegistry.counter("customer.added.to.segment", "service", "ai-product-recommendationation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer removed from segment counter")
        void shouldIncrementProductRemovedFromRecommendation() {
            metrics.incrementProductRemovedFromRecommendation();

            Counter counter = meterRegistry.counter("customer.removed.from.segment", "service", "ai-product-recommendationation");
            assertThat(counter.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Timer Recording Methods")
    class TimerRecordingTests {

        @Test
        @DisplayName("Should record segmentation time in milliseconds")
        void shouldRecordRecommendationTime() {
            metrics.recordRecommendationTime(250);

            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-product-recommendationation");
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isEqualTo(1);
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(250);
        }

        @Test
        @DisplayName("Should start and stop segmentation timer")
        void shouldStartAndStopRecommendationTimer() {
            Timer.Sample sample = metrics.startRecommendationTimer();
            assertThat(sample).isNotNull();

            metrics.stopRecommendationTimer(sample);

            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-product-recommendationation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment analysis timer")
        void shouldStartAndStopRecommendationAnalysisTimer() {
            Timer.Sample sample = metrics.startRecommendationAnalysisTimer();
            assertThat(sample).isNotNull();

            metrics.stopRecommendationAnalysisTimer(sample);

            Timer timer = meterRegistry.timer("segment.analysis.duration", "service", "ai-product-recommendationation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment creation timer")
        void shouldStartAndStopRecommendationCreationTimer() {
            Timer.Sample sample = metrics.startRecommendationCreationTimer();
            assertThat(sample).isNotNull();

            metrics.stopRecommendationCreationTimer(sample);

            Timer timer = meterRegistry.timer("segment.creation.duration", "service", "ai-product-recommendationation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment update timer")
        void shouldStartAndStopRecommendationUpdateTimer() {
            Timer.Sample sample = metrics.startRecommendationUpdateTimer();
            assertThat(sample).isNotNull();

            metrics.stopRecommendationUpdateTimer(sample);

            Timer timer = meterRegistry.timer("segment.update.duration", "service", "ai-product-recommendationation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop customer processing timer")
        void shouldStartAndStopProductProcessingTimer() {
            Timer.Sample sample = metrics.startProductProcessingTimer();
            assertThat(sample).isNotNull();

            metrics.stopProductProcessingTimer(sample);

            Timer timer = meterRegistry.timer("customer.processing.duration", "service", "ai-product-recommendationation");
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("SLO Compliance Methods")
    class SloComplianceTests {

        @Test
        @DisplayName("Should get P95 latency from timer")
        void shouldGetP95Latency() {
            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-product-recommendationation");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);

            double p95 = metrics.getRecommendationLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P99 latency from timer")
        void shouldGetP99Latency() {
            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-product-recommendationation");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(800, TimeUnit.MILLISECONDS);

            double p99 = metrics.getRecommendationLatencyP99();

            assertThat(p99).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get segment analysis P95 latency")
        void shouldGetRecommendationAnalysisLatencyP95() {
            Timer timer = meterRegistry.timer("segment.analysis.duration", "service", "ai-product-recommendationation");
            timer.record(100, TimeUnit.MILLISECONDS);
            timer.record(150, TimeUnit.MILLISECONDS);

            double p95 = metrics.getRecommendationAnalysisLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should calculate error rate correctly")
        void shouldCalculateErrorRate() {
            meterRegistry.counter("segmentation.total", "service", "ai-product-recommendationation").increment(100);
            meterRegistry.counter("segmentation.failure", "service", "ai-product-recommendationation").increment(10);

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
