package com.gogidix.infrastructure.database.domain.repository;

import com.gogidix.infrastructure.database.domain.model.ConnectionPoolConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing connection pool configurations.
 */
@Repository
public interface ConnectionPoolConfigurationRepository extends MongoRepository<ConnectionPoolConfiguration, String> {

    /**
     * Find by tenant ID and pool name.
     */
    Optional<ConnectionPoolConfiguration> findByTenantIdAndPoolName(String tenantId, String poolName);

    /**
     * Find all by active status.
     */
    List<ConnectionPoolConfiguration> findAllByIsActive(Boolean isActive);

    /**
     * Find all by tenant ID.
     */
    List<ConnectionPoolConfiguration> findAllByTenantId(String tenantId);

    /**
     * Find by tenant ID and environment.
     */
    List<ConnectionPoolConfiguration> findAllByTenantIdAndEnvironment(String tenantId,
            ConnectionPoolConfiguration.Environment environment);

    /**
     * Find by tenant ID and active status.
     */
    List<ConnectionPoolConfiguration> findAllByTenantIdAndIsActive(String tenantId, Boolean isActive);

    /**
     * Find by tenant ID, environment, and active status.
     */
    List<ConnectionPoolConfiguration> findAllByTenantIdAndEnvironmentAndIsActive(
            String tenantId,
            ConnectionPoolConfiguration.Environment environment,
            Boolean isActive);

    /**
     * Find by tenant ID and status.
     */
    List<ConnectionPoolConfiguration> findAllByTenantIdAndStatus(
            String tenantId,
            ConnectionPoolConfiguration.PoolStatus status);

    /**
     * Find by database type.
     */
    List<ConnectionPoolConfiguration> findAllByDatabaseType(ConnectionPoolConfiguration.DatabaseType databaseType);

    /**
     * Find pools needing health check.
     */
    @Query("{'isActive': true, 'healthCheckConfig.enabled': true, 'updatedAt': {$lt: ?0}}")
    List<ConnectionPoolConfiguration> findPoolsNeedingHealthCheck(LocalDateTime cutoffTime);

    /**
     * Count by tenant ID.
     */
    Long countByTenantId(String tenantId);

    /**
     * Count by tenant ID and status.
     */
    Long countByTenantIdAndStatus(String tenantId, ConnectionPoolConfiguration.PoolStatus status);

    /**
     * Delete by tenant ID and pool name.
     */
    void deleteByTenantIdAndPoolName(String tenantId, String poolName);

    /**
     * Exists by tenant ID and pool name.
     */
    boolean existsByTenantIdAndPoolName(String tenantId, String poolName);
}
