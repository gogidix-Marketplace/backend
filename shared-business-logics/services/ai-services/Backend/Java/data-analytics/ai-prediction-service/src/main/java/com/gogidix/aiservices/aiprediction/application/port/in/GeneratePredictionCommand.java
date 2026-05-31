package com.gogidix.aiservices.aiprediction.application.port.in;

import java.util.Map;

/**
 * Command for generating a prediction.
 */
public record GeneratePredictionCommand(
    String modelId,
    String modelVersion,
    Map<String, Object> inputData,
    Map<String, Object> options
) {
    public GeneratePredictionCommand {
        if (modelId == null || modelId.trim().isEmpty()) {
            throw new IllegalArgumentException("modelId cannot be null or empty");
        }
        if (inputData == null || inputData.isEmpty()) {
            throw new IllegalArgumentException("inputData cannot be null or empty");
        }
    }
}
