package com.gogidix.infrastructure.config.interfaces.rest;

import com.gogidix.infrastructure.config.application.service.ConfigurationPropertyService;
import com.gogidix.infrastructure.config.domain.model.ConfigVersion;
import com.gogidix.infrastructure.config.domain.model.ConfigurationProperty;
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
 * REST controller for configuration property management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/configurations")
@RequiredArgsConstructor
@Tag(name = "Configuration Properties", description = "APIs for configuration property management")
public class ConfigurationController {

    private final ConfigurationPropertyService configurationService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_USER_HEADER = "X-User-ID";

    /**
     * Creates a new configuration property.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Create configuration", description = "Creates a new configuration property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Configuration created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Configuration already exists")
    })
    public ResponseEntity<ConfigurationProperty> createConfiguration(
            @Valid @RequestBody CreateConfigurationRequest request,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("POST /api/v1/configurations - Creating configuration: key={}", request.key());

        ConfigurationProperty response = configurationService.createConfiguration(request, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Updates an existing configuration property.
     */
    @PutMapping(value = "/{tenantId}/{key}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Update configuration", description = "Updates an existing configuration property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    public ResponseEntity<ConfigurationProperty> updateConfiguration(
            @PathVariable String id,
            @PathVariable String tenantId,
            @PathVariable String key,
            @Valid @RequestBody UpdateConfigurationRequest request,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("PUT /api/v1/configurations/{}/{}/{} - Updating configuration", tenantId, key);

        ConfigurationProperty response = configurationService.updateConfiguration(
                id, request, tenantId, key, userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Gets a configuration property by tenant and key.
     */
    @GetMapping(value = "/{tenantId}/{key}", produces = "application/json")
    @Operation(summary = "Get configuration", description = "Retrieves a configuration property by tenant and key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration retrieved"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    public ResponseEntity<ConfigurationProperty> getConfiguration(
            @PathVariable String tenantId,
            @PathVariable String key) {

        log.debug("GET /api/v1/configurations/{}/{}", tenantId, key);

        ConfigurationProperty response = configurationService.getConfiguration(tenantId, key);

        return ResponseEntity.ok(response);
    }

    /**
     * Gets all configuration properties for a tenant.
     */
    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all configurations", description = "Retrieves all configuration properties for a tenant")
    public ResponseEntity<List<ConfigurationProperty>> getConfigurations(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

            @Parameter(description = "Environment filter") @RequestParam(required = false) String environment,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "50") int size) {

        log.debug("GET /api/v1/configurations - tenantId={}", tenantId);

        if (environment != null) {
            ConfigurationProperty.Environment env = ConfigurationProperty.Environment.valueOf(environment.toUpperCase());
            List<ConfigurationProperty> response = configurationService
                    .getConfigurationsByEnvironment(tenantId, env);
            return ResponseEntity.ok(response);
        }

        if (size > 0) {
            Page<ConfigurationProperty> response = configurationService.getConfigurations(
                    tenantId, PageRequest.of(page, size, Sort.by("key").ascending()));
            return ResponseEntity.ok(response.getContent());
        }

        List<ConfigurationProperty> response = configurationService.getConfigurations(tenantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Searches configuration properties by key pattern.
     */
    @GetMapping(value = "/search", produces = "application/json")
    @Operation(summary = "Search configurations", description = "Searches configuration properties by key pattern")
    public ResponseEntity<List<ConfigurationProperty>> searchConfigurations(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

            @Parameter(description = "Search pattern", required = true) @RequestParam String pattern) {

        log.debug("GET /api/v1/configurations/search - pattern={}", pattern);

        List<ConfigurationProperty> response = configurationService.searchConfigurations(tenantId, pattern);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets configuration values for multiple keys.
     */
    @PostMapping(value = "/batch", produces = "application/json")
    @Operation(summary = "Get configuration values", description = "Retrieves values for multiple configuration keys")
    public ResponseEntity<Map<String, String>> getConfigurationValues(
            @RequestBody Map<String, Set<String>> request,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        Set<String> keys = request.get("keys");
        Map<String, String> response = configurationService.getConfigurationValues(tenantId, keys);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a typed configuration value.
     */
    @GetMapping(value = "/{tenantId}/{key}/value", produces = "application/json")
    @Operation(summary = "Get configuration value", description = "Retrieves a typed configuration value")
    @ApiResponse(responseCode = "200", description = "Value retrieved",
            content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<Map<String, Object>> getConfigurationValue(
            @PathVariable String tenantId,
            @PathVariable String key,

            @Parameter(description = "Value type") @RequestParam(defaultValue = "STRING") String type) {

        log.debug("GET /api/v1/configurations/{}/{}/value - type={}", tenantId, key, type);

        Class<?> typeClass = switch (type.toUpperCase()) {
            case "INTEGER" -> Integer.class;
            case "LONG" -> Long.class;
            case "BOOLEAN" -> Boolean.class;
            case "DOUBLE" -> Double.class;
            default -> String.class;
        };

        Object value = configurationService.getConfigurationValue(tenantId, key, typeClass);

        return ResponseEntity.ok(Map.of("key", key, "value", value, "type", type));
    }

    /**
     * Gets version history for a configuration.
     */
    @GetMapping(value = "/{id}/versions", produces = "application/json")
    @Operation(summary = "Get configuration versions", description = "Retrieves version history for a configuration")
    public ResponseEntity<List<ConfigVersion>> getVersionHistory(@PathVariable String id) {

        log.debug("GET /api/v1/configurations/{}/versions", id);

        List<ConfigVersion> response = configurationService.getVersionHistory(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Rolls back a configuration to a previous version.
     */
    @PostMapping(value = "/{id}/rollback/{version}", produces = "application/json")
    @Operation(summary = "Rollback configuration", description = "Rolls back a configuration to a previous version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration rolled back"),
            @ApiResponse(responseCode = "400", description = "Cannot rollback to this version"),
            @ApiResponse(responseCode = "404", description = "Configuration or version not found")
    })
    public ResponseEntity<ConfigurationProperty> rollbackConfiguration(
            @PathVariable String id,
            @PathVariable Integer version,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("POST /api/v1/configurations/{}/rollback/{} - Rolling back", id, version);

        ConfigurationProperty response = configurationService.rollbackToVersion(id, version, userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a configuration property.
     */
    @DeleteMapping(value = "/{tenantId}/{key}")
    @Operation(summary = "Delete configuration", description = "Deletes a configuration property")
    @ApiResponse(responseCode = "204", description = "Configuration deleted")
    public ResponseEntity<Void> deleteConfiguration(
            @PathVariable String id,
            @PathVariable String tenantId,
            @PathVariable String key,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("DELETE /api/v1/configurations/{}/{} - Deleting configuration", tenantId, key);

        configurationService.deleteConfiguration(id, tenantId, key, userId);

        return ResponseEntity.noContent().build();
    }
}
