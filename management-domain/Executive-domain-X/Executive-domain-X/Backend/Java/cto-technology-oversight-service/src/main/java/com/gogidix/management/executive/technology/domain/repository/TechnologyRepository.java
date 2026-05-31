package com.gogidix.management.executive.technology.domain.repository;

import com.gogidix.management.executive.technology.domain.model.Technology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Technology entities
 */
@Repository
public interface TechnologyRepository extends MongoRepository<Technology, String> {

    /**
     * Find strategy by tenant ID
     */
    Optional<Technology> findByTenantId(String tenantId);

    /**
     * Find all technologys by tenant ID
     */
    List<Technology> findAllByTenantId(String tenantId);

    /**
     * Find all technologys by tenant ID and not deleted
     */
    List<Technology> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find technologys by tenant ID and not deleted
     */
    List<Technology> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find technologys by tenant ID with pagination
     */
    Page<Technology> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find technologys by tenant ID and not deleted with pagination
     */
    Page<Technology> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

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
    Optional<Technology> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find strategy by ID and tenant ID and not deleted
     */
    Optional<Technology> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find strategy by owner ID
     */
    Optional<Technology> findByOwnerId(String ownerId);

    /**
     * Find strategy by owner ID and not deleted
     */
    Optional<Technology> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count technologys by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count technologys by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
