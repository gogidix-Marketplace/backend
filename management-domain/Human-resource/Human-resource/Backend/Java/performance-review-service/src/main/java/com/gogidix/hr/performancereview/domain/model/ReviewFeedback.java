package com.gogidix.hr.performancereview.domain.model;

import com.gogidix.hr.performancereview.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ReviewFeedback Domain Entity
 * Represents feedback collected during performance reviews
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "review_feedback")
public class ReviewFeedback extends BaseEntity {

    @Indexed(unique = true)
    private String feedbackCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String reviewId;

    private String reviewCode;

    @Indexed
    private String reviewerId;

    private String reviewerName;
    private String reviewerPosition;
    private String reviewerRelationship; // PEER, MANAGER, DIRECT_REPORT, SELF

    @Indexed
    private String subjectId;

    private String subjectName;

    @Indexed
    private FeedbackType feedbackType;

    private String category;
    private String comments;
    private Integer rating;

    @Builder.Default
    private List<String> strengthIds = new ArrayList<>();

    @Builder.Default
    private List<String> improvementIds = new ArrayList<>();

    @Builder.Default
    private List<String> examples = new ArrayList<>();

    private Boolean isAnonymous;
    private Boolean isPrivate;

    @Indexed
    private FeedbackStatus status;

    private LocalDate feedbackDate;
    private LocalDate reviewDate;

    @Indexed
    private String acknowledgedBy;

    private LocalDate acknowledgedDate;

    @Builder.Default
    private List<String> attachmentIds = new ArrayList<>();

    public enum FeedbackType {
        POSITIVE,
        CONSTRUCTIVE,
        NEGATIVE,
        NEUTRAL,
        SUGGESTION,
        APPRECIATION
    }

    public enum FeedbackStatus {
        DRAFT,
        SUBMITTED,
        REVIEWED,
        ACKNOWLEDGED,
        ARCHIVED
    }

    /**
     * Creates a new feedback
     */
    public static ReviewFeedback create(String tenantId, String reviewId, String reviewCode,
                                         String reviewerId, String reviewerName,
                                         String subjectId, String subjectName,
                                         FeedbackType feedbackType) {
        String feedbackCode = generateFeedbackCode(reviewCode, reviewerId);

        ReviewFeedback feedback = new ReviewFeedback();
        feedback.setTenantId(tenantId);
        feedback.setReviewId(reviewId);
        feedback.setReviewCode(reviewCode);
        feedback.setReviewerId(reviewerId);
        feedback.setReviewerName(reviewerName);
        feedback.setSubjectId(subjectId);
        feedback.setSubjectName(subjectName);
        feedback.setFeedbackType(feedbackType);
        feedback.setFeedbackCode(feedbackCode);
        feedback.setStatus(FeedbackStatus.DRAFT);
        feedback.setIsAnonymous(false);
        feedback.setIsPrivate(false);
        feedback.setFeedbackDate(LocalDate.now());
        feedback.setStrengthIds(new ArrayList<>());
        feedback.setImprovementIds(new ArrayList<>());
        feedback.setExamples(new ArrayList<>());
        feedback.setAttachmentIds(new ArrayList<>());

        return feedback;
    }

    /**
     * Submits feedback
     */
    public void submit() {
        if (this.status != FeedbackStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft feedback");
        }
        this.status = FeedbackStatus.SUBMITTED;
    }

    /**
     * Marks as reviewed
     */
    public void markAsReviewed() {
        if (this.status != FeedbackStatus.SUBMITTED) {
            throw new IllegalStateException("Can only mark submitted feedback as reviewed");
        }
        this.status = FeedbackStatus.REVIEWED;
    }

    /**
     * Acknowledges feedback
     */
    public void acknowledge(String acknowledgedBy) {
        if (this.status != FeedbackStatus.REVIEWED) {
            throw new IllegalStateException("Can only acknowledge reviewed feedback");
        }
        this.status = FeedbackStatus.ACKNOWLEDGED;
        this.acknowledgedBy = acknowledgedBy;
        this.acknowledgedDate = LocalDate.now();
    }

    /**
     * Archives feedback
     */
    public void archive() {
        this.status = FeedbackStatus.ARCHIVED;
    }

    /**
     * Adds strength
     */
    public void addStrength(String strength) {
        if (this.strengthIds == null) {
            this.strengthIds = new ArrayList<>();
        }
        if (!this.strengthIds.contains(strength)) {
            this.strengthIds.add(strength);
        }
    }

    /**
     * Adds improvement
     */
    public void addImprovement(String improvement) {
        if (this.improvementIds == null) {
            this.improvementIds = new ArrayList<>();
        }
        if (!this.improvementIds.contains(improvement)) {
            this.improvementIds.add(improvement);
        }
    }

    /**
     * Adds example
     */
    public void addExample(String example) {
        if (this.examples == null) {
            this.examples = new ArrayList<>();
        }
        if (!this.examples.contains(example)) {
            this.examples.add(example);
        }
    }

    /**
     * Adds attachment
     */
    public void addAttachment(String attachmentId) {
        if (this.attachmentIds == null) {
            this.attachmentIds = new ArrayList<>();
        }
        if (!this.attachmentIds.contains(attachmentId)) {
            this.attachmentIds.add(attachmentId);
        }
    }

    /**
     * Sets as anonymous
     */
    public void setAsAnonymous() {
        this.isAnonymous = true;
    }

    /**
     * Sets as private
     */
    public void setAsPrivate() {
        this.isPrivate = true;
    }

    /**
     * Generates feedback code
     */
    private static String generateFeedbackCode(String reviewCode, String reviewerId) {
        return "FDB-" + reviewCode.substring(4) + "-" + reviewerId.substring(0, 8);
    }

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
