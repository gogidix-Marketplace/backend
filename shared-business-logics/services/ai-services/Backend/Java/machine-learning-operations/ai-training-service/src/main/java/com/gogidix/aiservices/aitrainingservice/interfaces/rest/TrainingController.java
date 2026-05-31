package com.gogidix.aiservices.aitrainingservice.interfaces.rest;

import com.gogidix.aiservices.aitrainingservice.application.dto.FineTuneRequestDto;
import com.gogidix.aiservices.aitrainingservice.application.dto.FineTuningJobResponseDto;
import com.gogidix.aiservices.aitrainingservice.application.service.TrainingApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for training operations.
 */
@RestController
@RequestMapping("/api/v1/training")
@Tag(name = "Training", description = "Model training API")
public class TrainingController {

    private final TrainingApplicationService trainingService;

    public TrainingController(TrainingApplicationService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/fine-tune")
    @Operation(summary = "Fine-tune a model")
    public ResponseEntity<FineTuningJobResponseDto> fineTuneModel(
            @Valid @RequestBody FineTuneRequestDto request
    ) {
        FineTuningJobResponseDto response = trainingService.fineTuneModel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/fine-tune/{jobId}")
    @Operation(summary = "Get fine-tuning job status")
    public ResponseEntity<FineTuningJobResponseDto> getFineTuningStatus(
            @PathVariable String jobId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        FineTuningJobResponseDto response = trainingService.getFineTuningStatus(jobId, tenantId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/schedule")
    @Operation(summary = "Schedule model retraining")
    public ResponseEntity<Void> scheduleRetraining(
            @RequestParam String baseModel,
            @RequestParam String cronExpression,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        trainingService.scheduleRetraining(baseModel, cronExpression, tenantId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/fine-tune/{jobId}")
    @Operation(summary = "Cancel fine-tuning job")
    public ResponseEntity<Void> cancelFineTuning(
            @PathVariable String jobId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        trainingService.cancelFineTuning(jobId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Training Service is running");
    }
}
