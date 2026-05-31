package com.gogidix.infrastructure.database.interfaces.rest;

import com.gogidix.infrastructure.database.application.service.TenantRoutingService;
import com.gogidix.infrastructure.database.domain.model.TenantDatabaseConfiguration;
import com.gogidix.infrastructure.database.interfaces.rest.dto.CommonDto;
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
 * REST Controller for Multi-Tenant Database Routing.
 */
@RestController
@RequestMapping("/api/v1/tenant-databases")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Tenant Databases", description = "APIs for managing multi-tenant database configurations")
public class TenantRoutingController {

    private final TenantRoutingService tenantRoutingService;

    /**
     * Create a new tenant database configuration.
     */
    @PostMapping
    @Operation(summary = "Create tenant database", description = "Create a new tenant database configuration")
    public ResponseEntity<CommonDto.ApiResponse<TenantDatabaseConfiguration>> createTenant(
            @Valid @RequestBody TenantDatabaseConfiguration request) {

        TenantDatabaseConfiguration created = tenantRoutingService.createTenant(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonDto.ApiResponse.success(
                        "Tenant database created successfully",
                        created));
    }

    /**
     * Get tenant configuration by ID.
     */
    @GetMapping("/{tenantId}")
    @Operation(summary = "Get tenant database", description = "Get tenant database configuration by tenant ID")
    public ResponseEntity<CommonDto.ApiResponse<TenantDatabaseConfiguration>> getTenant(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId) {

        return tenantRoutingService.getTenant(tenantId)
                .map(tenant -> ResponseEntity.ok(CommonDto.ApiResponse.success(tenant)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all tenant configurations.
     */
    @GetMapping
    @Operation(summary = "Get all tenants", description = "Get all tenant database configurations")
    public ResponseEntity<CommonDto.ApiResponse<List<TenantDatabaseConfiguration>>> getAllTenants() {

        List<TenantDatabaseConfiguration> tenants = tenantRoutingService.getAllTenants();

        return ResponseEntity.ok(CommonDto.ApiResponse.success(tenants));
    }

    /**
     * Update tenant configuration.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update tenant database", description = "Update an existing tenant database configuration")
    public ResponseEntity<CommonDto.ApiResponse<TenantDatabaseConfiguration>> updateTenant(
            @Parameter(description = "Tenant configuration ID") @PathVariable String id,
            @Valid @RequestBody TenantDatabaseConfiguration request) {

        TenantDatabaseConfiguration updated = tenantRoutingService.updateTenant(id, request);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(
                "Tenant database updated successfully",
                updated));
    }

    /**
     * Activate tenant database.
     */
    @PostMapping("/{tenantId}/activate")
    @Operation(summary = "Activate tenant database", description = "Activate an existing tenant database")
    public ResponseEntity<CommonDto.ApiResponse<TenantDatabaseConfiguration>> activateTenant(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId) {

        TenantDatabaseConfiguration activated = tenantRoutingService.activateTenant(tenantId);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(
                "Tenant database activated successfully",
                activated));
    }

    /**
     * Deactivate tenant database.
     */
    @PostMapping("/{tenantId}/deactivate")
    @Operation(summary = "Deactivate tenant database", description = "Deactivate an existing tenant database")
    public ResponseEntity<CommonDto.ApiResponse<TenantDatabaseConfiguration>> deactivateTenant(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId) {

        TenantDatabaseConfiguration deactivated = tenantRoutingService.deactivateTenant(tenantId);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(
                "Tenant database deactivated successfully",
                deactivated));
    }

    /**
     * Deprovision tenant database.
     */
    @DeleteMapping("/{tenantId}")
    @Operation(summary = "Deprovision tenant database", description = "Deprovision and delete a tenant database")
    public ResponseEntity<CommonDto.ApiResponse<Void>> deprovisionTenant(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId) {

        tenantRoutingService.deprovisionTenant(tenantId);

        return ResponseEntity.ok(CommonDto.ApiResponse.<Void>success(
                "Tenant database deprovisioned successfully", null));
    }

    /**
     * Get tenant statistics.
     */
    @GetMapping("/{tenantId}/statistics")
    @Operation(summary = "Get tenant statistics", description = "Get statistics for a specific tenant")
    public ResponseEntity<CommonDto.ApiResponse<Map<String, Object>>> getTenantStatistics(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId) {

        Map<String, Object> statistics = tenantRoutingService.getTenantStatistics(tenantId);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(statistics));
    }

    /**
     * Get all tenants statistics.
     */
    @GetMapping("/statistics/all")
    @Operation(summary = "Get all tenants statistics", description = "Get overall statistics for all tenants")
    public ResponseEntity<CommonDto.ApiResponse<Map<String, Object>>> getAllTenantsStatistics() {

        Map<String, Object> statistics = tenantRoutingService.getAllTenantsStatistics();

        return ResponseEntity.ok(CommonDto.ApiResponse.success(statistics));
    }

    /**
     * Upgrade tenant tier.
     */
    @PostMapping("/{tenantId}/tier")
    @Operation(summary = "Upgrade tenant tier", description = "Upgrade a tenant to a higher tier")
    public ResponseEntity<CommonDto.ApiResponse<TenantDatabaseConfiguration>> upgradeTier(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId,
            @Parameter(description = "New tier") @RequestParam TenantDatabaseConfiguration.TenantTier tier) {

        TenantDatabaseConfiguration upgraded = tenantRoutingService.upgradeTier(tenantId, tier);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(
                "Tenant tier upgraded successfully",
                upgraded));
    }

    /**
     * Check if tenant can accept connections.
     */
    @GetMapping("/{tenantId}/can-connect")
    @Operation(summary = "Check connection availability", description = "Check if tenant can accept new connections")
    public ResponseEntity<CommonDto.ApiResponse<Boolean>> canAcceptConnection(
            @Parameter(description = "Tenant ID") @PathVariable String tenantId) {

        boolean canConnect = tenantRoutingService.canTenantAcceptConnection(tenantId);

        return ResponseEntity.ok(CommonDto.ApiResponse.success(canConnect));
    }
}
