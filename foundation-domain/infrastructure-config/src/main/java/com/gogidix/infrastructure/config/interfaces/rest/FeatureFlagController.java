package com.gogidix.infrastructure.config.interfaces.rest;

import com.gogidix.infrastructure.config.application.service.FeatureFlagService;
import com.gogidix.infrastructure.config.domain.model.ConfigVersion;
import com.gogidix.infrastructure.config.domain.model.FeatureFlag;
import com.gogidix.infrastructure.config.interfaces.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * REST controller for feature flag management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/feature-flags")
@RequiredArgsConstructor
@Tag(name = "Feature Flags", description = "APIs for feature flag management")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_USER_HEADER = "X-User-ID";

    /**
     * Creates a new feature flag.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Create feature flag", description = "Creates a new feature flag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feature flag created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Feature flag already exists")
    })
    public ResponseEntity<FeatureFlag> createFeatureFlag(
            @Valid @RequestBody CreateFeatureFlagRequest request,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("POST /api/v1/feature-flags - Creating flag: key={}", request.flagKey());

        FeatureFlag response = featureFlagService.createFeatureFlag(request, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Updates an existing feature flag.
     */
    @PutMapping(value = "/{tenantId}/{flagKey}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Update feature flag", description = "Updates an existing feature flag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feature flag updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Feature flag not found")
    })
    public ResponseEntity<FeatureFlag> updateFeatureFlag(
            @PathVariable String id,
            @PathVariable String tenantId,
            @PathVariable String flagKey,
            @Valid @RequestBody UpdateFeatureFlagRequest request,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("PUT /api/v1/feature-flags/{}/{} - Updating flag", tenantId, flagKey);

        FeatureFlag response = featureFlagService.updateFeatureFlag(
                id, request, tenantId, flagKey, userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Evaluates a feature flag for a user.
     */
    @PostMapping(value = "/evaluate", produces = "application/json")
    @Operation(summary = "Evaluate feature flag", description = "Evaluates if a feature flag is enabled for a user")
    public ResponseEntity<FeatureFlagEvaluation> evaluateFlag(
            @RequestBody Map<String, Object> request,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        String flagKey = (String) request.get("flagKey");
        String userId = (String) request.get("userId");

        @SuppressWarnings("unchecked")
        Map<String, Object> context = request.get("context") != null
                ? (Map<String, Object>) request.get("context")
                : Map.of();

        log.debug("POST /api/v1/feature-flags/evaluate - flag={}, user={}", flagKey, userId);

        FeatureFlagEvaluation response = featureFlagService.evaluateFlag(
                tenantId, flagKey, userId, context);

        return ResponseEntity.ok(response);
    }

    /**
     * Evaluates multiple feature flags at once.
     */
    @PostMapping(value = "/evaluate/batch", produces = "application/json")
    @Operation(summary = "Evaluate multiple flags", description = "Evaluates multiple feature flags at once")
    public ResponseEntity<Map<String, Boolean>> evaluateFlags(
            @RequestBody Map<String, Object> request,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        @SuppressWarnings("unchecked")
        Set<String> flagKeys = (Set<String>) request.get("flagKeys");
        String userId = (String) request.get("userId");

        @SuppressWarnings("unchecked")
        Map<String, Object> context = request.get("context") != null
                ? (Map<String, Object>) request.get("context")
                : Map.of();

        log.debug("POST /api/v1/feature-flags/evaluate/batch - {} flags", flagKeys.size());

        Map<String, Boolean> response = featureFlagService.evaluateFlags(
                tenantId, flagKeys, userId, context);

        return ResponseEntity.ok(response);
    }

    /**
     * Gets all feature flags for a tenant.
     */
    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all feature flags", description = "Retrieves all feature flags for a tenant")
    public ResponseEntity<List<FeatureFlag>> getFeatureFlags(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

            @Parameter(description = "Active only") @RequestParam(required = false, defaultValue = "false") boolean activeOnly,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "50") int size) {

        log.debug("GET /api/v1/feature-flags - tenantId={}", tenantId);

        if (activeOnly) {
            List<FeatureFlag> response = featureFlagService.getActiveFeatureFlags(tenantId);
            return ResponseEntity.ok(response);
        }

        if (size > 0) {
            Page<FeatureFlag> response = featureFlagService.getFeatureFlags(
                    tenantId, PageRequest.of(page, size, Sort.by("flagKey").ascending()));
            return ResponseEntity.ok(response.getContent());
        }

        List<FeatureFlag> response = featureFlagService.getFeatureFlags(tenantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a feature flag by ID.
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Get feature flag by ID", description = "Retrieves a specific feature flag")
    public ResponseEntity<FeatureFlag> getFeatureFlag(@PathVariable String id) {

        log.debug("GET /api/v1/feature-flags/{}", id);

        FeatureFlag response = featureFlagService.getFeatureFlag(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Toggles a feature flag on/off.
     */
    @PutMapping(value = "/{tenantId}/{flagKey}/toggle", produces = "application/json")
    @Operation(summary = "Toggle feature flag", description = "Enables or disables a feature flag")
    public ResponseEntity<FeatureFlag> toggleFlag(
            @PathVariable String id,
            @PathVariable String tenantId,
            @PathVariable String flagKey,
            @RequestParam boolean enabled,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("PUT /api/v1/feature-flags/{}/{}/toggle - enabled={}", tenantId, flagKey, enabled);

        FeatureFlag response = featureFlagService.toggleFlag(id, enabled, tenantId, flagKey, userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Searches feature flags by key or name pattern.
     */
    @GetMapping(value = "/search", produces = "application/json")
    @Operation(summary = "Search feature flags", description = "Searches feature flags by key or name pattern")
    public ResponseEntity<List<FeatureFlag>> searchFeatureFlags(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

            @Parameter(description = "Search pattern", required = true) @RequestParam String pattern) {

        log.debug("GET /api/v1/feature-flags/search - pattern={}", pattern);

        List<FeatureFlag> response = featureFlagService.searchFeatureFlags(tenantId, pattern);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets version history for a feature flag.
     */
    @GetMapping(value = "/{id}/versions", produces = "application/json")
    @Operation(summary = "Get feature flag versions", description = "Retrieves version history for a feature flag")
    public ResponseEntity<List<ConfigVersion>> getVersionHistory(@PathVariable String id) {

        log.debug("GET /api/v1/feature-flags/{}/versions", id);

        List<ConfigVersion> response = featureFlagService.getVersionHistory(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a feature flag.
     */
    @DeleteMapping(value = "/{tenantId}/{flagKey}")
    @Operation(summary = "Delete feature flag", description = "Deletes a feature flag")
    @ApiResponse(responseCode = "204", description = "Feature flag deleted")
    public ResponseEntity<Void> deleteFeatureFlag(
            @PathVariable String id,
            @PathVariable String tenantId,
            @PathVariable String flagKey,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("DELETE /api/v1/feature-flags/{} - Deleting flag", flagKey);

        featureFlagService.deleteFeatureFlag(id, tenantId, flagKey, userId);

        return ResponseEntity.noContent().build();
    }
}
