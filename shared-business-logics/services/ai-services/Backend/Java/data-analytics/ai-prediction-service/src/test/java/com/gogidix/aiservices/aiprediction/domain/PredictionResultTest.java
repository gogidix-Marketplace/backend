package com.gogidix.aiservices.aiprediction.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Map;

@DisplayName("PredictionResult Domain Entity Tests")
class PredictionResultTest {

    @Test
    @DisplayName("Should create prediction result with valid parameters")
    void shouldCreatePredictionResult() {
        // Given
        String predictionId = "pred-123";
        String modelId = "model-001";
        String modelVersion = "1.0.0";
        Map<String, Object> result = Map.of("predictedValue", 42.5);
        double confidence = 0.95;

        // When
        PredictionResult prediction = new PredictionResult(
            predictionId,
            modelId,
            modelVersion,
            result,
            confidence,
            LocalDateTime.now()
        );

        // Then
        assertThat(prediction.getPredictionId()).isEqualTo(predictionId);
        assertThat(prediction.getModelId()).isEqualTo(modelId);
        assertThat(prediction.getConfidence()).isEqualTo(0.95);
    }

    @Test
    @DisplayName("Should throw exception when confidence is below threshold")
    void shouldThrowExceptionWhenConfidenceBelowThreshold() {
        // Then
        assertThatThrownBy(() -> new PredictionResult(
            "pred-low",
            "model-001",
            "1.0.0",
            Map.of("value", 10),
            0.05, // Below minimum threshold of 0.1
            LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("confidence");
    }

    @Test
    @DisplayName("Should validate confidence range")
    void shouldValidateConfidenceRange() {
        // Given
        PredictionResult valid = new PredictionResult(
            "pred-1", "m1", "v1", Map.of(), 0.5, LocalDateTime.now()
        );

        // Then
        assertThat(valid.getConfidence()).isBetween(0.0, 1.0);
    }

    @Test
    @DisplayName("Should mark prediction as high confidence when above 0.8")
    void shouldMarkHighConfidence() {
        // Given
        PredictionResult prediction = new PredictionResult(
            "pred-high", "m1", "v1", Map.of(), 0.85, LocalDateTime.now()
        );

        // Then
        assertThat(prediction.isHighConfidence()).isTrue();
    }

    @Test
    @DisplayName("Should mark prediction as low confidence when below 0.5")
    void shouldMarkLowConfidence() {
        // Given
        PredictionResult prediction = new PredictionResult(
            "pred-low", "m1", "v1", Map.of(), 0.3, LocalDateTime.now()
        );

        // Then
        assertThat(prediction.isLowConfidence()).isTrue();
    }

    @Test
    @DisplayName("Should throw exception when model ID is null")
    void shouldThrowExceptionWhenModelIdIsNull() {
        // Then
        assertThatThrownBy(() -> new PredictionResult(
            "pred-1", null, "v1", Map.of(), 0.5, LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
