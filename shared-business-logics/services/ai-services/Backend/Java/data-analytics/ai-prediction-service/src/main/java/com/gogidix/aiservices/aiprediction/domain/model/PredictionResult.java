package com.gogidix.aiservices.aiprediction.domain.model;

import java.time.Instant;
import java.util.Map;

public class PredictionResult {
    private final String predictionId;
    private final String modelId;
    private double confidence;
    private Map<String, Object> result;
    private String modelVersion;
    private Long processingTime;
    private final Instant generatedAt;

    private PredictionResult(String predictionId, String modelId) {
        if (predictionId == null) {
            throw new IllegalArgumentException("Prediction ID cannot be null");
        }
        if (modelId == null) {
            throw new IllegalArgumentException("Model ID cannot be null");
        }
        this.predictionId = predictionId;
        this.modelId = modelId;
        this.confidence = 0.0;
        this.generatedAt = Instant.now();
    }

    public static PredictionResult create(String predictionId, String modelId) {
        return new PredictionResult(predictionId, modelId);
    }

    public void setConfidence(double confidence) {
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("Confidence must be between 0 and 1");
        }
        this.confidence = confidence;
    }

    public void setResult(Map<String, Object> result) {
        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null");
        }
        this.result = result;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }

    public String getPredictionId() { return predictionId; }
    public String getModelId() { return modelId; }
    public double getConfidence() { return confidence; }
    public Map<String, Object> getResult() { return result; }
    public String getModelVersion() { return modelVersion; }
    public Long getProcessingTime() { return processingTime; }
    public Instant getGeneratedAt() { return generatedAt; }
}
