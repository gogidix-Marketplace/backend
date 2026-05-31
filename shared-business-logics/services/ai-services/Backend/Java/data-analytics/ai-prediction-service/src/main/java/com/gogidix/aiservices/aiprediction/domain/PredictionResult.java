package com.gogidix.aiservices.aiprediction.domain;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain entity representing a prediction result.
 */
public class PredictionResult {

    private static final double MIN_CONFIDENCE_THRESHOLD = 0.1;
    private static final double HIGH_CONFIDENCE_THRESHOLD = 0.8;
    private static final double LOW_CONFIDENCE_THRESHOLD = 0.5;

    private String predictionId;
    private String modelId;
    private String modelVersion;
    private Map<String, Object> result;
    private Double confidence;
    private LocalDateTime generatedAt;

    public PredictionResult(String predictionId, String modelId, String modelVersion,
                          Map<String, Object> result, Double confidence, LocalDateTime generatedAt) {
        if (predictionId == null || predictionId.trim().isEmpty()) {
            throw new IllegalArgumentException("predictionId cannot be null or empty");
        }
        if (modelId == null || modelId.trim().isEmpty()) {
            throw new IllegalArgumentException("modelId cannot be null or empty");
        }
        if (confidence == null || confidence < MIN_CONFIDENCE_THRESHOLD) {
            throw new IllegalArgumentException(
                "confidence must be at least " + MIN_CONFIDENCE_THRESHOLD + ", got: " + confidence);
        }
        if (confidence > 1.0) {
            throw new IllegalArgumentException("confidence cannot exceed 1.0");
        }

        this.predictionId = predictionId;
        this.modelId = modelId;
        this.modelVersion = modelVersion != null ? modelVersion : "1.0.0";
        this.result = result != null ? result : Map.of();
        this.confidence = confidence;
        this.generatedAt = generatedAt != null ? generatedAt : LocalDateTime.now();
    }

    public String getPredictionId() {
        return predictionId;
    }

    public String getModelId() {
        return modelId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public Double getConfidence() {
        return confidence;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public boolean isHighConfidence() {
        return confidence >= HIGH_CONFIDENCE_THRESHOLD;
    }

    public boolean isLowConfidence() {
        return confidence < LOW_CONFIDENCE_THRESHOLD;
    }

    public boolean isMediumConfidence() {
        return !isHighConfidence() && !isLowConfidence();
    }
}
