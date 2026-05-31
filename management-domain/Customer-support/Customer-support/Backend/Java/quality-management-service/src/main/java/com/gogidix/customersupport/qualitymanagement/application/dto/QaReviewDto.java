package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

/**
 * DTO for QA Review operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QaReviewDto {

    private String id;
    private String tenantId;
    private String reviewId;

    // Identification
    @NotBlank(message = "Ticket ID is required")
    private String ticketId;

    private String interactionId;

    @NotBlank(message = "Agent ID is required")
    private String agentId;

    private String agentName;

    @NotBlank(message = "Reviewer ID is required")
    private String reviewerId;

    private String reviewerName;

    // Review Details
    private String reviewType;
    private String reviewStatus;
    private String channelType;
    private String scorecardTemplateId;
    private String scorecardTemplateName;

    // Scoring
    private Double totalScore;
    private Double maxScore;
    private Double percentageScore;
    private Double weightedScore;
    private Boolean passed;
    private Integer criticalFailures;

    // Criteria Scores
    private List<CriteriaScoreDto> criteriaScores;

    // Comments
    private String overallComments;
    private List<String> strengths;
    private List<String> areasForImprovement;
    private String agentCoachingNotes;

    // Timeline
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant reviewDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant interactionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant completedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant dueDate;

    // Calibration
    private String calibrationSessionId;
    private Boolean isCalibrated;
    private String calibrationNotes;

    // Escalation
    private Boolean requiresEscalation;
    private String escalationReason;
    private String escalatedTo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant escalatedAt;

    // Follow-up
    private Boolean requiresFollowUp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant followUpDate;

    private Boolean followUpCompleted;

    // Metadata
    private String reviewCycle;
    private String batchId;
    private String externalReferenceId;
    private List<String> tags;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * DTO for Criteria Score
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CriteriaScoreDto {
        private String criteriaId;
        private String criteriaName;
        private String category;
        private Double score;
        private Double maxScore;
        private Double weight;
        private String comments;
        private Boolean isCritical;
        private Boolean passed;
    }

    /**
     * Request DTO for creating QA Review
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateQaReviewRequest {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Ticket ID is required")
        private String ticketId;

        private String interactionId;

        @NotBlank(message = "Agent ID is required")
        private String agentId;

        private String agentName;

        @NotBlank(message = "Reviewer ID is required")
        private String reviewerId;

        private String reviewerName;

        @NotBlank(message = "Review type is required")
        private String reviewType;

        private String channelType;

        @NotBlank(message = "Scorecard template ID is required")
        private String scorecardTemplateId;

        private List<CriteriaScoreDto> criteriaScores;

        private String overallComments;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant interactionDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant dueDate;

        private String reviewCycle;
        private String batchId;
        private List<String> tags;
    }

    /**
     * Request DTO for updating QA Review
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateQaReviewRequest {
        private String reviewStatus;
        private List<CriteriaScoreDto> criteriaScores;
        private String overallComments;
        private List<String> strengths;
        private List<String> areasForImprovement;
        private String agentCoachingNotes;
        private Boolean requiresEscalation;
        private String escalationReason;
        private String escalatedTo;
        private Boolean requiresFollowUp;
        private Instant followUpDate;
        private String calibrationSessionId;
        private String calibrationNotes;
        private List<String> tags;
    }

    /**
     * Response DTO for QA Review summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QaReviewSummaryDto {
        private String reviewId;
        private String ticketId;
        private String agentId;
        private String agentName;
        private String reviewerName;
        private String reviewType;
        private String reviewStatus;
        private Double percentageScore;
        private Boolean passed;
        private String channelType;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant reviewDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant dueDate;

        private Boolean isOverdue;
        private Boolean isCalibrated;
    }
}
