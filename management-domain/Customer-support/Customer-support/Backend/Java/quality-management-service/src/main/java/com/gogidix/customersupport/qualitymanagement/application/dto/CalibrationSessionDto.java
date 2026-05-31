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
 * DTO for Calibration Session operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalibrationSessionDto {

    private String id;
    private String tenantId;
    private String sessionId;

    // Basic Info
    @NotBlank(message = "Session name is required")
    private String sessionName;

    private String sessionCode;
    private String description;

    // Session Details
    private String sessionType;
    private String sessionStatus;

    @NotBlank(message = "Facilitator ID is required")
    private String facilitatorId;

    private String facilitatorName;

    // Schedule
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant scheduledDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant scheduledEndDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant actualStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant actualEndDate;

    private Integer durationMinutes;

    // Participants
    private List<ParticipantDto> participants;
    private Integer minParticipants;
    private Integer maxParticipants;

    // Content
    private String scorecardTemplateId;
    private String scorecardTemplateName;
    private List<CalibrationReviewDto> calibrationReviews;
    private Integer targetInteractionsCount;
    private Integer completedInteractionsCount;

    // Results
    private Double averageScoreVariance;
    private Double maxScoreVariance;
    private Double interRaterReliability;
    private Double calibrationScore;
    private Boolean calibrationPassed;
    private Double calibrationThreshold;

    // Outcomes
    private List<String> findings;
    private List<ActionItemDto> actionItems;
    private String notes;
    private Boolean followUpRequired;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant followUpDate;

    // Metadata
    private String location;
    private Boolean isVirtual;
    private String meetingLink;
    private List<String> tags;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * DTO for Participant
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ParticipantDto {
        private String participantId;
        private String participantName;
        private String participantEmail;
        private String role;
        private String teamId;
        private String teamName;
        private Boolean hasAttended;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant joinedAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant leftAt;

        private Double complianceScore;
    }

    /**
     * DTO for Calibration Review
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CalibrationReviewDto {
        private String reviewId;
        private String interactionId;
        private String ticketId;
        private List<ReviewerScoreDto> reviewerScores;
        private Double averageScore;
        private Double scoreVariance;
        private Double standardDeviation;
        private Integer reviewerCount;
        private Boolean isOutlier;
        private String outlierReason;
    }

    /**
     * DTO for Reviewer Score
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReviewerScoreDto {
        private String reviewerId;
        private String reviewerName;
        private Double score;
        private Double maxScore;
        private Double percentageScore;
        private String comments;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant submittedAt;
    }

    /**
     * DTO for Action Item
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ActionItemDto {
        private String actionItemId;
        private String description;
        private String assignedTo;
        private String assignedToName;
        private String status;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant dueDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant completedAt;

        private String notes;
    }

    /**
     * Request DTO for creating Calibration Session
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCalibrationSessionRequest {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Session name is required")
        private String sessionName;

        private String sessionCode;
        private String description;

        @NotBlank(message = "Session type is required")
        private String sessionType;

        @NotBlank(message = "Facilitator ID is required")
        private String facilitatorId;

        private String facilitatorName;

        @NotNull(message = "Scheduled date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant scheduledDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant scheduledEndDate;

        private List<ParticipantDto> participants;
        private Integer minParticipants;
        private Integer maxParticipants;

        private String scorecardTemplateId;
        private Integer targetInteractionsCount;

        private String location;
        private Boolean isVirtual;
        private String meetingLink;
        private List<String> tags;
    }

    /**
     * Request DTO for updating Calibration Session
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCalibrationSessionRequest {
        private String sessionName;
        private String description;
        private String sessionStatus;
        private String facilitatorId;
        private Instant scheduledDate;
        private Instant scheduledEndDate;
        private List<ParticipantDto> participants;
        private String scorecardTemplateId;
        private List<CalibrationReviewDto> calibrationReviews;
        private Double calibrationThreshold;
        private List<String> findings;
        private List<ActionItemDto> actionItems;
        private String notes;
        private Boolean followUpRequired;
        private Instant followUpDate;
    }

    /**
     * Response DTO for Calibration Session summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalibrationSessionSummaryDto {
        private String sessionId;
        private String sessionName;
        private String sessionType;
        private String sessionStatus;
        private String facilitatorName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant scheduledDate;

        private Integer participantCount;
        private Integer completedInteractionsCount;
        private Double calibrationScore;
        private Boolean calibrationPassed;
        private Boolean followUpRequired;
        private Boolean isOverdue;
    }
}
