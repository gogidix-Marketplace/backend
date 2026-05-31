package com.gogidix.platform.platform.interfaces.rest;

import com.gogidix.platform.platform.application.dto.FeatureFlagDto;
import com.gogidix.platform.platform.application.service.FeatureFlagService;
import com.gogidix.platform.platform.domain.port.in.CreateFeatureFlagCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for feature flag operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/platform/feature-flags")
@RequiredArgsConstructor
@Tag(name = "Feature Flags", description = "Feature flag management APIs")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    @PostMapping
    @Operation(summary = "Create a new feature flag")
    public ResponseEntity<FeatureFlagDto> createFeatureFlag(@Valid @RequestBody CreateFeatureFlagCommand command) {
        log.info("REST request to create feature flag: {}", command.getFeatureKey());
        FeatureFlagDto result = featureFlagService.createFeatureFlag(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get feature flag by key")
    public ResponseEntity<FeatureFlagDto> getFeatureFlag(@PathVariable String key) {
        log.info("REST request to get feature flag: {}", key);
        FeatureFlagDto result = featureFlagService.getFeatureFlag(key);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{key}/enabled")
    @Operation(summary = "Check if feature is enabled")
    public ResponseEntity<Boolean> isFeatureEnabled(@PathVariable String key, @RequestParam(required = false) String userId) {
        log.info("REST request to check if feature is enabled: {}", key);
        boolean enabled = featureFlagService.isFeatureEnabled(key, userId);
        return ResponseEntity.ok(enabled);
    }

    @GetMapping
    @Operation(summary = "Get all feature flags")
    public ResponseEntity<List<FeatureFlagDto>> getAllFeatureFlags() {
        log.info("REST request to get all feature flags");
        List<FeatureFlagDto> results = featureFlagService.getAllFeatureFlags();
        return ResponseEntity.ok(results);
    }

    @PutMapping("/{id}/toggle")
    @Operation(summary = "Toggle feature flag")
    public ResponseEntity<FeatureFlagDto> toggleFeatureFlag(
        @PathVariable String id,
        @RequestParam boolean enabled) {
        log.info("REST request to toggle feature flag: id={}, enabled={}", id, enabled);
        FeatureFlagDto result = featureFlagService.toggleFeatureFlag(id, enabled);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feature flag")
    public ResponseEntity<Void> deleteFeatureFlag(@PathVariable String id) {
        log.info("REST request to delete feature flag: {}", id);
        featureFlagService.deleteFeatureFlag(id);
        return ResponseEntity.noContent().build();
    }
}
