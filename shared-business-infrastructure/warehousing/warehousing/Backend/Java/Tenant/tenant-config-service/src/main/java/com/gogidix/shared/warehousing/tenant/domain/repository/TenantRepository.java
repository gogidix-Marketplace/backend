package com.gogidix.shared.warehousing.tenant.domain.repository;

import com.gogidix.shared.warehousing.tenant.domain.entity.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Tenant Repository
 *
 * Manages tenant configurations in MongoDB
 */
@Repository
public interface TenantRepository extends MongoRepository<Tenant, String> {

    /**
     * Find tenant by tenant ID
     */
    Optional<Tenant> findByTenantId(String tenantId);

    /**
     * Check if tenant exists by tenant ID
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Find tenants by status
     */
    List<Tenant> findByStatus(String status);

    /**
     * Find tenants by tenant type
     */
    List<Tenant> findByTenantType(String tenantType);
}
