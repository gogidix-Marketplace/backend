package com.gogidix.customersupport.feedback.domain.repository;

import com.gogidix.customersupport.feedback.domain.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Feedback aggregate
 */
@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {

    Optional<Feedback> findByFeedbackId(String feedbackId);

    List<Feedback> findByTenantId(String tenantId);

    Page<Feedback> findByTenantId(String tenantId, Pageable pageable);

    List<Feedback> findByTenantIdAndTicketId(String tenantId, String ticketId);

    List<Feedback> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Feedback> findByTenantIdAndAgentId(String tenantId, String agentId);

    List<Feedback> findByTenantIdAndFeedbackType(String tenantId, Feedback.FeedbackType feedbackType);

    List<Feedback> findByTenantIdAndReviewed(String tenantId, boolean reviewed);

    List<Feedback> findByTenantIdAndFollowUpRequired(String tenantId, boolean followUpRequired);

    List<Feedback> findByTenantIdAndFeedbackTypeAndCreatedAtBetween(
            String tenantId, Feedback.FeedbackType feedbackType, Instant startDate, Instant endDate);

    @Query("{'tenantId': ?0, 'rating': {$gte: ?1, $lte: ?2}}")
    List<Feedback> findByTenantIdAndRatingBetween(String tenantId, Integer minRating, Integer maxRating);

    @Query("{'tenantId': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<Feedback> findByTenantIdAndCreatedAtBetween(String tenantId, Instant startDate, Instant endDate);

    long countByTenantIdAndFeedbackType(String tenantId, Feedback.FeedbackType feedbackType);

    long countByTenantIdAndReviewed(String tenantId, boolean reviewed);

    @Query(value = "{'tenantId': ?0}", count = true)
    long countByTenantId(String tenantId);

    double getAverageRatingByTenantIdAndAgentId(String tenantId, String agentId);

    @Query("{'tenantId': ?0, 'agentId': ?1}")
    List<Feedback> findByTenantIdAndAgentIdOrderByCreatedAtDesc(String tenantId, String agentId);

    @Query("{'tenantId': ?0, 'sentiment': ?1}")
    List<Feedback> findByTenantIdAndSentiment(String tenantId, Feedback.SentimentType sentiment);

    void deleteByTenantIdAndFeedbackId(String tenantId, String feedbackId);
}
