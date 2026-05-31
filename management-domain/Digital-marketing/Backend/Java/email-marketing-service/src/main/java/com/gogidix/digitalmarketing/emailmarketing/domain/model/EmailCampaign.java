package com.gogidix.digitalmarketing.emailmarketing.domain.model;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmailCampaign - Represents an email marketing campaign with scheduling and targeting.
 *
 * <p>Campaigns are the main organizational unit for email marketing efforts.
 * They can be scheduled, targeted to specific lists, and tracked for performance.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "email_campaigns")
@TypeAlias("email_campaign")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "campaign_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'scheduledAt': -1}")
@CompoundIndex(name = "campaign_tenant_dates_idx", def = "{'tenantId': 1, 'startDate': 1, 'endDate': -1}")
public class EmailCampaign extends BaseEntity {

    /**
     * Campaign name (e.g., "Q1 Newsletter", "Product Launch")
     */
    @Indexed
    private String name;

    /**
     * Campaign description
     */
    private String description;

    /**
     * Campaign subject line
     */
    private String subject;

    /**
     * Preheader text (preview text shown in inbox)
     */
    private String preheader;

    /**
     * From name displayed to recipients
     */
    private String fromName;

    /**
     * Reply-to email address
     */
    private String replyToEmail;

    /**
     * Campaign status (DRAFT, SCHEDULED, SENDING, SENT, PAUSED, CANCELLED, FAILED)
     */
    @Indexed
    private String status;

    /**
     * Campaign type (NEWSLETTER, PROMOTIONAL, TRANSACTIONAL, ANNOUNCEMENT)
     */
    @Indexed
    private String campaignType;

    /**
     * ID of the template to use
     */
    @Indexed
    private String templateId;

    /**
     * ID of the email list to send to
     */
    @Indexed
    private String listId;

    /**
     * Additional segment IDs to target
     */
    private List<String> segmentIds;

    /**
     * Scheduled send date/time
     */
    @Indexed
    private Instant scheduledAt;

    /**
     * Actual start date/time
     */
    private Instant startedAt;

    /**
     * Actual completion date/time
     */
    private Instant completedAt;

    /**
     * Campaign start date (for reporting)
     */
    @Indexed
    private Instant startDate;

    /**
     * Campaign end date (for reporting)
     */
    @Indexed
    private Instant endDate;

    /**
     * Total number of recipients
     */
    private Integer totalRecipients;

    /**
     * Number of emails successfully sent
     */
    private Integer sentCount;

    /**
     * Number of emails that failed to send
     */
    private Integer failedCount;

    /**
     * Number of emails currently in queue
     */
    private Integer queuedCount;

    /**
     * Target audience tags
     */
    private List<String> tags;

    /**
     * Campaign priority (1-10, higher = more important)
     */
    private Integer priority;

    /**
     * Whether to track opens
     */
    @Builder.Default
    private Boolean trackOpens = true;

    /**
     * Whether to track clicks
     */
    @Builder.Default
    private Boolean trackClicks = true;

    /**
     * Custom UTM parameters for tracking
     */
    private Map<String, String> utmParameters;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Whether this campaign is visible in reports
     */
    @Builder.Default
    private Boolean visible = true;

    /**
     * Campaign budget (in currency units)
     */
    private Double budget;

    /**
     * Actual spend
     */
    private Double actualSpend;

    /**
     * Expected conversion rate (percentage)
     */
    private Double expectedConversionRate;

    /**
     * Owner user ID
     */
    @Indexed
    private String ownerId;

    /**
     * Team assigned to this campaign
     */
    private String teamId;

    /**
     * Associated campaign IDs (e.g., A/B test variants)
     */
    private List<String> relatedCampaignIds;

    /**
     * Parent campaign ID (for variants)
     */
    private String parentCampaignId;

    /**
     * A/B test variant name (if applicable)
     */
    private String variantName;

    /**
     * Campaign notes
     */
    private List<String> notes;

