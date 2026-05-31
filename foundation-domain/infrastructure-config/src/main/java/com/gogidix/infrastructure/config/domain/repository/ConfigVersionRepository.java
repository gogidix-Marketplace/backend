package com.gogidix.infrastructure.config.domain.repository;

import com.gogidix.infrastructure.config.domain.model.ConfigVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB repository for ConfigVersion entities.
 */
@Repository
public interface ConfigVersionRepository extends MongoRepository<ConfigVersion, String> {

    /**
     * Find all versions for a specific configuration.
     */
    List<ConfigVersion> findByConfigId(String configId);

    /**
     * Find all versions for a specific configuration with pagination.
     */
    Page<ConfigVersion> findByConfigId(String configId, Pageable pageable);

    /**
     * Find all versions for a specific configuration ordered by version number descending.
     */
    List<ConfigVersion> findByConfigIdOrderByVersionDesc(String configId);

    /**
     * Find a specific version of a configuration.
     */
    ConfigVersion findByConfigIdAndVersion(String configId, Integer version);

    /**
     * Find latest version of a configuration.
     */
    ConfigVersion findFirstByConfigIdOrderByVersionDesc(String configId);

    /**
     * Find all versions by tenant ID.
     */
    List<ConfigVersion> findByTenantId(String tenantId);

    /**
     * Find versions by tenant ID and config type.
     */
    List<ConfigVersion> findByTenantIdAndConfigType(String tenantId, ConfigVersion.ConfigType configType);

    /**
     * Find versions by tenant ID, config type, and config key.
     */
    List<ConfigVersion> findByTenantIdAndConfigTypeAndConfigKeyOrderByVersionDesc(
            String tenantId, ConfigVersion.ConfigType configType, String configKey);

    /**
     * Find versions by tenant ID and changed by user.
     */
    List<ConfigVersion> findByTenantIdAndChangedBy(String tenantId, String changedBy);

    /**
     * Find versions changed within a time range.
     */
    List<ConfigVersion> findByTenantIdAndChangedAtBetween(
            String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Find versions by change type.
     */
    List<ConfigVersion> findByTenantIdAndChangeType(String tenantId, ConfigVersion.ChangeType changeType);

    /**
     * Count versions for a configuration.
     */
    long countByConfigId(String configId);

    /**
     * Delete all versions for a configuration.
     */
    void deleteByConfigId(String configId);

    /**
     * Delete all versions for a tenant.
     */
    void deleteByTenantId(String tenantId);

    /**
     * Find versions before a specific timestamp.
     */
    List<ConfigVersion> findByConfigIdAndChangedAtBefore(String configId, LocalDateTime timestamp);

    /**
     * Find rollback-eligible versions for a configuration.
     */
    @Query("{ 'configId': ?0, 'canRollback': true }")
    List<ConfigVersion> findRollbackEligibleVersions(String configId);

    /**
     * Find recent versions by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'changedAt': { $gte: ?1 } }")
    List<ConfigVersion> findRecentVersions(String tenantId, LocalDateTime since);

    /**
     * Search versions by config key pattern.
     */
    @Query("{ 'tenantId': ?0, 'configKey': { $regex: ?1, $options: 'i' } }")
    List<ConfigVersion> searchByConfigKey(String tenantId, String pattern);
}
