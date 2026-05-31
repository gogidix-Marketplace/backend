package com.gogidix.aiservices.aiprediction.interfaces;

import com.gogidix.aiservices.aiprediction.application.*;
import com.gogidix.aiservices.aiprediction.application.port.in.GeneratePredictionCommand;
import com.gogidix.aiservices.aiprediction.application.port.in.GeneratePredictionUseCase;
import com.gogidix.aiservices.aiprediction.domain.PredictionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

/**
 * REST controller for prediction operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/predictions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PredictionController {

    private final GeneratePredictionUseCase predictionUseCase;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generatePrediction(
            @Valid @RequestBody GeneratePredictionRequest request) {
        log.info("Received prediction request for model: {}", request.modelId());

        GeneratePredictionCommand command = new GeneratePredictionCommand(
            request.modelId(),
            request.modelVersion(),
            request.inputData(),
            request.options()
        );

        PredictionResult result = predictionUseCase.generatePrediction(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseJson(result));
    }

    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> generateBatchPrediction(
            @Valid @RequestBody GeneratePredictionRequest request) {
        log.info("Received batch prediction request for model: {}", request.modelId());

        GeneratePredictionCommand command = new GeneratePredictionCommand(
            request.modelId(),
            request.modelVersion(),
            request.inputData(),
            request.options()
        );

        PredictionResult result = predictionUseCase.generateBatchPrediction(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseJson(result));
    }

    @GetMapping("/{predictionId}")
    public ResponseEntity<Map<String, Object>> getPrediction(@PathVariable String predictionId) {
        log.info("Fetching prediction for ID: {}", predictionId);

        return predictionUseCase.getPrediction(predictionId)
            .map(result -> ResponseEntity.ok(toResponseJson(result)))
            .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(com.gogidix.aiservices.aiprediction.application.ModelNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleModelNotFound(com.gogidix.aiservices.aiprediction.application.ModelNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(com.gogidix.aiservices.aiprediction.domain.PredictionTimeoutException.class)
    public ResponseEntity<Map<String, String>> handlePredictionTimeout(
            com.gogidix.aiservices.aiprediction.domain.PredictionTimeoutException ex) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
            .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
            .body(Map.of("message", ex.getMessage()));
    }

    private Map<String, Object> toResponseJson(PredictionResult result) {
        return Map.of(
            "predictionId", result.getPredictionId(),
            "modelId", result.getModelId(),
            "modelVersion", result.getModelVersion(),
            "result", result.getResult(),
            "confidence", result.getConfidence(),
            "generatedAt", result.getGeneratedAt().toString()
        );
    }

    public record GeneratePredictionRequest(
        String modelId,
        String modelVersion,
        Map<String, Object> inputData,
        Map<String, Object> options
    ) {}
}
