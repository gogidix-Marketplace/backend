package com.gogidix.aiservices.aiprediction.interfaces.rest;

import com.gogidix.aiservices.aiprediction.application.dto.request.GeneratePredictionRequest;
import com.gogidix.aiservices.aiprediction.application.dto.response.PredictionResponse;
import com.gogidix.aiservices.aiprediction.application.service.PredictionService;
import com.gogidix.aiservices.aiprediction.shared.exception.PredictionException;
import com.gogidix.aiservices.aiprediction.shared.exception.PredictionNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/predictions")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @PostMapping("/generate")
    public ResponseEntity<PredictionResponse> generatePrediction(
            @Valid @RequestBody GeneratePredictionRequest request) {
        PredictionResponse response = predictionService.generatePrediction(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PredictionResponse> getPrediction(@PathVariable String id) {
        PredictionResponse response = predictionService.getPrediction(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<PredictionResponse>> batchPredict(
            @RequestBody List<GeneratePredictionRequest> requests) {
        List<PredictionResponse> responses = predictionService.batchPredict(requests);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrediction(@PathVariable String id) {
        predictionService.deletePrediction(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(PredictionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(PredictionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(PredictionException.class)
    public ResponseEntity<Map<String, Object>> handlePredictionException(PredictionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("message", message);
        error.put("timestamp", Instant.now());
        return error;
    }
}
