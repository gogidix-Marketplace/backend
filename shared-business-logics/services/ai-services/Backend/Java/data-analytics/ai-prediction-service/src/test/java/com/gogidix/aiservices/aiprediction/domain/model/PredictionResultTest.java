package com.gogidix.aiservices.aiprediction.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PredictionResult Domain Model Tests")
class PredictionResultTest {

    private static final String PREDICTION_ID = "pred-123";
    private static final String MODEL_ID = "model-abc";

    @Nested
    @DisplayName("PredictionResult Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create prediction result with valid parameters")
        void shouldCreateWithValidParameters() {
            PredictionResult result = PredictionResult.create(PREDICTION_ID, MODEL_ID);

            assertThat(result).isNotNull();
            assertThat(result.getPredictionId()).isEqualTo(PREDICTION_ID);
            assertThat(result.getModelId()).isEqualTo(MODEL_ID);
            assertThat(result.getConfidence()).isEqualTo(0.0);
            assertThat(result.getGeneratedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should reject null prediction ID")
        void shouldRejectNullPredictionId() {
            assertThatThrownBy(() -> PredictionResult.create(null, MODEL_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Prediction ID cannot be null");
        }

        @Test
        @DisplayName("Should reject null model ID")
        void shouldRejectNullModelId() {
            assertThatThrownBy(() -> PredictionResult.create(PREDICTION_ID, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Model ID cannot be null");
        }
    }

    @Nested
    @DisplayName("Confidence Tests")
    class ConfidenceTests {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.5, 1.0})
        @DisplayName("Should set valid confidence")
        void shouldSetValidConfidence(double confidence) {
            PredictionResult result = PredictionResult.create(PREDICTION_ID, MODEL_ID);

            result.setConfidence(confidence);

            assertThat(result.getConfidence()).isEqualTo(confidence);
        }

        @Test
        @DisplayName("Should reject confidence below minimum")
        void shouldRejectLowConfidence() {
            PredictionResult result = PredictionResult.create(PREDICTION_ID, MODEL_ID);

            assertThatThrownBy(() -> result.setConfidence(-0.1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("between 0 and 1");
        }

        @Test
        @DisplayName("Should reject confidence above maximum")
        void shouldRejectHighConfidence() {
            PredictionResult result = PredictionResult.create(PREDICTION_ID, MODEL_ID);

            assertThatThrownBy(() -> result.setConfidence(1.1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("between 0 and 1");
        }
    }

    @Nested
    @DisplayName("Result Data Tests")
    class ResultDataTests {

        @Test
        @DisplayName("Should set result")
        void shouldSetResult() {
            PredictionResult result = PredictionResult.create(PREDICTION_ID, MODEL_ID);
            Map<String, Object> data = Map.of("class", "positive", "probability", 0.95);

            result.setResult(data);

            assertThat(result.getResult()).isEqualTo(data);
        }

        @Test
        @DisplayName("Should reject null result")
        void shouldRejectNullResult() {
            PredictionResult result = PredictionResult.create(PREDICTION_ID, MODEL_ID);

            assertThatThrownBy(() -> result.setResult(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Result cannot be null");
        }
    }

    @Nested
    @DisplayName("Metadata Tests")
    class MetadataTests {

        @Test
        @DisplayName("Should set model version")
        void shouldSetModelVersion() {
            PredictionResult result = PredictionResult.create(PREDICTION_ID, MODEL_ID);

            result.setModelVersion("v2.0");

            assertThat(result.getModelVersion()).isEqualTo("v2.0");
        }

        @Test
        @DisplayName("Should set processing time")
        void shouldSetProcessingTime() {
            PredictionResult result = PredictionResult.create(PREDICTION_ID, MODEL_ID);

            result.setProcessingTime(150);

            assertThat(result.getProcessingTime()).isEqualTo(150);
        }
    }
}
