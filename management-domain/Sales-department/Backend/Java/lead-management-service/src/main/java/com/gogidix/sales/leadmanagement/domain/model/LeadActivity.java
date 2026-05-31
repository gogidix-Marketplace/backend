package com.gogidix.sales.leadmanagement.domain.model;

import com.gogidix.sales.leadmanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Lead Activity Domain Entity
 * Tracks all activities and interactions with a lead
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "lead_activities")
public class LeadActivity extends BaseEntity {

    @Indexed
    private String activityId;

    @Indexed
    private String leadId;

    @Indexed
    private String tenantId;

    private ActivityType activityType;

    private String subject;

    private String description;

    private String createdBy;

    private String createdByName;

    private Instant dueDate;

    private Instant completedAt;

    private Priority priority;

    private ActivityStatus status;

    private Integer durationMinutes;

    private String outcome;

    // Activity Type
    public enum ActivityType {
        EMAIL,
        CALL,
        MEETING,
        NOTE,
        TASK,
        WEB_VISIT,
        FORM_SUBMIT,
        DEMO_REQUEST,
        PRICING_VIEW,
        STAGE_CHANGE,
        STATUS_CHANGE,
        ASSIGNMENT,
        CONVERSION,
        EMAIL_OPEN,
        EMAIL_CLICK,
        SOCIAL_ENGAGEMENT,
        OTHER
    }

    // Priority
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    // Status
    public enum ActivityStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        DEFERRED
    }

    /**
     * Creates a new activity
     */
    public static LeadActivity create(String leadId, String tenantId, String createdBy,
                                     ActivityType type, String subject, String description) {
        LeadActivity activity = LeadActivity.builder()
                .leadId(leadId)
                .tenantId(tenantId)
                .createdBy(createdBy)
                .activityType(type)
                .subject(subject)
                .description(description)
                .status(ActivityStatus.COMPLETED)
                .priority(Priority.MEDIUM)
                .build();

        activity.generateActivityId();
        activity.completedAt = Instant.now();

        return activity;
    }

    /**
     * Schedules a new activity
     */
    public static LeadActivity schedule(String leadId, String tenantId, String createdBy,
                                       ActivityType type, String subject, Instant dueDate,
                                       Priority priority) {
        LeadActivity activity = LeadActivity.builder()
                .leadId(leadId)
                .tenantId(tenantId)
                .createdBy(createdBy)
                .activityType(type)
                .subject(subject)
                .dueDate(dueDate)
                .status(ActivityStatus.PENDING)
                .priority(priority != null ? priority : Priority.MEDIUM)
                .build();

        activity.generateActivityId();

        return activity;
    }

    /**
     * Marks the activity as completed
     */
    public void complete(String outcome, Integer durationMinutes) {
        this.status = ActivityStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.outcome = outcome;
        this.durationMinutes = durationMinutes;
    }

    /**
     * Marks the activity as in progress
     */
    public void start() {
        this.status = ActivityStatus.IN_PROGRESS;
    }

    /**
     * Cancels the activity
     */
    public void cancel(String reason) {
        this.status = ActivityStatus.CANCELLED;
        this.outcome = "Cancelled: " + reason;
    }

    /**
     * Defers the activity
     */
    public void defer(Instant newDueDate, String reason) {
        this.status = ActivityStatus.DEFERRED;
        this.dueDate = newDueDate;
        this.outcome = "Deferred: " + reason;
    }

    /**
     * Updates the priority
     */
    public void updatePriority(Priority newPriority) {
        this.priority = newPriority;
    }

    /**
     * Checks if activity is overdue
     */
    public boolean isOverdue() {
        return this.status == ActivityStatus.PENDING &&
                this.dueDate != null &&
                this.dueDate.isBefore(Instant.now());
    }

    /**
     * Checks if activity is completed
     */
    public boolean isCompleted() {
        return this.status == ActivityStatus.COMPLETED;
    }

    /**
     * Generates a unique activity ID
     */
    private void generateActivityId() {
        if (this.activityId == null) {
            this.activityId = java.util.UUID.randomUUID().toString();
        }
    }
}