    /**
     * Create a new campaign for a tenant.
     *
     * @param tenantId the tenant ID
     * @param name     the campaign name
     * @param subject  the campaign subject
     */
    public EmailCampaign(String tenantId, String name, String subject) {
        super(tenantId);
        this.name = name;
        this.subject = subject;
        this.status = "DRAFT";
        this.segmentIds = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.relatedCampaignIds = new ArrayList<>();
        this.notes = new ArrayList<>();
        this.utmParameters = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Create a new campaign with template and list.
     *
     * @param tenantId  the tenant ID
     * @param name      the campaign name
     * @param subject   the campaign subject
     * @param templateId the template ID
     * @param listId    the list ID
     */
    public EmailCampaign(String tenantId, String name, String subject, String templateId, String listId) {
        this(tenantId, name, subject);
        this.templateId = templateId;
        this.listId = listId;
    }

    /**
     * Check if campaign is in draft status.
     *
     * @return true if draft
     */
    public boolean isDraft() {
        return "DRAFT".equals(this.status);
    }

    /**
     * Check if campaign is scheduled.
     *
     * @return true if scheduled
     */
    public boolean isScheduled() {
        return "SCHEDULED".equals(this.status);
    }

    /**
     * Check if campaign is active (sending or queued).
     *
     * @return true if active
     */
    public boolean isActive() {
        return "SENDING".equals(this.status) || "SCHEDULED".equals(this.status);
    }

    /**
     * Check if campaign is completed.
     *
     * @return true if completed
     */
    public boolean isCompleted() {
        return "SENT".equals(this.status) || "CANCELLED".equals(this.status) || "FAILED".equals(this.status);
    }

    /**
     * Check if campaign can be edited.
     *
     * @return true if editable
     */
    public boolean isEditable() {
        return "DRAFT".equals(this.status) || "SCHEDULED".equals(this.status);
    }

    /**
     * Calculate send progress percentage.
     *
     * @return progress percentage (0-100)
     */
    public double getProgress() {
        if (totalRecipients == null || totalRecipients == 0) {
            return 0.0;
        }
        int sent = sentCount != null ? sentCount : 0;
        int failed = failedCount != null ? failedCount : 0;
        return ((double) (sent + failed) / totalRecipients) * 100.0;
    }

    /**
     * Mark campaign as scheduled.
     */
    public void markAsScheduled() {
        this.status = "SCHEDULED";
        this.touch();
    }

    /**
     * Mark campaign as sending.
     */
    public void markAsSending() {
        this.status = "SENDING";
        this.startedAt = Instant.now();
        this.touch();
    }

    /**
     * Mark campaign as sent.
     */
    public void markAsSent() {
        this.status = "SENT";
        this.completedAt = Instant.now();
        this.touch();
    }

    /**
     * Mark campaign as paused.
     */
    public void markAsPaused() {
        this.status = "PAUSED";
        this.touch();
    }

    /**
     * Mark campaign as cancelled.
     */
    public void markAsCancelled() {
        this.status = "CANCELLED";
        this.completedAt = Instant.now();
        this.touch();
    }

    /**
     * Mark campaign as failed.
     */
    public void markAsFailed() {
        this.status = "FAILED";
        this.completedAt = Instant.now();
        this.touch();
    }

    /**
     * Update recipient counts.
     *
     * @param total total recipients
     * @param sent  sent count
     * @param failed failed count
     * @param queued queued count
     */
    public void updateCounts(Integer total, Integer sent, Integer failed, Integer queued) {
        this.totalRecipients = total;
        this.sentCount = sent;
        this.failedCount = failed;
        this.queuedCount = queued;
        this.touch();
    }

    /**
     * Increment sent count.
     */
    public void incrementSent() {
        this.sentCount = (this.sentCount != null ? this.sentCount : 0) + 1;
        this.touch();
    }

    /**
     * Increment failed count.
     */
    public void incrementFailed() {
        this.failedCount = (this.failedCount != null ? this.failedCount : 0) + 1;
        this.touch();
    }

    /**
     * Add a tag to the campaign.
     *
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Remove a tag from the campaign.
     *
     * @param tag the tag to remove
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Add a segment ID.
     *
     * @param segmentId the segment ID to add
     */
    public void addSegmentId(String segmentId) {
        if (this.segmentIds == null) {
            this.segmentIds = new ArrayList<>();
        }
        if (!this.segmentIds.contains(segmentId)) {
            this.segmentIds.add(segmentId);
        }
    }

    /**
     * Add UTM parameter.
     *
     * @param key   the UTM key
     * @param value the UTM value
     */
    public void addUtmParameter(String key, String value) {
        if (this.utmParameters == null) {
            this.utmParameters = new HashMap<>();
        }
        this.utmParameters.put(key, value);
    }

    /**
     * Add metadata.
     *
     * @param key   the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Add a note.
     *
     * @param note the note to add
     */
    public void addNote(String note) {
        if (this.notes == null) {
            this.notes = new ArrayList<>();
        }
        this.notes.add(note);
    }

    /**
     * Get all notes.
     *
     * @return list of notes
     */
    public List<String> getNotes() {
        return this.notes != null ? this.notes : new ArrayList<>();
    }

    /**
     * Check if campaign is overdue.
     *
     * @return true if scheduled time has passed and not sent
     */
    public boolean isOverdue() {
        return "SCHEDULED".equals(this.status) &&
            scheduledAt != null &&
            scheduledAt.isBefore(Instant.now());
    }

    /**
     * Check if campaign is within budget.
     *
     * @return true if within budget or no budget set
     */
    public boolean isWithinBudget() {
        if (budget == null || actualSpend == null) {
            return true;
        }
        return actualSpend <= budget;
    }

    /**
     * Calculate remaining budget.
     *
     * @return remaining budget, or null if no budget set
     */
    public Double getRemainingBudget() {
        if (budget == null || actualSpend == null) {
            return null;
        }
        return budget - actualSpend;
    }
}
