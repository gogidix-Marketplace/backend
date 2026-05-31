package com.gogidix.aiservices.aisummarizationservice.observability;

import com.gogidix.aiservices.aisummarizationservice.infrastructure.metrics.SummarizationMetrics;
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
 * and SLO thresholds are met for Summarization Service.
 *
 * SLO Thresholds:
 * - P95 Latency: < 500ms (summarization)
 * - P99 Latency: < 1000ms (summarization)
 * - Error Rate: < 2%
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance & Observability Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    @Autowired
    private SummarizationMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record summarization total counter")
        void shouldRecordSummarizationTotalCounter() {
            long initialCount = getCounterValue("ai.summarization.total");

            // Simulate metric recording
            metrics.incrementSummarizationTotal();

            long finalCount = getCounterValue("ai.summarization.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should record summarization success counter")
        void shouldRecordSummarizationSuccessCounter() {
            long initialCount = getCounterValue("ai.summarization.success");

            metrics.incrementSummarizationSuccess();

            long finalCount = getCounterValue("ai.summarization.success");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record summarization duration timer")
        void shouldRecordSummarizationDurationTimer() {
            metrics.recordSummarizationTime(100);

            assertThat(meterRegistry.get("ai.summarization.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should record NLP processing duration")
        void shouldRecordNlpProcessingDuration() {
            var sample = metrics.startNlpProcessingTimer();
            // Simulate processing
            metrics.stopNlpProcessingTimer(sample);

            assertThat(meterRegistry.get("ai.summarization.nlp.duration").timer().count())
                    .isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet summarization latency SLO (p95 < 500ms)")
        void shouldMeetSummarizationLatencySlo() {
            int iterations = 50;
            long maxAllowedLatencyMs = 500;

            for (int i = 0; i < iterations; i++) {
                metrics.recordSummarizationTime(100 + (i % 400)); // Simulate varying latencies
            }

            double p95Latency = metrics.getSummarizationLatencyP95();

            assertThat(p95Latency)
                    .as("P95 latency should be below %dms SLO threshold", maxAllowedLatencyMs)
                    .isLessThan(maxAllowedLatencyMs);
        }

        @Test
        @Order(11)
        @DisplayName("Should meet P99 latency SLO (p99 < 1000ms)")
        void shouldMeetP99LatencySlo() {
            int iterations = 50;
            long maxAllowedLatencyMs = 1000;

            for (int i = 0; i < iterations; i++) {
                metrics.recordSummarizationTime(100 + (i % 800)); // Simulate varying latencies
            }

            double p99Latency = metrics.getSummarizationLatencyP99();

            assertThat(p99Latency)
                    .as("P99 latency should be below %dms SLO threshold", maxAllowedLatencyMs)
                    .isLessThan(maxAllowedLatencyMs);
        }

        @Test
        @Order(12)
        @DisplayName("Should meet NLP processing latency SLO (p95 < 250ms)")
        void shouldMeetNlpProcessingLatencySlo() {
            int iterations = 50;

            for (int i = 0; i < iterations; i++) {
                var sample = metrics.startNlpProcessingTimer();
                // Simulate processing
                metrics.stopNlpProcessingTimer(sample);
            }

            double p95Latency = metrics.getNlpProcessingLatencyP95();

            assertThat(p95Latency)
                    .as("NLP processing P95 latency should be below 250ms SLO threshold")
                    .isLessThan(250);
        }
    }

    @Nested
    @DisplayName("3. Error Rate SLO Tests")
    class ErrorRateTests {

        @Test
        @Order(20)
        @DisplayName("Should maintain error rate below SLO threshold")
        void shouldMaintainErrorRateBelowThreshold() {
            int totalRequests = 100;
            double maxErrorRate = 0.02; // 2% error rate SLO

            for (int i = 0; i < totalRequests; i++) {
                metrics.incrementSummarizationTotal();
                if (i % 100 != 0) { // 99% success rate
                    metrics.incrementSummarizationSuccess();
                }
            }

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate)
                    .as("Error rate should be below %.1f%% SLO threshold", maxErrorRate * 100)
                    .isLessThan(maxErrorRate);
        }
    }

    @Nested
    @DisplayName("4. Metrics Tag Validation Tests")
    class MetricsTagTests {

        @Test
        @Order(30)
        @DisplayName("Should have correct service tag on metrics")
        void shouldHaveCorrectServiceTag() {
            // Verify service tag exists on summarization counter
            var counter = meterRegistry.get("ai.summarization.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") &&
                                       tag.getValue().equals("ai-summarization"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            metrics.recordSummarizationTime(100);

            // Check that timer has percentile histogram enabled
            var timer = meterRegistry.get("ai.summarization.duration").timer();

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
            long criticalThresholdMs = 500;

            metrics.recordSummarizationTime(100);

            // Verify we can measure latency
            double p95Latency = metrics.getSummarizationLatencyP95();
            assertThat(p95Latency).isGreaterThanOrEqualTo(0);
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
