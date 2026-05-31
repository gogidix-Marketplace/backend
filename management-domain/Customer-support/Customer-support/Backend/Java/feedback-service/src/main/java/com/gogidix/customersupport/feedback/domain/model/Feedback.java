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
import java.util.Map;

/**
 * Feedback aggregate root
 * Represents customer feedback for support interactions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "feedback")
public class Feedback extends BaseEntity {

    @Field("feedback_id")
    @Indexed(unique = true)
    private String feedbackId;

    @Field("ticket_id")
    @Indexed
    private String ticketId;

    @Field("chat_session_id")
    private String chatSessionId;

    @Field("customer_id")
    @Indexed
    private String customerId;

    @Field("customer_name")
    private String customerName;

    @Field("customer_email")
    private String customerEmail;

    @Field("feedback_type")
    @Indexed
    private FeedbackType feedbackType;

    @Field("rating")
    private Integer rating;

    @Field("nps_score")
    private Integer npsScore;

    @Field("csat_score")
    private Integer csatScore;

    @Field("categories")
    private Map<String, Integer> categories;

    @Field("comment")
    private String comment;

    @Field("sentiment")
    private SentimentType sentiment;

    @Field("agent_id")
    @Indexed
    private String agentId;

    @Field("agent_name")
    private String agentName;

    @Field("source_channel")
    private String sourceChannel;

    @Field("submitted_at")
    private Instant submittedAt;

    @Field("reviewed")
    private Boolean reviewed;

    @Field("reviewed_at")
    private Instant reviewedAt;

    @Field("reviewed_by")
    private String reviewedBy;

    @Field("tags")
    private java.util.List<String> tags;

    @Field("follow_up_required")
    private Boolean followUpRequired;

    @Field("follow_up_status")
    private FollowUpStatus followUpStatus;

    @Field("response_sent")
    private Boolean responseSent;

    @Field("response_sent_at")
    private Instant responseSentAt;

    public static Feedback create(String tenantId, String ticketId, String customerId,
                                  FeedbackType type, Integer rating) {
        Feedback feedback = new Feedback();
        feedback.setId(java.util.UUID.randomUUID().toString());
        feedback.setTenantId(tenantId);
        feedback.setFeedbackId(generateFeedbackId());
        feedback.setTicketId(ticketId);
        feedback.setCustomerId(customerId);
        feedback.setFeedbackType(type);
        feedback.setRating(rating);
        feedback.setReviewed(false);
        feedback.setFollowUpRequired(false);
        feedback.setFollowUpStatus(FollowUpStatus.NONE);
        feedback.setResponseSent(false);
        feedback.setSubmittedAt(Instant.now());
        feedback.setCreatedAt(Instant.now());
        feedback.setUpdatedAt(Instant.now());
        return feedback;
    }

    private static String generateFeedbackId() {
        return "FDB-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    public void markAsReviewed(String reviewedBy) {
        this.reviewed = true;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = Instant.now();
        this.updateTimestamp();
    }

    public void requestFollowUp() {
        this.followUpRequired = true;
        this.followUpStatus = FollowUpStatus.PENDING;
        this.updateTimestamp();
    }

    public void completeFollowUp() {
        this.followUpStatus = FollowUpStatus.COMPLETED;
        this.updateTimestamp();
    }

    public void markResponseSent() {
        this.responseSent = true;
        this.responseSentAt = Instant.now();
        this.updateTimestamp();
    }

    public void setCategories(Map<String, Integer> categories) {
        this.categories = categories;
        this.updateTimestamp();
    }

    public enum FeedbackType {
        CSAT, NPS, CUSTOMER_SATISFACTION, AGENT_RATING, PRODUCT_FEEDBACK, BUG_REPORT, FEATURE_REQUEST
    }

    public enum SentimentType {
        POSITIVE, NEUTRAL, NEGATIVE
    }

    public enum FollowUpStatus {
        NONE, PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
