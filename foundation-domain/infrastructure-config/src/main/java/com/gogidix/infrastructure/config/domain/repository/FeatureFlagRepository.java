package com.gogidix.infrastructure.config.domain.repository;

import com.gogidix.infrastructure.config.domain.model.FeatureFlag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for FeatureFlag entities.
 */
@Repository
public interface FeatureFlagRepository extends MongoRepository<FeatureFlag, String> {

    /**
     * Find all feature flags by tenant ID.
     */
    List<FeatureFlag> findByTenantId(String tenantId);

    /**
     * Find feature flags by tenant ID with pagination.
     */
    Page<FeatureFlag> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find feature flag by tenant ID and flag key.
     */
    Optional<FeatureFlag> findByTenantIdAndFlagKey(String tenantId, String flagKey);

    /**
     * Find enabled feature flags by tenant ID.
     */
    List<FeatureFlag> findByTenantIdAndIsEnabledTrue(String tenantId);

    /**
     * Find active feature flags (enabled and not expired) by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'isEnabled': true, $or: [ { 'expiresAt': null }, { 'expiresAt': { $gt: ?1 } } ] }")
    List<FeatureFlag> findActiveFlags(String tenantId, LocalDateTime currentTime);

    /**
     * Find feature flags by tenant ID and rollout strategy.
     */
    List<FeatureFlag> findByTenantIdAndRolloutStrategy(
            String tenantId, FeatureFlag.RolloutStrategy strategy);

    /**
     * Find feature flags by tenant ID and tags.
     */
    List<FeatureFlag> findByTenantIdAndTagsIn(String tenantId, List<String> tags);

    /**
     * Find feature flags by owner.
     */
    List<FeatureFlag> findByTenantIdAndOwner(String tenantId, String owner);

    /**
     * Find feature flags by priority range.
     */
    List<FeatureFlag> findByTenantIdAndPriorityGreaterThanEqualOrderByPriorityDesc(
            String tenantId, Integer priority);

    /**
     * Search feature flags by key or name pattern.
     */
    @Query("{ 'tenantId': ?0, $or: [ { 'flagKey': { $regex: ?1, $options: 'i' } }, { 'name': { $regex: ?1, $options: 'i' } } ] }")
    List<FeatureFlag> searchByKeyOrName(String tenantId, String pattern);

    /**
     * Count feature flags by tenant ID.
     */
    long countByTenantId(String tenantId);

    /**
     * Count enabled feature flags by tenant ID.
     */
    long countByTenantIdAndIsEnabledTrue(String tenantId);

    /**
     * Delete all feature flags for a tenant.
     */
    void deleteByTenantId(String tenantId);

    /**
     * Find feature flags created after a given timestamp.
     */
    List<FeatureFlag> findByTenantIdAndCreatedAtAfter(String tenantId, LocalDateTime timestamp);

    /**
     * Find feature flags that are expiring soon.
     */
    @Query("{ 'tenantId': ?0, 'expiresAt': { $ne: null, $lte: ?1 } }")
    List<FeatureFlag> findFlagsExpiringBefore(String tenantId, LocalDateTime dateTime);

    /**
     * Find feature flags with specific rollout percentage.
     */
    List<FeatureFlag> findByTenantIdAndRolloutStrategyAndRolloutPercentageGreaterThanEqual(
            String tenantId, FeatureFlag.RolloutStrategy strategy, Integer percentage);
}
