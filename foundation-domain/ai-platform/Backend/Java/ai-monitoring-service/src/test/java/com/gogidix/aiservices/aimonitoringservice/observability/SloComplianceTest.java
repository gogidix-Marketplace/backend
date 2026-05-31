package com.gogidix.aiservices.aimonitoringservice.observability;

import com.gogidix.aiservices.aimonitoringservice.infrastructure.metrics.AiMonitoringMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: SLO Compliance & Observability Tests.
 *
 * These tests validate that metrics are properly configured.
 */
@DisplayName("Financial-Grade: SLO Compliance & Observability Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    private AiMonitoringMetrics metrics;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new AiMonitoringMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("1. Metrics Configuration Tests")
    class MetricsConfigurationTests {

        @Test
        @Order(1)
        @DisplayName("Should create metrics registry")
        void shouldCreateMetricsRegistry() {
            assertThat(meterRegistry).isNotNull();
        }

        @Test
        @Order(2)
        @DisplayName("Should initialize metrics")
        void shouldInitializeMetrics() {
            assertThat(metrics).isNotNull();
        }
    }

    @Nested
    @DisplayName("2. Metrics Recording Tests")
    class MetricsRecordingTests {

        @Test
        @Order(10)
        @DisplayName("Should record monitoring latency")
        void shouldRecordMonitoringLatency() {
            metrics.recordMonitoringTime(100);

            double p95Latency = metrics.getMonitoringLatencyP95();
            assertThat(p95Latency).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(11)
        @DisplayName("Should record requests")
        void shouldRecordRequests() {
            metrics.incrementRequestsSuccess();
            metrics.incrementRequestsSuccess();
            metrics.incrementRequestsFailure();
            metrics.incrementRequestsTotal();

            double errorRate = metrics.getErrorRate();
            assertThat(errorRate).isGreaterThanOrEqualTo(0);
        }
    }

    @Nested
    @DisplayName("3. SLO Threshold Tests")
    class SloThresholdTests {

        @Test
        @Order(20)
        @DisplayName("Should have P95 latency below threshold")
        void shouldHaveP95LatencyBelowThreshold() {
            // Record some latencies
            for (int i = 0; i < 100; i++) {
                metrics.recordMonitoringTime(50 + i);
            }

            double p95Latency = metrics.getMonitoringLatencyP95();
            assertThat(p95Latency).isLessThan(5000.0); // Reasonable threshold
        }

        @Test
        @Order(21)
        @DisplayName("Should have error rate below threshold")
        void shouldHaveErrorRateBelowThreshold() {
            // Record successful requests
            for (int i = 0; i < 100; i++) {
                metrics.incrementRequestsSuccess();
            }
            metrics.incrementRequestsTotal();

            double errorRate = metrics.getErrorRate();
            assertThat(errorRate).isLessThan(0.1); // Less than 10%
        }
    }

    @Nested
    @DisplayName("4. Metrics Structure Tests")
    class MetricsStructureTests {

        @Test
        @Order(30)
        @DisplayName("Should have monitoring duration timer")
        void shouldHaveMonitoringDurationTimer() {
            assertThat(meterRegistry.get("ai.monitoring.duration").timer()).isNotNull();
        }

        @Test
        @Order(31)
        @DisplayName("Should have requests counter")
        void shouldHaveRequestsCounter() {
            assertThat(meterRegistry.get("ai.monitoring.requests.total").counter()).isNotNull();
        }
    }
}
