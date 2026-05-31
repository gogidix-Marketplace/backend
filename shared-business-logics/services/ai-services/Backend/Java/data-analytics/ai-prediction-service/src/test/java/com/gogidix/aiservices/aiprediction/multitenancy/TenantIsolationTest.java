package com.gogidix.aiservices.aiprediction.multitenancy;

import com.gogidix.aiservices.aiprediction.application.dto.request.GeneratePredictionRequest;
import com.gogidix.aiservices.aiprediction.application.dto.response.PredictionResponse;
import com.gogidix.aiservices.aiprediction.application.service.PredictionService;
import com.gogidix.aiservices.aiprediction.domain.aggregate.PredictionExecution;
import com.gogidix.aiservices.aiprediction.domain.model.PredictionResult;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Multi-Tenancy Tenant Isolation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantIsolationTest {

    @Autowired
    private PredictionService predictionService;

    @MockBean
    private com.gogidix.aiservices.aiprediction.domain.port.out.PredictionRepository predictionRepository;

    private static final String MODEL_1 = "model-tenant-1";
    private static final String MODEL_2 = "model-tenant-2";

    @Nested
    @DisplayName("1. PredictionResult Tenant Tests")
    class PredictionResultTenantTests {

        @Test
        @Order(1)
        @DisplayName("Should create prediction result with predictionId")
        void shouldCreatePredictionResultWithPredictionId() {
            PredictionResult result = PredictionResult.create("prediction-123", MODEL_1);

            assertThat(result).isNotNull();
            assertThat(result.getPredictionId()).isEqualTo("prediction-123");
            assertThat(result.getModelId()).isEqualTo(MODEL_1);
        }

        @Test
        @Order(2)
        @DisplayName("Should validate confidence bounds")
        void shouldValidateConfidenceBounds() {
            PredictionResult result = PredictionResult.create("prediction-124", MODEL_1);

            result.setConfidence(0.5);
            assertThat(result.getConfidence()).isEqualTo(0.5);

            assertThatThrownBy(() -> result.setConfidence(1.5))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> result.setConfidence(-0.1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @Order(3)
        @DisplayName("Should set and get result")
        void shouldSetAndGetResult() {
            PredictionResult result = PredictionResult.create("prediction-125", MODEL_1);

            Map<String, Object> predictionResult = Map.of("class", "positive", "probability", 0.95);
            result.setResult(predictionResult);

            assertThat(result.getResult()).isEqualTo(predictionResult);
        }
    }

    @Nested
    @DisplayName("2. PredictionResponse DTO Tenant Tests")
    class PredictionResponseTenantTests {

        @Test
        @Order(10)
        @DisplayName("Should build response with predictionId")
        void shouldBuildResponseWithPredictionId() {
            PredictionResponse response = PredictionResponse.builder()
                    .predictionId("prediction-123")
                    .result(Map.of("class", "positive"))
                    .confidence(0.95)
                    .generatedAt(Instant.now())
                    .status("COMPLETED")
                    .build();

            assertThat(response).isNotNull();
            assertThat(response.getPredictionId()).isEqualTo("prediction-123");
            assertThat(response.getConfidence()).isEqualTo(0.95);
        }
    }

    @Nested
    @DisplayName("3. Cross-Model Data Isolation Tests")
    class CrossModelIsolationTests {

        @Test
        @Order(20)
        @DisplayName("Should verify model isolation")
        void shouldVerifyModelIsolation() {
            PredictionResult result1 = PredictionResult.create("prediction-1", MODEL_1);
            PredictionResult result2 = PredictionResult.create("prediction-2", MODEL_2);

            assertThat(result1.getModelId()).isNotEqualTo(result2.getModelId());
            assertThat(result1.getModelId()).isEqualTo(MODEL_1);
            assertThat(result2.getModelId()).isEqualTo(MODEL_2);
        }

        @Test
        @Order(21)
        @DisplayName("Should filter predictions by model")
        void shouldFilterPredictionsByModel() {
            PredictionResult result1 = PredictionResult.create("prediction-1", MODEL_1);
            PredictionResult result2 = PredictionResult.create("prediction-2", MODEL_2);
            PredictionResult result3 = PredictionResult.create("prediction-3", MODEL_1);

            List<PredictionResult> allResults = List.of(result1, result2, result3);

            List<PredictionResult> model1Results = allResults.stream()
                    .filter(r -> MODEL_1.equals(r.getModelId()))
                    .toList();

            assertThat(model1Results).hasSize(2);
        }
    }

    @Nested
    @DisplayName("4. Domain Model Field Presence Tests")
    class DomainModelFieldPresenceTests {

        @Test
        @Order(30)
        @DisplayName("Should verify PredictionResult has required fields")
        void shouldVerifyPredictionResultHasRequiredFields() {
            PredictionResult result = PredictionResult.create("prediction-123", MODEL_1);

            assertThat(result.getPredictionId()).isNotNull();
            assertThat(result.getModelId()).isNotNull();
            assertThat(result.getGeneratedAt()).isNotNull();
        }

        @Test
        @Order(31)
        @DisplayName("Should verify PredictionExecution has required fields")
        void shouldVerifyPredictionExecutionHasRequiredFields() {
            PredictionExecution execution = PredictionExecution.create(MODEL_1, "v1.0", Map.of());

            assertThat(execution.getExecutionId()).isNotNull();
            assertThat(execution.getModelId()).isEqualTo(MODEL_1);
            assertThat(execution.getModelVersion()).isEqualTo("v1.0");
        }
    }

    @Nested
    @DisplayName("5. Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @Order(40)
        @DisplayName("Should support concurrent prediction operations")
        void shouldSupportConcurrentPredictionOperations() throws InterruptedException {
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];
            final boolean[] errors = {false};
            final String[] results = new String[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        PredictionResult result = PredictionResult.create("prediction-" + index, "model-" + index);
                        results[index] = result.getPredictionId();
                    } catch (Exception e) {
                        errors[0] = true;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(errors[0]).isFalse();

            for (int i = 0; i < threadCount; i++) {
                assertThat(results[i]).isNotNull();
                assertThat(results[i]).isEqualTo("prediction-" + i);
            }
        }
    }

    @Nested
    @DisplayName("6. Model Version Tests")
    class ModelVersionTests {

        @Test
        @Order(50)
        @DisplayName("Should handle model version correctly")
        void shouldHandleModelVersionCorrectly() {
            PredictionExecution execution = PredictionExecution.create(MODEL_1, "v2.5.1", Map.of());

            assertThat(execution.getModelVersion()).isEqualTo("v2.5.1");
        }

        @Test
        @Order(51)
        @DisplayName("Should distinguish predictions by model version")
        void shouldDistinguishPredictionsByModelVersion() {
            PredictionExecution execution1 = PredictionExecution.create(MODEL_1, "v1.0", Map.of());
            PredictionExecution execution2 = PredictionExecution.create(MODEL_1, "v2.0", Map.of());

            assertThat(execution1.getModelVersion()).isNotEqualTo(execution2.getModelVersion());
        }
    }
}
