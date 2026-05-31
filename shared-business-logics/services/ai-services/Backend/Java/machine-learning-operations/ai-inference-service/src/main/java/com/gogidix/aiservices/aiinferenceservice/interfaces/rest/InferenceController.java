package com.gogidix.aiservices.aiinferenceservice.interfaces.rest;

import com.gogidix.aiservices.aiinferenceservice.application.dto.InferenceRequestDto;
import com.gogidix.aiservices.aiinferenceservice.application.dto.InferenceResponseDto;
import com.gogidix.aiservices.aiinferenceservice.application.dto.ModelStatusResponseDto;
import com.gogidix.aiservices.aiinferenceservice.domain.port.in.InferenceServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for inference operations.
 */
@RestController
@RequestMapping("/api/v1/inference")
@Tag(name = "Inference", description = "Model inference API")
public class InferenceController {

    private final InferenceServicePort inferenceService;

    public InferenceController(InferenceServicePort inferenceService) {
        this.inferenceService = inferenceService;
    }

    @PostMapping("/predict")
    @Operation(summary = "Run inference")
    public ResponseEntity<InferenceResponseDto> runInference(
            @Valid @RequestBody InferenceRequestDto request
    ) {
        InferenceResponseDto response = inferenceService.runInference(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/models/{modelId}/status")
    @Operation(summary = "Get model status")
    public ResponseEntity<ModelStatusResponseDto> getModelStatus(
            @PathVariable String modelId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        ModelStatusResponseDto response = inferenceService.getModelStatus(modelId, tenantId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/models/{modelId}")
    @Operation(summary = "Unload model")
    public ResponseEntity<Void> unloadModel(
            @PathVariable String modelId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        inferenceService.unloadModel(modelId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Inference Service is running");
    }
}
