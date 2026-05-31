package com.gogidix.aiservices.aimanagementservice.interfaces.rest;

import com.gogidix.aiservices.aimanagementservice.application.dto.*;
import com.gogidix.aiservices.aimanagementservice.domain.port.in.ModelManagementServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for model management operations.
 */
@RestController
@RequestMapping("/api/v1/models")
@Tag(name = "Model Management", description = "Model management API")
public class ModelManagementController {

    private final ModelManagementServicePort modelManagementService;

    public ModelManagementController(ModelManagementServicePort modelManagementService) {
        this.modelManagementService = modelManagementService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new model")
    public ResponseEntity<RegisterModelResponseDto> registerModel(
            @Valid @RequestBody RegisterModelRequestDto request
    ) {
        RegisterModelResponseDto response = modelManagementService.registerModel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{modelId}/deploy")
    @Operation(summary = "Deploy a model")
    public ResponseEntity<Void> deployModel(
            @PathVariable String modelId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        modelManagementService.deployModel(modelId, tenantId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{modelId}/undeploy")
    @Operation(summary = "Undeploy a model")
    public ResponseEntity<Void> undeployModel(
            @PathVariable String modelId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        modelManagementService.undeployModel(modelId, tenantId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{modelId}")
    @Operation(summary = "Get model by ID")
    public ResponseEntity<ModelResponseDto> getModel(
            @PathVariable String modelId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        ModelResponseDto response = modelManagementService.getModel(modelId, tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List models")
    public ResponseEntity<List<ModelResponseDto>> listModels(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<ModelResponseDto> response = modelManagementService.listModels(tenantId, page, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{modelId}")
    @Operation(summary = "Archive a model")
    public ResponseEntity<Void> archiveModel(
            @PathVariable String modelId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        modelManagementService.archiveModel(modelId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Model Management Service is running");
    }
}
