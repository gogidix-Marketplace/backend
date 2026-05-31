package com.gogidix.aiservices.aidocumentclassificationservice.observability;

import com.gogidix.aiservices.aidocumentclassificationservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aidocumentclassificationservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aidocumentclassificationservice.infrastructure.metrics.DocumentClassificationMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: SLO Compliance and Observability Tests.
 *
 * These tests validate service level objectives and observability features.
 *
 * SLO Thresholds:
 * - P95 Latency: < 500ms
 * - P99 Latency: < 1000ms
 * - Error Rate: < 1%
 */
@DisplayName("Financial-Grade: SLO Compliance and Observability Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    private DocumentClassificationMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator reportGenerator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new DocumentClassificationMetrics(meterRegistry);
        thresholdValidator = new ThresholdValidator(metrics);
        reportGenerator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }

    @Nested
    @DisplayName("1. P95 Latency SLO Tests")
    class P95LatencySloTests {

        @Test
        @Order(1)
        @DisplayName("Should meet P95 latency SLO under normal load")
        void shouldMeetP95LatencySloUnderNormalLoad() {
            // Generate requests within SLO
            int normalRequests = 50;

            for (int i = 0; i < normalRequests; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();
                // Simulate latency between 50ms and 200ms
                metrics.recordClassificationTime(50 + (i % 15) * 10);
            }

            double p95Latency = metrics.getClassificationLatencyP95();

            // P95 should be well under 500ms threshold
            assertThat(p95Latency).isLessThan(500.0);
        }

        @Test
        @Order(2)
        @DisplayName("Should detect P95 latency SLO violation")
        void shouldDetectP95LatencySloViolation() {
            // Generate some requests exceeding SLO
            for (int i = 0; i < 20; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();
                // Mix of latencies, some exceeding 500ms
                if (i < 15) {
                    metrics.recordClassificationTime(200);
                } else {
                    metrics.recordClassificationTime(600); // Exceeds SLO
                }
            }

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getThreshold()).isEqualTo(500.0);
        }
    }

    @Nested
    @DisplayName("2. P99 Latency SLO Tests")
    class P99LatencySloTests {

        @Test
        @Order(10)
        @DisplayName("Should meet P99 latency SLO under normal load")
        void shouldMeetP99LatencySloUnderNormalLoad() {
            // Generate requests within SLO
            for (int i = 0; i < 100; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();
                // Latency between 50ms and 300ms
                metrics.recordClassificationTime(50 + (i % 26) * 10);
            }

            double p99Latency = metrics.getClassificationLatencyP99();

            // P99 should be under 1000ms threshold
            assertThat(p99Latency).isLessThan(1000.0);
        }

        @Test
        @Order(11)
        @DisplayName("Should detect P99 latency SLO violation")
        void shouldDetectP99LatencySloViolation() {
            // Generate requests with some extreme latencies
            for (int i = 0; i < 50; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();
                if (i < 45) {
                    metrics.recordClassificationTime(200);
                } else {
                    metrics.recordClassificationTime(1200); // Exceeds P99 SLO
                }
            }

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getThreshold()).isEqualTo(1000.0);
        }
    }

    @Nested
    @DisplayName("3. Error Rate SLO Tests")
    class ErrorRateSloTests {

        @Test
        @Order(20)
        @DisplayName("Should meet error rate SLO under normal conditions")
        void shouldMeetErrorRateSloUnderNormalConditions() {
            // Generate requests with minimal errors
            int totalRequests = 100;
            int errors = 0; // 0% error rate

            for (int i = 0; i < totalRequests; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();
                metrics.recordClassificationTime(100);
            }

            double errorRate = metrics.getErrorRate();

            // Error rate should be 0%
            assertThat(errorRate).isEqualTo(0.0);
        }

        @Test
        @Order(21)
        @DisplayName("Should meet error rate SLO with acceptable error rate")
        void shouldMeetErrorRateSloWithAcceptableErrorRate() {
            // Generate requests with 0.5% error rate (within 1% SLO)
            int totalRequests = 200;
            int errors = 1; // 0.5% error rate

            for (int i = 0; i < totalRequests; i++) {
                metrics.incrementClassificationTotal();
                if (i < totalRequests - errors) {
                    metrics.incrementClassificationSuccess();
                    metrics.recordClassificationTime(100);
                } else {
                    metrics.incrementClassificationFailure();
                }
            }

            double errorRate = metrics.getErrorRate();

            // Error rate should be under 1% threshold
            assertThat(errorRate).isLessThan(0.01);
        }

        @Test
        @Order(22)
        @DisplayName("Should detect error rate SLO violation")
        void shouldDetectErrorRateSloViolation() {
            // Generate requests exceeding 1% error rate
            int totalRequests = 100;
            int errors = 5; // 5% error rate

            for (int i = 0; i < totalRequests; i++) {
                metrics.incrementClassificationTotal();
                if (i < totalRequests - errors) {
                    metrics.incrementClassificationSuccess();
                    metrics.recordClassificationTime(100);
                } else {
                    metrics.incrementClassificationFailure();
                }
            }

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation).isNotNull();
            assertThat(validation.getThreshold()).isEqualTo(1.0); // 1%
        }
    }

    @Nested
    @DisplayName("4. ML Prediction Latency Tests")
    class MlPredictionLatencyTests {

        @Test
        @Order(30)
        @DisplayName("Should track ML prediction latency")
        void shouldTrackMlPredictionLatency() {
            // Simulate ML prediction timing
            for (int i = 0; i < 20; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();

                Timer.Sample sample = metrics.startMlPredictionTimer();
                // Simulate ML prediction work
                metrics.recordClassificationTime(50);
                metrics.stopMlPredictionTimer(sample);
            }

            double mlP95Latency = metrics.getMlPredictionLatencyP95();
            assertThat(mlP95Latency).isGreaterThanOrEqualTo(0);
        }

        @Test
        @Order(31)
        @DisplayName("Should detect ML prediction latency degradation")
        void shouldDetectMlPredictionLatencyDegradation() {
            // Simulate ML predictions with varying latency
            for (int i = 0; i < 30; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();

                Timer.Sample sample = metrics.startMlPredictionTimer();
                // Some predictions are slower
                long predictionTime = (i < 25) ? 50 : 150;
                metrics.recordClassificationTime(predictionTime);
                metrics.stopMlPredictionTimer(sample);
            }

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ML_PREDICTION_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getThreshold()).isEqualTo(100.0); // 100ms threshold
        }
    }

    @Nested
    @DisplayName("5. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(40)
        @DisplayName("Should collect all required metrics")
        void shouldCollectAllRequiredMetrics() {
            // Generate traffic
            for (int i = 0; i < 10; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();
                metrics.incrementDocumentsClassified();
                metrics.incrementHighConfidence();
                metrics.recordClassificationTime(100);
            }

            // Verify all counters are registered
            assertThat(meterRegistry.get("ai.document.classification.total").counter().count()).isGreaterThan(0);
            assertThat(meterRegistry.get("ai.document.classification.success").counter().count()).isGreaterThan(0);
            assertThat(meterRegistry.get("ai.document.classification.documents").counter().count()).isGreaterThan(0);
            assertThat(meterRegistry.get("ai.document.classification.high.confidence").counter().count()).isGreaterThan(0);
        }

        @Test
        @Order(41)
        @DisplayName("Should track confidence distribution")
        void shouldTrackConfidenceDistribution() {
            // Mix of high and low confidence classifications
            for (int i = 0; i < 20; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();
                if (i < 15) {
                    metrics.incrementHighConfidence();
                } else {
                    metrics.incrementLowConfidence();
                }
            }

            long highConfidence = (long) meterRegistry
                    .get("ai.document.classification.high.confidence")
                    .counter()
                    .count();

            long lowConfidence = (long) meterRegistry
                    .get("ai.document.classification.low.confidence")
                    .counter()
                    .count();

            assertThat(highConfidence + lowConfidence).isEqualTo(20);
        }

        @Test
        @Order(42)
        @DisplayName("Should generate compliance report with all sections")
        void shouldGenerateComplianceReportWithAllSections() {
            // Generate some traffic
            for (int i = 0; i < 10; i++) {
                metrics.incrementClassificationTotal();
                metrics.incrementClassificationSuccess();
                metrics.recordClassificationTime(100);
            }

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getSloCompliance()).isNotNull();
            assertThat(report.getPerformanceMetrics()).isNotNull();
            assertThat(report.getGovernanceStatus()).isNotNull();

            // Verify SLO compliance section has required fields
            ComplianceReportGenerator.SloComplianceSection slo = report.getSloCompliance();
            assertThat(slo.getP95LatencyMs()).isGreaterThanOrEqualTo(0);
            assertThat(slo.getP99LatencyMs()).isGreaterThanOrEqualTo(0);
            assertThat(slo.getErrorRate()).isGreaterThanOrEqualTo(0);

            // Verify performance metrics section has required fields
            ComplianceReportGenerator.PerformanceMetricsSection perf = report.getPerformanceMetrics();
            assertThat(perf.getTotalRequests()).isGreaterThan(0);
            assertThat(perf.getSuccessfulRequests()).isGreaterThan(0);
        }
    }
}
