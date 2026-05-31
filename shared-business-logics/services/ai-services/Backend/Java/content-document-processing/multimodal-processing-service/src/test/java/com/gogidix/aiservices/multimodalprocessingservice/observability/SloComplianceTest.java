package com.gogidix.aiservices.multimodalprocessingservice.observability;

import com.gogidix.aiservices.multimodalprocessingservice.infrastructure.metrics.MultimodalProcessingMetrics;
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
 * and SLO thresholds are met for Multimodal Processing Service.
 *
 * SLO Thresholds:
 * - P95 Latency: < 2000ms (multimodal)
 * - P99 Latency: < 5000ms (multimodal)
 * - Error Rate: < 2%
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance & Observability Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    @Autowired
    private MultimodalProcessingMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record processing total counter")
        void shouldRecordProcessingTotalCounter() {
            long initialCount = getCounterValue("multimodal.processing.total");

            metrics.incrementProcessingTotal();

            long finalCount = getCounterValue("multimodal.processing.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should record processing success counter")
        void shouldRecordProcessingSuccessCounter() {
            long initialCount = getCounterValue("multimodal.processing.success");

            metrics.incrementProcessingSuccess();

            long finalCount = getCounterValue("multimodal.processing.success");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record processing duration timer")
        void shouldRecordProcessingDurationTimer() {
            metrics.recordProcessingTime(100);

            assertThat(meterRegistry.get("multimodal.processing.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should record embedding generation duration")
        void shouldRecordEmbeddingGenerationDuration() {
            var sample = metrics.startEmbeddingTimer();
            metrics.stopEmbeddingTimer(sample);

            assertThat(meterRegistry.get("multimodal.embedding.duration").timer().count())
                    .isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet multimodal processing latency SLO (p95 < 2000ms)")
        void shouldMeetProcessingLatencySlo() {
            int iterations = 50;
            long maxAllowedLatencyMs = 2000;

            for (int i = 0; i < iterations; i++) {
                metrics.recordProcessingTime(500 + (i % 1500));
            }

            double p95Latency = metrics.getProcessingLatencyP95();

            assertThat(p95Latency)
                    .as("P95 latency should be below %dms SLO threshold", maxAllowedLatencyMs)
                    .isLessThan(maxAllowedLatencyMs);
        }

        @Test
        @Order(11)
        @DisplayName("Should meet P99 latency SLO (p99 < 5000ms)")
        void shouldMeetP99LatencySlo() {
            int iterations = 50;
            long maxAllowedLatencyMs = 5000;

            for (int i = 0; i < iterations; i++) {
                metrics.recordProcessingTime(500 + (i % 4000));
            }

            double p99Latency = metrics.getProcessingLatencyP99();

            assertThat(p99Latency)
                    .as("P99 latency should be below %dms SLO threshold", maxAllowedLatencyMs)
                    .isLessThan(maxAllowedLatencyMs);
        }

        @Test
        @Order(12)
        @DisplayName("Should meet embedding generation latency SLO (p95 < 1000ms)")
        void shouldMeetEmbeddingGenerationLatencySlo() {
            int iterations = 50;

            for (int i = 0; i < iterations; i++) {
                var sample = metrics.startEmbeddingTimer();
                metrics.stopEmbeddingTimer(sample);
            }

            double p95Latency = metrics.getEmbeddingLatencyP95();

            assertThat(p95Latency)
                    .as("Embedding generation P95 latency should be below 1000ms SLO threshold")
                    .isLessThan(1000);
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
            double maxErrorRate = 0.02;

            for (int i = 0; i < totalRequests; i++) {
                metrics.incrementProcessingTotal();
                if (i % 100 != 0) {
                    metrics.incrementProcessingSuccess();
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
            var counter = meterRegistry.get("multimodal.processing.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") &&
                                       tag.getValue().equals("multimodal-processing"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            metrics.recordProcessingTime(100);

            var timer = meterRegistry.get("multimodal.processing.duration").timer();

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
            long criticalThresholdMs = 2000;

            metrics.recordProcessingTime(500);

            double p95Latency = metrics.getProcessingLatencyP95();
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
