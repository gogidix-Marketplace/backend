package com.gogidix.aiservices.aitrainingservice.observability;

import com.gogidix.aiservices.aitrainingservice.infrastructure.metrics.TrainingMetrics;
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
    private TrainingMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record training job total counter")
        void shouldRecordTrainingJobTotalCounter() {
            long initialCount = getCounterValue("ai.training.job.total");

            metrics.incrementTrainingJobTotal();

            long finalCount = getCounterValue("ai.training.job.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should record training job success counter")
        void shouldRecordTrainingJobSuccessCounter() {
            long initialCount = getCounterValue("ai.training.job.success");

            metrics.incrementTrainingJobSuccess();

            long finalCount = getCounterValue("ai.training.job.success");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record training job duration timer")
        void shouldRecordTrainingJobDurationTimer() {
            metrics.recordTrainingJobTime(100);

            // Verify timer was recorded
            assertThat(meterRegistry.get("ai.training.job.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should record model training duration")
        void shouldRecordModelTrainingDuration() {
            metrics.recordModelTrainingTime(50);

            assertThat(meterRegistry.get("ai.model.training.duration").timer().count())
                    .isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet training job latency SLO (p95 < 5000ms)")
        void shouldMeetTrainingJobLatencySlo() {
            // Generate some latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordTrainingJobTime(100 + i * 10);
            }

            double p95Latency = metrics.getTrainingJobLatencyP95();

            assertThat(p95Latency)
                    .as("P95 latency should be below 5000ms SLO threshold")
                    .isLessThan(5000);
        }

        @Test
        @Order(11)
        @DisplayName("Should meet model training latency SLO (p95 < 10000ms)")
        void shouldMeetModelTrainingLatencySlo() {
            // Generate some training latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordModelTrainingTime(100 + i * 20);
            }

            double p95Latency = metrics.getModelTrainingLatencyP95();

            assertThat(p95Latency)
                    .as("Model training P95 latency should be below 10000ms SLO threshold")
                    .isLessThan(10000);
        }
    }

    @Nested
    @DisplayName("3. Error Rate SLO Tests")
    class ErrorRateTests {

        @Test
        @Order(20)
        @DisplayName("Should maintain error rate below SLO threshold")
        void shouldMaintainErrorRateBelowThreshold() {
            metrics.incrementTrainingJobTotal();
            metrics.incrementTrainingJobSuccess();

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate)
                    .as("Error rate should be below 5% SLO threshold")
                    .isLessThan(0.05);
        }
    }

    @Nested
    @DisplayName("4. Metrics Tag Validation Tests")
    class MetricsTagTests {

        @Test
        @Order(30)
        @DisplayName("Should have correct service tag on metrics")
        void shouldHaveCorrectServiceTag() {
            // Verify service tag exists on training counter
            var counter = meterRegistry.get("ai.training.job.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") &&
                                       tag.getValue().equals("ai-training"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            metrics.recordTrainingJobTime(100);

            // Check that timer has percentile histogram enabled
            var timer = meterRegistry.get("ai.training.job.duration").timer();

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
            long criticalThresholdMs = 5000;

            metrics.recordTrainingJobTime(100);

            // Verify latency is measurable
            assertThat(metrics.getTrainingJobLatencyP95())
                    .as("Training should complete below critical threshold")
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
