package com.gogidix.infrastructure.config.domain.repository;

import com.gogidix.infrastructure.config.domain.model.ConfigurationProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for ConfigurationProperty entities.
 */
@Repository
public interface ConfigurationPropertyRepository extends MongoRepository<ConfigurationProperty, String> {

    /**
     * Find all configuration properties by tenant ID.
     */
    List<ConfigurationProperty> findByTenantId(String tenantId);

    /**
     * Find configuration properties by tenant ID with pagination.
     */
    Page<ConfigurationProperty> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find configuration property by tenant ID and key.
     */
    Optional<ConfigurationProperty> findByTenantIdAndKey(String tenantId, String key);

    /**
     * Find configuration properties by tenant ID and environment.
     */
    List<ConfigurationProperty> findByTenantIdAndEnvironment(String tenantId,
            ConfigurationProperty.Environment environment);

    /**
     * Find configuration properties by tenant ID, environment, and active status.
     */
    List<ConfigurationProperty> findByTenantIdAndEnvironmentAndIsActive(
            String tenantId, ConfigurationProperty.Environment environment, boolean isActive);

    /**
     * Find configuration properties by tenant ID and category.
     */
    List<ConfigurationProperty> findByTenantIdAndCategory(String tenantId, String category);

    /**
     * Find configuration properties by tenant ID and tags.
     */
    List<ConfigurationProperty> findByTenantIdAndTagsIn(String tenantId, List<String> tags);

    /**
     * Find active configuration properties valid at a given time.
     */
    @Query("{ 'tenantId': ?0, 'isActive': true, $or: [ { 'validUntil': null }, { 'validUntil': { $gt: ?1 } } ] }")
    List<ConfigurationProperty> findActiveConfigurationsAtTime(String tenantId, LocalDateTime time);

    /**
     * Search configuration properties by key pattern.
     */
    @Query("{ 'tenantId': ?0, 'key': { $regex: ?1, $options: 'i' } }")
    List<ConfigurationProperty> searchByKey(String tenantId, String keyPattern);

    /**
     * Find sensitive configuration properties.
     */
    List<ConfigurationProperty> findByTenantIdAndIsSensitiveTrue(String tenantId);

    /**
     * Find configuration properties by owner.
     */
    List<ConfigurationProperty> findByTenantIdAndOwner(String tenantId, String owner);

    /**
     * Count configuration properties by tenant ID.
     */
    long countByTenantId(String tenantId);

    /**
     * Delete all configuration properties for a tenant.
     */
    void deleteByTenantId(String tenantId);

    /**
     * Find configuration properties created after a given timestamp.
     */
    List<ConfigurationProperty> findByTenantIdAndCreatedAtAfter(String tenantId, LocalDateTime timestamp);

    /**
     * Find configuration properties updated after a given timestamp.
     */
    List<ConfigurationProperty> findByTenantIdAndUpdatedAtAfter(String tenantId, LocalDateTime timestamp);
}
