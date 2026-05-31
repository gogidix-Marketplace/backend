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
 * CalibrationSession - Domain model representing a QA calibration session
 *
 * Calibration sessions bring together multiple QA reviewers to evaluate
 * the same interactions and ensure consistency in scoring.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CalibrationSession extends BaseEntity {

    // Session Identification
    @Field("session_id")
    private String sessionId;

    @Field("session_name")
    private String sessionName;

    @Field("session_code")
    private String sessionCode;

    // Session Details
    @Field("session_type")
    private SessionType sessionType;

    @Field("session_status")
    private SessionStatus sessionStatus;

    @Field("description")
    private String description;

    @Field("facilitator_id")
    private String facilitatorId;

    @Field("facilitator_name")
    private String facilitatorName;

    // Schedule
    @Field("scheduled_date")
    private Instant scheduledDate;

    @Field("scheduled_end_date")
    private Instant scheduledEndDate;

    @Field("actual_start_date")
    private Instant actualStartDate;

    @Field("actual_end_date")
    private Instant actualEndDate;

    @Field("duration_minutes")
    private Integer durationMinutes;

    // Participants
    @Field("participants")
    private List<Participant> participants;

    @Field("min_participants")
    private Integer minParticipants;

    @Field("max_participants")
    private Integer maxParticipants;

    // Calibration Content
    @Field("scorecard_template_id")
    private String scorecardTemplateId;

    @Field("scorecard_template_name")
    private String scorecardTemplateName;

    @Field("calibration_reviews")
    private List<CalibrationReview> calibrationReviews;

    @Field("target_interactions_count")
    private Integer targetInteractionsCount;

    @Field("completed_interactions_count")
    private Integer completedInteractionsCount;

    // Scoring and Results
    @Field("average_score_variance")
    private Double averageScoreVariance;

    @Field("max_score_variance")
    private Double maxScoreVariance;

    @Field("inter_rater_reliability")
    private Double interRaterReliability;

    @Field("calibration_score")
    private Double calibrationScore;

    @Field("calibration_passed")
    private Boolean calibrationPassed;

    @Field("calibration_threshold")
    private Double calibrationThreshold;

    // Outcomes
    @Field("findings")
    private List<String> findings;

    @Field("action_items")
    private List<ActionItem> actionItems;

    @Field("notes")
    private String notes;

    @Field("follow_up_required")
    private Boolean followUpRequired;

    @Field("follow_up_date")
    private Instant followUpDate;

    // Metadata
    @Field("location")
    private String location;

    @Field("is_virtual")
    private Boolean isVirtual;

    @Field("meeting_link")
    private String meetingLink;

    @Field("tags")
    private List<String> tags;

    /**
     * Enums for Session Type
     */
    public enum SessionType {
        GROUP_CALIBRATION,
        PEER_CALIBRATION,
        EXTERNAL_CALIBRATION,
        SELF_CALIBRATION,
        TEAM_CALIBRATION
    }

    /**
     * Enums for Session Status
     */
    public enum SessionStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        POSTPONED,
        AWAITING_PARTICIPANTS
    }

    /**
     * Inner class for Participant
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Participant {
        private String participantId;
        private String participantName;
        private String participantEmail;
        private ParticipantRole role;
        private String teamId;
        private String teamName;
        private Boolean hasAttended;
        private Instant joinedAt;
        private Instant leftAt;
        private Double complianceScore;

        public enum ParticipantRole {
            FACILITATOR,
            REVIEWER,
            OBSERVER,
            QA_MANAGER,
            TEAM_LEAD
        }
    }

    /**
     * Inner class for Calibration Review
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalibrationReview {
        private String reviewId;
        private String interactionId;
        private String ticketId;
        private List<ReviewerScore> reviewerScores;
        private Double averageScore;
        private Double scoreVariance;
        private Double standardDeviation;
        private Integer reviewerCount;
        private Boolean isOutlier;
        private String outlierReason;
    }

    /**
     * Inner class for Reviewer Score
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewerScore {
        private String reviewerId;
        private String reviewerName;
        private Double score;
        private Double maxScore;
        private Double percentageScore;
        private String comments;
        private Instant submittedAt;
    }

    /**
     * Inner class for Action Item
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionItem {
        private String actionItemId;
        private String description;
        private String assignedTo;
        private String assignedToName;
        private ActionItemStatus status;
        private Instant dueDate;
        private Instant completedAt;
        private String notes;

        public enum ActionItemStatus {
            PENDING,
            IN_PROGRESS,
            COMPLETED,
            CANCELLED
        }
    }

    /**
     * Constructor with tenant ID
     */
    public CalibrationSession(String tenantId) {
        super(tenantId);
        this.sessionId = generateSessionId();
        this.sessionStatus = SessionStatus.SCHEDULED;
        this.participants = new ArrayList<>();
        this.calibrationReviews = new ArrayList<>();
        this.findings = new ArrayList<>();
        this.actionItems = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.isVirtual = true;
        this.completedInteractionsCount = 0;
        this.calibrationThreshold = 80.0;
        this.followUpRequired = false;
    }

    /**
     * Generate unique session ID
     */
    private String generateSessionId() {
        return "CAL-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    /**
     * Check if session is active
     */
    public boolean isActive() {
        return SessionStatus.IN_PROGRESS.equals(sessionStatus);
    }

    /**
     * Check if session is scheduled for future
     */
    public boolean isFuture() {
        return SessionStatus.SCHEDULED.equals(sessionStatus) &&
                scheduledDate != null && scheduledDate.isAfter(Instant.now());
    }

    /**
     * Check if session has started but not completed
     */
    public boolean isInProgress() {
        return SessionStatus.IN_PROGRESS.equals(sessionStatus);
    }

    /**
     * Check if minimum participants reached
     */
    public boolean hasMinParticipants() {
        int attendedCount = getAttendedParticipantCount();
        return minParticipants != null && attendedCount >= minParticipants;
    }

    /**
     * Get attended participant count
     */
    public int getAttendedParticipantCount() {
        if (participants == null) {
            return 0;
        }
        return (int) participants.stream()
                .filter(p -> Boolean.TRUE.equals(p.getHasAttended()))
                .count();
    }

    /**
     * Check if calibration passed based on threshold
     */
    public void determineCalibrationResult() {
        if (calibrationScore != null && calibrationThreshold != null) {
            this.calibrationPassed = calibrationScore >= calibrationThreshold;
        } else {
            this.calibrationPassed = false;
        }
    }

    /**
     * Calculate inter-rater reliability score
     */
    public void calculateInterRaterReliability() {
        if (calibrationReviews == null || calibrationReviews.isEmpty()) {
            this.interRaterReliability = 0.0;
            return;
        }

        double totalVariance = calibrationReviews.stream()
                .filter(r -> r.getScoreVariance() != null)
                .mapToDouble(CalibrationReview::getScoreVariance)
                .average()
                .orElse(0.0);

        this.averageScoreVariance = totalVariance;

        // Calculate IRR as inverse of normalized variance (0-100 scale)
        this.interRaterReliability = Math.max(0, 100 - (totalVariance * 10));
        this.calibrationScore = this.interRaterReliability;
    }

    /**
     * Add participant
     */
    public void addParticipant(Participant participant) {
        if (this.participants == null) {
            this.participants = new ArrayList<>();
        }
        this.participants.add(participant);
        updateTimestamp();
    }

    /**
     * Add calibration review
     */
    public void addCalibrationReview(CalibrationReview review) {
        if (this.calibrationReviews == null) {
            this.calibrationReviews = new ArrayList<>();
        }
        this.calibrationReviews.add(review);
        if (review.getReviewerScores() != null) {
            this.completedInteractionsCount++;
        }
        updateTimestamp();
    }

    /**
     * Add finding
     */
    public void addFinding(String finding) {
        if (this.findings == null) {
            this.findings = new ArrayList<>();
        }
        this.findings.add(finding);
        updateTimestamp();
    }

    /**
     * Add action item
     */
    public void addActionItem(ActionItem actionItem) {
        if (this.actionItems == null) {
            this.actionItems = new ArrayList<>();
        }
        this.actionItems.add(actionItem);
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

    /**
     * Get reviewers (excluding facilitators and observers)
     */
    public List<Participant> getReviewers() {
        if (participants == null) {
            return new ArrayList<>();
        }
        return participants.stream()
                .filter(p -> Participant.ParticipantRole.REVIEWER.equals(p.getRole()))
                .toList();
    }

    /**
     * Get completed action items
     */
    public List<ActionItem> getCompletedActionItems() {
        if (actionItems == null) {
            return new ArrayList<>();
        }
        return actionItems.stream()
                .filter(ai -> ActionItem.ActionItemStatus.COMPLETED.equals(ai.getStatus()))
                .toList();
    }

    /**
     * Get pending action items
     */
    public List<ActionItem> getPendingActionItems() {
        if (actionItems == null) {
            return new ArrayList<>();
        }
        return actionItems.stream()
                .filter(ai -> ActionItem.ActionItemStatus.PENDING.equals(ai.getStatus()) ||
                        ActionItem.ActionItemStatus.IN_PROGRESS.equals(ai.getStatus()))
                .toList();
    }
}
