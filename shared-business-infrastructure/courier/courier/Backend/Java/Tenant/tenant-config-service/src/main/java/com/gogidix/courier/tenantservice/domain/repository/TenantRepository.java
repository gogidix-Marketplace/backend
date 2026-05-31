package com.gogidix.courier.tenantservice.domain.repository;

import com.gogidix.courier.tenantservice.domain.entity.Tenant;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Tenant aggregates.
 * Defines the contract for tenant persistence operations.
 * All implementations MUST filter by tenantId for multi-tenancy.
 */
public interface TenantRepository {

    /**
     * Save a tenant.
     *
     * @param tenant the tenant to save
     * @return the saved tenant
     */
    Tenant save(Tenant tenant);

    /**
     * Find a tenant by ID.
     *
     * @param id the tenant ID
     * @return the tenant if found
     */
    Optional<Tenant> findById(String id);

    /**
     * Find a tenant by tenantId.
     *
     * @param tenantId the tenant identifier
     * @return the tenant if found
     */
    Optional<Tenant> findByTenantId(String tenantId);

    /**
     * Find all tenants.
     *
     * @return list of all tenants
     */
    List<Tenant> findAll();

    /**
     * Find tenants by status.
     *
     * @param status the tenant status
     * @return list of tenants
     */
    List<Tenant> findByStatus(Tenant.TenantStatus status);

    /**
     * Find a tenant by name.
     *
     * @param name the tenant name
     * @return the tenant if found
     */
    Optional<Tenant> findByName(String name);

    /**
     * Check if a tenant exists by tenantId.
     *
     * @param tenantId the tenant identifier
     * @return true if exists, false otherwise
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Check if a tenant exists by name.
     *
     * @param name the tenant name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Delete a tenant by ID.
     *
     * @param id the tenant ID
     */
    void deleteById(String id);

    /**
     * Count all tenants.
     *
     * @return the count
     */
    long count();

    /**
     * Count tenants by status.
     *
     * @param status the tenant status
     * @return the count
     */
    long countByStatus(Tenant.TenantStatus status);

    /**
     * Find tenants with pagination.
     *
     * @param page the page number (0-indexed)
     * @param size the page size
     * @return list of tenants
     */
    List<Tenant> findAllPaginated(int page, int size);
}
