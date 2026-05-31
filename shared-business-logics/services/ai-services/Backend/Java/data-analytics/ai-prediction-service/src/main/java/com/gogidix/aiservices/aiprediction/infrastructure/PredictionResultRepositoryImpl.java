package com.gogidix.aiservices.aiprediction.infrastructure;

import com.gogidix.aiservices.aiprediction.application.port.out.PredictionRepository;
import com.gogidix.aiservices.aiprediction.domain.PredictionResult;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of PredictionRepository.
 */
@Repository
public class PredictionResultRepositoryImpl implements PredictionRepository {

    private final Map<String, PredictionResult> predictions = new ConcurrentHashMap<>();

    @Override
    public PredictionResult save(PredictionResult result) {
        predictions.put(result.getPredictionId(), result);
        return result;
    }

    @Override
    public Optional<PredictionResult> findById(String predictionId) {
        return Optional.ofNullable(predictions.get(predictionId));
    }

    @Override
    public void deleteById(String predictionId) {
        predictions.remove(predictionId);
    }
}
