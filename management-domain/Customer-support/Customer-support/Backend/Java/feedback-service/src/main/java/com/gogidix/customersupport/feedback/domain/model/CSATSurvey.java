package com.gogidix.customersupport.feedback.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * CSAT Survey aggregate root
 * Customer Satisfaction Survey configuration and responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "csat_surveys")
public class CSATSurvey extends BaseEntity {

    @Field("survey_id")
    @Indexed(unique = true)
    private String surveyId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("status")
    @Indexed
    private SurveyStatus status;

    @Field("questions")
    private List<SurveyQuestion> questions;

    @Field("rating_scale")
    private Integer ratingScale;

    @Field("trigger_events")
    private List<String> triggerEvents;

    @Field("trigger_delay_hours")
    private Integer triggerDelayHours;

    @Field("locale")
    private String locale;

    @Field("active_from")
    private Instant activeFrom;

    @Field("active_until")
    private Instant activeUntil;

    @Field("max_responses")
    private Integer maxResponses;

    @Field("response_count")
    private Integer responseCount;

    @Field("created_by")
    private String createdBy;

    @Field("modified_by")
    private String modifiedBy;

    public static CSATSurvey create(String tenantId, String name, String description, List<SurveyQuestion> questions) {
        CSATSurvey survey = new CSATSurvey();
        survey.setId(java.util.UUID.randomUUID().toString());
        survey.setTenantId(tenantId);
        survey.setSurveyId(generateSurveyId());
        survey.setName(name);
        survey.setDescription(description);
        survey.setQuestions(questions);
        survey.setStatus(SurveyStatus.DRAFT);
        survey.setRatingScale(5);
        survey.setTriggerDelayHours(24);
        survey.setLocale("en");
        survey.setResponseCount(0);
        survey.setCreatedAt(Instant.now());
        survey.setUpdatedAt(Instant.now());
        return survey;
    }

    private static String generateSurveyId() {
        return "CSAT-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    public void activate() {
        this.status = SurveyStatus.ACTIVE;
        this.activeFrom = Instant.now();
        this.updateTimestamp();
    }

    public void pause() {
        this.status = SurveyStatus.PAUSED;
        this.updateTimestamp();
    }

    public void close() {
        this.status = SurveyStatus.CLOSED;
        this.activeUntil = Instant.now();
        this.updateTimestamp();
    }

    public void incrementResponseCount() {
        this.responseCount = (this.responseCount != null ? this.responseCount : 0) + 1;
        this.updateTimestamp();
    }

    public boolean isActive() {
        return this.status == SurveyStatus.ACTIVE &&
               (this.activeFrom == null || !Instant.now().isBefore(this.activeFrom)) &&
               (this.activeUntil == null || !Instant.now().isAfter(this.activeUntil)) &&
               (this.maxResponses == null || this.responseCount < this.maxResponses);
    }

    public enum SurveyStatus {
        DRAFT, ACTIVE, PAUSED, CLOSED, ARCHIVED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SurveyQuestion {
        private String questionId;
        private String text;
        private QuestionType type;
        private Integer order;
        private Boolean required;
        private List<QuestionOption> options;
        private Integer minRating;
        private Integer maxRating;

        public enum QuestionType {
            RATING, TEXT, MULTIPLE_CHOICE, BOOLEAN, NPS
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionOption {
        private String value;
        private String label;
        private Integer order;
    }
}
