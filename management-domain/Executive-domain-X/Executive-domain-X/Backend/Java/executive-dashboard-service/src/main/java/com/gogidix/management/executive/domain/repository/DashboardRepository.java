package com.gogidix.management.executive.domain.repository;

import com.gogidix.management.executive.domain.model.Dashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Dashboard entities
 */
@Repository
public interface DashboardRepository extends MongoRepository<Dashboard, String> {

    /**
     * Find dashboard by tenant ID
     */
    Optional<Dashboard> findByTenantId(String tenantId);

    /**
     * Find all dashboards by tenant ID
     */
    List<Dashboard> findAllByTenantId(String tenantId);

    /**
     * Find all dashboards by tenant ID and not deleted
     */
    List<Dashboard> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find dashboards by tenant ID and not deleted
     */
    List<Dashboard> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find dashboards by tenant ID with pagination
     */
    Page<Dashboard> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find dashboards by tenant ID and not deleted with pagination
     */
    Page<Dashboard> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

    /**
     * Check if dashboard exists by tenant ID
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Check if dashboard exists by tenant ID and not deleted
     */
    boolean existsByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Check if dashboard exists by ID and tenant ID and not deleted
     */
    boolean existsByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find dashboard by ID and tenant ID
     */
    Optional<Dashboard> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find dashboard by ID and tenant ID and not deleted
     */
    Optional<Dashboard> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find dashboard by owner ID
     */
    Optional<Dashboard> findByOwnerId(String ownerId);

    /**
     * Find dashboard by owner ID and not deleted
     */
    Optional<Dashboard> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count dashboards by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count dashboards by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
