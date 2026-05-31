package com.gogidix.management.executive.audit.domain.repository;

import com.gogidix.management.executive.audit.domain.model.Audit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Audit entities
 */
@Repository
public interface AuditRepository extends MongoRepository<Audit, String> {

    /**
     * Find audit by tenant ID
     */
    Optional<Audit> findByTenantId(String tenantId);

    /**
     * Find all audits by tenant ID
     */
    List<Audit> findAllByTenantId(String tenantId);

    /**
     * Find all audits by tenant ID and not deleted
     */
    List<Audit> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find audits by tenant ID and not deleted
     */
    List<Audit> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find audits by tenant ID with pagination
     */
    Page<Audit> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find audits by tenant ID and not deleted with pagination
     */
    Page<Audit> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

    /**
     * Check if audit exists by tenant ID
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Check if audit exists by tenant ID and not deleted
     */
    boolean existsByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Check if audit exists by ID and tenant ID and not deleted
     */
    boolean existsByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find audit by ID and tenant ID
     */
    Optional<Audit> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find audit by ID and tenant ID and not deleted
     */
    Optional<Audit> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find audit by owner ID
     */
    Optional<Audit> findByOwnerId(String ownerId);

    /**
     * Find audit by owner ID and not deleted
     */
    Optional<Audit> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count audits by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count audits by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
