package com.gogidix.management.executive.analytics.domain.repository;

import com.gogidix.management.executive.analytics.domain.model.Analytics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Analytics entities
 */
@Repository
public interface AnalyticsRepository extends MongoRepository<Analytics, String> {

    /**
     * Find analytics by tenant ID
     */
    Optional<Analytics> findByTenantId(String tenantId);

    /**
     * Find all dashboards by tenant ID
     */
    List<Analytics> findAllByTenantId(String tenantId);

    /**
     * Find all dashboards by tenant ID and not deleted
     */
    List<Analytics> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find dashboards by tenant ID and not deleted
     */
    List<Analytics> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find dashboards by tenant ID with pagination
     */
    Page<Analytics> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find dashboards by tenant ID and not deleted with pagination
     */
    Page<Analytics> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

    /**
     * Check if analytics exists by tenant ID
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Check if analytics exists by tenant ID and not deleted
     */
    boolean existsByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Check if analytics exists by ID and tenant ID and not deleted
     */
    boolean existsByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find analytics by ID and tenant ID
     */
    Optional<Analytics> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find analytics by ID and tenant ID and not deleted
     */
    Optional<Analytics> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find analytics by owner ID
     */
    Optional<Analytics> findByOwnerId(String ownerId);

    /**
     * Find analytics by owner ID and not deleted
     */
    Optional<Analytics> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count dashboards by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count dashboards by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
