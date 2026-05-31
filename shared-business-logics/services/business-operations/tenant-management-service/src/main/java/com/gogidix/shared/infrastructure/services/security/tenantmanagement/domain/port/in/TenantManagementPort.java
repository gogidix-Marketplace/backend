package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.CreateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.UpdateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response.TenantResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * Tenant Management use case port.
 */
public interface TenantManagementPort {

    /**
     * Creates a new tenant.
     */
    TenantResponseDto createTenant(CreateTenantRequestDto request);

    /**
     * Updates an existing tenant.
     */
    TenantResponseDto updateTenant(String tenantId, UpdateTenantRequestDto request);

    /**
     * Gets a tenant by ID.
     */
    Optional<TenantResponseDto> getTenant(String tenantId);

    /**
     * Gets a tenant by domain.
     */
    Optional<TenantResponseDto> getTenantByDomain(String domain);

    /**
     * Lists all tenants (admin only).
     */
    List<TenantResponseDto> listTenants();

    /**
     * Activates a tenant.
     */
    void activateTenant(String tenantId);

    /**
     * Suspends a tenant.
     */
    void suspendTenant(String tenantId);

    /**
     * Deletes a tenant.
     */
    void deleteTenant(String tenantId);
}
