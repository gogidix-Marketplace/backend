package com.gogidix.digitalmarketing.leadgeneration.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * LeadActivity - Track lead interaction history
 *
 * <p>Records all interactions with leads including emails, calls, meetings,
 * website visits, and other engagement activities.</p>
 *
 * <p>Activity types:</p>
 * <ul>
 *   <li>EMAIL_SENT - Email sent to lead</li>
 *   <li>EMAIL_OPENED - Lead opened email</li>
 *   <li>EMAIL_CLICKED - Lead clicked email link</li>
 *   <li>CALL_ATTEMPTED - Sales rep attempted call</li>
 *   <li>CALL_COMPLETED - Completed call with lead</li>
 *   <li>MEETING_SCHEDULED - Meeting scheduled with lead</li>
 *   <li>MEETING_COMPLETED - Meeting completed</li>
 *   <li>WEBINAR_ATTENDED - Lead attended webinar</li>
 *   <li>CONTENT_DOWNLOADED - Lead downloaded content</li>
 *   <li>WEBSITE_VISIT - Lead visited website</li>
 *   <li>FORM_SUBMITTED - Lead submitted form</li>
 *   <li>SOCIAL_ENGAGEMENT - Lead engaged on social media</li>
 *   <li>NOTE_ADDED - Note added by sales rep</li>
 *   <li>STATUS_CHANGED - Lead status changed</li>
 *   <li>ASSIGNED - Lead assigned to sales rep</li>
 *   <li>HANDOFF_COMPLETE - Lead handed off to sales</li>
 * </ul>
 */
@Document(collection = "lead_activities")
@TypeAlias("lead_activity")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "activity_tenant_lead_idx", def = "{'tenantId': 1, 'leadId': 1, 'timestamp': -1}")
@CompoundIndex(name = "activity_tenant_type_idx", def = "{'tenantId': 1, 'type': 1, 'timestamp': -1}")
@CompoundIndex(name = "activity_tenant_user_idx", def = "{'tenantId': 1, 'performedBy': 1, 'timestamp': -1}")
public class LeadActivity extends BaseEntity {

    /**
     * Lead ID this activity belongs to
     */
    @Indexed
    private String leadId;

    /**
     * Activity type
     */
    @Indexed
    private String type;

    /**
     * Activity title/summary
     */
    private String title;

    /**
     * Detailed description of the activity
     */
    private String description;

    /**
     * Activity timestamp
     */
    private Instant timestamp;

    /**
     * User who performed the action
     */
    @Indexed
    private String performedBy;

    /**
     * User role (SYSTEM, SALES_REP, MARKETING, ADMIN)
     */
    private String performedByRole;

    /**
     * Activity direction (INBOUND, OUTBOUND, INTERNAL)
     */
    private String direction;

    /**
     * Activity status (COMPLETED, PENDING, FAILED, CANCELLED)
     */
    private String status;

    /**
     * Activity duration in seconds
     */
    private Long duration;

    /**
     * Engagement score (0-100)
     */
    private Integer engagementScore;

    /**
     * Activity outcome
     */
    private String outcome;

    /**
     * Next action date (if scheduled)
     */
    private Instant nextActionDate;

    /**
     * Activity channel (EMAIL, PHONE, WEB, SOCIAL, IN_PERSON)
     */
    private String channel;

    /**
     * Related campaign ID
     */
    @Indexed
    private String campaignId;

    /**
     * Related content ID
     */
    private String contentId;

    /**
     * Attachment URLs
     */
    private java.util.List<String> attachments;

    /**
     * Activity metadata
     */
    private Map<String, Object> metadata;

    /**
     * IP address (for web activities)
     */
    private String ipAddress;

    /**
     * User agent (for web activities)
     */
    private String userAgent;

    /**
     * Referrer URL (for web activities)
     */
    private String referrer;

    /**
     * Geographic location (for web activities)
     */
    private String location;

    /**
     * Device type (for web activities)
     */
    private String deviceType;

    /**
     * Whether this activity was automated
     */
    @Builder.Default
    private Boolean automated = false;

    /**
     * Scheduled activity ID (if this is a scheduled activity execution)
     */
    private String scheduledActivityId;

    /**
     * Follow-up activities created
     */
    private java.util.List<String> followUpActivityIds;

    /**
     * Previous activity ID (if this is a follow-up)
     */
    private String previousActivityId;

    /**
     * Activity tags
     */
    private java.util.List<String> tags;

    /**
     * Priority (HIGH, MEDIUM, LOW)
     */
    private String priority;

