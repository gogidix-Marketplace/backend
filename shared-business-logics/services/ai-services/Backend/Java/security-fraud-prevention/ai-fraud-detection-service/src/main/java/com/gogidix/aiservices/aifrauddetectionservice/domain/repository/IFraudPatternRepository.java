package com.gogidix.aiservices.aifrauddetectionservice.domain.repository;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for FraudPattern aggregate persistence.
 * Extends Spring Data MongoDB repository for standard CRUD operations.
 * Uses hexagonal architecture - this is the domain repository interface.
 */
@Repository
public interface IFraudPatternRepository extends MongoRepository<FraudPattern, String> {

    /**
     * Find pattern by pattern ID.
     */
    Optional<FraudPattern> findByPatternId(String patternId);

    /**
     * Find all active patterns for a tenant ordered by last seen.
     */
    List<FraudPattern> findByTenantIdAndIsActiveTrueOrderByLastSeenDesc(String tenantId);

    /**
     * Find all active patterns across all tenants.
     */
    List<FraudPattern> findByIsActiveTrueOrderByLastSeenDesc();

    /**
     * Find patterns by name pattern (case-insensitive search).
     */
    List<FraudPattern> findByPatternNameContainingIgnoreCase(String patternName);

    /**
     * Find patterns with confidence score above threshold.
     */
    List<FraudPattern> findByConfidenceScoreGreaterThanEqualOrderByConfidenceScoreDesc(double threshold);

    /**
     * Find high-confidence active patterns for a tenant.
     */
    List<FraudPattern> findByTenantIdAndIsActiveTrueAndConfidenceScoreGreaterThanEqualOrderByConfidenceScoreDesc(
            String tenantId, double threshold);

    /**
     * Count all patterns for a tenant.
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
    List<FraudPattern> findByHighOccurrenceCount(int threshold);

    /**
     * Find recently seen patterns for a tenant since a given time.
     */
    @Query("{ 'tenantId': ?0, 'isActive': true, 'lastSeen': { $gte: ?1 } }")
    List<FraudPattern> findRecentPatternsForTenant(String tenantId, Instant since);

    /**
     * Find patterns by pattern type.
     */
    @Query("{ 'patternType': ?0, 'isActive': true }")
    List<FraudPattern> findActivePatternsByType(String patternType);

    /**
     * Find all inactive (deactivated) patterns for a tenant.
     */
    List<FraudPattern> findByTenantIdAndIsActiveFalse(String tenantId);

    /**
     * Find patterns created within a date range.
     */
    List<FraudPattern> findByCreatedAtBetween(Instant start, Instant end);

    /**
     * Update pattern active status by pattern ID.
     */
    @Query("{ 'patternId': ?0 }, { $set: { 'isActive': ?1, 'updatedAt': ?2 } }")
    void updatePatternStatus(String patternId, boolean isActive, Instant updatedAt);
}
