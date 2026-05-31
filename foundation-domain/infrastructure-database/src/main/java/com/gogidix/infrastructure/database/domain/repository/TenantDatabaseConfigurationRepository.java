package com.gogidix.infrastructure.database.domain.repository;

import com.gogidix.infrastructure.database.domain.model.TenantDatabaseConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing tenant database configurations.
 */
@Repository
public interface TenantDatabaseConfigurationRepository extends MongoRepository<TenantDatabaseConfiguration, String> {

    /**
     * Find by tenant ID.
     */
    Optional<TenantDatabaseConfiguration> findByTenantId(String tenantId);

    /**
     * Find by tenant ID and environment.
     */
    Optional<TenantDatabaseConfiguration> findByTenantIdAndEnvironment(
            String tenantId,
            TenantDatabaseConfiguration.Environment environment);

    /**
     * Find all by environment.
     */
    List<TenantDatabaseConfiguration> findAllByEnvironment(TenantDatabaseConfiguration.Environment environment);

    /**
     * Find all by status.
     */
    List<TenantDatabaseConfiguration> findAllByStatus(TenantDatabaseConfiguration.TenantStatus status);

    /**
     * Find all by strategy.
     */
    List<TenantDatabaseConfiguration> findAllByStrategy(TenantDatabaseConfiguration.TenancyStrategy strategy);

    /**
     * Find by database name.
     */
    List<TenantDatabaseConfiguration> findAllByDatabaseName(String databaseName);

    /**
     * Find by connection pool name.
     */
    List<TenantDatabaseConfiguration> findAllByConnectionPoolName(String connectionPoolName);

    /**
     * Find all active tenants.
     */
    List<TenantDatabaseConfiguration> findAllByIsActiveAndStatus(Boolean isActive,
            TenantDatabaseConfiguration.TenantStatus status);

    /**
     * Find by tier.
     */
    List<TenantDatabaseConfiguration> findAllByTier(TenantDatabaseConfiguration.TenantTier tier);

    /**
     * Find expired tenants.
     */
    @Query("{'validUntil': {$lt: ?0}}")
    List<TenantDatabaseConfiguration> findExpiredTenants(LocalDateTime now);

    /**
     * Find tenants to deprovision.
     */
    @Query("{'status': 'DEPROVISIONING', 'isActive': false}")
    List<TenantDatabaseConfiguration> findTenantsToDeprovision();

    /**
     * Count by status.
     */
    Long countByStatus(TenantDatabaseConfiguration.TenantStatus status);

    /**
     * Count by tier.
     */
    Long countByTier(TenantDatabaseConfiguration.TenantTier tier);

    /**
     * Count by strategy.
     */
    Long countByStrategy(TenantDatabaseConfiguration.TenancyStrategy strategy);

    /**
     * Count active tenants.
     */
    Long countByIsActiveAndStatus(Boolean isActive, TenantDatabaseConfiguration.TenantStatus status);

    /**
     * Delete by tenant ID.
     */
    void deleteByTenantId(String tenantId);

    /**
     * Exists by tenant ID.
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Find by tenant ID and status.
     */
    Optional<TenantDatabaseConfiguration> findByTenantIdAndStatus(
            String tenantId,
            TenantDatabaseConfiguration.TenantStatus status);

    /**
     * Find all paginated.
     */
    Page<TenantDatabaseConfiguration> findAll(Pageable pageable);

    /**
     * Find by owner.
     */
    List<TenantDatabaseConfiguration> findAllByOwner(String owner);

    /**
     * Find by tags containing.
     */
    @Query("{'tags': {$in: ?0}}")
    List<TenantDatabaseConfiguration> findByTagsContaining(List<String> tags);

    /**
     * Count total connections used by tenant.
     */
    @Query(value = "{'tenantId': ?0, 'isActive': true}", fields = "{'currentConnections': 1}")
    List<TenantDatabaseConfiguration> findConnectionUsageByTenant(String tenantId);
}
