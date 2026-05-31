package com.gogidix.management.executive.alert.domain.repository;

import com.gogidix.management.executive.alert.domain.model.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Alert entities
 */
@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {

    /**
     * Find strategy by tenant ID
     */
    Optional<Alert> findByTenantId(String tenantId);

    /**
     * Find all alerts by tenant ID
     */
    List<Alert> findAllByTenantId(String tenantId);

    /**
     * Find all alerts by tenant ID and not deleted
     */
    List<Alert> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find alerts by tenant ID and not deleted
     */
    List<Alert> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find alerts by tenant ID with pagination
     */
    Page<Alert> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find alerts by tenant ID and not deleted with pagination
     */
    Page<Alert> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

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
    Optional<Alert> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find strategy by ID and tenant ID and not deleted
     */
    Optional<Alert> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find strategy by owner ID
     */
    Optional<Alert> findByOwnerId(String ownerId);

    /**
     * Find strategy by owner ID and not deleted
     */
    Optional<Alert> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count alerts by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count alerts by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
