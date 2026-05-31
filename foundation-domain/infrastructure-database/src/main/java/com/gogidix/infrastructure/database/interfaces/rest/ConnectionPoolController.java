package com.gogidix.infrastructure.database.interfaces.rest;

import com.gogidix.infrastructure.database.application.service.ConnectionPoolService;
import com.gogidix.infrastructure.database.domain.model.ConnectionPoolConfiguration;
import com.gogidix.infrastructure.database.interfaces.rest.dto.CommonDto;
import com.gogidix.infrastructure.database.interfaces.rest.dto.ConnectionPoolDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Connection Pool Management.
 */
@RestController
@RequestMapping("/api/v1/connection-pools")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Connection Pools", description = "APIs for managing database connection pools")
public class ConnectionPoolController {

    private final ConnectionPoolService connectionPoolService;

    /**
     * Create a new connection pool.
     */
    @PostMapping
    @Operation(summary = "Create connection pool", description = "Create a new database connection pool configuration")
    public ResponseEntity<CommonDto.ApiResponse<ConnectionPoolDto>> createPool(
            @Valid @RequestBody ConnectionPoolDto request) {

        ConnectionPoolConfiguration configuration = request.toDomain();
        ConnectionPoolConfiguration created = connectionPoolService.createPool(configuration);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonDto.ApiResponse.success(
                        "Connection pool created successfully",
                        ConnectionPoolDto.fromDomain(created)));
    }

    /**
     * Get connection pool by tenant and pool name.
     */
    @GetMapping("/{tenantId}/{poolName}")
    @Operation(summary = "Get connection pool", description = "Get a connection pool by tenant ID and pool name")
    public ResponseEntity<CommonDto.ApiResponse<ConnectionPoolDto>> getPool(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId,
            @Parameter(description = "Pool name") @PathVariable String poolName) {

        return connectionPoolService.getPool(tenantId, poolName)
                .map(pool -> ResponseEntity.ok(
                        CommonDto.ApiResponse.success(ConnectionPoolDto.fromDomain(pool))))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all connection pools for a tenant.
     */
    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "Get tenant pools", description = "Get all connection pools for a tenant")
    public ResponseEntity<CommonDto.ApiResponse<List<ConnectionPoolDto>>> getPoolsByTenant(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId) {

        List<ConnectionPoolConfiguration> pools = connectionPoolService.getPoolsByTenant(tenantId);
        List<ConnectionPoolDto> dtos = pools.stream()
                .map(ConnectionPoolDto::fromDomain)
                .toList();

        return ResponseEntity.ok(CommonDto.ApiResponse.success(dtos));
    }

    /**
     * Update connection pool configuration.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update connection pool", description = "Update an existing connection pool configuration")
    public ResponseEntity<CommonDto.ApiResponse<ConnectionPoolDto>> updatePool(
            @Parameter(description = "Pool ID") @PathVariable String id,
            @Valid @RequestBody ConnectionPoolDto request) {

        ConnectionPoolConfiguration configuration = request.toDomain();
        ConnectionPoolConfiguration updated = connectionPoolService.updatePool(id, configuration);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(
                "Connection pool updated successfully",
                ConnectionPoolDto.fromDomain(updated)));
    }

    /**
     * Delete connection pool.
     */
    @DeleteMapping("/{tenantId}/{poolName}")
    @Operation(summary = "Delete connection pool", description = "Delete a connection pool configuration")
    public ResponseEntity<CommonDto.ApiResponse<Void>> deletePool(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId,
            @Parameter(description = "Pool name") @PathVariable String poolName) {

        connectionPoolService.deletePool(tenantId, poolName);

        return ResponseEntity.ok(CommonDto.ApiResponse.<Void>success(
                "Connection pool deleted successfully", null));
    }

    /**
     * Activate connection pool.
     */
    @PostMapping("/{tenantId}/{poolName}/activate")
    @Operation(summary = "Activate connection pool", description = "Activate an existing connection pool")
    public ResponseEntity<CommonDto.ApiResponse<ConnectionPoolDto>> activatePool(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId,
            @Parameter(description = "Pool name") @PathVariable String poolName) {

        ConnectionPoolConfiguration activated = connectionPoolService.activatePool(tenantId, poolName);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(
                "Connection pool activated successfully",
                ConnectionPoolDto.fromDomain(activated)));
    }

    /**
     * Deactivate connection pool.
     */
    @PostMapping("/{tenantId}/{poolName}/deactivate")
    @Operation(summary = "Deactivate connection pool", description = "Deactivate an existing connection pool")
    public ResponseEntity<CommonDto.ApiResponse<ConnectionPoolDto>> deactivatePool(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId,
            @Parameter(description = "Pool name") @PathVariable String poolName) {

        ConnectionPoolConfiguration deactivated = connectionPoolService.deactivatePool(tenantId, poolName);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(
                "Connection pool deactivated successfully",
                ConnectionPoolDto.fromDomain(deactivated)));
    }

    /**
     * Get pool statistics.
     */
    @GetMapping("/{tenantId}/{poolName}/statistics")
    @Operation(summary = "Get pool statistics", description = "Get statistics for a specific connection pool")
    public ResponseEntity<CommonDto.ApiResponse<Map<String, Object>>> getPoolStatistics(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId,
            @Parameter(description = "Pool name") @PathVariable String poolName) {

        Map<String, Object> statistics = connectionPoolService.getPoolStatistics(tenantId, poolName);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(statistics));
    }

    /**
     * Get all pool statistics.
     */
    @GetMapping("/statistics/all")
    @Operation(summary = "Get all pool statistics", description = "Get statistics for all connection pools")
    public ResponseEntity<CommonDto.ApiResponse<Map<String, Map<String, Object>>>> getAllPoolStatistics() {

        Map<String, Map<String, Object>> statistics = connectionPoolService.getAllPoolStatistics();

        return ResponseEntity.ok(CommonDto.ApiResponse.success(statistics));
    }

    /**
     * Health check for all connection pools.
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Perform health check on all connection pools")
    public ResponseEntity<CommonDto.ApiResponse<Map<String, Boolean>>> healthCheck() {

        Map<String, Boolean> healthStatus = connectionPoolService.healthCheck();

        return ResponseEntity.ok(CommonDto.ApiResponse.success(healthStatus));
    }
}
