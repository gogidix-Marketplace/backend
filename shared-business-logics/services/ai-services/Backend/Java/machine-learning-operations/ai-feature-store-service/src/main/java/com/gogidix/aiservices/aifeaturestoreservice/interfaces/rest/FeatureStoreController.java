package com.gogidix.aiservices.aifeaturestoreservice.interfaces.rest;

import com.gogidix.aiservices.aifeaturestoreservice.application.dto.*;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.in.FeatureStoreServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for feature store operations.
 */
@RestController
@RequestMapping("/api/v1/feature-store")
@Tag(name = "Feature Store", description = "Feature store API")
public class FeatureStoreController {

    private final FeatureStoreServicePort featureStoreService;

    public FeatureStoreController(FeatureStoreServicePort featureStoreService) {
        this.featureStoreService = featureStoreService;
    }

    @PostMapping("/features")
    @Operation(summary = "Store features")
    public ResponseEntity<StoreFeaturesResponseDto> storeFeatures(
            @Valid @RequestBody StoreFeaturesRequestDto request
    ) {
        StoreFeaturesResponseDto response = featureStoreService.storeFeatures(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/features/{featureName}/entities/{entityId}")
    @Operation(summary = "Get feature value for entity")
    public ResponseEntity<List<EntityFeatureDto>> getFeatures(
            @PathVariable String featureName,
            @PathVariable String entityId,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        List<EntityFeatureDto> response = featureStoreService.getFeatures(featureName, entityId, tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/features/{featureName}/definition")
    @Operation(summary = "Get feature definition")
    public ResponseEntity<FeatureDefinitionResponseDto> getFeatureDefinition(
            @PathVariable String featureName,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        FeatureDefinitionResponseDto response = featureStoreService.getFeatureDefinition(featureName, tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/features")
    @Operation(summary = "List feature definitions")
    public ResponseEntity<List<FeatureDefinitionResponseDto>> listFeatureDefinitions(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<FeatureDefinitionResponseDto> response = featureStoreService.listFeatureDefinitions(tenantId, page, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/features/{featureName}")
    @Operation(summary = "Delete feature definition")
    public ResponseEntity<Void> deleteFeatureDefinition(
            @PathVariable String featureName,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        featureStoreService.deleteFeatureDefinition(featureName, tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Feature Store Service is running");
    }
}
