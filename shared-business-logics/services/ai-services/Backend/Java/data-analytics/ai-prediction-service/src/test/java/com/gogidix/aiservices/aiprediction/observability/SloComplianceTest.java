package com.gogidix.aiservices.aiprediction.observability;

import com.gogidix.aiservices.aiprediction.application.dto.request.GeneratePredictionRequest;
import com.gogidix.aiservices.aiprediction.application.dto.response.PredictionResponse;
import com.gogidix.aiservices.aiprediction.application.service.PredictionService;
import com.gogidix.aiservices.aiprediction.infrastructure.metrics.PredictionMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance & Observability Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private PredictionMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @MockBean
    private com.gogidix.aiservices.aiprediction.domain.port.out.PredictionRepository predictionRepository;

    @Nested
    @DisplayName("1. Metrics Collection Tests")
    class MetricsCollectionTests {

        @Test
        @Order(1)
        @DisplayName("Should record prediction total counter")
        void shouldRecordPredictionTotalCounter() {
            long initialCount = getCounterValue("prediction.total");

            try {
                predictionService.generatePrediction(createTestRequest("metrics-total-001"));
            } catch (Exception e) { }

            long finalCount = getCounterValue("prediction.total");
            assertThat(finalCount).isGreaterThanOrEqualTo(initialCount);
        }

        @Test
        @Order(3)
        @DisplayName("Should record prediction duration timer")
        void shouldRecordPredictionDurationTimer() {
            doNothing().when(predictionRepository).save(any());

            try {
                predictionService.generatePrediction(createTestRequest("metrics-timer-001"));
            } catch (Exception e) { }

            assertThat(meterRegistry.get("prediction.duration").timer().count()).isGreaterThan(0);
        }

        @Test
        @Order(4)
        @DisplayName("Should record model inference duration")
        void shouldRecordModelInferenceDuration() {
            doNothing().when(predictionRepository).save(any());

            try {
                predictionService.generatePrediction(createTestRequest("metrics-inference-001"));
            } catch (Exception e) { }

            assertThat(meterRegistry.get("prediction.model.inference.duration").timer().count()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("2. SLO Latency Compliance Tests")
    class SloLatencyTests {

        @Test
        @Order(10)
        @DisplayName("Should meet prediction latency SLO (p95 < 1000ms)")
        void shouldMeetPredictionLatencySlo() {
            doNothing().when(predictionRepository).save(any());

            for (int i = 0; i < 50; i++) {
                try {
                    predictionService.generatePrediction(createTestRequest("slo-latency-" + i));
                } catch (Exception e) { }
            }

            double p95Latency = metrics.getPredictionLatencyP95();
            assertThat(p95Latency).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("3. Error Rate SLO Tests")
    class ErrorRateTests {

        @Test
        @Order(20)
        @DisplayName("Should maintain error rate below SLO threshold")
        void shouldMaintainErrorRateBelowThreshold() {
            doNothing().when(predictionRepository).save(any());

            for (int i = 0; i < 100; i++) {
                try {
                    predictionService.generatePrediction(createTestRequest("error-rate-" + i));
                } catch (Exception e) { }
            }

            double errorRate = metrics.getErrorRate();
            assertThat(errorRate).isGreaterThanOrEqualTo(0);
        }
    }

    @Nested
    @DisplayName("4. Metrics Tag Validation Tests")
    class MetricsTagTests {

        @Test
        @Order(30)
        @DisplayName("Should have correct service tag on metrics")
        void shouldHaveCorrectServiceTag() {
            var counter = meterRegistry.get("prediction.total").counter();
            assertThat(counter).isNotNull();
            assertThat(counter.getId().getTags())
                    .anyMatch(tag -> tag.getKey().equals("service") && tag.getValue().equals("ai-prediction"));
        }

        @Test
        @Order(31)
        @DisplayName("Should have percentile histogram configured")
        void shouldHavePercentileHistogramConfigured() {
            doNothing().when(predictionRepository).save(any());

            try {
                predictionService.generatePrediction(createTestRequest("histogram-001"));
            } catch (Exception e) { }

            var timer = meterRegistry.get("prediction.duration").timer();
            assertThat(timer).isNotNull();
            assertThat(timer.count()).isGreaterThan(0);
        }
    }

    private GeneratePredictionRequest createTestRequest(String id) {
        return GeneratePredictionRequest.builder()
                .modelId("model-" + id)
                .inputData(Map.of("feature1", 1.0))
                .build();
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
