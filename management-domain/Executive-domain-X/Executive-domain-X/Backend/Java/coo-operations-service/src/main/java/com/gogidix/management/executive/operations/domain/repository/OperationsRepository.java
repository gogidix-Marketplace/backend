package com.gogidix.management.executive.operations.domain.repository;

import com.gogidix.management.executive.operations.domain.model.Operations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Operations entities
 */
@Repository
public interface OperationsRepository extends MongoRepository<Operations, String> {

    /**
     * Find strategy by tenant ID
     */
    Optional<Operations> findByTenantId(String tenantId);

    /**
     * Find all operationss by tenant ID
     */
    List<Operations> findAllByTenantId(String tenantId);

    /**
     * Find all operationss by tenant ID and not deleted
     */
    List<Operations> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find operationss by tenant ID and not deleted
     */
    List<Operations> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find operationss by tenant ID with pagination
     */
    Page<Operations> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find operationss by tenant ID and not deleted with pagination
     */
    Page<Operations> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

    /**
     * Check if strategy exists by tenant ID
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Check if strategy exists by tenant ID and not deleted
     */
    boolean existsByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Check if strategy exists by ID and tenant ID and not deleted
     */
    boolean existsByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find strategy by ID and tenant ID
     */
    Optional<Operations> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find strategy by ID and tenant ID and not deleted
     */
    Optional<Operations> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find strategy by owner ID
     */
    Optional<Operations> findByOwnerId(String ownerId);

    /**
     * Find strategy by owner ID and not deleted
     */
    Optional<Operations> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count operationss by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count operationss by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
