package com.gogidix.customersupport.feedback.application.service;

import com.gogidix.customersupport.feedback.application.dto.request.FeedbackRequestDto;
import com.gogidix.customersupport.feedback.application.dto.response.FeedbackAnalyticsDto;
import com.gogidix.customersupport.feedback.application.dto.response.FeedbackResponseDto;
import com.gogidix.customersupport.feedback.application.mapper.FeedbackMapper;
import com.gogidix.customersupport.feedback.domain.model.Feedback;
import com.gogidix.customersupport.feedback.domain.repository.FeedbackRepository;
import com.gogidix.customersupport.feedback.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Application service for Feedback management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    public Page<FeedbackResponseDto> getAllFeedback(Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all feedback for tenant: {}", tenantId);
        return feedbackRepository.findByTenantId(tenantId, pageable)
                .map(feedbackMapper::toResponseDto);
    }

    public FeedbackResponseDto getFeedbackById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback with id: {} for tenant: {}", id, tenantId);
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + id));
        return feedbackMapper.toResponseDto(feedback);
    }

    public FeedbackResponseDto getFeedbackByFeedbackId(String feedbackId) {
        log.debug("Fetching feedback with feedbackId: {}", feedbackId);
        Feedback feedback = feedbackRepository.findByFeedbackId(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with feedbackId: " + feedbackId));
        return feedbackMapper.toResponseDto(feedback);
    }

    public List<FeedbackResponseDto> getFeedbackByTicketId(String ticketId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback for ticket: {} and tenant: {}", ticketId, tenantId);
        return feedbackRepository.findByTenantIdAndTicketId(tenantId, ticketId).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDto> getFeedbackByCustomerId(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback for customer: {} and tenant: {}", customerId, tenantId);
        return feedbackRepository.findByTenantIdAndCustomerId(tenantId, customerId).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDto> getFeedbackByAgentId(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback for agent: {} and tenant: {}", agentId, tenantId);
        return feedbackRepository.findByTenantIdAndAgentId(tenantId, agentId).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDto> getFeedbackByType(Feedback.FeedbackType feedbackType) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback with type: {} for tenant: {}", feedbackType, tenantId);
        return feedbackRepository.findByTenantIdAndFeedbackType(tenantId, feedbackType).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDto> getUnreviewedFeedback() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching unreviewed feedback for tenant: {}", tenantId);
        return feedbackRepository.findByTenantIdAndReviewed(tenantId, false).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDto> getFeedbackRequiringFollowUp() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback requiring follow-up for tenant: {}", tenantId);
        return feedbackRepository.findByTenantIdAndFollowUpRequired(tenantId, true).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public FeedbackResponseDto createFeedback(FeedbackRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating new feedback for tenant: {}", tenantId);

        Feedback feedback = feedbackMapper.toEntity(request, tenantId);

        feedback.setCustomerName(request.getCustomerName());
        feedback.setCustomerEmail(request.getCustomerEmail());
        feedback.setChatSessionId(request.getChatSessionId());
        feedback.setNpsScore(request.getNpsScore());
        feedback.setCsatScore(request.getCsatScore());
        feedback.setCategories(request.getCategories());
        feedback.setComment(request.getComment());
        feedback.setSentiment(request.getSentiment());
        feedback.setAgentId(request.getAgentId());
        feedback.setAgentName(request.getAgentName());
        feedback.setSourceChannel(request.getSourceChannel());
        feedback.setTags(request.getTags());

        Feedback saved = feedbackRepository.save(feedback);

        log.info("Created feedback with feedbackId: {} for tenant: {}", saved.getFeedbackId(), tenantId);
        return feedbackMapper.toResponseDto(saved);
    }

    @Transactional
    public FeedbackResponseDto updateFeedback(String id, FeedbackRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Updating feedback with id: {} for tenant: {}", id, tenantId);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + id));

        feedbackMapper.updateEntityFromDto(request, feedback);

        if (request.getCustomerName() != null) {
            feedback.setCustomerName(request.getCustomerName());
        }
        if (request.getCustomerEmail() != null) {
            feedback.setCustomerEmail(request.getCustomerEmail());
        }
        if (request.getAgentId() != null) {
            feedback.setAgentId(request.getAgentId());
        }
        if (request.getAgentName() != null) {
            feedback.setAgentName(request.getAgentName());
        }
        if (request.getSourceChannel() != null) {
            feedback.setSourceChannel(request.getSourceChannel());
        }

        Feedback updated = feedbackRepository.save(feedback);
        log.info("Updated feedback with feedbackId: {} for tenant: {}", updated.getFeedbackId(), tenantId);
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public FeedbackResponseDto markAsReviewed(String id, String reviewedBy) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Marking feedback: {} as reviewed by: {} for tenant: {}", id, reviewedBy, tenantId);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + id));

        feedback.markAsReviewed(reviewedBy);

        Feedback updated = feedbackRepository.save(feedback);
        log.info("Marked feedback: {} as reviewed", updated.getFeedbackId());
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public FeedbackResponseDto requestFollowUp(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Requesting follow-up for feedback: {} for tenant: {}", id, tenantId);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + id));

        feedback.requestFollowUp();

        Feedback updated = feedbackRepository.save(feedback);
        log.info("Requested follow-up for feedback: {}", updated.getFeedbackId());
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public FeedbackResponseDto completeFollowUp(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Completing follow-up for feedback: {} for tenant: {}", id, tenantId);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + id));

        feedback.completeFollowUp();

        Feedback updated = feedbackRepository.save(feedback);
        log.info("Completed follow-up for feedback: {}", updated.getFeedbackId());
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteFeedback(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Deleting feedback with id: {} for tenant: {}", id, tenantId);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found with id: " + id));

        feedbackRepository.delete(feedback);

        log.info("Deleted feedback: {} for tenant: {}", feedback.getFeedbackId(), tenantId);
    }

    public FeedbackAnalyticsDto getFeedbackAnalytics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback analytics for tenant: {}", tenantId);

        List<Feedback> allFeedback = feedbackRepository.findByTenantId(tenantId);

        long totalFeedback = allFeedback.size();
        double averageRating = allFeedback.stream()
                .filter(f -> f.getRating() != null)
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);

        long totalReviews = allFeedback.stream()
                .filter(f -> f.getReviewed() != null && f.getReviewed())
                .count();
        long pendingReviews = totalFeedback - totalReviews;

        long followUpsRequired = allFeedback.stream()
                .filter(f -> f.getFollowUpRequired() != null && f.getFollowUpRequired())
                .count();
        long followUpsCompleted = allFeedback.stream()
                .filter(f -> f.getFollowUpStatus() == Feedback.FollowUpStatus.COMPLETED)
                .count();

        double csatScore = allFeedback.stream()
                .filter(f -> f.getCsatScore() != null)
                .mapToInt(Feedback::getCsatScore)
                .average()
                .orElse(0.0);

        java.util.OptionalDouble npsAvg = allFeedback.stream()
                .filter(f -> f.getNpsScore() != null)
                .mapToInt(Feedback::getNpsScore)
                .average();
        Integer npsScore = npsAvg.isPresent() ? (int) Math.round(npsAvg.getAsDouble()) : 0;

        Map<String, Long> feedbackByType = allFeedback.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getFeedbackType().toString(),
                        Collectors.counting()
                ));

        Map<String, Long> feedbackBySentiment = allFeedback.stream()
                .filter(f -> f.getSentiment() != null)
                .collect(Collectors.groupingBy(
                        f -> f.getSentiment().toString(),
                        Collectors.counting()
                ));

        Map<String, Double> averageRatingByAgent = allFeedback.stream()
                .filter(f -> f.getAgentId() != null && f.getRating() != null)
                .collect(Collectors.groupingBy(
                        Feedback::getAgentId,
                        Collectors.averagingInt(Feedback::getRating)
                ));

        return FeedbackAnalyticsDto.builder()
                .totalFeedback(totalFeedback)
                .averageRating(averageRating)
                .totalReviews(totalReviews)
                .pendingReviews(pendingReviews)
                .followUpsRequired(followUpsRequired)
                .followUpsCompleted(followUpsCompleted)
                .csatScore(csatScore)
                .npsScore(npsScore)
                .feedbackByType(feedbackByType)
                .feedbackBySentiment(feedbackBySentiment)
                .averageRatingByAgent(averageRatingByAgent)
                .build();
    }

    public List<FeedbackResponseDto> getFeedbackByDateRange(Instant startDate, Instant endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback for date range: {} to {} for tenant: {}", startDate, endDate, tenantId);
        return feedbackRepository.findByTenantIdAndCreatedAtBetween(tenantId, startDate, endDate).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<FeedbackResponseDto> getFeedbackByRatingRange(Integer minRating, Integer maxRating) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching feedback with rating range: {} to {} for tenant: {}", minRating, maxRating, tenantId);
        return feedbackRepository.findByTenantIdAndRatingBetween(tenantId, minRating, maxRating).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
