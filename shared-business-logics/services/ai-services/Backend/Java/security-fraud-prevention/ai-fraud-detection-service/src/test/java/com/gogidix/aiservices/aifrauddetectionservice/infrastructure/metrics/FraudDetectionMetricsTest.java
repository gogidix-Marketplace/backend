package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.metrics;

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
 * Unit tests for FraudDetectionMetrics.
 * Tests all counter and timer methods for SLO monitoring.
 */
@DisplayName("FraudDetectionMetrics Tests")
class FraudDetectionMetricsTest {

    private MeterRegistry meterRegistry;
    private FraudDetectionMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new FraudDetectionMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("Counter Increment Methods")
    class CounterIncrementTests {

        @Test
        @DisplayName("Should increment analysis total counter")
        void shouldIncrementAnalysisTotal() {
            metrics.incrementAnalysisTotal();

            Counter counter = meterRegistry.counter("fraud.analysis.total", "service", "ai-fraud-detection");
            assertThat(counter).isNotNull();
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment analysis success counter")
        void shouldIncrementAnalysisSuccess() {
            metrics.incrementAnalysisSuccess();

            Counter counter = meterRegistry.counter("fraud.analysis.success", "service", "ai-fraud-detection");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment analysis failure counter")
        void shouldIncrementAnalysisFailure() {
            metrics.incrementAnalysisFailure();

            Counter counter = meterRegistry.counter("fraud.analysis.failure", "service", "ai-fraud-detection");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment fraud detected counter")
        void shouldIncrementFraudDetected() {
            metrics.incrementFraudDetected();

            Counter counter = meterRegistry.counter("fraud.detected", "service", "ai-fraud-detection");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment high risk transactions counter")
        void shouldIncrementHighRisk() {
            metrics.incrementHighRisk();

            Counter counter = meterRegistry.counter("fraud.high.risk", "service", "ai-fraud-detection");
            assertThat(counter.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should increment blocked transactions counter")
        void shouldIncrementBlocked() {
            metrics.incrementBlocked();

            Counter counter = meterRegistry.counter("fraud.blocked", "service", "ai-fraud-detection");
            assertThat(counter.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Timer Recording Methods")
    class TimerRecordingTests {

        @Test
        @DisplayName("Should record analysis time in milliseconds")
        void shouldRecordAnalysisTime() {
            metrics.recordAnalysisTime(250);

            Timer timer = meterRegistry.timer("fraud.analysis.duration", "service", "ai-fraud-detection");
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isEqualTo(1);
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(250);
        }

        @Test
        @DisplayName("Should start and stop analysis timer")
        void shouldStartAndStopAnalysisTimer() {
            Timer.Sample sample = metrics.startAnalysisTimer();
            assertThat(sample).isNotNull();

            metrics.stopAnalysisTimer(sample);

            Timer timer = meterRegistry.timer("fraud.analysis.duration", "service", "ai-fraud-detection");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should start and stop ML prediction timer")
        void shouldStartAndStopMlPredictionTimer() {
            Timer.Sample sample = metrics.startMlPredictionTimer();
            assertThat(sample).isNotNull();

            metrics.stopMlPredictionTimer(sample);

            Timer timer = meterRegistry.timer("fraud.ml.prediction.duration", "service", "ai-fraud-detection");
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record database save time in milliseconds")
        void shouldRecordDatabaseSaveTime() {
            metrics.recordDatabaseSaveTime(100);

            Timer timer = meterRegistry.timer("fraud.database.save.duration", "service", "ai-fraud-detection");
            assertThat(timer).isNotNull();
            assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isEqualTo(100);
        }

        @Test
        @DisplayName("Should start and stop database save timer")
        void shouldStartAndStopDatabaseSaveTimer() {
            Timer.Sample sample = metrics.startDatabaseSaveTimer();
            assertThat(sample).isNotNull();

            metrics.stopDatabaseSaveTimer(sample);

            Timer timer = meterRegistry.timer("fraud.database.save.duration", "service", "ai-fraud-detection");
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("SLO Compliance Methods")
    class SloComplianceTests {

        @Test
        @DisplayName("Should get P95 latency from timer")
        void shouldGetP95Latency() {
            Timer timer = meterRegistry.timer("fraud.analysis.duration", "service", "ai-fraud-detection");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);

            double p95 = metrics.getAnalysisLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get P99 latency from timer")
        void shouldGetP99Latency() {
            Timer timer = meterRegistry.timer("fraud.analysis.duration", "service", "ai-fraud-detection");
            timer.record(300, TimeUnit.MILLISECONDS);
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(800, TimeUnit.MILLISECONDS);

            double p99 = metrics.getAnalysisLatencyP99();

            assertThat(p99).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should get ML prediction P95 latency")
        void shouldGetMlPredictionLatencyP95() {
            Timer timer = meterRegistry.timer("fraud.ml.prediction.duration", "service", "ai-fraud-detection");
            timer.record(50, TimeUnit.MILLISECONDS);
            timer.record(75, TimeUnit.MILLISECONDS);

            double p95 = metrics.getMlPredictionLatencyP95();

            assertThat(p95).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should calculate error rate correctly")
        void shouldCalculateErrorRate() {
            meterRegistry.counter("fraud.analysis.total", "service", "ai-fraud-detection").increment(100);
            meterRegistry.counter("fraud.analysis.failure", "service", "ai-fraud-detection").increment(10);

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
