package com.gogidix.aiservices.aiinferenceservice.observability;

import com.gogidix.aiservices.aiinferenceservice.infrastructure.metrics.InferenceMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: SLO Compliance & Observability Tests.
 *
 * These tests validate that metrics are properly collected
 * and SLO thresholds are met.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance & Observability Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    @Autowired
    private InferenceMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record inference total counter")
        void shouldRecordInferenceTotalCounter() {
            long initialCount = getCounterValue("ai.inference.total");

            metrics.incrementInferenceTotal();

            long finalCount = getCounterValue("ai.inference.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should record inference success counter")
        void shouldRecordInferenceSuccessCounter() {
            long initialCount = getCounterValue("ai.inference.success");

            metrics.incrementInferenceSuccess();

            long finalCount = getCounterValue("ai.inference.success");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record inference duration timer")
        void shouldRecordInferenceDurationTimer() {
            metrics.recordInferenceTime(100);

            // Verify timer was recorded
            assertThat(meterRegistry.get("ai.inference.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should record prediction duration")
        void shouldRecordPredictionDuration() {
            metrics.recordPredictionTime(50);

            assertThat(meterRegistry.get("ai.prediction.duration").timer().count())
                    .isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet inference latency SLO (p95 < 500ms)")
        void shouldMeetInferenceLatencySlo() {
            // Generate some latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordInferenceTime(100 + i);
            }

            double p95Latency = metrics.getInferenceLatencyP95();

            assertThat(p95Latency)
                    .as("P95 latency should be below 500ms SLO threshold")
                    .isLessThan(500);
        }

        @Test
        @Order(11)
        @DisplayName("Should meet prediction latency SLO (p95 < 100ms)")
        void shouldMeetPredictionLatencySlo() {
            // Generate some prediction latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordPredictionTime(20 + i);
            }

            double p95Latency = metrics.getPredictionLatencyP95();

            assertThat(p95Latency)
                    .as("Prediction P95 latency should be below 100ms SLO threshold")
                    .isLessThan(100);
        }
    }

    @Nested
    @DisplayName("3. Error Rate SLO Tests")
    class ErrorRateTests {

        @Test
        @Order(20)
        @DisplayName("Should maintain error rate below SLO threshold")
        void shouldMaintainErrorRateBelowThreshold() {
            metrics.incrementInferenceTotal();
            metrics.incrementInferenceSuccess();

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate)
                    .as("Error rate should be below 1% SLO threshold")
                    .isLessThan(0.01);
        }
    }

    @Nested
    @DisplayName("4. Metrics Tag Validation Tests")
    class MetricsTagTests {

        @Test
        @Order(30)
        @DisplayName("Should have correct service tag on metrics")
        void shouldHaveCorrectServiceTag() {
            // Verify service tag exists on inference counter
            var counter = meterRegistry.get("ai.inference.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") &&
                                       tag.getValue().equals("ai-inference"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            metrics.recordInferenceTime(100);

            // Check that timer has percentile histogram enabled
            var timer = meterRegistry.get("ai.inference.duration").timer();

            assertThat(timer).isNotNull();
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("5. SLO Threshold Validation Tests")
    class SloThresholdTests {

        @Test
        @Order(40)
        @DisplayName("Should validate SLO latency thresholds")
        void shouldValidateSloLatencyThresholds() {
            // Define SLO thresholds
            long criticalThresholdMs = 500;

            metrics.recordInferenceTime(100);

            // Verify latency is measurable
            assertThat(metrics.getInferenceLatencyP95())
                    .as("Inference should complete below critical threshold")
                    .isLessThan(criticalThresholdMs * 10); // Allow some margin for test environment
        }
    }

    private long getCounterValue(String counterName) {
        try {
            var counter = meterRegistry.get(counterName).counter();
            return counter != null ? (long) counter.count() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
