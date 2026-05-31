package com.gogidix.aiservices.aiprediction.application;

import com.gogidix.aiservices.aiprediction.domain.*;
import com.gogidix.aiservices.aiprediction.application.port.in.GeneratePredictionCommand;
import com.gogidix.aiservices.aiprediction.application.port.in.GeneratePredictionUseCase;
import com.gogidix.aiservices.aiprediction.application.port.out.ModelRepository;
import com.gogidix.aiservices.aiprediction.application.port.out.PredictionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for prediction operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService implements GeneratePredictionUseCase {

    private final ModelRepository modelRepository;
    private final PredictionRepository predictionRepository;
    private final PredictionCacheService cacheService;
    private final ModelInferenceService inferenceService;

    @Override
    public PredictionResult generatePrediction(GeneratePredictionCommand command) {
        log.info("Generating prediction for model: {}", command.modelId());

        // Check cache first
        String cacheKey = cacheService.generateCacheKey(command.modelId(), command.inputData());
        Optional<PredictionResult> cached = cacheService.get(cacheKey);
        if (cached.isPresent()) {
            log.info("Returning cached prediction for: {}", cacheKey);
            return cached.get();
        }

        // Load model
        ModelMetadata model = modelRepository.findById(command.modelId())
            .orElseThrow(() -> new ModelNotFoundException("Model not found: " + command.modelId()));

        // Perform prediction
        Map<String, Object> result = inferenceService.predict(model, command.inputData());
        double confidence = inferenceService.calculateConfidence(model, result);

        // Create prediction result
        PredictionResult predictionResult = new PredictionResult(
            UUID.randomUUID().toString(),
            model.getModelId(),
            model.getVersion(),
            result,
            confidence,
            LocalDateTime.now()
        );

        // Save and cache
        predictionRepository.save(predictionResult);
        cacheService.put(cacheKey, predictionResult);

        log.info("Prediction generated: {}", predictionResult.getPredictionId());
        return predictionResult;
    }

    @Override
    public PredictionResult generateBatchPrediction(GeneratePredictionCommand command) {
        log.info("Generating batch prediction for model: {}", command.modelId());

        ModelMetadata model = modelRepository.findById(command.modelId())
            .orElseThrow(() -> new ModelNotFoundException("Model not found: " + command.modelId()));

        Map<String, Object> result = inferenceService.predictBatch(model, command.inputData());
        double confidence = inferenceService.calculateConfidence(model, result);

        PredictionResult predictionResult = new PredictionResult(
            UUID.randomUUID().toString(),
            model.getModelId(),
            model.getVersion(),
            result,
            confidence,
            LocalDateTime.now()
        );

        predictionRepository.save(predictionResult);

        return predictionResult;
    }

    @Override
    public Optional<PredictionResult> getPrediction(String predictionId) {
        return predictionRepository.findById(predictionId);
    }
}
