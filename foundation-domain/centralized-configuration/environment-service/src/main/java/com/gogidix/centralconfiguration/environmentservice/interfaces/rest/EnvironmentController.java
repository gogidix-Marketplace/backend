package com.gogidix.centralconfiguration.environmentservice.interfaces.rest;

import com.gogidix.centralconfiguration.environmentservice.application.service.EnvironmentService;
import com.gogidix.centralconfiguration.environmentservice.domain.model.Environment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * REST controller for Environment operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/environments")
@RequiredArgsConstructor
@Tag(name = "Environments", description = "APIs for environment configuration management")
public class EnvironmentController {

    private final EnvironmentService environmentService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";

    @PostMapping(produces = "application/json")
    @Operation(summary = "Create environment", description = "Creates a new environment")
    public ResponseEntity<Environment> createEnvironment(
            @RequestBody Map<String, Object> request,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

            @Parameter(description = "User ID")
            @RequestHeader(value = "X-User-ID", defaultValue = "system") String userId) {

        String environmentName = (String) request.get("environmentName");
        String displayName = (String) request.get("displayName");
        String description = (String) request.get("description");
        String type = (String) request.getOrDefault("type", "DEVELOPMENT");

        Environment response = environmentService.createEnvironment(tenantId, environmentName, displayName, description, type, userId);

        return ResponseEntity
                .created(URI.create("/api/v1/environments/" + response.getId()))
                .body(response);
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all environments", description = "Retrieves all environments for the tenant")
    public ResponseEntity<List<Environment>> getEnvironments(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        List<Environment> response = environmentService.getEnvironments(tenantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{environmentName}", produces = "application/json")
    @Operation(summary = "Get environment by name", description = "Retrieves a specific environment")
    public ResponseEntity<Environment> getEnvironment(@PathVariable String environmentName) {
        Environment response = environmentService.getEnvironment(environmentName);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{environmentId}", produces = "application/json")
    @Operation(summary = "Update environment", description = "Updates an environment")
    public ResponseEntity<Environment> updateEnvironment(
            @PathVariable Long environmentId,
            @RequestBody Map<String, Object> request) {

        String displayName = (String) request.get("displayName");
        String description = (String) request.get("description");
        Boolean isActive = (Boolean) request.get("isActive");

        Environment response = environmentService.updateEnvironment(environmentId, displayName, description, isActive);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{environmentId}")
    @Operation(summary = "Delete environment", description = "Deletes an environment")
    public ResponseEntity<Void> deleteEnvironment(@PathVariable Long environmentId) {
        environmentService.deleteEnvironment(environmentId);
        return ResponseEntity.noContent().build();
    }
}
