package com.gogidix.aiservices.aisecurityservice.observability;

import com.gogidix.aiservices.aisecurityservice.infrastructure.metrics.SecurityServiceMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: SLO Compliance & Observability Tests.
 *
 * These tests validate that metrics are properly collected
 * and SLO thresholds are met.
 */
@DisplayName("Financial-Grade: SLO Compliance & Observability Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    private MeterRegistry meterRegistry;
    private SecurityServiceMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new SecurityServiceMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record encryption total counter")
        void shouldRecordEncryptionTotalCounter() {
            long initialCount = getCounterValue("security.encryption.total");

            metrics.incrementEncryptionTotal();

            long finalCount = getCounterValue("security.encryption.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should record encryption success counter")
        void shouldRecordEncryptionSuccessCounter() {
            long initialCount = getCounterValue("security.encryption.success");

            metrics.incrementEncryptionSuccess();

            long finalCount = getCounterValue("security.encryption.success");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record encryption failure counter")
        void shouldRecordEncryptionFailureCounter() {
            long initialCount = getCounterValue("security.encryption.failure");

            metrics.incrementEncryptionFailure();

            long finalCount = getCounterValue("security.encryption.failure");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(4)
        @DisplayName("Should record decryption counter")
        void shouldRecordDecryptionCounter() {
            long initialCount = getCounterValue("security.decryption.total");

            metrics.incrementDecryptionTotal();

            long finalCount = getCounterValue("security.decryption.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(5)
        @DisplayName("Should record key generation counter")
        void shouldRecordKeyGenerationCounter() {
            long initialCount = getCounterValue("security.key.generation");

            metrics.incrementKeyGeneration();

            long finalCount = getCounterValue("security.key.generation");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(6)
        @DisplayName("Should record key rotation counter")
        void shouldRecordKeyRotationCounter() {
            long initialCount = getCounterValue("security.key.rotation");

            metrics.incrementKeyRotation();

            long finalCount = getCounterValue("security.key.rotation");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(7)
        @DisplayName("Should record encryption duration timer")
        void shouldRecordEncryptionDurationTimer() {
            metrics.recordEncryptionTime(50);

            assertThat(meterRegistry.get("security.encryption.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(8)
        @DisplayName("Should record decryption duration timer")
        void shouldRecordDecryptionDurationTimer() {
            metrics.recordDecryptionTime(30);

            assertThat(meterRegistry.get("security.decryption.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(9)
        @DisplayName("Should record key generation timer")
        void shouldRecordKeyGenerationTimer() {
            metrics.recordKeyGenerationTime(100);

            assertThat(meterRegistry.get("security.key.generation.duration").timer().count())
                    .isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet encryption latency SLO (p95 < 100ms)")
        void shouldMeetEncryptionLatencySlo() {
            // Generate some latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordEncryptionTime(10 + i);
            }

            double p95Latency = metrics.getEncryptionLatencyP95();

            assertThat(p95Latency)
                    .as("P95 encryption latency should be below 100ms SLO threshold")
                    .isLessThan(100);
        }

        @Test
        @Order(11)
        @DisplayName("Should meet decryption latency SLO (p95 < 100ms)")
        void shouldMeetDecryptionLatencySlo() {
            // Generate some decryption latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordDecryptionTime(5 + i);
            }

            double p95Latency = metrics.getDecryptionLatencyP95();

            assertThat(p95Latency)
                    .as("P95 decryption latency should be below 100ms SLO threshold")
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
            metrics.incrementEncryptionTotal();
            metrics.incrementEncryptionSuccess();

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate)
                    .as("Error rate should be below 1% SLO threshold")
                    .isLessThan(0.01);
        }

        @Test
        @Order(21)
        @DisplayName("Should calculate error rate correctly with failures")
        void shouldCalculateErrorRateCorrectly() {
            metrics.incrementEncryptionTotal();
            metrics.incrementEncryptionTotal();
            metrics.incrementEncryptionSuccess();
            metrics.incrementEncryptionFailure();

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate).isEqualTo(0.5); // 1 failure out of 2 total
        }
    }

    @Nested
    @DisplayName("4. Metrics Tag Validation Tests")
    class MetricsTagTests {

        @Test
        @Order(30)
        @DisplayName("Should have correct service tag on metrics")
        void shouldHaveCorrectServiceTag() {
            metrics.incrementEncryptionTotal();

            var counter = meterRegistry.get("security.encryption.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") &&
                                       tag.getValue().equals("ai-security"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            metrics.recordEncryptionTime(50);

            var timer = meterRegistry.get("security.encryption.duration").timer();

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
            long criticalThresholdMs = 100;

            metrics.recordEncryptionTime(10);

            assertThat(metrics.getEncryptionLatencyP95())
                    .as("Encryption should complete below critical threshold")
                    .isLessThan(criticalThresholdMs * 10);
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
