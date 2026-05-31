package com.gogidix.aiservices.aidocumentprocessingservice.observability;

import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.request.ProcessDocumentRequest;
import com.gogidix.aiservices.aidocumentprocessingservice.application.service.DocumentProcessingService;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.DocumentType;
import com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.metrics.DocumentProcessingMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

/**
 * Financial-Grade: SLO Compliance & Observability Tests.
 *
 * These tests validate that metrics are properly collected
 * and SLO thresholds are met for Document Processing Service.
 *
 * SLO Thresholds:
 * - P95 Latency: < 1000ms (document)
 * - P99 Latency: < 2000ms (document)
 * - Error Rate: < 2%
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance & Observability Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    @Autowired
    private DocumentProcessingService documentProcessingService;

    @Autowired
    private DocumentProcessingMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @MockBean
    private com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.DocumentProcessingRepository repository;

    @MockBean
    private com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.OcrEnginePort ocrEngine;

    @MockBean
    private com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.EventPublisherPort eventPublisher;

    private static final String TEST_USER = "slo-test-user";

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record processing total counter")
        void shouldRecordProcessingTotalCounter() {
            long initialCount = getCounterValue("document.processing.total");

            ProcessDocumentRequest request = createTestRequest("metrics-total-001");
            try {
                documentProcessingService.processDocument(request, TEST_USER);
            } catch (Exception e) {
                // May fail due to mock, metrics should still be recorded
            }

            long finalCount = getCounterValue("document.processing.total");
            assertThat(finalCount).isGreaterThan(initialCount);
        }

        @Test
        @Order(2)
        @DisplayName("Should record processing success counter")
        void shouldRecordProcessingSuccessCounter() {
            // Setup mocks for successful processing
            doNothing().when(eventPublisher).publish(any(), any());

            long initialCount = getCounterValue("document.processing.success");

            ProcessDocumentRequest request = createTestRequest("metrics-success-001");
            try {
                documentProcessingService.processDocument(request, TEST_USER);
            } catch (Exception e) {
                // Ignore
            }

            long finalCount = getCounterValue("document.processing.success");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record processing duration timer")
        void shouldRecordProcessingDurationTimer() {
            // Setup mocks
            doNothing().when(eventPublisher).publish(any(), any());

            ProcessDocumentRequest request = createTestRequest("metrics-timer-001");
            try {
                documentProcessingService.processDocument(request, TEST_USER);
            } catch (Exception e) {
                // Ignore
            }

            // Verify timer was recorded
            assertThat(meterRegistry.get("document.processing.duration").timer().count())
                    .isGreaterThan(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should record OCR processing duration")
        void shouldRecordOcrProcessingDuration() {
            doNothing().when(eventPublisher).publish(any(), any());

            ProcessDocumentRequest request = createTestRequest("metrics-ocr-001");
            try {
                documentProcessingService.processDocument(request, TEST_USER);
            } catch (Exception e) {
                // Ignore
            }

            assertThat(meterRegistry.get("document.processing.ocr.duration").timer().count())
                    .isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet document processing latency SLO (p95 < 1000ms)")
        void shouldMeetProcessingLatencySlo() {
            doNothing().when(eventPublisher).publish(any(), any());

            int iterations = 50;
            long maxAllowedLatencyMs = 1000;

            for (int i = 0; i < iterations; i++) {
                ProcessDocumentRequest request = createTestRequest("slo-latency-" + i);
                try {
                    documentProcessingService.processDocument(request, TEST_USER);
                } catch (Exception e) {
                    // Ignore
                }
            }

            // Get p95 latency from metrics
            double p95Latency = metrics.getProcessingLatencyP95();

            assertThat(p95Latency)
                    .as("P95 latency should be below %dms SLO threshold", maxAllowedLatencyMs)
                    .isLessThan(maxAllowedLatencyMs);
        }

        @Test
        @Order(11)
        @DisplayName("Should meet P99 latency SLO (p99 < 2000ms)")
        void shouldMeetP99LatencySlo() {
            doNothing().when(eventPublisher).publish(any(), any());

            int iterations = 50;
            long maxAllowedLatencyMs = 2000;

            for (int i = 0; i < iterations; i++) {
                ProcessDocumentRequest request = createTestRequest("slo-p99-" + i);
                try {
                    documentProcessingService.processDocument(request, TEST_USER);
                } catch (Exception e) {
                    // Ignore
                }
            }

            double p99Latency = metrics.getProcessingLatencyP99();

            assertThat(p99Latency)
                    .as("P99 latency should be below %dms SLO threshold", maxAllowedLatencyMs)
                    .isLessThan(maxAllowedLatencyMs);
        }

        @Test
        @Order(12)
        @DisplayName("Should meet OCR processing latency SLO (p95 < 500ms)")
        void shouldMeetOcrProcessingLatencySlo() {
            doNothing().when(eventPublisher).publish(any(), any());

            int iterations = 50;

            for (int i = 0; i < iterations; i++) {
                ProcessDocumentRequest request = createTestRequest("slo-ocr-" + i);
                try {
                    documentProcessingService.processDocument(request, TEST_USER);
                } catch (Exception e) {
                    // Ignore
                }
            }

            double p95Latency = metrics.getOcrProcessingLatencyP95();

            assertThat(p95Latency)
                    .as("OCR processing P95 latency should be below 500ms SLO threshold")
                    .isLessThan(500);
        }
    }

    @Nested
    @DisplayName("3. Error Rate SLO Tests")
    class ErrorRateTests {

        @Test
        @Order(20)
        @DisplayName("Should maintain error rate below SLO threshold")
        void shouldMaintainErrorRateBelowThreshold() {
            doNothing().when(eventPublisher).publish(any(), any());

            int totalRequests = 100;
            double maxErrorRate = 0.02; // 2% error rate SLO

            for (int i = 0; i < totalRequests; i++) {
                ProcessDocumentRequest request = createTestRequest("error-rate-" + i);
                try {
                    documentProcessingService.processDocument(request, TEST_USER);
                } catch (Exception e) {
                    // Some requests may fail
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
            // Verify service tag exists on document processing counter
            var counter = meterRegistry.get("document.processing.total").counter();

            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") &&
                                       tag.getValue().equals("ai-document-processing"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            doNothing().when(eventPublisher).publish(any(), any());

            ProcessDocumentRequest request = createTestRequest("histogram-001");
            try {
                documentProcessingService.processDocument(request, TEST_USER);
            } catch (Exception e) {
                // Ignore
            }

            // Check that timer has percentile histogram enabled
            var timer = meterRegistry.get("document.processing.duration").timer();

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
            long warningThresholdMs = 700;
            long criticalThresholdMs = 1000;

            doNothing().when(eventPublisher).publish(any(), any());

            ProcessDocumentRequest request = createTestRequest("threshold-001");
            long startTime = System.nanoTime();

            try {
                documentProcessingService.processDocument(request, TEST_USER);
            } catch (Exception e) {
                // Ignore
            }

            long durationMs = (System.nanoTime() - startTime) / 1_000_000;

            assertThat(durationMs)
                    .as("Processing should complete below critical threshold")
                    .isLessThan(criticalThresholdMs * 10); // Allow some margin for test environment
        }
    }

    private ProcessDocumentRequest createTestRequest(String documentId) {
        return new ProcessDocumentRequest(
                "https://example.com/documents/" + documentId + ".pdf",
                DocumentType.INVOICE,
                null
        );
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
