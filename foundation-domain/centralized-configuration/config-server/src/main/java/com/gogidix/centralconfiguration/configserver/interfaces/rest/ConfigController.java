package com.gogidix.centralconfiguration.configserver.interfaces.rest;

import com.gogidix.centralconfiguration.configserver.application.dto.request.CreateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.request.UpdateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigHistoryResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigurationResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.PagedConfigResponseDto;
import com.gogidix.centralconfiguration.configserver.application.service.ConfigCommandService;
import com.gogidix.centralconfiguration.configserver.application.service.ConfigQueryService;
import com.gogidix.centralconfiguration.configserver.domain.port.in.SearchConfigsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for Configuration operations.
 * Provides API endpoints for managing centralized configurations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/configs")
@RequiredArgsConstructor
@Tag(name = "Config Server", description = "APIs for centralized configuration management")
public class ConfigController {

    private final ConfigCommandService commandService;
    private final ConfigQueryService queryService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_USER_HEADER = "X-User-ID";

    /**
     * Create a new configuration entry
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a new configuration",
        description = "Creates a new configuration entry for the specified application and profile"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Configuration created successfully",
            headers = @Header(name = "X-Tenant-ID", description = "Tenant ID"),
            content = @Content(schema = @Schema(implementation = ConfigurationResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Configuration already exists")
    })
    public ResponseEntity<ConfigurationResponseDto> createConfig(
        @Parameter(description = "Configuration creation request", required = true)
        @Valid @RequestBody CreateConfigRequestDto request,

        @Parameter(description = "Tenant ID header", example = "tenant-1")
        @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

        @Parameter(description = "User ID header", example = "user-1")
        @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId
    ) {
        log.info("POST /api/v1/configs - Creating config: application={}, key={}",
                request.getApplicationName(), request.getConfigKey());

        ConfigurationResponseDto response = commandService.createConfig(request, tenantId, userId);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create("/api/v1/configs/" + response.getId()))
            .body(response);
    }

    /**
     * Get configuration by ID
     */
    @GetMapping(value = "/{configId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get configuration by ID",
        description = "Retrieves detailed information about a specific configuration entry"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Configuration found",
                content = @Content(schema = @Schema(implementation = ConfigurationResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    public ResponseEntity<ConfigurationResponseDto> getConfig(
        @Parameter(description = "Configuration ID", required = true, example = "1")
        @PathVariable Long configId
    ) {
        log.info("GET /api/v1/configs/{} - Getting configuration", configId);

        ConfigurationResponseDto response = queryService.getConfigById(configId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get configuration by key
     */
    @GetMapping(value = "/by-key", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get configuration by key",
        description = "Retrieves a specific configuration entry by its composite key"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Configuration found"),
        @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    public ResponseEntity<ConfigurationResponseDto> getConfigByKey(
        @Parameter(description = "Application name", required = true) @RequestParam String application,
        @Parameter(description = "Profile", required = true) @RequestParam String profile,
        @Parameter(description = "Configuration key", required = true) @RequestParam String key,

        @Parameter(description = "Tenant ID", example = "tenant-1")
        @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId
    ) {
        log.info("GET /api/v1/configs/by-key - application={}, profile={}, key={}", application, profile, key);

        ConfigurationResponseDto response = queryService.getConfigByKey(tenantId, application, profile, key);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all configurations for an application and profile
     */
    @GetMapping(value = "/application/{application}/profile/{profile}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get all configurations for an application and profile",
        description = "Retrieves all configuration entries for the specified application and profile"
    )
    public ResponseEntity<List<ConfigurationResponseDto>> getConfigsByApplicationAndProfile(
        @Parameter(description = "Application name", required = true) @PathVariable String application,
        @Parameter(description = "Profile", required = true) @PathVariable String profile,

        @Parameter(description = "Tenant ID")
        @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId
    ) {
        log.info("GET /api/v1/configs/application/{}/profile/{}", application, profile);

        List<ConfigurationResponseDto> response = queryService.getConfigsByApplicationAndProfile(
                tenantId, application, profile);
        return ResponseEntity.ok(response);
    }

    /**
     * Search configurations
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Search configurations",
        description = "Searches for configuration entries with optional filters"
    )
    public ResponseEntity<PagedConfigResponseDto<ConfigurationResponseDto>> searchConfigs(
        @Parameter(description = "Filter by application name") @RequestParam(required = false) String application,
        @Parameter(description = "Filter by profile") @RequestParam(required = false) String profile,
        @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean isActive,
        @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") Integer page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") Integer size,

        @Parameter(description = "Tenant ID")
        @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId
    ) {
        log.info("GET /api/v1/configs - Searching: application={}, page={}", application, page);

        SearchConfigsQuery query = SearchConfigsQuery.builder()
            .tenantId(tenantId)
            .applicationName(application)
            .profiles(profile != null ? List.of(profile) : null)
            .isActive(isActive)
            .page(page)
            .size(size)
            .sortBy("createdAt")
            .sortDirection("DESC")
            .build();

        PagedConfigResponseDto<ConfigurationResponseDto> response = queryService.searchConfigs(query);
        return ResponseEntity.ok(response);
    }

    /**
     * Update configuration
     */
    @PutMapping(value = "/{configId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update configuration",
        description = "Updates an existing configuration entry"
    )
    public ResponseEntity<ConfigurationResponseDto> updateConfig(
        @Parameter(description = "Configuration ID", required = true) @PathVariable Long configId,
        @Parameter(description = "Update request", required = true) @Valid @RequestBody UpdateConfigRequestDto request,

        @Parameter(description = "Tenant ID")
        @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

        @Parameter(description = "User ID")
        @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId
    ) {
        log.info("PUT /api/v1/configs/{} - Updating configuration", configId);

        ConfigurationResponseDto response = commandService.updateConfig(configId, request, tenantId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete configuration
     */
    @DeleteMapping(value = "/{configId}")
    @Operation(
        summary = "Delete configuration",
        description = "Deletes a configuration entry"
    )
    public ResponseEntity<Void> deleteConfig(
        @Parameter(description = "Configuration ID", required = true) @PathVariable Long configId,

        @Parameter(description = "Tenant ID")
        @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

        @Parameter(description = "User ID")
        @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId
    ) {
        log.info("DELETE /api/v1/configs/{} - Deleting configuration", configId);

        commandService.deleteConfig(configId, tenantId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get configuration history
     */
    @GetMapping(value = "/{configId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get configuration history",
        description = "Retrieves the audit trail for a specific configuration entry"
    )
    public ResponseEntity<List<ConfigHistoryResponseDto>> getConfigHistory(
        @Parameter(description = "Configuration ID", required = true) @PathVariable Long configId
    ) {
        log.info("GET /api/v1/configs/{}/history", configId);

        List<ConfigHistoryResponseDto> response = queryService.getConfigHistory(configId);
        return ResponseEntity.ok(response);
    }
}
