package com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.metrics;

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
 * Unit tests for CustomerSegmentationMetrics.
 * Tests all counter and timer methods for SLO monitoring.
 */
@DisplayName("CustomerSegmentationMetrics Tests")
class CustomerSegmentationMetricsTest {

    private MeterRegistry meterRegistry;
    private CustomerSegmentationMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new CustomerSegmentationMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("Counter Increment Methods")
    class CounterIncrementTests {

        @Test
        @DisplayName("Should increment segmentation total counter")
        void shouldIncrementSegmentationTotal() {
            metrics.incrementSegmentationTotal();

            Counter counter = meterRegistry.counter("segmentation.total", "service", "ai-customer-segmentation");
            assertThat(counter).isNotNull();
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segmentation success counter")
        void shouldIncrementSegmentationSuccess() {
            metrics.incrementSegmentationSuccess();

            Counter counter = meterRegistry.counter("segmentation.success", "service", "ai-customer-segmentation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segmentation failure counter")
        void shouldIncrementSegmentationFailure() {
            metrics.incrementSegmentationFailure();

            Counter counter = meterRegistry.counter("segmentation.failure", "service", "ai-customer-segmentation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment created counter")
        void shouldIncrementSegmentCreated() {
            metrics.incrementSegmentCreated();

            Counter counter = meterRegistry.counter("segment.created", "service", "ai-customer-segmentation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment updated counter")
        void shouldIncrementSegmentUpdated() {
            metrics.incrementSegmentUpdated();

            Counter counter = meterRegistry.counter("segment.updated", "service", "ai-customer-segmentation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment deleted counter")
        void shouldIncrementSegmentDeleted() {
            metrics.incrementSegmentDeleted();

            Counter counter = meterRegistry.counter("segment.deleted", "service", "ai-customer-segmentation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer analyzed counter")
        void shouldIncrementCustomerAnalyzed() {
            metrics.incrementCustomerAnalyzed();

            Counter counter = meterRegistry.counter("customer.analyzed", "service", "ai-customer-segmentation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer added to segment counter")
        void shouldIncrementCustomerAddedToSegment() {
            metrics.incrementCustomerAddedToSegment();

            Counter counter = meterRegistry.counter("customer.added.to.segment", "service", "ai-customer-segmentation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer removed from segment counter")
        void shouldIncrementCustomerRemovedFromSegment() {
            metrics.incrementCustomerRemovedFromSegment();

            Counter counter = meterRegistry.counter("customer.removed.from.segment", "service", "ai-customer-segmentation");
            assertThat(counter.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Timer Recording Methods")
    class TimerRecordingTests {

        @Test
        @DisplayName("Should record segmentation time in milliseconds")
        void shouldRecordSegmentationTime() {
            metrics.recordSegmentationTime(250);

            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-customer-segmentation");
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isEqualTo(1);
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(250);
        }

        @Test
        @DisplayName("Should start and stop segmentation timer")
        void shouldStartAndStopSegmentationTimer() {
            Timer.Sample sample = metrics.startSegmentationTimer();
            assertThat(sample).isNotNull();

            metrics.stopSegmentationTimer(sample);

            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-customer-segmentation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment analysis timer")
        void shouldStartAndStopSegmentAnalysisTimer() {
            Timer.Sample sample = metrics.startSegmentAnalysisTimer();
            assertThat(sample).isNotNull();

            metrics.stopSegmentAnalysisTimer(sample);

            Timer timer = meterRegistry.timer("segment.analysis.duration", "service", "ai-customer-segmentation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment creation timer")
        void shouldStartAndStopSegmentCreationTimer() {
            Timer.Sample sample = metrics.startSegmentCreationTimer();
            assertThat(sample).isNotNull();

            metrics.stopSegmentCreationTimer(sample);

            Timer timer = meterRegistry.timer("segment.creation.duration", "service", "ai-customer-segmentation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment update timer")
        void shouldStartAndStopSegmentUpdateTimer() {
            Timer.Sample sample = metrics.startSegmentUpdateTimer();
            assertThat(sample).isNotNull();

            metrics.stopSegmentUpdateTimer(sample);

            Timer timer = meterRegistry.timer("segment.update.duration", "service", "ai-customer-segmentation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop customer processing timer")
        void shouldStartAndStopCustomerProcessingTimer() {
            Timer.Sample sample = metrics.startCustomerProcessingTimer();
            assertThat(sample).isNotNull();

            metrics.stopCustomerProcessingTimer(sample);

            Timer timer = meterRegistry.timer("customer.processing.duration", "service", "ai-customer-segmentation");
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("SLO Compliance Methods")
    class SloComplianceTests {

        @Test
        @DisplayName("Should get P95 latency from timer")
        void shouldGetP95Latency() {
            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-customer-segmentation");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);

            double p95 = metrics.getSegmentationLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P99 latency from timer")
        void shouldGetP99Latency() {
            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-customer-segmentation");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(800, TimeUnit.MILLISECONDS);

            double p99 = metrics.getSegmentationLatencyP99();

            assertThat(p99).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get segment analysis P95 latency")
        void shouldGetSegmentAnalysisLatencyP95() {
            Timer timer = meterRegistry.timer("segment.analysis.duration", "service", "ai-customer-segmentation");
            timer.record(100, TimeUnit.MILLISECONDS);
            timer.record(150, TimeUnit.MILLISECONDS);

            double p95 = metrics.getSegmentAnalysisLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should calculate error rate correctly")
        void shouldCalculateErrorRate() {
            meterRegistry.counter("segmentation.total", "service", "ai-customer-segmentation").increment(100);
            meterRegistry.counter("segmentation.failure", "service", "ai-customer-segmentation").increment(10);

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
