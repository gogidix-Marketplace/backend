package com.gogidix.aiservices.aimodeltrainingservice.interfaces.rest;

import com.gogidix.aiservices.aimodeltrainingservice.application.dto.StartTrainingJobRequestDto;
import com.gogidix.aiservices.aimodeltrainingservice.application.dto.TrainingJobResponseDto;
import com.gogidix.aiservices.aimodeltrainingservice.application.service.ModelTrainingApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for model training operations.
 */
@RestController
@RequestMapping("/api/v1/training")
@Tag(name = "Model Training", description = "Model training API")
public class ModelTrainingController {

    private final ModelTrainingApplicationService trainingService;

    public ModelTrainingController(ModelTrainingApplicationService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/jobs")
    @Operation(summary = "Start a training job")
    public ResponseEntity<TrainingJobResponseDto> startTrainingJob(
            @Valid @RequestBody StartTrainingJobRequestDto request
    ) {
        TrainingJobResponseDto response = trainingService.startTrainingJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/jobs/{jobId}/status")
    @Operation(summary = "Get training job status")
    public ResponseEntity<TrainingJobResponseDto> getTrainingStatus(
            @PathVariable String jobId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        TrainingJobResponseDto response = trainingService.getTrainingStatus(jobId, tenantId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/jobs/{jobId}")
    @Operation(summary = "Cancel training job")
    public ResponseEntity<Void> cancelTrainingJob(
            @PathVariable String jobId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        trainingService.cancelTrainingJob(jobId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Model Training Service is running");
    }
}
