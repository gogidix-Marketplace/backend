package com.gogidix.courier.tenantservice.infrastructure.persistence.repository;

import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for Tenant.
 */
@Repository
public interface MongoTenantRepository extends MongoRepository<Tenant, String> {

    /**
     * Find tenant by tenantId.
     */
    Optional<Tenant> findByTenantId(String tenantId);

    /**
     * Find tenant by name.
     */
    Optional<Tenant> findByName(String name);

    /**
     * Find tenants by status.
     */
    List<Tenant> findByStatus(Tenant.TenantStatus status);

    /**
     * Find tenants by status with pagination.
     */
    Page<Tenant> findByStatus(Tenant.TenantStatus status, Pageable pageable);

    /**
     * Find tenants by name containing.
     */
    List<Tenant> findByNameContainingIgnoreCase(String name);

    /**
     * Count tenants by status.
     */
    long countByStatus(Tenant.TenantStatus status);

    /**
     * Check if tenant exists by tenantId.
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Check if tenant exists by name.
     */
    boolean existsByName(String name);

    /**
     * Find active tenants.
     */
    @Query("{ 'status': 'ACTIVE' }")
    List<Tenant> findActiveTenants();
}
