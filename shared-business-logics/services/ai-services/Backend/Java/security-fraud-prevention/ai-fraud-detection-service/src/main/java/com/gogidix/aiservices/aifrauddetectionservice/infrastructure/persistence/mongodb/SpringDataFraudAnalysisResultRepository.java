package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data MongoDB repository for FraudAnalysisResultEntity.
 * Provides database operations for fraud analysis result persistence.
 */
@Repository
public interface SpringDataFraudAnalysisResultRepository extends MongoRepository<FraudAnalysisResultEntity, String> {

    /**
     * Find analysis result by analysis ID.
     */
    FraudAnalysisResultEntity findByAnalysisId(String analysisId);

    /**
     * Find all analysis results for a specific user.
     */
    List<FraudAnalysisResultEntity> findByUserIdOrderByTimestampDesc(String userId);

    /**
     * Find analysis results for a user with limit.
     */
    List<FraudAnalysisResultEntity> findByUserIdOrderByTimestampDesc(String userId, org.springframework.data.domain.Pageable pageable);

    /**
     * Find analysis results for a specific tenant.
     */
    List<FraudAnalysisResultEntity> findByTenantIdOrderByTimestampDesc(String tenantId);

    /**
     * Find analysis results by tenant with pagination.
     */
    List<FraudAnalysisResultEntity> findByTenantIdOrderByTimestampDesc(String tenantId, org.springframework.data.domain.Pageable pageable);

    /**
     * Find analysis results for a transaction.
     */
    List<FraudAnalysisResultEntity> findByTransactionIdOrderByTimestampDesc(String transactionId);

    /**
     * Find high-risk fraud scores for a user.
     */
    List<FraudAnalysisResultEntity> findByUserIdAndFraudScoreGreaterThanOrderByTimestampDesc(String userId, double threshold);

    /**
     * Find analyses older than specified date (for cleanup).
     */
    List<FraudAnalysisResultEntity> findByTimestampBefore(Instant cutoff);

    /**
     * Count analyses for a user.
     */
    long countByUserId(String userId);

    /**
     * Count analyses for a tenant.
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
    List<FraudAnalysisResultEntity> findByTenantIdAndTimestampBetween(String tenantId, Instant start, Instant end);

    /**
     * Delete analysis result by analysis ID.
     */
    void deleteByAnalysisId(String analysisId);
}
