package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.metrics;

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
 * Unit tests for DashboardMetrics.
 * Tests all counter and timer methods for SLO monitoring.
 */
@DisplayName("DashboardMetrics Tests")
class DashboardMetricsTest {

    private MeterRegistry meterRegistry;
    private DashboardMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new DashboardMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("Counter Increment Methods")
    class CounterIncrementTests {

        @Test
        @DisplayName("Should increment dashboard created counter")
        void shouldIncrementDashboardCreated() {
            metrics.incrementDashboardCreated();

            Counter counter = meterRegistry.counter("dashboard.created", "service", "ai-analytics-dashboard");
            assertThat(counter).isNotNull();
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment dashboard updated counter")
        void shouldIncrementDashboardUpdated() {
            metrics.incrementDashboardUpdated();

            Counter counter = meterRegistry.counter("dashboard.updated", "service", "ai-analytics-dashboard");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment dashboard deleted counter")
        void shouldIncrementDashboardDeleted() {
            metrics.incrementDashboardDeleted();

            Counter counter = meterRegistry.counter("dashboard.deleted", "service", "ai-analytics-dashboard");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment dashboard view counter")
        void shouldIncrementDashboardView() {
            metrics.incrementDashboardView();

            Counter counter = meterRegistry.counter("dashboard.view", "service", "ai-analytics-dashboard");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment widget added counter")
        void shouldIncrementWidgetAdded() {
            metrics.incrementWidgetAdded();

            Counter counter = meterRegistry.counter("widget.added", "service", "ai-analytics-dashboard");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment widget removed counter")
        void shouldIncrementWidgetRemoved() {
            metrics.incrementWidgetRemoved();

            Counter counter = meterRegistry.counter("widget.removed", "service", "ai-analytics-dashboard");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment metric query counter")
        void shouldIncrementMetricQuery() {
            metrics.incrementMetricQuery();

            Counter counter = meterRegistry.counter("metric.query.total", "service", "ai-analytics-dashboard");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment metric query success counter")
        void shouldIncrementMetricQuerySuccess() {
            metrics.incrementMetricQuerySuccess();

            Counter counter = meterRegistry.counter("metric.query.success", "service", "ai-analytics-dashboard");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment metric query failure counter")
        void shouldIncrementMetricQueryFailure() {
            metrics.incrementMetricQueryFailure();

            Counter counter = meterRegistry.counter("metric.query.failure", "service", "ai-analytics-dashboard");
            assertThat(counter.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Timer Recording Methods")
    class TimerRecordingTests {

        @Test
        @DisplayName("Should record dashboard creation time in milliseconds")
        void shouldRecordDashboardCreationTime() {
            metrics.recordDashboardCreationTime(250);

            Timer timer = meterRegistry.timer("dashboard.creation.duration", "service", "ai-analytics-dashboard");
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isEqualTo(1);
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(250);
        }

        @Test
        @DisplayName("Should start and stop dashboard creation timer")
        void shouldStartAndStopDashboardCreationTimer() {
            Timer.Sample sample = metrics.startDashboardCreationTimer();
            assertThat(sample).isNotNull();

            metrics.stopDashboardCreationTimer(sample);

            Timer timer = meterRegistry.timer("dashboard.creation.duration", "service", "ai-analytics-dashboard");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record dashboard update time in milliseconds")
        void shouldRecordDashboardUpdateTime() {
            metrics.recordDashboardUpdateTime(150);

            Timer timer = meterRegistry.timer("dashboard.update.duration", "service", "ai-analytics-dashboard");
            assertThat(timer).isNotNull();
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(150);
        }

        @Test
        @DisplayName("Should start and stop dashboard update timer")
        void shouldStartAndStopDashboardUpdateTimer() {
            Timer.Sample sample = metrics.startDashboardUpdateTimer();
            assertThat(sample).isNotNull();

            metrics.stopDashboardUpdateTimer(sample);

            Timer timer = meterRegistry.timer("dashboard.update.duration", "service", "ai-analytics-dashboard");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record dashboard query time in milliseconds")
        void shouldRecordDashboardQueryTime() {
            metrics.recordDashboardQueryTime(100);

            Timer timer = meterRegistry.timer("dashboard.query.duration", "service", "ai-analytics-dashboard");
            assertThat(timer).isNotNull();
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(100);
        }

        @Test
        @DisplayName("Should start and stop dashboard query timer")
        void shouldStartAndStopDashboardQueryTimer() {
            Timer.Sample sample = metrics.startDashboardQueryTimer();
            assertThat(sample).isNotNull();

            metrics.stopDashboardQueryTimer(sample);

            Timer timer = meterRegistry.timer("dashboard.query.duration", "service", "ai-analytics-dashboard");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record metric aggregation time in milliseconds")
        void shouldRecordMetricAggregationTime() {
            metrics.recordMetricAggregationTime(200);

            Timer timer = meterRegistry.timer("metric.aggregation.duration", "service", "ai-analytics-dashboard");
            assertThat(timer).isNotNull();
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(200);
        }

        @Test
        @DisplayName("Should start and stop metric aggregation timer")
        void shouldStartAndStopMetricAggregationTimer() {
            Timer.Sample sample = metrics.startMetricAggregationTimer();
            assertThat(sample).isNotNull();

            metrics.stopMetricAggregationTimer(sample);

            Timer timer = meterRegistry.timer("metric.aggregation.duration", "service", "ai-analytics-dashboard");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record widget operation time in milliseconds")
        void shouldRecordWidgetOperationTime() {
            metrics.recordWidgetOperationTime(50);

            Timer timer = meterRegistry.timer("widget.operation.duration", "service", "ai-analytics-dashboard");
            assertThat(timer).isNotNull();
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(50);
        }

        @Test
        @DisplayName("Should start and stop widget operation timer")
        void shouldStartAndStopWidgetOperationTimer() {
            Timer.Sample sample = metrics.startWidgetOperationTimer();
            assertThat(sample).isNotNull();

            metrics.stopWidgetOperationTimer(sample);

            Timer timer = meterRegistry.timer("widget.operation.duration", "service", "ai-analytics-dashboard");
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("SLO Compliance Methods")
    class SloComplianceTests {

        @Test
        @DisplayName("Should get P95 dashboard creation latency")
        void shouldGetP95DashboardCreationLatency() {
            Timer timer = meterRegistry.timer("dashboard.creation.duration", "service", "ai-analytics-dashboard");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);

            double p95 = metrics.getDashboardCreationLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P99 dashboard creation latency")
        void shouldGetP99DashboardCreationLatency() {
            Timer timer = meterRegistry.timer("dashboard.creation.duration", "service", "ai-analytics-dashboard");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(800, TimeUnit.MILLISECONDS);

            double p99 = metrics.getDashboardCreationLatencyP99();

            assertThat(p99).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P95 dashboard query latency")
        void shouldGetP95DashboardQueryLatency() {
            Timer timer = meterRegistry.timer("dashboard.query.duration", "service", "ai-analytics-dashboard");
            timer.record(100, TimeUnit.MILLISECONDS);
            timer.record(150, TimeUnit.MILLISECONDS);

            double p95 = metrics.getDashboardQueryLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P95 metric aggregation latency")
        void shouldGetP95MetricAggregationLatency() {
            Timer timer = meterRegistry.timer("metric.aggregation.duration", "service", "ai-analytics-dashboard");
            timer.record(200, TimeUnit.MILLISECONDS);
            timer.record(250, TimeUnit.MILLISECONDS);

            double p95 = metrics.getMetricAggregationLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should calculate error rate correctly")
        void shouldCalculateErrorRate() {
            meterRegistry.counter("metric.query.total", "service", "ai-analytics-dashboard").increment(100);
            meterRegistry.counter("metric.query.failure", "service", "ai-analytics-dashboard").increment(10);

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
