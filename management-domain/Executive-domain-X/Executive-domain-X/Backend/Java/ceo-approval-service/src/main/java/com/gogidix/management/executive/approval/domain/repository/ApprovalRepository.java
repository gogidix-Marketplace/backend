package com.gogidix.management.executive.approval.domain.repository;

import com.gogidix.management.executive.approval.domain.model.Approval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Approval entities
 */
@Repository
public interface ApprovalRepository extends MongoRepository<Approval, String> {

    /**
     * Find approval by tenant ID
     */
    Optional<Approval> findByTenantId(String tenantId);

    /**
     * Find all dashboards by tenant ID
     */
    List<Approval> findAllByTenantId(String tenantId);

    /**
     * Find all dashboards by tenant ID and not deleted
     */
    List<Approval> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find dashboards by tenant ID and not deleted
     */
    List<Approval> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find dashboards by tenant ID with pagination
     */
    Page<Approval> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find dashboards by tenant ID and not deleted with pagination
     */
    Page<Approval> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

    /**
     * Check if approval exists by tenant ID
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Check if approval exists by tenant ID and not deleted
     */
    boolean existsByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Check if approval exists by ID and tenant ID and not deleted
     */
    boolean existsByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find approval by ID and tenant ID
     */
    Optional<Approval> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find approval by ID and tenant ID and not deleted
     */
    Optional<Approval> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find approval by owner ID
     */
    Optional<Approval> findByOwnerId(String ownerId);

    /**
     * Find approval by owner ID and not deleted
     */
    Optional<Approval> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count dashboards by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count dashboards by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
