package com.gogidix.aiservices.aisalesforecastingservice.infrastructure.metrics;

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
 * Unit tests for SalesForecastingMetrics.
 * Tests all counter and timer methods for SLO monitoring.
 */
@DisplayName("SalesForecastingMetrics Tests")
class SalesForecastingMetricsTest {

    private MeterRegistry meterRegistry;
    private SalesForecastingMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new SalesForecastingMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("Counter Increment Methods")
    class CounterIncrementTests {

        @Test
        @DisplayName("Should increment segmentation total counter")
        void shouldIncrementForecastingTotal() {
            metrics.incrementForecastingTotal();

            Counter counter = meterRegistry.counter("segmentation.total", "service", "ai-sales-forecastation");
            assertThat(counter).isNotNull();
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segmentation success counter")
        void shouldIncrementForecastingSuccess() {
            metrics.incrementForecastingSuccess();

            Counter counter = meterRegistry.counter("segmentation.success", "service", "ai-sales-forecastation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segmentation failure counter")
        void shouldIncrementForecastingFailure() {
            metrics.incrementForecastingFailure();

            Counter counter = meterRegistry.counter("segmentation.failure", "service", "ai-sales-forecastation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment created counter")
        void shouldIncrementForecastCreated() {
            metrics.incrementForecastCreated();

            Counter counter = meterRegistry.counter("segment.created", "service", "ai-sales-forecastation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment updated counter")
        void shouldIncrementForecastUpdated() {
            metrics.incrementForecastUpdated();

            Counter counter = meterRegistry.counter("segment.updated", "service", "ai-sales-forecastation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment segment deleted counter")
        void shouldIncrementForecastDeleted() {
            metrics.incrementForecastDeleted();

            Counter counter = meterRegistry.counter("segment.deleted", "service", "ai-sales-forecastation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer analyzed counter")
        void shouldIncrementForecastModelAnalyzed() {
            metrics.incrementForecastModelAnalyzed();

            Counter counter = meterRegistry.counter("customer.analyzed", "service", "ai-sales-forecastation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer added to segment counter")
        void shouldIncrementForecastModelAddedToForecast() {
            metrics.incrementForecastModelAddedToForecast();

            Counter counter = meterRegistry.counter("customer.added.to.segment", "service", "ai-sales-forecastation");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment customer removed from segment counter")
        void shouldIncrementForecastModelRemovedFromForecast() {
            metrics.incrementForecastModelRemovedFromForecast();

            Counter counter = meterRegistry.counter("customer.removed.from.segment", "service", "ai-sales-forecastation");
            assertThat(counter.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Timer Recording Methods")
    class TimerRecordingTests {

        @Test
        @DisplayName("Should record segmentation time in milliseconds")
        void shouldRecordForecastingTime() {
            metrics.recordForecastingTime(250);

            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-sales-forecastation");
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isEqualTo(1);
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(250);
        }

        @Test
        @DisplayName("Should start and stop segmentation timer")
        void shouldStartAndStopForecastingTimer() {
            Timer.Sample sample = metrics.startForecastingTimer();
            assertThat(sample).isNotNull();

            metrics.stopForecastingTimer(sample);

            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-sales-forecastation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment analysis timer")
        void shouldStartAndStopForecastAnalysisTimer() {
            Timer.Sample sample = metrics.startForecastAnalysisTimer();
            assertThat(sample).isNotNull();

            metrics.stopForecastAnalysisTimer(sample);

            Timer timer = meterRegistry.timer("segment.analysis.duration", "service", "ai-sales-forecastation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment creation timer")
        void shouldStartAndStopForecastCreationTimer() {
            Timer.Sample sample = metrics.startForecastCreationTimer();
            assertThat(sample).isNotNull();

            metrics.stopForecastCreationTimer(sample);

            Timer timer = meterRegistry.timer("segment.creation.duration", "service", "ai-sales-forecastation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop segment update timer")
        void shouldStartAndStopForecastUpdateTimer() {
            Timer.Sample sample = metrics.startForecastUpdateTimer();
            assertThat(sample).isNotNull();

            metrics.stopForecastUpdateTimer(sample);

            Timer timer = meterRegistry.timer("segment.update.duration", "service", "ai-sales-forecastation");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop customer processing timer")
        void shouldStartAndStopForecastModelProcessingTimer() {
            Timer.Sample sample = metrics.startForecastModelProcessingTimer();
            assertThat(sample).isNotNull();

            metrics.stopForecastModelProcessingTimer(sample);

            Timer timer = meterRegistry.timer("customer.processing.duration", "service", "ai-sales-forecastation");
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("SLO Compliance Methods")
    class SloComplianceTests {

        @Test
        @DisplayName("Should get P95 latency from timer")
        void shouldGetP95Latency() {
            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-sales-forecastation");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);

            double p95 = metrics.getForecastingLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P99 latency from timer")
        void shouldGetP99Latency() {
            Timer timer = meterRegistry.timer("segmentation.duration", "service", "ai-sales-forecastation");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(800, TimeUnit.MILLISECONDS);

            double p99 = metrics.getForecastingLatencyP99();

            assertThat(p99).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get segment analysis P95 latency")
        void shouldGetForecastAnalysisLatencyP95() {
            Timer timer = meterRegistry.timer("segment.analysis.duration", "service", "ai-sales-forecastation");
            timer.record(100, TimeUnit.MILLISECONDS);
            timer.record(150, TimeUnit.MILLISECONDS);

            double p95 = metrics.getForecastAnalysisLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should calculate error rate correctly")
        void shouldCalculateErrorRate() {
            meterRegistry.counter("segmentation.total", "service", "ai-sales-forecastation").increment(100);
            meterRegistry.counter("segmentation.failure", "service", "ai-sales-forecastation").increment(10);

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
