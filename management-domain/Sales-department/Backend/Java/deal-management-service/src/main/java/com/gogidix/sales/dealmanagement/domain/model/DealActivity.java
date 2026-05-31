package com.gogidix.sales.dealmanagement.domain.model;

import com.gogidix.sales.dealmanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Deal Activity Domain Entity
 * Tracks activities and interactions on a deal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "deal_activities")
public class DealActivity extends BaseEntity {

    @Indexed
    private String activityId;

    @Indexed
    private String tenantId;

    @Indexed
    private String dealId;

    private ActivityType activityType;

    private String subject;

    private String description;

    private String userId;

    private String userName;

    private Instant activityDate;

    private ActivityStatus status;

    private Priority priority;

    private Instant dueDate;

    private Boolean isCompleted;

    private Instant completedAt;

    private String completedBy;

    private String outcome;

    private String location;

    private Integer durationMinutes;

    private Boolean isAllDay;

    private String relatedEntityType;

    private String relatedEntityId;

    private String notes;

    @Builder.Default
    private Integer reminderMinutesBefore = 0;

    private Boolean reminderSent;

    private String attachmentUrl;

    private Boolean isPrivate;

    public enum ActivityType {
        CALL,
        EMAIL,
        MEETING,
        TASK,
        NOTE,
        DEMO,
        PROPOSAL_SENT,
        STAGE_CHANGE,
        DEAL_WON,
        DEAL_LOST,
        FOLLOW_UP,
        SITE_VISIT,
        CONTRACT_REVIEW,
        OTHER
    }

    public enum ActivityStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW,
        RESCHEDULED
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    /**
     * Creates a new activity
     */
    public static DealActivity create(String dealId, String tenantId, String userId,
                                       ActivityType type, String subject, String notes) {
        DealActivity activity = DealActivity.builder()
                .dealId(dealId)
                .tenantId(tenantId)
                .userId(userId)
                .activityType(type)
                .subject(subject)
                .description(notes)
                .activityDate(Instant.now())
                .status(ActivityStatus.COMPLETED)
                .isCompleted(true)
                .completedAt(Instant.now())
                .isPrivate(false)
                .build();

        return activity;
    }

    /**
     * Schedules a future activity
     */
    public static DealActivity schedule(String dealId, String tenantId, String userId,
                                         ActivityType type, String subject, Instant dueDate,
                                         Priority priority) {
        DealActivity activity = DealActivity.builder()
                .dealId(dealId)
                .tenantId(tenantId)
                .userId(userId)
                .activityType(type)
                .subject(subject)
                .activityDate(Instant.now())
                .dueDate(dueDate)
                .status(ActivityStatus.SCHEDULED)
                .isCompleted(false)
                .priority(priority)
                .isPrivate(false)
                .build();

        return activity;
    }

    /**
     * Marks the activity as completed
     */
    public void complete(String userId, String outcome) {
        if (Boolean.TRUE.equals(this.isCompleted)) {
            throw new IllegalStateException("Activity is already completed");
        }

        this.isCompleted = true;
        this.completedAt = Instant.now();
        this.completedBy = userId;
        this.status = ActivityStatus.COMPLETED;
        this.outcome = outcome;
    }

    /**
     * Cancels the activity
     */
    public void cancel(String reason) {
        if (Boolean.TRUE.equals(this.isCompleted)) {
            throw new IllegalStateException("Cannot cancel completed activity");
        }

        this.status = ActivityStatus.CANCELLED;
        this.description = reason;
    }

    /**
     * Reschedules the activity
     */
    public void reschedule(Instant newDueDate, String reason) {
        if (Boolean.TRUE.equals(this.isCompleted)) {
            throw new IllegalStateException("Cannot reschedule completed activity");
        }

        this.dueDate = newDueDate;
        this.status = ActivityStatus.RESCHEDULED;
        this.notes = reason;
    }

    /**
     * Checks if the activity is overdue
     */
    public boolean isOverdue() {
        return !Boolean.TRUE.equals(this.isCompleted)
                && this.dueDate != null
                && this.dueDate.isBefore(Instant.now());
    }

    /**
     * Checks if a reminder should be sent
     */
    public boolean shouldSendReminder() {
        return !Boolean.TRUE.equals(this.isCompleted)
                && !Boolean.TRUE.equals(this.reminderSent)
                && this.dueDate != null
                && this.reminderMinutesBefore != null
                && this.reminderMinutesBefore > 0;
    }

    /**
     * Marks reminder as sent
     */
    public void markReminderSent() {
        this.reminderSent = true;
    }
}
