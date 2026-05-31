package com.gogidix.digitalmarketing.socialmedia.domain.model;

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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * SocialCalendar - Content calendar for scheduling
 *
 * <p>Represents scheduled content entries in a calendar view.
 * Each entry can represent a post, campaign, or content block.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "social_calendar")
@TypeAlias("social_calendar")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "calendar_tenant_date_idx", def = "{'tenantId': 1, 'scheduledDate': 1}")
@CompoundIndex(name = "calendar_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'scheduledDate': 1}")
public class SocialCalendar extends BaseEntity {

    /**
     * Entry title
     */
    @Indexed
    private String title;

    /**
     * Entry description
     */
    private String description;

    /**
     * Scheduled date
     */
    @Indexed
    private LocalDate scheduledDate;

    /**
     * Scheduled time
     */
    private Instant scheduledTime;

    /**
     * Duration in minutes
     */
    private Integer durationMinutes;

    /**
     * Entry type (POST, CAMPAIGN, EVENT, HOLIDAY, NOTE)
     */
    @Indexed
    private String entryType;

    /**
     * Entry status (PLANNED, SCHEDULED, PUBLISHED, CANCELLED, POSTPONED)
     */
    @Indexed
    private String status;

    /**
     * Associated post IDs
     */
    private java.util.List<String> postIds;

    /**
     * Associated campaign ID
     */
    private String campaignId;

    /**
     * Associated content library IDs
     */
    private java.util.List<String> contentIds;

    /**
     * Platform(s) for this entry
     */
    private java.util.List<String> platforms;

    /**
     * Account ID(s)
     */
    private java.util.List<String> accountIds;

    /**
     * Color code for calendar display
     */
    private String color;

    /**
     * Priority (LOW, NORMAL, HIGH, URGENT)
     */
    @Builder.Default
    private String priority = "NORMAL";

    /**
     * Is all-day event
     */
    @Builder.Default
    private Boolean allDay = false;

    /**
     * Reminder time (minutes before event)
     */
    private Integer reminderMinutes;

    /**
     * Recurrence rule (for recurring entries)
     */
    private String recurrenceRule;

    /**
     * Recurrence end date
     */
    private LocalDate recurrenceEndDate;

    /**
     * Parent entry ID (for recurring series)
     */
    private String parentEntryId;

    /**
     * Tags for categorization
     */
    private java.util.List<String> tags;

    /**
     * Calendar category
     */
    private String category;

    /**
     * Assigned user ID
     */
    private String assignedTo;

    /**
     * Created by user ID
     */
    private String createdByUser;

    /**
     * Collaborators
     */
    private java.util.List<String> collaborators;

    /**
     * Location (for events)
     */
    private String location;

    /**
     * Meeting URL (for virtual events)
     */
    private String meetingUrl;

    /**
     * Estimated engagement
     */
    private Long estimatedEngagement;

    /**
     * Budget allocated
     */
    private java.math.BigDecimal budget;

    /**
     * Actual spend
     */
    private java.math.BigDecimal actualSpend;

    /**
     * Approval status (DRAFT, PENDING, APPROVED, REJECTED)
     */
    @Builder.Default
    private String approvalStatus = "DRAFT";

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Completed timestamp
     */
    private Instant completedAt;

    /**
     * Completed by user ID
     */
    private String completedBy;

    /**
     * Completion notes
     */
    private String completionNotes;

    /**
     * Create a new SocialCalendar entry.
     *
     * @param tenantId the tenant ID
     * @param title the title
     * @param scheduledDate the scheduled date
     * @param entryType the entry type
     */
    public SocialCalendar(String tenantId, String title, LocalDate scheduledDate, String entryType) {
        super(tenantId);
        this.title = title;
        this.scheduledDate = scheduledDate;
        this.entryType = entryType;
        this.status = "PLANNED";
        this.priority = "NORMAL";
        this.allDay = false;
        this.approvalStatus = "DRAFT";
    }

    /**
     * Schedule the entry.
     *
     * @param scheduledTime the scheduled time
     */
    public void schedule(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
        this.status = "SCHEDULED";
        this.touch();
    }

    /**
     * Mark as published.
     */
    public void markAsPublished() {
        this.status = "PUBLISHED";
        this.completedAt = Instant.now();
        this.touch();
    }

    /**
     * Mark as cancelled.
     */
    public void cancel() {
        this.status = "CANCELLED";
        this.touch();
    }

    /**
     * Postpone to new date.
     *
     * @param newDate new scheduled date
     * @param newTime new scheduled time
     */
    public void postpone(LocalDate newDate, Instant newTime) {
        this.scheduledDate = newDate;
        this.scheduledTime = newTime;
        this.status = "POSTPONED";
        this.touch();
    }

    /**
     * Complete the entry.
     *
     * @param completedBy user completing
     * @param notes completion notes
     */
    public void complete(String completedBy, String notes) {
        this.status = "PUBLISHED";
        this.completedAt = Instant.now();
        this.completedBy = completedBy;
        this.completionNotes = notes;
        this.touch();
    }

