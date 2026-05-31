package com.gogidix.customersupport.feedback.application.mapper;

import com.gogidix.customersupport.feedback.application.dto.request.FeedbackRequestDto;
import com.gogidix.customersupport.feedback.application.dto.request.CSATSurveyRequestDto;
import com.gogidix.customersupport.feedback.application.dto.response.CSATSurveyResponseDto;
import com.gogidix.customersupport.feedback.application.dto.response.FeedbackResponseDto;
import com.gogidix.customersupport.feedback.application.dto.response.NPSMetricResponseDto;
import com.gogidix.customersupport.feedback.domain.model.CSATSurvey;
import com.gogidix.customersupport.feedback.domain.model.Feedback;
import com.gogidix.customersupport.feedback.domain.model.NPSMetric;
import org.springframework.stereotype.Component;

/**
 * Mapper for Feedback domain
 */
@Component
public class FeedbackMapper {

    public Feedback toEntity(FeedbackRequestDto dto, String tenantId) {
        return Feedback.create(
                tenantId,
                dto.getTicketId(),
                dto.getCustomerId(),
                dto.getFeedbackType(),
                dto.getRating()
        );
    }

    public FeedbackResponseDto toResponseDto(Feedback feedback) {
        return FeedbackResponseDto.builder()
                .id(feedback.getId())
                .feedbackId(feedback.getFeedbackId())
                .tenantId(feedback.getTenantId())
                .ticketId(feedback.getTicketId())
                .chatSessionId(feedback.getChatSessionId())
                .customerId(feedback.getCustomerId())
                .customerName(feedback.getCustomerName())
                .customerEmail(feedback.getCustomerEmail())
                .feedbackType(feedback.getFeedbackType())
                .rating(feedback.getRating())
                .npsScore(feedback.getNpsScore())
                .csatScore(feedback.getCsatScore())
                .categories(feedback.getCategories())
                .comment(feedback.getComment())
                .sentiment(feedback.getSentiment())
                .agentId(feedback.getAgentId())
                .agentName(feedback.getAgentName())
                .sourceChannel(feedback.getSourceChannel())
                .submittedAt(feedback.getSubmittedAt())
                .reviewed(feedback.getReviewed())
                .reviewedAt(feedback.getReviewedAt())
                .reviewedBy(feedback.getReviewedBy())
                .tags(feedback.getTags())
                .followUpRequired(feedback.getFollowUpRequired())
                .followUpStatus(feedback.getFollowUpStatus())
                .responseSent(feedback.getResponseSent())
                .responseSentAt(feedback.getResponseSentAt())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .build();
    }

    public CSATSurvey toEntity(CSATSurveyRequestDto dto, String tenantId, String createdBy) {
        CSATSurvey survey = CSATSurvey.create(
                tenantId,
                dto.getName(),
                dto.getDescription(),
                dto.getQuestions()
        );
        survey.setRatingScale(dto.getRatingScale());
        survey.setTriggerEvents(dto.getTriggerEvents());
        survey.setTriggerDelayHours(dto.getTriggerDelayHours());
        survey.setLocale(dto.getLocale());
        survey.setActiveFrom(dto.getActiveFrom());
        survey.setActiveUntil(dto.getActiveUntil());
        survey.setMaxResponses(dto.getMaxResponses());
        survey.setCreatedBy(createdBy);
        return survey;
    }

    public CSATSurveyResponseDto toResponseDto(CSATSurvey survey) {
        return CSATSurveyResponseDto.builder()
                .id(survey.getId())
                .surveyId(survey.getSurveyId())
                .tenantId(survey.getTenantId())
                .name(survey.getName())
                .description(survey.getDescription())
                .status(survey.getStatus())
                .questions(survey.getQuestions())
                .ratingScale(survey.getRatingScale())
                .triggerEvents(survey.getTriggerEvents())
                .triggerDelayHours(survey.getTriggerDelayHours())
                .locale(survey.getLocale())
                .activeFrom(survey.getActiveFrom())
                .activeUntil(survey.getActiveUntil())
                .maxResponses(survey.getMaxResponses())
                .responseCount(survey.getResponseCount())
                .createdBy(survey.getCreatedBy())
                .modifiedBy(survey.getModifiedBy())
                .createdAt(survey.getCreatedAt())
                .updatedAt(survey.getUpdatedAt())
                .isActive(survey.isActive())
                .build();
    }

    public NPSMetricResponseDto toResponseDto(NPSMetric metric) {
        return NPSMetricResponseDto.builder()
                .id(metric.getId())
                .metricId(metric.getMetricId())
                .tenantId(metric.getTenantId())
                .periodStart(metric.getPeriodStart())
                .periodEnd(metric.getPeriodEnd())
                .periodType(metric.getPeriodType())
                .npsScore(metric.getNpsScore())
                .promotersCount(metric.getPromotersCount())
                .promotersPercentage(metric.getPromotersPercentage())
                .passivesCount(metric.getPassivesCount())
                .passivesPercentage(metric.getPassivesPercentage())
                .detractorsCount(metric.getDetractorsCount())
                .detractorsPercentage(metric.getDetractorsPercentage())
                .totalResponses(metric.getTotalResponses())
                .averageScore(metric.getAverageScore())
                .countryCode(metric.getCountryCode())
                .agentId(metric.getAgentId())
                .teamId(metric.getTeamId())
                .channel(metric.getChannel())
                .previousNpsScore(metric.getPreviousNpsScore())
                .scoreChange(metric.getScoreChange())
                .createdAt(metric.getCreatedAt())
                .updatedAt(metric.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDto(FeedbackRequestDto dto, Feedback feedback) {
        if (dto.getComment() != null) {
            feedback.setComment(dto.getComment());
        }
        if (dto.getCategories() != null) {
            feedback.setCategories(dto.getCategories());
        }
        if (dto.getTags() != null) {
            feedback.setTags(dto.getTags());
        }
        if (dto.getSentiment() != null) {
            feedback.setSentiment(dto.getSentiment());
        }
        if (dto.getNpsScore() != null) {
            feedback.setNpsScore(dto.getNpsScore());
        }
        if (dto.getCsatScore() != null) {
            feedback.setCsatScore(dto.getCsatScore());
        }
        feedback.updateTimestamp();
    }
}
