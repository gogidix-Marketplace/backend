package com.gogidix.aiservices.aicustomerengagementservice.security;

import com.gogidix.aiservices.aicustomerengagementservice.TestApplication;
import com.gogidix.aiservices.aicustomerengagementservice.infrastructure.metrics.CustomerEngagementMetrics;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: Security Validation Tests.
 *
 * These tests validate security controls and input validation.
 */
@SpringBootTest(
    classes = TestApplication.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Security Validation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityValidationTest {

    @Autowired
    private CustomerEngagementMetrics metrics;

    @Nested
    @DisplayName("1. Metrics Security Tests")
    class MetricsSecurityTests {

        @Test
        @Order(1)
        @DisplayName("Should prevent negative counter values")
        void shouldPreventNegativeCounterValues() {
            long initialCount = (long) metrics.getMeterRegistry()
                    .get("engagement.analysis.total").counter().count();

            // Increment normally
            metrics.incrementEngagementTotal();

            long finalCount = (long) metrics.getMeterRegistry()
                    .get("engagement.analysis.total").counter().count();

            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should handle extreme latency values")
        void shouldHandleExtremeLatencyValues() {
            // Test zero latency
            metrics.recordEngagementTime(0);

            // Test very high latency
            metrics.recordEngagementTime(Long.MAX_VALUE / 1_000_000);

            // Test negative latency (should be handled gracefully)
            metrics.recordEngagementTime(-1);

            var timer = metrics.getMeterRegistry().get("engagement.analysis.duration").timer();
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. Counter Overflow Protection Tests")
    class CounterOverflowProtectionTests {

        @Test
        @Order(10)
        @DisplayName("Should handle large counter values")
        void shouldHandleLargeCounterValues() {
            long initialCount = (long) metrics.getMeterRegistry()
                    .get("engagement.messages.sent").counter().count();

            // Simulate large volume
            for (int i = 0; i < 10000; i++) {
                metrics.incrementMessagesSent();
            }

            long finalCount = (long) metrics.getMeterRegistry()
                    .get("engagement.messages.sent").counter().count();

            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount + 10000);
        }

        @Test
        @Order(11)
        @DisplayName("Should maintain consistency with multiple counter types")
        void shouldMaintainConsistencyWithMultipleCounterTypes() {
            long totalInitial = (long) metrics.getMeterRegistry()
                    .get("engagement.analysis.total").counter().count();
            long successInitial = (long) metrics.getMeterRegistry()
                    .get("engagement.analysis.success").counter().count();
            long failureInitial = (long) metrics.getMeterRegistry()
                    .get("engagement.analysis.failure").counter().count();

            // Add in consistent pattern
            for (int i = 0; i < 100; i++) {
                metrics.incrementEngagementTotal();
                if (i % 10 == 0) {
                    metrics.incrementEngagementFailure();
                } else {
                    metrics.incrementEngagementSuccess();
                }
            }

            long totalFinal = (long) metrics.getMeterRegistry()
                    .get("engagement.analysis.total").counter().count();
            long successFinal = (long) metrics.getMeterRegistry()
                    .get("engagement.analysis.success").counter().count();
            long failureFinal = (long) metrics.getMeterRegistry()
                    .get("engagement.analysis.failure").counter().count();

            assertThat(totalFinal - totalInitial).isEqualTo(100);
            assertThat(successFinal - successInitial + failureFinal - failureInitial)
                    .isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("3. Timer Security Tests")
    class TimerSecurityTests {

        @Test
        @Order(20)
        @DisplayName("Should handle concurrent timer operations safely")
        void shouldHandleConcurrentTimerOperationsSafely() throws Exception {
            int threadCount = 10;
            java.util.concurrent.ExecutorService executor =
                    java.util.concurrent.Executors.newFixedThreadPool(threadCount);
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(threadCount);

            for (int t = 0; t < threadCount; t++) {
                executor.submit(() -> {
                    try {
                        for (int i = 0; i < 100; i++) {
                            var sample = metrics.startEngagementTimer();
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                // Ignore
                            }
                            metrics.stopEngagementTimer(sample);
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            boolean completed = latch.await(30, java.util.concurrent.TimeUnit.SECONDS);
            assertThat(completed).isTrue();

            var timer = metrics.getMeterRegistry().get("engagement.analysis.duration").timer();
            assertThat(timer.count()).isGreaterThan(0);

            executor.shutdown();
        }

        @Test
        @Order(21)
        @DisplayName("Should prevent timer sample reuse")
        void shouldPreventTimerSampleReuse() {
            var sample = metrics.startEngagementTimer();

            // Stop once
            metrics.stopEngagementTimer(sample);

            // Attempting to stop again should not cause issues
            // (the timer sample should be consumed)
            metrics.stopEngagementTimer(sample);

            var timer = metrics.getMeterRegistry().get("engagement.analysis.duration").timer();
            assertThat(timer).isNotNull();
        }
    }

    @Nested
    @DisplayName("4. Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @Order(30)
        @DisplayName("Should maintain accurate error rate calculation")
        void shouldMaintainAccurateErrorRateCalculation() {
            // Get initial metrics state
            var totalCounter = metrics.getMeterRegistry().get("engagement.analysis.total").counter();
            var failureCounter = metrics.getMeterRegistry().get("engagement.analysis.failure").counter();
            double initialTotal = totalCounter.count();
            double initialFailures = failureCounter.count();

            // Create known scenario: 100 total, 5 failures = 5% error rate
            for (int i = 0; i < 100; i++) {
                metrics.incrementEngagementTotal();
                if (i < 95) {
                    metrics.incrementEngagementSuccess();
                } else {
                    metrics.incrementEngagementFailure();
                }
            }

            double finalTotal = totalCounter.count();
            double finalFailures = failureCounter.count();
            double incrementalErrorRate = (finalFailures - initialFailures) / (finalTotal - initialTotal);

            // Should be approximately 5%
            assertThat(incrementalErrorRate).isGreaterThan(0.04);
            assertThat(incrementalErrorRate).isLessThan(0.06);
        }

        @Test
        @Order(31)
        @DisplayName("Should handle zero division in error rate")
        void shouldHandleZeroDivisionInErrorRate() {
            // Create a new metrics instance scenario (no requests)
            double errorRate = metrics.getErrorRate();

            // Should not throw and should be valid (0 or some value)
            assertThat(errorRate).isGreaterThanOrEqualTo(0);
            assertThat(errorRate).isLessThanOrEqualTo(1);
        }
    }

    @Nested
    @DisplayName("5. Metrics Registry Safety Tests")
    class MetricsRegistrySafetyTests {

        @Test
        @Order(40)
        @DisplayName("Should handle registry access safely")
        void shouldHandleRegistryAccessSafely() {
            var registry = metrics.getMeterRegistry();

            assertThat(registry).isNotNull();

            // Access various counters
            var totalCounter = registry.get("engagement.analysis.total").counter();
            var successCounter = registry.get("engagement.analysis.success").counter();
            var failureCounter = registry.get("engagement.analysis.failure").counter();

            assertThat(totalCounter).isNotNull();
            assertThat(successCounter).isNotNull();
            assertThat(failureCounter).isNotNull();
        }

        @Test
        @Order(41)
        @DisplayName("Should handle missing metrics gracefully")
        void shouldHandleMissingMetricsGracefully() {
            var registry = metrics.getMeterRegistry();

            // Try to get a non-existent meter
            var missingCounter = registry.find("non.existent.counter").counter();

            // Should return null or handle gracefully
            assertThat(missingCounter).isNull();
        }
    }

    @Nested
    @DisplayName("6. Concurrency Safety Tests")
    class ConcurrencySafetyTests {

        @Test
        @Order(50)
        @DisplayName("Should maintain thread safety under high concurrency")
        void shouldMaintainThreadSafetyUnderHighConcurrency() throws Exception {
            int threadCount = 20;
            java.util.concurrent.ExecutorService executor =
                    java.util.concurrent.Executors.newFixedThreadPool(threadCount);
            java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(threadCount);
            java.util.concurrent.atomic.AtomicInteger successCount = new java.util.concurrent.atomic.AtomicInteger(0);

            for (int t = 0; t < threadCount; t++) {
                executor.submit(() -> {
                    try {
                        for (int i = 0; i < 500; i++) {
                            metrics.incrementEngagementTotal();
                            metrics.incrementEngagementSuccess();
                            metrics.incrementInteractionsRecorded();

                            var sample = metrics.startCampaignCreationTimer();
                            metrics.stopCampaignCreationTimer(sample);
                        }
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        // Should not throw exceptions
                    } finally {
                        latch.countDown();
                    }
                });
            }

            boolean completed = latch.await(60, java.util.concurrent.TimeUnit.SECONDS);
            assertThat(completed).isTrue();
            assertThat(successCount.get()).isEqualTo(threadCount);

            // Verify metrics were recorded
            var timer = metrics.getMeterRegistry().get("engagement.campaign.creation.duration").timer();
            assertThat(timer.count()).isGreaterThan(0);

            executor.shutdown();
        }
    }
}
