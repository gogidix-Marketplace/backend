package com.gogidix.aiservices.aifeatureextractionservice.interfaces.rest;

import com.gogidix.aiservices.aifeatureextractionservice.application.dto.ExtractFeaturesRequestDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSchemaResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSetResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.domain.port.in.FeatureExtractionServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for feature extraction operations.
 */
@RestController
@RequestMapping("/api/v1/features")
@Tag(name = "Feature Extraction", description = "Feature extraction API")
public class FeatureExtractionController {

    private final FeatureExtractionServicePort featureExtractionService;

    public FeatureExtractionController(FeatureExtractionServicePort featureExtractionService) {
        this.featureExtractionService = featureExtractionService;
    }

    @PostMapping("/extract")
    @Operation(summary = "Extract features from data source")
    public ResponseEntity<FeatureSetResponseDto> extractFeatures(
            @Valid @RequestBody ExtractFeaturesRequestDto request
    ) {
        FeatureSetResponseDto response = featureExtractionService.extractFeatures(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{featureSetId}")
    @Operation(summary = "Get feature set by ID")
    public ResponseEntity<FeatureSetResponseDto> getFeatureSet(
            @Parameter(description = "Feature set ID") @PathVariable String featureSetId,
            @Parameter(description = "Tenant ID") @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        FeatureSetResponseDto response = featureExtractionService.getFeatureSet(featureSetId, tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{featureSetId}/schema")
    @Operation(summary = "Get feature schema")
    public ResponseEntity<FeatureSchemaResponseDto> getFeatureSchema(
            @Parameter(description = "Feature set ID") @PathVariable String featureSetId,
            @Parameter(description = "Tenant ID") @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        FeatureSchemaResponseDto response = featureExtractionService.getFeatureSchema(featureSetId, tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List feature sets")
    public ResponseEntity<List<FeatureSetResponseDto>> listFeatureSets(
            @Parameter(description = "Tenant ID") @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "Data source filter") @RequestParam(required = false) String dataSource,
            @Parameter(description = "Status filter") @RequestParam(required = false) String status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size
    ) {
        List<FeatureSetResponseDto> response = featureExtractionService.listFeatureSets(
                tenantId, dataSource, status, page, size
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{featureSetId}")
    @Operation(summary = "Delete feature set")
    public ResponseEntity<Void> deleteFeatureSet(
            @Parameter(description = "Feature set ID") @PathVariable String featureSetId,
            @Parameter(description = "Tenant ID") @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        featureExtractionService.deleteFeatureSet(featureSetId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Feature Extraction Service is running");
    }
}
