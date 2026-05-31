package com.gogidix.customersupport.qualitymanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * QaReview - Domain model representing a Quality Assurance review
 *
 * Represents a complete QA review of an agent's interaction with a customer,
 * including scored criteria, comments, and overall assessment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QaReview extends BaseEntity {

    // Review Identification
    @Field("review_id")
    private String reviewId;

    @Field("ticket_id")
    private String ticketId;

    @Field("interaction_id")
    private String interactionId;

    @Field("agent_id")
    private String agentId;

    @Field("agent_name")
    private String agentName;

    @Field("reviewer_id")
    private String reviewerId;

    @Field("reviewer_name")
    private String reviewerName;

    // Review Details
    @Field("review_type")
    private ReviewType reviewType;

    @Field("review_status")
    private ReviewStatus reviewStatus;

    @Field("channel_type")
    private ChannelType channelType;

    @Field("scorecard_template_id")
    private String scorecardTemplateId;

    @Field("scorecard_template_name")
    private String scorecardTemplateName;

    // Scoring
    @Field("total_score")
    private Double totalScore;

    @Field("max_score")
    private Double maxScore;

    @Field("percentage_score")
    private Double percentageScore;

    @Field("weighted_score")
    private Double weightedScore;

    @Field("passed")
    private Boolean passed;

    @Field("critical_failures")
    private Integer criticalFailures;

    // Criteria Scores
    @Field("criteria_scores")
    private List<CriteriaScore> criteriaScores;

    // Comments and Feedback
    @Field("overall_comments")
    private String overallComments;

    @Field("strengths")
    private List<String> strengths;

    @Field("areas_for_improvement")
    private List<String> areasForImprovement;

    @Field("agent_coaching_notes")
    private String agentCoachingNotes;

    // Review Timeline
    @Field("review_date")
    private Instant reviewDate;

    @Field("interaction_date")
    private Instant interactionDate;

    @Field("completed_at")
    private Instant completedAt;

    @Field("due_date")
    private Instant dueDate;

    // Calibration
    @Field("calibration_session_id")
    private String calibrationSessionId;

    @Field("is_calibrated")
    private Boolean isCalibrated;

    @Field("calibration_notes")
    private String calibrationNotes;

    // Escalation
    @Field("requires_escalation")
    private Boolean requiresEscalation;

    @Field("escalation_reason")
    private String escalationReason;

    @Field("escalated_to")
    private String escalatedTo;

    @Field("escalated_at")
    private Instant escalatedAt;

    // Follow-up
    @Field("requires_follow_up")
    private Boolean requiresFollowUp;

    @Field("follow_up_date")
    private Instant followUpDate;

    @Field("follow_up_completed")
    private Boolean followUpCompleted;

    // Metadata
    @Field("review_cycle")
    private String reviewCycle;

    @Field("batch_id")
    private String batchId;

    @Field("external_reference_id")
    private String externalReferenceId;

    @Field("tags")
    private List<String> tags;

    /**
     * Enums for Review Type
     */
    public enum ReviewType {
        TICKET_REVIEW,
        CALL_REVIEW,
        CHAT_REVIEW,
        EMAIL_REVIEW,
        SOCIAL_REVIEW,
        CALLBACK_REVIEW
    }

    /**
     * Enums for Review Status
     */
    public enum ReviewStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        APPROVED,
        REJECTED,
        CALIBRATING,
        ESCALATED
    }

    /**
     * Enums for Channel Type
     */
    public enum ChannelType {
        PHONE,
        EMAIL,
        CHAT,
        WEB,
        SOCIAL,
        SMS,
        WHATSAPP
    }

    /**
     * Inner class for Criteria Score
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteriaScore {
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
     * Constructor with tenant ID
     */
    public QaReview(String tenantId) {
        super(tenantId);
        this.reviewId = generateReviewId();
        this.reviewStatus = ReviewStatus.PENDING;
        this.criteriaScores = new ArrayList<>();
        this.strengths = new ArrayList<>();
        this.areasForImprovement = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.isCalibrated = false;
        this.requiresEscalation = false;
        this.requiresFollowUp = false;
        this.followUpCompleted = false;
        this.criticalFailures = 0;
        this.reviewDate = Instant.now();
    }

    /**
     * Calculate percentage score
     */
    public void calculatePercentageScore() {
        if (maxScore != null && maxScore > 0) {
            this.percentageScore = (totalScore / maxScore) * 100.0;
        }
    }

    /**
     * Determine if review passed based on percentage score
     */
    public void determinePassStatus(double passingScore) {
        this.passed = percentageScore != null && percentageScore >= passingScore;
    }

    /**
     * Calculate weighted score
     */
    public void calculateWeightedScore() {
        if (criteriaScores != null && !criteriaScores.isEmpty()) {
            this.weightedScore = criteriaScores.stream()
                    .mapToDouble(cs -> (cs.getScore() != null ? cs.getScore() : 0) *
                            (cs.getWeight() != null ? cs.getWeight() : 1.0))
                    .sum();
        }
    }

    /**
     * Count critical failures
     */
    public void countCriticalFailures() {
        if (criteriaScores != null) {
            this.criticalFailures = (int) criteriaScores.stream()
                    .filter(cs -> Boolean.TRUE.equals(cs.getIsCritical()) && Boolean.FALSE.equals(cs.getPassed()))
                    .count();
        }
    }

    /**
     * Check if review is overdue
     */
    public boolean isOverdue() {
        return dueDate != null && Instant.now().isAfter(dueDate) &&
                reviewStatus != ReviewStatus.COMPLETED && reviewStatus != ReviewStatus.APPROVED;
    }

    /**
     * Generate unique review ID
     */
    private String generateReviewId() {
        return "QA-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    /**
     * Add criteria score
     */
    public void addCriteriaScore(CriteriaScore criteriaScore) {
        if (this.criteriaScores == null) {
            this.criteriaScores = new ArrayList<>();
        }
        this.criteriaScores.add(criteriaScore);
        updateTimestamp();
    }

    /**
     * Add strength
     */
    public void addStrength(String strength) {
        if (this.strengths == null) {
            this.strengths = new ArrayList<>();
        }
        this.strengths.add(strength);
        updateTimestamp();
    }

    /**
     * Add area for improvement
     */
    public void addAreaForImprovement(String area) {
        if (this.areasForImprovement == null) {
            this.areasForImprovement = new ArrayList<>();
        }
        this.areasForImprovement.add(area);
        updateTimestamp();
    }

    /**
     * Add tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
        updateTimestamp();
    }
}