    /**
     * Sentiment (POSITIVE, NEUTRAL, NEGATIVE)
     */
    private String sentiment;

    /**
     * Create a new LeadActivity.
     *
     * @param tenantId the tenant ID
     * @param leadId the lead ID
     * @param type the activity type
     */
    public LeadActivity(String tenantId, String leadId, String type) {
        super(tenantId);
        this.leadId = leadId;
        this.type = type;
        this.timestamp = Instant.now();
        this.automated = false;
        this.attachments = new java.util.ArrayList<>();
        this.followUpActivityIds = new java.util.ArrayList<>();
        this.tags = new java.util.ArrayList<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Create a system automated activity.
     *
     * @param tenantId the tenant ID
     * @param leadId the lead ID
     * @param type the activity type
     * @param title the activity title
     * @return new LeadActivity instance
     */
    public static LeadActivity createSystemActivity(String tenantId, String leadId, String type, String title) {
        LeadActivity activity = new LeadActivity(tenantId, leadId, type);
        activity.setTitle(title);
        activity.setPerformedBy("SYSTEM");
        activity.setPerformedByRole("SYSTEM");
        activity.setDirection("INTERNAL");
        activity.setAutomated(true);
        return activity;
    }

    /**
     * Check if activity is completed.
     *
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }

    /**
     * Check if activity is pending.
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    /**
     * Check if activity is automated.
     *
     * @return true if automated
     */
    public boolean isAutomated() {
        return this.automated != null && this.automated;
    }

    /**
     * Check if activity is inbound.
     *
     * @return true if direction is INBOUND
     */
    public boolean isInbound() {
        return "INBOUND".equals(this.direction);
    }

    /**
     * Check if activity is outbound.
     *
     * @return true if direction is OUTBOUND
     */
    public boolean isOutbound() {
        return "OUTBOUND".equals(this.direction);
    }

    /**
     * Check if activity is high priority.
     *
     * @return true if priority is HIGH
     */
    public boolean isHighPriority() {
        return "HIGH".equals(this.priority);
    }

    /**
     * Check if sentiment is positive.
     *
     * @return true if sentiment is POSITIVE
     */
    public boolean isPositiveSentiment() {
        return "POSITIVE".equals(this.sentiment);
    }

    /**
     * Check if sentiment is negative.
     *
     * @return true if sentiment is NEGATIVE
     */
    public boolean isNegativeSentiment() {
        return "NEGATIVE".equals(this.sentiment);
    }

    /**
     * Mark activity as completed.
     */
    public void markAsCompleted() {
        this.status = "COMPLETED";
        this.touch();
    }

    /**
     * Mark activity as failed.
     *
     * @param reason the failure reason
     */
    public void markAsFailed(String reason) {
        this.status = "FAILED";
        this.outcome = reason;
        this.touch();
    }

    /**
     * Mark activity as pending.
     */
    public void markAsPending() {
        this.status = "PENDING";
        this.touch();
    }

    /**
     * Add follow-up activity ID.
     *
     * @param activityId the follow-up activity ID
     */
    public void addFollowUpActivity(String activityId) {
        if (this.followUpActivityIds == null) {
            this.followUpActivityIds = new java.util.ArrayList<>();
        }
        if (!this.followUpActivityIds.contains(activityId)) {
            this.followUpActivityIds.add(activityId);
            this.touch();
        }
    }

    /**
     * Add attachment URL.
     *
     * @param url the attachment URL
     */
    public void addAttachment(String url) {
        if (this.attachments == null) {
            this.attachments = new java.util.ArrayList<>();
        }
        if (!this.attachments.contains(url)) {
            this.attachments.add(url);
            this.touch();
        }
    }

    /**
     * Add tag.
     *
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new java.util.ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
            this.touch();
        }
    }

    /**
     * Add metadata.
     *
     * @param key the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Get metadata value.
     *
     * @param key the metadata key
     * @return the metadata value, or null if not set
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }

    /**
     * Set engagement score and auto-calculate sentiment.
     *
     * @param score the engagement score (0-100)
     */
    public void setEngagementScore(int score) {
        this.engagementScore = score;
        if (score >= 70) {
            this.sentiment = "POSITIVE";
        } else if (score >= 40) {
            this.sentiment = "NEUTRAL";
        } else {
            this.sentiment = "NEGATIVE";
        }
    }

    /**
     * Schedule next action.
     *
     * @param date the next action date
     * @param priority the priority level
     */
    public void scheduleNextAction(Instant date, String priority) {
        this.nextActionDate = date;
        this.priority = priority;
        this.touch();
    }
}
