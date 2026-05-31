package com.gogidix.aiservices.aiinferenceservice.domain.model;

import com.gogidix.aiservices.aiinferenceservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InferenceResult domain model.
 */
@DisplayName("InferenceResult Domain Model Tests")
class InferenceResultTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String MODEL_ID = "model-001";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create inference result with valid parameters")
        void shouldCreateInferenceResultWithValidParameters() {
            InferenceResult result = new InferenceResult(TENANT_ID, MODEL_ID, Map.of("key", "value"));

            assertNotNull(result.getId());
            assertNotNull(result.getInferenceId());
            assertEquals(TENANT_ID, result.getTenantId());
            assertEquals(MODEL_ID, result.getModelId());
            assertNotNull(result.getExecutedAt());
        }

        @Test
        @DisplayName("Should throw exception when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new InferenceResult(null, MODEL_ID, Map.of()));
        }

        @Test
        @DisplayName("Should throw exception when modelId is null")
        void shouldThrowWhenModelIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new InferenceResult(TENANT_ID, null, Map.of()));
        }

        @Test
        @DisplayName("Should throw exception when inputData is null")
        void shouldThrowWhenInputDataIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new InferenceResult(TENANT_ID, MODEL_ID, null));
        }
    }

    @Nested
    @DisplayName("Prediction Tests")
    class PredictionTests {

        @Test
        @DisplayName("Should set predictions and latency")
        void shouldSetPredictionsAndLatency() {
            InferenceResult result = new InferenceResult(TENANT_ID, MODEL_ID, Map.of());
            List<InferenceResult.Prediction> predictions = List.of(
                    new InferenceResult.Prediction("class_0", 0.85, "value1")
            );

            result.setPredictions(predictions, 150L);

            assertEquals(1, result.getPredictions().size());
            assertEquals(150L, result.getLatencyMs());
        }

        @Test
        @DisplayName("Should throw exception when predictions is null")
        void shouldThrowWhenPredictionsIsNull() {
            InferenceResult result = new InferenceResult(TENANT_ID, MODEL_ID, Map.of());

            assertThrows(NullPointerException.class,
                    () -> result.setPredictions(null, 100L));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate valid inference result")
        void shouldValidateValidInferenceResult() {
            InferenceResult result = new InferenceResult(TENANT_ID, MODEL_ID, Map.of());

            assertDoesNotThrow(result::validate);
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            InferenceResult result = new InferenceResult("  ", MODEL_ID, Map.of());

            assertThrows(ValidationException.class, result::validate);
        }

        @Test
        @DisplayName("Should throw when modelId is blank")
        void shouldThrowWhenModelIdIsBlank() {
            InferenceResult result = new InferenceResult(TENANT_ID, "  ", Map.of());

            assertThrows(ValidationException.class, result::validate);
        }
    }

    @Nested
    @DisplayName("Prediction Record Tests")
    class PredictionRecordTests {

        @Test
        @DisplayName("Should create prediction with all fields")
        void shouldCreatePredictionWithAllFields() {
            InferenceResult.Prediction prediction = new InferenceResult.Prediction("class_0", 0.85, "value");

            assertEquals("class_0", prediction.label());
            assertEquals(0.85, prediction.confidence());
            assertEquals("value", prediction.value());
        }

        @Test
        @DisplayName("Should implement equals and hashCode")
        void shouldImplementEqualsAndHashCode() {
            InferenceResult.Prediction p1 = new InferenceResult.Prediction("class_0", 0.85, "value");
            InferenceResult.Prediction p2 = new InferenceResult.Prediction("class_0", 0.85, "value");

            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }
    }
}
