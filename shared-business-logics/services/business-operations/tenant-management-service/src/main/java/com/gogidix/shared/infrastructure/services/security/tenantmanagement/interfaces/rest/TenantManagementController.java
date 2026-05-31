package com.gogidix.shared.infrastructure.services.security.tenantmanagement.interfaces.rest;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.CreateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.UpdateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response.TenantResponseDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.service.TenantManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for tenant management.
 */
@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant Management", description = "Multi-tenant administration APIs")
public class TenantManagementController {

    private final TenantManagementService tenantManagementService;

    @PostMapping
    @Operation(summary = "Create tenant", description = "Create a new tenant")
    public ResponseEntity<TenantResponseDto> createTenant(
            @Valid @RequestBody CreateTenantRequestDto request) {
        TenantResponseDto response = tenantManagementService.createTenant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{tenantId}")
    @Operation(summary = "Get tenant", description = "Get tenant by ID")
    public ResponseEntity<TenantResponseDto> getTenant(@PathVariable String tenantId) {
        return tenantManagementService.getTenant(tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/domain/{domain}")
    @Operation(summary = "Get tenant by domain", description = "Get tenant by domain name")
    public ResponseEntity<TenantResponseDto> getTenantByDomain(@PathVariable String domain) {
        return tenantManagementService.getTenantByDomain(domain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{tenantId}")
    @Operation(summary = "Update tenant", description = "Update tenant information")
    public ResponseEntity<TenantResponseDto> updateTenant(
            @PathVariable String tenantId,
            @Valid @RequestBody UpdateTenantRequestDto request) {
        TenantResponseDto response = tenantManagementService.updateTenant(tenantId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{tenantId}/activate")
    @Operation(summary = "Activate tenant", description = "Activate a suspended tenant")
    public ResponseEntity<Void> activateTenant(@PathVariable String tenantId) {
        tenantManagementService.activateTenant(tenantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tenantId}/suspend")
    @Operation(summary = "Suspend tenant", description = "Suspend an active tenant")
    public ResponseEntity<Void> suspendTenant(@PathVariable String tenantId) {
        tenantManagementService.suspendTenant(tenantId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tenantId}")
    @Operation(summary = "Delete tenant", description = "Delete a tenant")
    public ResponseEntity<Void> deleteTenant(@PathVariable String tenantId) {
        tenantManagementService.deleteTenant(tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "List tenants", description = "List all tenants")
    public ResponseEntity<List<TenantResponseDto>> listTenants() {
        List<TenantResponseDto> tenants = tenantManagementService.listTenants();
        return ResponseEntity.ok(tenants);
    }
}
