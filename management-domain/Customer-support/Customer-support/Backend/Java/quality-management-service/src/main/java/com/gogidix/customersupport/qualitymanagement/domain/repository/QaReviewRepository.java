package com.gogidix.customersupport.qualitymanagement.domain.repository;

import com.gogidix.customersupport.qualitymanagement.domain.model.QaReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QaReview entity
 */
@Repository
public interface QaReviewRepository extends MongoRepository<QaReview, String> {

    /**
     * Find review by review ID
     */
    Optional<QaReview> findByReviewId(String reviewId);

    /**
     * Find reviews by tenant ID
     */
    List<QaReview> findByTenantIdOrderByReviewDateDesc(String tenantId);

    /**
     * Find reviews by tenant ID with pagination
     */
    Page<QaReview> findByTenantIdOrderByReviewDateDesc(String tenantId, Pageable pageable);

    /**
     * Find reviews by agent ID
     */
    List<QaReview> findByTenantIdAndAgentIdOrderByReviewDateDesc(String tenantId, String agentId);

    /**
     * Find reviews by reviewer ID
     */
    List<QaReview> findByTenantIdAndReviewerIdOrderByReviewDateDesc(String tenantId, String reviewerId);

    /**
     * Find reviews by ticket ID
     */
    List<QaReview> findByTenantIdAndTicketId(String tenantId, String ticketId);

    /**
     * Find reviews by status
     */
    List<QaReview> findByTenantIdAndReviewStatusOrderByReviewDateDesc(String tenantId, QaReview.ReviewStatus reviewStatus);

    /**
     * Find reviews by review type
     */
    List<QaReview> findByTenantIdAndReviewTypeOrderByReviewDateDesc(String tenantId, QaReview.ReviewType reviewType);

    /**
     * Find pending reviews
     */
    List<QaReview> findByTenantIdAndReviewStatusInOrderByReviewDateAsc(
            String tenantId, List<QaReview.ReviewStatus> statuses);

    /**
     * Find overdue reviews
     */
    @Query("{ 'tenantId': ?0, 'reviewStatus': { $in: ['PENDING', 'IN_PROGRESS'] }, 'dueDate': { $lt: ?1 } }")
    List<QaReview> findOverdueReviews(String tenantId, Instant now);

    /**
     * Find reviews by date range
     */
    List<QaReview> findByTenantIdAndReviewDateBetweenOrderByReviewDateDesc(
            String tenantId, Instant startDate, Instant endDate);

    /**
     * Find reviews by scorecard template
     */
    List<QaReview> findByTenantIdAndScorecardTemplateIdOrderByReviewDateDesc(
            String tenantId, String templateId);

    /**
     * Find calibrated reviews
     */
    List<QaReview> findByTenantIdAndIsCalibratedTrueOrderByReviewDateDesc(String tenantId);

    /**
     * Find reviews requiring escalation
     */
    List<QaReview> findByTenantIdAndRequiresEscalationTrueOrderByReviewDateDesc(String tenantId);

    /**
     * Find reviews requiring follow-up
     */
    List<QaReview> findByTenantIdAndRequiresFollowUpTrueAndFollowUpCompletedFalseOrderByReviewDateDesc(String tenantId);

    /**
     * Count reviews by agent
     */
    long countByTenantIdAndAgentId(String tenantId, String agentId);

    /**
     * Count reviews by status
     */
    long countByTenantIdAndReviewStatus(String tenantId, QaReview.ReviewStatus reviewStatus);

    /**
     * Find completed reviews for agent in date range
     */
    @Query("{ 'tenantId': ?0, 'agentId': ?1, 'reviewStatus': 'COMPLETED', 'reviewDate': { $gte: ?2, $lte: ?3 } }")
    List<QaReview> findCompletedReviewsForAgentInDateRange(String tenantId, String agentId, Instant startDate, Instant endDate);

    /**
     * Calculate average score for agent
     */
    @Query(value = "{ 'tenantId': ?0, 'agentId': ?1, 'reviewStatus': 'COMPLETED' }", fields = "{ 'percentageScore': 1 }")
    List<QaReview> findCompletedReviewsForAgentScoring(String tenantId, String agentId);

    /**
     * Find reviews by batch ID
     */
    List<QaReview> findByTenantIdAndBatchIdOrderByReviewDateDesc(String tenantId, String batchId);

    /**
     * Find reviews by calibration session
     */
    List<QaReview> findByTenantIdAndCalibrationSessionIdOrderByReviewDateDesc(String tenantId, String sessionId);

    /**
     * Delete reviews older than specified date
     */
    void deleteByTenantIdAndUpdatedAtBefore(String tenantId, Instant cutoffDate);

    /**
     * Find reviews by tags
     */
    @Query("{ 'tenantId': ?0, 'tags': { $in: ?1 } }")
    List<QaReview> findByTenantIdAndTagsContaining(String tenantId, List<String> tags);

    /**
     * Find recent reviews for multiple agents
     */
    @Query("{ 'tenantId': ?0, 'agentId': { $in: ?1 }, 'reviewStatus': 'COMPLETED' }")
    List<QaReview> findRecentReviewsForAgents(String tenantId, List<String> agentIds);

    /**
     * Find reviews with scores below threshold
     */
    @Query("{ 'tenantId': ?0, 'reviewStatus': 'COMPLETED', 'percentageScore': { $lt: ?1 } }")
    List<QaReview> findReviewsBelowScoreThreshold(String tenantId, double threshold);

    /**
     * Find passed/failed reviews for agent
     */
    List<QaReview> findByTenantIdAndAgentIdAndReviewStatusAndPassedOrderByReviewDateDesc(
            String tenantId, String agentId, QaReview.ReviewStatus reviewStatus, Boolean passed);

    /**
     * Get agent performance summary
     */
    @Query("{ 'tenantId': ?0, 'agentId': ?1, 'reviewStatus': 'COMPLETED' }")
    List<QaReview> getAgentPerformanceReviews(String tenantId, String agentId);

    /**
     * Find reviews by channel type
     */
    List<QaReview> findByTenantIdAndChannelTypeOrderByReviewDateDesc(
            String tenantId, QaReview.ChannelType channelType);
}
