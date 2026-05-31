package com.gogidix.management.executive.workflow.domain.repository;

import com.gogidix.management.executive.workflow.domain.model.Workflow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Workflow entities
 */
@Repository
public interface WorkflowRepository extends MongoRepository<Workflow, String> {

    /**
     * Find strategy by tenant ID
     */
    Optional<Workflow> findByTenantId(String tenantId);

    /**
     * Find all workflows by tenant ID
     */
    List<Workflow> findAllByTenantId(String tenantId);

    /**
     * Find all workflows by tenant ID and not deleted
     */
    List<Workflow> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find workflows by tenant ID and not deleted
     */
    List<Workflow> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find workflows by tenant ID with pagination
     */
    Page<Workflow> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find workflows by tenant ID and not deleted with pagination
     */
    Page<Workflow> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

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
    Optional<Workflow> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find strategy by ID and tenant ID and not deleted
     */
    Optional<Workflow> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find strategy by owner ID
     */
    Optional<Workflow> findByOwnerId(String ownerId);

    /**
     * Find strategy by owner ID and not deleted
     */
    Optional<Workflow> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count workflows by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count workflows by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
