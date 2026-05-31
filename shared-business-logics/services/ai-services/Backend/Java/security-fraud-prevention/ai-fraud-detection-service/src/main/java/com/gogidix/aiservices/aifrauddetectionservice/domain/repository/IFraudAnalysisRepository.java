package com.gogidix.aiservices.aifrauddetectionservice.domain.repository;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for FraudAnalysis aggregate persistence.
 * Extends Spring Data MongoDB repository for standard CRUD operations.
 * Uses hexagonal architecture - this is the domain repository interface.
 */
@Repository
public interface IFraudAnalysisRepository extends MongoRepository<FraudAnalysisResult, String> {

    /**
     * Find analysis result by analysis ID.
     */
    Optional<FraudAnalysisResult> findByAnalysisId(String analysisId);

    /**
     * Find all analysis results for a specific user ordered by timestamp.
     */
    List<FraudAnalysisResult> findByUserIdOrderByTimestampDesc(String userId);

    /**
     * Find analysis results for a user with pagination support.
     */
    List<FraudAnalysisResult> findByUserIdOrderByTimestampDesc(String userId, org.springframework.data.domain.Pageable pageable);

    /**
     * Find analysis results for a specific tenant ordered by timestamp.
     */
    List<FraudAnalysisResult> findByTenantIdOrderByTimestampDesc(String tenantId);

    /**
     * Find analysis results for a tenant with pagination.
     */
    List<FraudAnalysisResult> findByTenantIdOrderByTimestampDesc(String tenantId, org.springframework.data.domain.Pageable pageable);

    /**
     * Find all analysis results for a specific transaction.
     */
    List<FraudAnalysisResult> findByTransactionIdOrderByTimestampDesc(String transactionId);

    /**
     * Find high-risk fraud scores for a user (above threshold).
     */
    List<FraudAnalysisResult> findByUserIdAndFraudScoreGreaterThanOrderByTimestampDesc(String userId, double threshold);

    /**
     * Find analyses older than specified date for cleanup/archival.
     */
    List<FraudAnalysisResult> findByTimestampBefore(Instant cutoff);

    /**
     * Count analyses for a specific user.
     */
    long countByUserId(String userId);

    /**
     * Count analyses for a specific tenant.
     */
    long countByTenantId(String tenantId);

    /**
     * Delete analyses older than specified date.
     */
    void deleteByTimestampBefore(Instant cutoff);

    /**
     * Custom query to find active analyses for a tenant within date range.
     */
    @Query("{ 'tenantId': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } }")
    List<FraudAnalysisResult> findByTenantIdAndTimestampBetween(String tenantId, Instant start, Instant end);

    /**
     * Find analyses by risk level.
     */
    List<FraudAnalysisResult> findByRiskLevel(com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel riskLevel);

    /**
     * Find recent analyses for a tenant within last N hours.
     */
    @Query("{ 'tenantId': ?0, 'timestamp': { $gte: ?1 } }")
    List<FraudAnalysisResult> findRecentAnalysesForTenant(String tenantId, Instant since);

    /**
     * Find analyses with fraud score above a threshold.
     */
    List<FraudAnalysisResult> findByFraudScoreGreaterThanEqual(double threshold);

    /**
     * Delete analysis result by analysis ID.
     */
    void deleteByAnalysisId(String analysisId);
}
