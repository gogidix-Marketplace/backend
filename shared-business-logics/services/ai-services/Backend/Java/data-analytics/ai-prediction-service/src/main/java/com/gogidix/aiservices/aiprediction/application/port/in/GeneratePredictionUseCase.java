package com.gogidix.aiservices.aiprediction.application.port.in;

import com.gogidix.aiservices.aiprediction.domain.PredictionResult;

import java.util.Optional;

/**
 * Use case interface for generating predictions.
 */
public interface GeneratePredictionUseCase {

    /**
     * Generates a prediction using the specified model.
     */
    PredictionResult generatePrediction(GeneratePredictionCommand command);

    /**
     * Generates a batch prediction.
     */
    PredictionResult generateBatchPrediction(GeneratePredictionCommand command);

    /**
     * Retrieves a prediction by ID.
     */
    Optional<PredictionResult> getPrediction(String predictionId);
}
