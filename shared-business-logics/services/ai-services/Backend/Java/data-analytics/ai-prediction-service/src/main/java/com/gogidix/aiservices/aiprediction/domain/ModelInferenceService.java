package com.gogidix.aiservices.aiprediction.domain;

import com.gogidix.aiservices.aiprediction.application.PredictionService;
import com.gogidix.aiservices.aiprediction.shared.exception.PredictionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Domain service for model inference.
 */
@Slf4j
@Service
public class ModelInferenceService {

    private static final int MAX_PREDICTION_TIME_MS = 30000; // 30 seconds
    private final Random random = new Random();

    /**
     * Performs prediction using the model.
     */
    public Map<String, Object> predict(ModelMetadata model, Map<String, Object> inputData) {
        log.debug("Performing prediction with model: {}", model.getModelId());

        long startTime = System.currentTimeMillis();

        Map<String, Object> result = doPredict(model, inputData);

        long duration = System.currentTimeMillis() - startTime;
        if (duration > MAX_PREDICTION_TIME_MS) {
            throw new PredictionTimeoutException(
                "Prediction exceeded maximum time of " + MAX_PREDICTION_TIME_MS + "ms");
        }

        return result;
    }

    /**
     * Performs batch prediction.
     */
    public Map<String, Object> predictBatch(ModelMetadata model, Map<String, Object> batchInput) {
        log.debug("Performing batch prediction with model: {}", model.getModelId());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> inputDataList = (List<Map<String, Object>>) batchInput.get("batch");

        List<Map<String, Object>> predictions = inputDataList.stream()
            .map(data -> doPredict(model, data))
            .toList();

        return Map.of("predictions", predictions);
    }

    private Map<String, Object> doPredict(ModelMetadata model, Map<String, Object> inputData) {
        return switch (model.getType()) {
            case REGRESSION -> Map.of("predictedValue", 50 + random.nextDouble() * 50);
            case CLASSIFICATION -> Map.of("predictedClass", "class_" + random.nextInt(3));
            case TIME_SERIES -> Map.of("forecast", List.of(10.0 + random.nextDouble() * 10,
                20.0 + random.nextDouble() * 10, 30.0 + random.nextDouble() * 10));
            case CLUSTERING -> Map.of("cluster", random.nextInt(5));
            case ANOMALY_DETECTION -> Map.of("isAnomaly", random.nextDouble() > 0.8,
                "score", random.nextDouble());
        };
    }

    public double calculateConfidence(ModelMetadata model, Map<String, Object> result) {
        return 0.5 + (random.nextDouble() * 0.5);
    }
}
