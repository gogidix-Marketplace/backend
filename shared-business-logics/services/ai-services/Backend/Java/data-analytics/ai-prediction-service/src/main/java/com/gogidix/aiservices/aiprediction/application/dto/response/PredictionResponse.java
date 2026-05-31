package com.gogidix.aiservices.aiprediction.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class PredictionResponse {
    private String predictionId;
    private Object result;
    private double confidence;
    private Instant generatedAt;
    private String status;
}
