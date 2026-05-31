package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for FraudPatternEntity.
 * Provides database operations for fraud pattern persistence.
 */
@Repository
public interface SpringDataFraudPatternRepository extends MongoRepository<FraudPatternEntity, String> {

    /**
     * Find pattern by pattern ID.
     */
    FraudPatternEntity findByPatternId(String patternId);

    /**
     * Find all active patterns for a tenant.
     */
    List<FraudPatternEntity> findByTenantIdAndIsActiveTrueOrderByLastSeenDesc(String tenantId);

    /**
     * Find all active patterns (all tenants).
     */
    List<FraudPatternEntity> findByIsActiveTrueOrderByLastSeenDesc();

    /**
     * Find patterns by name pattern (case-insensitive).
     */
    List<FraudPatternEntity> findByPatternNameContainingIgnoreCase(String patternName);

    /**
     * Find patterns with confidence score above threshold.
     */
    List<FraudPatternEntity> findByConfidenceScoreGreaterThanEqualOrderByConfidenceScoreDesc(double threshold);

    /**
     * Find high-confidence patterns for a tenant.
     */
    List<FraudPatternEntity> findByTenantIdAndIsActiveTrueAndConfidenceScoreGreaterThanEqualOrderByConfidenceScoreDesc(
            String tenantId, double threshold);

    /**
     * Count patterns for a tenant.
     */
    long countByTenantId(String tenantId);

    /**
     * Count active patterns for a tenant.
     */
    long countByTenantIdAndIsActiveTrue(String tenantId);

    /**
     * Custom query to find patterns with occurrence count above threshold.
     */
    @Query("{ 'occurrenceCount': { $gte: ?0 } }")
    List<FraudPatternEntity> findByHighOccurrenceCount(int threshold);

    /**
     * Find recently seen patterns for a tenant.
     */
    @Query("{ 'tenantId': ?0, 'isActive': true, 'lastSeen': { $gte: ?1 } }")
    List<FraudPatternEntity> findRecentPatternsForTenant(String tenantId, java.time.Instant since);
}
