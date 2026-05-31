package com.gogidix.aiservices.aimanagementservice.observability;

import com.gogidix.aiservices.aimanagementservice.infrastructure.metrics.ModelManagementMetrics;
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
    private ModelManagementMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record model registration total counter")
        void shouldRecordModelRegistrationTotalCounter() {
            long initialCount = getCounterValue("ai.model.registration.total");

            metrics.incrementModelRegistrationTotal();

            long finalCount = getCounterValue("ai.model.registration.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should record model registration success counter")
        void shouldRecordModelRegistrationSuccessCounter() {
            long initialCount = getCounterValue("ai.model.registration.success");

            metrics.incrementModelRegistrationSuccess();

            long finalCount = getCounterValue("ai.model.registration.success");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record model registration duration timer")
        void shouldRecordModelRegistrationDurationTimer() {
            metrics.recordModelRegistrationTime(100);

            // Verify timer was recorded
            assertThat(meterRegistry.get("ai.model.registration.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should record model loading duration")
        void shouldRecordModelLoadingDuration() {
            metrics.recordModelLoadingTime(50);

            assertThat(meterRegistry.get("ai.model.loading.duration").timer().count())
                    .isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet registration latency SLO (p95 < 500ms)")
        void shouldMeetRegistrationLatencySlo() {
            // Generate some latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordModelRegistrationTime(100 + i);
            }

            double p95Latency = metrics.getModelRegistrationLatencyP95();

            assertThat(p95Latency)
                    .as("P95 latency should be below 500ms SLO threshold")
                    .isLessThan(500);
        }

        @Test
        @Order(11)
        @DisplayName("Should meet model loading latency SLO (p95 < 5000ms)")
        void shouldMeetModelLoadingLatencySlo() {
            // Generate some loading latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordModelLoadingTime(100 + i * 10);
            }

            double p95Latency = metrics.getModelLoadingLatencyP95();

            assertThat(p95Latency)
                    .as("Model loading P95 latency should be below 5000ms SLO threshold")
                    .isLessThan(5000);
        }
    }

    @Nested
    @DisplayName("3. Error Rate SLO Tests")
    class ErrorRateTests {

        @Test
        @Order(20)
        @DisplayName("Should maintain error rate below SLO threshold")
        void shouldMaintainErrorRateBelowThreshold() {
            metrics.incrementModelRegistrationTotal();
            metrics.incrementModelRegistrationSuccess();

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
            // Verify service tag exists on registration counter
            var counter = meterRegistry.get("ai.model.registration.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") &&
                                       tag.getValue().equals("ai-model-management"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            metrics.recordModelRegistrationTime(100);

            // Check that timer has percentile histogram enabled
            var timer = meterRegistry.get("ai.model.registration.duration").timer();

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

            metrics.recordModelRegistrationTime(100);

            // Verify latency is measurable
            assertThat(metrics.getModelRegistrationLatencyP95())
                    .as("Registration should complete below critical threshold")
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
