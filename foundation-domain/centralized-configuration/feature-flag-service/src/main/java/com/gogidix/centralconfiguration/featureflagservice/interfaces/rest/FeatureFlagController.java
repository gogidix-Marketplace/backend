package com.gogidix.centralconfiguration.featureflagservice.interfaces.rest;

import com.gogidix.centralconfiguration.featureflagservice.application.service.FeatureFlagService;
import com.gogidix.centralconfiguration.featureflagservice.domain.model.FeatureFlag;
import com.gogidix.centralconfiguration.featureflagservice.domain.model.FeatureFlagEvaluation;
import com.gogidix.centralconfiguration.featureflagservice.domain.port.in.CreateFeatureFlagCommand;
import com.gogidix.centralconfiguration.featureflagservice.domain.port.in.EvaluateFlagQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * REST controller for Feature Flag operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/feature-flags")
@RequiredArgsConstructor
@Tag(name = "Feature Flags", description = "APIs for feature flag management")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";

    /**
     * Create a new feature flag
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Create a new feature flag", description = "Creates a new feature flag with specified rollout strategy")
    public ResponseEntity<FeatureFlag> createFlag(
            @Valid @RequestBody CreateFeatureFlagCommand command,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        log.info("POST /api/v1/feature-flags - Creating flag: key={}", command.flagKey());

        CreateFeatureFlagCommand effectiveCommand = CreateFeatureFlagCommand.builder()
                .tenantId(tenantId)
                .flagKey(command.flagKey())
                .name(command.name())
                .description(command.description())
                .isEnabled(command.isEnabled())
                .rolloutPercentage(command.rolloutPercentage())
                .rolloutStrategy(command.rolloutStrategy())
                .whitelistedUsers(command.whitelistedUsers())
                .isSticky(command.isSticky())
                .tags(command.tags())
                .owner(command.owner())
                .createdBy(command.createdBy())
                .build();

        FeatureFlag response = featureFlagService.createFeatureFlag(effectiveCommand);

        return ResponseEntity
                .created(URI.create("/api/v1/feature-flags/" + response.getId()))
                .body(response);
    }

    /**
     * Evaluate a feature flag for a user
     */
    @PostMapping(value = "/evaluate", produces = "application/json")
    @Operation(summary = "Evaluate feature flag", description = "Evaluates if a feature flag is enabled for a specific user")
    public ResponseEntity<FeatureFlagEvaluation> evaluateFlag(
            @RequestBody Map<String, Object> request,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        String flagKey = (String) request.get("flagKey");
        String userId = (String) request.get("userId");
        @SuppressWarnings("unchecked")
        Map<String, Object> context = (Map<String, Object>) request.get("context");

        EvaluateFlagQuery query = EvaluateFlagQuery.of(flagKey, tenantId, userId, context);

        FeatureFlagEvaluation response = featureFlagService.evaluateFlag(query);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all feature flags for tenant
     */
    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all feature flags", description = "Retrieves all feature flags for the tenant")
    public ResponseEntity<List<FeatureFlag>> getFlags(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        List<FeatureFlag> response = featureFlagService.getFeatureFlags(tenantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get feature flag by ID
     */
    @GetMapping(value = "/{flagId}", produces = "application/json")
    @Operation(summary = "Get feature flag by ID", description = "Retrieves a specific feature flag")
    public ResponseEntity<FeatureFlag> getFlag(@PathVariable Long flagId) {
        FeatureFlag response = featureFlagService.getFeatureFlag(flagId);
        return ResponseEntity.ok(response);
    }

    /**
     * Toggle feature flag
     */
    @PutMapping(value = "/{flagId}/toggle", produces = "application/json")
    @Operation(summary = "Toggle feature flag", description = "Enables or disables a feature flag")
    public ResponseEntity<FeatureFlag> toggleFlag(
            @PathVariable Long flagId,
            @RequestParam boolean enabled,

            @Parameter(description = "User ID")
            @RequestHeader(value = "X-User-ID", defaultValue = "system") String userId) {

        FeatureFlag response = featureFlagService.toggleFlag(flagId, enabled, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete feature flag
     */
    @DeleteMapping(value = "/{flagId}")
    @Operation(summary = "Delete feature flag", description = "Deletes a feature flag")
    public ResponseEntity<Void> deleteFlag(@PathVariable Long flagId) {
        featureFlagService.deleteFeatureFlag(flagId);
        return ResponseEntity.noContent().build();
    }
}
