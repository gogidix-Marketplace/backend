package com.gogidix.customersupport.qualitymanagement.application.service;

import com.gogidix.customersupport.qualitymanagement.application.dto.QaReviewDto;
import com.gogidix.customersupport.qualitymanagement.application.mapper.QualityManagementMapper;
import com.gogidix.customersupport.qualitymanagement.domain.model.QaReview;
import com.gogidix.customersupport.qualitymanagement.domain.repository.QaReviewRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for QA Review operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QaReviewService {

    private final QaReviewRepository reviewRepository;
    private final QualityManagementMapper mapper;
    private final AgentQualityProfileService profileService;

    private static final String DEFAULT_TENANT_ID = "default";
    private static final double DEFAULT_PASSING_SCORE = 70.0;

    /**
     * Create a new QA review
     */
    @Transactional
    @CacheEvict(value = {"qaReviews", "agentProfiles"}, allEntries = true)
    public QaReviewDto createReview(QaReviewDto.CreateQaReviewRequest request) {
        log.info("Creating QA review for tenant: {}, agent: {}, ticket: {}",
                request.getTenantId(), request.getAgentId(), request.getTicketId());

        QaReview review = mapper.toEntity(request);
        review.setReviewStatus(QaReview.ReviewStatus.PENDING);

        // Set default values
        if (review.getDueDate() == null) {
            review.setDueDate(Instant.now().plusSeconds(86400 * 3)); // 3 days
        }
        if (review.getReviewDate() == null) {
            review.setReviewDate(Instant.now());
        }

        QaReview saved = reviewRepository.save(review);
        log.info("Created QA review with ID: {}", saved.getReviewId());

        return mapper.toDto(saved);
    }

    /**
     * Get review by ID
     */
    @Cacheable(value = "qaReviews", key = "#reviewId")
    public QaReviewDto getReviewById(String reviewId) {
        log.debug("Fetching QA review: {}", reviewId);
        return reviewRepository.findByReviewId(reviewId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("QA review not found: " + reviewId));
    }

    /**
     * Get reviews by agent
     */
    @Cacheable(value = "qaReviews", key = "'agent:' + #tenantId + ':' + #agentId")
    public List<QaReviewDto> getReviewsByAgent(String tenantId, String agentId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        log.debug("Fetching QA reviews for tenant: {}, agent: {}", tenantId, agentId);

        return reviewRepository.findByTenantIdAndAgentIdOrderByReviewDateDesc(tenantId, agentId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get reviews by reviewer
     */
    public List<QaReviewDto> getReviewsByReviewer(String tenantId, String reviewerId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        log.debug("Fetching QA reviews for tenant: {}, reviewer: {}", tenantId, reviewerId);

        return reviewRepository.findByTenantIdAndReviewerIdOrderByReviewDateDesc(tenantId, reviewerId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get reviews by status
     */
    public List<QaReviewDto> getReviewsByStatus(String tenantId, String status) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        QaReview.ReviewStatus reviewStatus = QaReview.ReviewStatus.valueOf(status);

        return reviewRepository.findByTenantIdAndReviewStatusOrderByReviewDateDesc(tenantId, reviewStatus)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get pending reviews
     */
    public List<QaReviewDto> getPendingReviews(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return reviewRepository.findByTenantIdAndReviewStatusInOrderByReviewDateAsc(
                tenantId,
                List.of(QaReview.ReviewStatus.PENDING, QaReview.ReviewStatus.IN_PROGRESS)
        ).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get overdue reviews
     */
    public List<QaReviewDto> getOverdueReviews(String tenantId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return reviewRepository.findOverdueReviews(tenantId, Instant.now())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get reviews by date range
     */
    public List<QaReviewDto> getReviewsByDateRange(String tenantId, Instant startDate, Instant endDate) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return reviewRepository.findByTenantIdAndReviewDateBetweenOrderByReviewDateDesc(tenantId, startDate, endDate)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Update QA review
     */
    @Transactional
    @CacheEvict(value = {"qaReviews", "agentProfiles"}, allEntries = true)
    public QaReviewDto updateReview(String reviewId, QaReviewDto.UpdateQaReviewRequest request) {
        log.info("Updating QA review: {}", reviewId);

        QaReview review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("QA review not found: " + reviewId));

        mapper.updateEntity(review, request);

        // Recalculate scores if criteria scores provided
        if (request.getCriteriaScores() != null && !request.getCriteriaScores().isEmpty()) {
            recalculateScores(review);
        }

        QaReview saved = reviewRepository.save(review);
        log.info("Updated QA review: {}", reviewId);

        return mapper.toDto(saved);
    }

    /**
     * Submit/completed QA review
     */
    @Transactional
    @CacheEvict(value = {"qaReviews", "agentProfiles"}, allEntries = true)
    public QaReviewDto submitReview(String reviewId) {
        log.info("Submitting QA review: {}", reviewId);

        QaReview review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("QA review not found: " + reviewId));

        review.setReviewStatus(QaReview.ReviewStatus.COMPLETED);
        review.setCompletedAt(Instant.now());

        // Calculate final scores
        recalculateScores(review);
        review.determinePassStatus(DEFAULT_PASSING_SCORE);
        review.countCriticalFailures();

        QaReview saved = reviewRepository.save(review);

        // Trigger agent profile recalculation
        profileService.recalculateAgentProfile(review.getTenantId(), review.getAgentId());

        log.info("Submitted QA review: {} with score: {}", reviewId, saved.getPercentageScore());

        return mapper.toDto(saved);
    }

    /**
     * Approve QA review
     */
    @Transactional
    @CacheEvict(value = {"qaReviews", "agentProfiles"}, allEntries = true)
    public QaReviewDto approveReview(String reviewId, String approverId) {
        log.info("Approving QA review: {} by: {}", reviewId, approverId);

        QaReview review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("QA review not found: " + reviewId));

        review.setReviewStatus(QaReview.ReviewStatus.APPROVED);
        review.updateTimestamp();

        QaReview saved = reviewRepository.save(review);
        return mapper.toDto(saved);
    }

    /**
     * Reject QA review
     */
    @Transactional
    @CacheEvict(value = {"qaReviews", "agentProfiles"}, allEntries = true)
    public QaReviewDto rejectReview(String reviewId, String reason) {
        log.info("Rejecting QA review: {}, reason: {}", reviewId, reason);

        QaReview review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("QA review not found: " + reviewId));

        review.setReviewStatus(QaReview.ReviewStatus.REJECTED);
        review.setEscalationReason(reason);
        review.updateTimestamp();

        QaReview saved = reviewRepository.save(review);
        return mapper.toDto(saved);
    }

    /**
     * Delete QA review
     */
    @Transactional
    @CacheEvict(value = {"qaReviews", "agentProfiles"}, allEntries = true)
    public void deleteReview(String reviewId) {
        log.info("Deleting QA review: {}", reviewId);

        QaReview review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("QA review not found: " + reviewId));

        reviewRepository.delete(review);
        log.info("Deleted QA review: {}", reviewId);
    }

    /**
     * Get reviews with pagination
     */
    public Page<QaReviewDto> getReviewsPaginated(String tenantId, int page, int size, String sortBy, String sortDir) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return reviewRepository.findByTenantIdOrderByReviewDateDesc(tenantId, pageable)
                .map(mapper::toDto);
    }

    /**
     * Get review summaries for agent
     */
    public List<QaReviewDto.QaReviewSummaryDto> getReviewSummariesForAgent(String tenantId, String agentId) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        return reviewRepository.findByTenantIdAndAgentIdOrderByReviewDateDesc(tenantId, agentId)
                .stream()
                .map(mapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * Get review statistics for tenant
     */
    public QaReviewStatisticsDto getReviewStatistics(String tenantId, Instant startDate, Instant endDate) {
        tenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        List<QaReview> reviews = reviewRepository.findByTenantIdAndReviewDateBetweenOrderByReviewDateDesc(
                tenantId, startDate, endDate);

        QaReviewStatisticsDto stats = new QaReviewStatisticsDto();
        stats.setTotalReviews((long) reviews.size());
        stats.setCompletedReviews(reviews.stream().filter(r -> r.getReviewStatus() == QaReview.ReviewStatus.COMPLETED).count());
        stats.setPendingReviews(reviews.stream().filter(r -> r.getReviewStatus() == QaReview.ReviewStatus.PENDING).count());
        stats.setPassedReviews(reviews.stream().filter(r -> Boolean.TRUE.equals(r.getPassed())).count());
        stats.setFailedReviews(reviews.stream().filter(r -> Boolean.FALSE.equals(r.getPassed())).count());

        double avgScore = reviews.stream()
                .filter(r -> r.getPercentageScore() != null)
                .mapToDouble(QaReview::getPercentageScore)
                .average()
                .orElse(0.0);
        stats.setAverageScore(avgScore);

        return stats;
    }

    /**
     * Recalculate scores for review
     */
    private void recalculateScores(QaReview review) {
        if (review.getCriteriaScores() == null || review.getCriteriaScores().isEmpty()) {
            return;
        }

        // Calculate total score
        double totalScore = review.getCriteriaScores().stream()
                .mapToDouble(cs -> cs.getScore() != null ? cs.getScore() : 0.0)
                .sum();

        double maxScore = review.getCriteriaScores().stream()
                .mapToDouble(cs -> cs.getMaxScore() != null ? cs.getMaxScore() : 0.0)
                .sum();

        review.setTotalScore(totalScore);
        review.setMaxScore(maxScore);
        review.calculatePercentageScore();
        review.calculateWeightedScore();
        review.countCriticalFailures();
        review.updateTimestamp();
    }

    /**
     * Statistics DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QaReviewStatisticsDto {
        private Long totalReviews;
        private Long completedReviews;
        private Long pendingReviews;
        private Long passedReviews;
        private Long failedReviews;
        private Double averageScore;
        private Double passRate;
    }
}
