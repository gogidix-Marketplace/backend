package com.gogidix.shared.warehousing.tenant.interfaces.rest;

import com.gogidix.shared.warehousing.tenant.application.command.CreateTenantCommand;
import com.gogidix.shared.warehousing.tenant.application.command.UpdateTenantCommand;
import com.gogidix.shared.warehousing.tenant.application.mapper.TenantDtoMapper;
import com.gogidix.shared.warehousing.tenant.application.service.TenantConfigApplicationService;
import com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.TenantRequest;
import com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.TenantResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tenant REST Controller
 *
 * Provides tenant configuration management APIs
 */
@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant Configuration", description = "APIs for managing multi-tenant configurations")
public class TenantController {

    private final TenantConfigApplicationService tenantService;
    private final TenantDtoMapper tenantDtoMapper;

    /**
     * Create new tenant
     * POST /api/v1/tenants
     */
    @PostMapping
    @Operation(summary = "Create tenant", description = "Create a new tenant in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tenant created successfully",
                    content = @Content(schema = @Schema(implementation = TenantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Tenant ID already exists")
    })
    public ResponseEntity<TenantResponse> createTenant(
            @Parameter(description = "Tenant creation request", required = true)
            @Valid @RequestBody TenantRequest request) {
        CreateTenantCommand command = CreateTenantCommand.builder()
                .tenantId(request.getTenantId())
                .tenantName(request.getTenantName())
                .tenantType(request.getTenantType())
                .storageModel(request.getStorageModel())
                .businessRules(request.getBusinessRules())
                .pricingModel(request.getPricingModel())
                .integrationEndpoints(request.getIntegrationEndpoints())
                .complianceRequirements(request.getComplianceRequirements())
                .sla(request.getSla())
                .status(request.getStatus())
                .build();

        TenantResponse response = tenantService.createTenant(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all tenants
     * GET /api/v1/tenants
     */
    @GetMapping
    @Operation(summary = "Get all tenants", description = "Retrieve all tenants in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenants retrieved successfully")
    })
    public ResponseEntity<List<TenantResponse>> getAllTenants() {
        List<TenantResponse> tenants = tenantService.getAllTenants();
        return ResponseEntity.ok(tenants);
    }

    /**
     * Get tenant by ID
     * GET /api/v1/tenants/{tenantId}
     */
    @GetMapping("/{tenantId}")
    @Operation(summary = "Get tenant by ID", description = "Retrieve a specific tenant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant found"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponse> getTenant(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @PathVariable String tenantId) {
        TenantResponse tenant = tenantService.getTenantByTenantId(tenantId);
        return ResponseEntity.ok(tenant);
    }

    /**
     * Update tenant
     * PUT /api/v1/tenants/{tenantId}
     */
    @PutMapping("/{tenantId}")
    @Operation(summary = "Update tenant", description = "Update an existing tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenant updated successfully"),
            @ApiResponse(responseCode = "404", description = "Tenant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TenantResponse> updateTenant(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @PathVariable String tenantId,
            @Parameter(description = "Tenant update command", required = true)
            @Valid @RequestBody UpdateTenantCommand command) {
        TenantResponse response = tenantService.updateTenant(tenantId, command);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete tenant
     * DELETE /api/v1/tenants/{tenantId}
     */
    @DeleteMapping("/{tenantId}")
    @Operation(summary = "Delete tenant", description = "Delete a tenant from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tenant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<Void> deleteTenant(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @PathVariable String tenantId) {
        tenantService.deleteTenant(tenantId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get business rules for tenant
     * GET /api/v1/tenants/{tenantId}/rules
     */
    @GetMapping("/{tenantId}/rules")
    @Operation(summary = "Get business rules", description = "Retrieve business rules for a specific tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Business rules retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<Object> getBusinessRules(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @PathVariable String tenantId) {
        Object rules = tenantService.getBusinessRules(tenantId);
        return ResponseEntity.ok(rules);
    }

    /**
     * Update business rules for tenant
     * PUT /api/v1/tenants/{tenantId}/rules
     */
    @PutMapping("/{tenantId}/rules")
    @Operation(summary = "Update business rules", description = "Update business rules for a specific tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Business rules updated successfully"),
            @ApiResponse(responseCode = "404", description = "Tenant not found")
    })
    public ResponseEntity<TenantResponse> updateBusinessRules(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @PathVariable String tenantId,
            @Parameter(description = "Business rules object", required = true)
            @RequestBody Object businessRules) {
        TenantResponse response = tenantService.updateBusinessRules(tenantId, businessRules);
        return ResponseEntity.ok(response);
    }
}
