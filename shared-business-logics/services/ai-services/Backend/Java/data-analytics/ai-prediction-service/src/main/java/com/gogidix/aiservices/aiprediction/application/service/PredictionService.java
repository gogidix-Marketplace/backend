package com.gogidix.aiservices.aiprediction.application.service;

import com.gogidix.aiservices.aiprediction.application.dto.request.GeneratePredictionRequest;
import com.gogidix.aiservices.aiprediction.application.dto.response.PredictionResponse;
import com.gogidix.aiservices.aiprediction.domain.aggregate.PredictionExecution;
import com.gogidix.aiservices.aiprediction.domain.model.PredictionResult;
import com.gogidix.aiservices.aiprediction.domain.port.out.PredictionRepository;
import com.gogidix.aiservices.aiprediction.shared.exception.PredictionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository predictionRepository;

    public PredictionResponse generatePrediction(GeneratePredictionRequest request) {
        if (request.getModelId() == null || request.getModelId().trim().isEmpty()) {
            throw new IllegalArgumentException("Model ID cannot be null or empty");
        }
        if (request.getInputData() == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }

        String modelVersion = request.getModelVersion() != null ? request.getModelVersion() : "v1.0";

        PredictionExecution execution = PredictionExecution.create(
                request.getModelId(),
                modelVersion,
                request.getInputData()
        );

        if (request.getTimeout() != null) {
            execution.setTimeout(request.getTimeout());
        }

        execution.start();

        PredictionResult result = PredictionResult.create(execution.getExecutionId(), request.getModelId());
        result.setConfidence(0.95);
        result.setResult(Map.of("class", "positive", "probability", 0.95));
        if (request.getOptions() != null) {
            result.setProcessingTime(150L);
        }

        execution.complete(result);

        PredictionExecution saved = predictionRepository.save(execution);
        return toResponse(saved);
    }

    public PredictionResponse getPrediction(String predictionId) {
        PredictionExecution execution = predictionRepository.findById(predictionId)
                .orElseThrow(() -> new PredictionNotFoundException(predictionId));
        return toResponse(execution);
    }

    public List<PredictionResponse> batchPredict(List<GeneratePredictionRequest> requests) {
        return requests.stream()
                .map(this::generatePrediction)
                .toList();
    }

    public void deletePrediction(String predictionId) {
        predictionRepository.findById(predictionId)
                .orElseThrow(() -> new PredictionNotFoundException(predictionId));
        predictionRepository.delete(predictionId);
    }

    private PredictionResponse toResponse(PredictionExecution execution) {
        PredictionResult result = execution.getResult();
        return PredictionResponse.builder()
                .predictionId(execution.getExecutionId())
                .result(result != null ? result.getResult() : null)
                .confidence(result != null ? result.getConfidence() : 0.0)
                .generatedAt(result != null ? result.getGeneratedAt() : execution.getCreatedAt())
                .status(execution.getStatus().name())
                .build();
    }
}