    /**
     * Assign to user.
     *
     * @param userId user to assign to
     */
    public void assignTo(String userId) {
        this.assignedTo = userId;
        this.touch();
    }

    /**
     * Add collaborator.
     *
     * @param userId user ID
     */
    public void addCollaborator(String userId) {
        if (this.collaborators == null) {
            this.collaborators = new java.util.ArrayList<>();
        }
        if (!this.collaborators.contains(userId)) {
            this.collaborators.add(userId);
        }
        this.touch();
    }

    /**
     * Remove collaborator.
     *
     * @param userId user ID
     */
    public void removeCollaborator(String userId) {
        if (this.collaborators != null) {
            this.collaborators.remove(userId);
            this.touch();
        }
    }

    /**
     * Add post to entry.
     *
     * @param postId post ID
     */
    public void addPost(String postId) {
        if (this.postIds == null) {
            this.postIds = new java.util.ArrayList<>();
        }
        if (!this.postIds.contains(postId)) {
            this.postIds.add(postId);
        }
        this.touch();
    }

    /**
     * Add content to entry.
     *
     * @param contentId content ID
     */
    public void addContent(String contentId) {
        if (this.contentIds == null) {
            this.contentIds = new java.util.ArrayList<>();
        }
        if (!this.contentIds.contains(contentId)) {
            this.contentIds.add(contentId);
        }
        this.touch();
    }

    /**
     * Check if entry is for today.
     *
     * @return true if scheduled for today
     */
    public boolean isToday() {
        return this.scheduledDate != null && this.scheduledDate.equals(LocalDate.now());
    }

    /**
     * Check if entry is upcoming (in the future).
     *
     * @return true if upcoming
     */
    public boolean isUpcoming() {
        return this.scheduledDate != null && this.scheduledDate.isAfter(LocalDate.now());
    }

    /**
     * Check if entry is overdue.
     *
     * @return true if overdue
     */
    public boolean isOverdue() {
        if (this.scheduledDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        boolean datePast = this.scheduledDate.isBefore(today);
        boolean timePast = this.scheduledTime != null && this.scheduledTime.isBefore(Instant.now());
        return datePast || (this.scheduledDate.equals(today) && timePast);
    }

    /**
     * Check if entry is completed.
     *
     * @return true if completed
     */
    public boolean isCompleted() {
        return "PUBLISHED".equals(this.status);
    }

    /**
     * Check if entry is scheduled.
     *
     * @return true if scheduled
     */
    public boolean isScheduled() {
        return "SCHEDULED".equals(this.status);
    }

    /**
     * Approve entry.
     *
     * @param approvedBy user approving
     */
    public void approve(String approvedBy) {
        this.approvalStatus = "APPROVED";
        this.touch();
    }

    /**
     * Reject entry.
     *
     * @param reason rejection reason
     */
    public void reject(String reason) {
        this.approvalStatus = "REJECTED";
        addMetadata("rejectionReason", reason);
        this.touch();
    }

    /**
     * Add tag to entry.
     *
     * @param tag tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new java.util.ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
        this.touch();
    }

    /**
     * Add metadata to entry.
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
     * Get a metadata value.
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
     * Set budget and calculate remaining.
     *
     * @param budget allocated budget
     */
    public void setBudget(java.math.BigDecimal budget) {
        this.budget = budget;
        this.touch();
    }

    /**
     * Record actual spend.
     *
     * @param spend actual spend amount
     */
    public void recordSpend(java.math.BigDecimal spend) {
        this.actualSpend = spend;
        this.touch();
    }

    /**
     * Get remaining budget.
     *
     * @return remaining budget or null if not set
     */
    public java.math.BigDecimal getRemainingBudget() {
        if (budget != null && actualSpend != null) {
            return budget.subtract(actualSpend);
        }
        return budget;
    }

    /**
     * Check if over budget.
     *
     * @return true if over budget
     */
    public boolean isOverBudget() {
        if (budget != null && actualSpend != null) {
            return actualSpend.compareTo(budget) > 0;
        }
        return false;
    }

    /**
     * Get estimated duration in hours.
     *
     * @return duration in hours or null
     */
    public Double getDurationHours() {
        if (durationMinutes != null) {
            return durationMinutes / 60.0;
        }
        return null;
    }

    /**
     * Check if entry is high priority.
     *
     * @return true if high priority
     */
    public boolean isHighPriority() {
        return "HIGH".equals(this.priority) || "URGENT".equals(this.priority);
    }

    /**
     * Get number of posts associated.
     *
     * @return post count
     */
    public int getPostCount() {
        return postIds != null ? postIds.size() : 0;
    }

    /**
     * Get number of content items associated.
     *
     * @return content count
     */
    public int getContentCount() {
        return contentIds != null ? contentIds.size() : 0;
    }
}
