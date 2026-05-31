package com.gogidix.customersupport.feedback.application.dto.response;

import com.gogidix.customersupport.feedback.domain.model.CSATSurvey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Response DTO for CSAT Survey
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CSATSurveyResponseDto {

    private String id;
    private String surveyId;
    private String tenantId;
    private String name;
    private String description;
    private CSATSurvey.SurveyStatus status;
    private List<CSATSurvey.SurveyQuestion> questions;
    private Integer ratingScale;
    private List<String> triggerEvents;
    private Integer triggerDelayHours;
    private String locale;
    private Instant activeFrom;
    private Instant activeUntil;
    private Integer maxResponses;
    private Integer responseCount;
    private String createdBy;
    private String modifiedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isActive;
}
