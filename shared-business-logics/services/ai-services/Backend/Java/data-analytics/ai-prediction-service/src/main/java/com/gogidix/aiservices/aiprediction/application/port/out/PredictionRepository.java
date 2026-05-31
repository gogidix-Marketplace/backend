package com.gogidix.aiservices.aiprediction.application.port.out;

import com.gogidix.aiservices.aiprediction.domain.PredictionResult;

import java.util.Optional;

/**
 * Repository port for prediction results.
 */
public interface PredictionRepository {

    /**
     * Saves a prediction result.
     */
    PredictionResult save(PredictionResult result);

    /**
     * Finds a prediction by ID.
     */
    Optional<PredictionResult> findById(String predictionId);

    /**
     * Deletes a prediction by ID.
     */
    void deleteById(String predictionId);
}
