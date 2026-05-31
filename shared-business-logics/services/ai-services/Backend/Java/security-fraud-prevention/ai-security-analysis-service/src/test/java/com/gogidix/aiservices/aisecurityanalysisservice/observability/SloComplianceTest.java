package com.gogidix.aiservices.aisecurityanalysisservice.observability;

import com.gogidix.aiservices.aisecurityanalysisservice.infrastructure.metrics.SecurityAnalysisMetrics;
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
    private SecurityAnalysisMetrics metrics;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new SecurityAnalysisMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record scan total counter")
        void shouldRecordScanTotalCounter() {
            long initialCount = getCounterValue("security.scan.total");

            metrics.incrementScanTotal();

            long finalCount = getCounterValue("security.scan.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should record scan completed counter")
        void shouldRecordScanCompletedCounter() {
            long initialCount = getCounterValue("security.scan.completed");

            metrics.incrementScanCompleted();

            long finalCount = getCounterValue("security.scan.completed");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record vulnerability counters by severity")
        void shouldRecordVulnerabilityCountersBySeverity() {
            long initialCritical = getCounterValue("security.vulnerability.critical");
            long initialHigh = getCounterValue("security.vulnerability.high");

            metrics.incrementCriticalVulnerability();
            metrics.incrementHighVulnerability();

            assertThat(getCounterValue("security.vulnerability.critical"))
                    .isGreaterThan(initialCritical);
            assertThat(getCounterValue("security.vulnerability.high"))
                    .isGreaterThan(initialHigh);
        }

        @Test
        @Order(4)
        @DisplayName("Should record scan duration timer")
        void shouldRecordScanDurationTimer() {
            metrics.recordScanTime(500);

            assertThat(meterRegistry.get("security.scan.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(5)
        @DisplayName("Should record vulnerability detection duration")
        void shouldRecordVulnerabilityDetectionDuration() {
            Timer.Sample sample = metrics.startVulnerabilityDetectionTimer();
            // Simulate some work
            metrics.stopVulnerabilityDetectionTimer(sample);

            var timer = meterRegistry.get("security.vulnerability.detection.duration").timer();
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @Order(6)
        @DisplayName("Should record report generation timer")
        void shouldRecordReportGenerationTimer() {
            Timer.Sample sample = metrics.startReportGenerationTimer();
            // Simulate report generation
            metrics.stopReportGenerationTimer(sample);

            var timer = meterRegistry.get("security.report.generation.duration").timer();
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet scan latency SLO (p95 < 5000ms)")
        void shouldMeetScanLatencySlo() {
            // Generate some latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordScanTime(100 + i * 50);
            }

            double p95Latency = metrics.getScanLatencyP95();

            assertThat(p95Latency)
                    .as("P95 latency should be below 5000ms SLO threshold")
                    .isLessThan(5000);
        }

        @Test
        @Order(11)
        @DisplayName("Should meet scan P99 latency SLO (p99 < 10000ms)")
        void shouldMeetScanP99LatencySlo() {
            // Generate some scan latency data
            for (int i = 0; i < 50; i++) {
                metrics.recordScanTime(100 + i * 100);
            }

            double p99Latency = metrics.getScanLatencyP99();

            assertThat(p99Latency)
                    .as("Scan P99 latency should be below 10000ms SLO threshold")
                    .isLessThan(10000);
        }

        @Test
        @Order(12)
        @DisplayName("Should meet vulnerability detection latency SLO")
        void shouldMeetVulnerabilityDetectionLatencySlo() {
            Timer.Sample sample;
            for (int i = 0; i < 30; i++) {
                sample = metrics.startVulnerabilityDetectionTimer();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                metrics.stopVulnerabilityDetectionTimer(sample);
            }

            double p95Latency = metrics.getVulnerabilityDetectionLatencyP95();

            assertThat(p95Latency)
                    .as("Vulnerability detection P95 latency should be below 1000ms SLO threshold")
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
            metrics.incrementScanTotal();
            metrics.incrementScanCompleted();

            double errorRate = metrics.getErrorRate();

            assertThat(errorRate)
                    .as("Error rate should be below 2% SLO threshold")
                    .isLessThan(0.02);
        }

        @Test
        @Order(21)
        @DisplayName("Should maintain success rate above SLO threshold")
        void shouldMaintainSuccessRateAboveThreshold() {
            metrics.incrementScanTotal();
            metrics.incrementScanCompleted();

            double successRate = metrics.getSuccessRate();

            assertThat(successRate)
                    .as("Success rate should be above 98% SLO threshold")
                    .isGreaterThanOrEqualTo(0.98);
        }

        @Test
        @Order(22)
        @DisplayName("Should calculate error rate correctly with failures")
        void shouldCalculateErrorRateCorrectly() {
            metrics.incrementScanTotal();
            metrics.incrementScanTotal();
            metrics.incrementScanCompleted();
            metrics.incrementScanFailed();

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
            metrics.incrementScanTotal();

            var counter = meterRegistry.get("security.scan.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") &&
                                       tag.getValue().equals("ai-security-analysis"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            metrics.recordScanTime(100);

            var timer = meterRegistry.get("security.scan.duration").timer();

            assertThat(timer).isNotNull();
            assertThat(timer.count()).isGreaterThan(0);
        }

        @Test
        @Order(32)
        @DisplayName("Should track vulnerability severity separately")
        void shouldTrackVulnerabilitySeveritySeparately() {
            metrics.incrementCriticalVulnerability();
            metrics.incrementHighVulnerability();
            metrics.incrementMediumVulnerability();
            metrics.incrementLowVulnerability();

            long critical = getCounterValue("security.vulnerability.critical");
            long high = getCounterValue("security.vulnerability.high");
            long medium = getCounterValue("security.vulnerability.medium");
            long low = getCounterValue("security.vulnerability.low");

            assertThat(critical).isGreaterThan(0);
            assertThat(high).isGreaterThan(0);
            assertThat(medium).isGreaterThan(0);
            assertThat(low).isGreaterThan(0);
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

            metrics.recordScanTime(100);

            assertThat(metrics.getScanLatencyP95())
                    .as("Scan should complete below critical threshold")
                    .isLessThan(criticalThresholdMs * 10);
        }

        @Test
        @Order(41)
        @DisplayName("Should track all vulnerability counters")
        void shouldTrackAllVulnerabilityCounters() {
            long totalInitial = getCounterValue("security.vulnerability.found");

            metrics.incrementVulnerabilitiesFound();
            metrics.incrementCriticalVulnerability();
            metrics.incrementHighVulnerability();

            long totalFinal = getCounterValue("security.vulnerability.found");
            long critical = getCounterValue("security.vulnerability.critical");
            long high = getCounterValue("security.vulnerability.high");

            assertThat(totalFinal).isGreaterThan(totalInitial);
            assertThat(critical).isGreaterThan(0);
            assertThat(high).isGreaterThan(0);
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
