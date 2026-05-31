package com.gogidix.management.executive.strategy.domain.repository;

import com.gogidix.management.executive.strategy.domain.model.Strategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Strategy entities
 */
@Repository
public interface StrategyRepository extends MongoRepository<Strategy, String> {

    /**
     * Find strategy by tenant ID
     */
    Optional<Strategy> findByTenantId(String tenantId);

    /**
     * Find all dashboards by tenant ID
     */
    List<Strategy> findAllByTenantId(String tenantId);

    /**
     * Find all dashboards by tenant ID and not deleted
     */
    List<Strategy> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find dashboards by tenant ID and not deleted
     */
    List<Strategy> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find dashboards by tenant ID with pagination
     */
    Page<Strategy> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find dashboards by tenant ID and not deleted with pagination
     */
    Page<Strategy> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

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
    Optional<Strategy> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find strategy by ID and tenant ID and not deleted
     */
    Optional<Strategy> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find strategy by owner ID
     */
    Optional<Strategy> findByOwnerId(String ownerId);

    /**
     * Find strategy by owner ID and not deleted
     */
    Optional<Strategy> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count dashboards by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count dashboards by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
