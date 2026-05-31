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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * ReviewCycle Domain Entity
 * Represents a performance review cycle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "review_cycles")
public class ReviewCycle extends BaseEntity {

    @Indexed(unique = true)
    private String cycleCode;

    @Indexed
    private String tenantId;

    private String cycleName;
    private String description;

    @Indexed
    private LocalDate startDate;

    @Indexed
    private LocalDate endDate;

    private LocalDate reviewStartDate;
    private LocalDate reviewEndDate;

    @Indexed
    private CycleStatus status;

    @Indexed
    private String reviewType;

    @Indexed
    private String createdBy;

    @Indexed
    private Integer employeeCount;

    private Integer completedCount;
    private Integer pendingCount;

    @Builder.Default
    private List<String> reviewIds = new ArrayList<>();

    @Builder.Default
    private List<String> participantIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> configuration = new HashMap<>();

    @Builder.Default
    private Map<String, Object> milestones = new HashMap<>();

    @Builder.Default
    private List<String> reminderScheduleIds = new ArrayList<>();

    @Indexed
    private Boolean isActive;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    public enum CycleStatus {
        DRAFT,
        SCHEDULED,
        ACTIVE,
        IN_REVIEW,
        COMPLETED,
        CANCELLED,
        ARCHIVED
    }

    /**
     * Creates a new review cycle
     */
    public static ReviewCycle create(String tenantId, String cycleName, String description,
                                      LocalDate startDate, LocalDate endDate,
                                      LocalDate reviewStartDate, LocalDate reviewEndDate,
                                      String createdBy) {
        String cycleCode = generateCycleCode(tenantId, cycleName);

        ReviewCycle cycle = new ReviewCycle();
        cycle.setTenantId(tenantId);
        cycle.setCycleName(cycleName);
        cycle.setDescription(description);
        cycle.setCycleCode(cycleCode);
        cycle.setStartDate(startDate);
        cycle.setEndDate(endDate);
        cycle.setReviewStartDate(reviewStartDate);
        cycle.setReviewEndDate(reviewEndDate);
        cycle.setCreatedBy(createdBy);
        cycle.setStatus(CycleStatus.DRAFT);
        cycle.setIsActive(true);
        cycle.setEmployeeCount(0);
        cycle.setCompletedCount(0);
        cycle.setPendingCount(0);
        cycle.setReviewIds(new ArrayList<>());
        cycle.setParticipantIds(new ArrayList<>());
        cycle.setConfiguration(new HashMap<>());
        cycle.setMilestones(new HashMap<>());
        cycle.setReminderScheduleIds(new ArrayList<>());
        cycle.setTags(new ArrayList<>());
        cycle.setMetadata(new HashMap<>());

        return cycle;
    }

    /**
     * Activates cycle
     */
    public void activate() {
        if (this.status != CycleStatus.DRAFT && this.status != CycleStatus.SCHEDULED) {
            throw new IllegalStateException("Can only activate draft or scheduled cycles");
        }
        this.status = CycleStatus.ACTIVE;
    }

    /**
     * Starts review period
     */
    public void startReviewPeriod() {
        if (this.status != CycleStatus.ACTIVE) {
            throw new IllegalStateException("Can only start review period for active cycles");
        }
        this.status = CycleStatus.IN_REVIEW;
    }

    /**
     * Completes cycle
     */
    public void complete() {
        if (this.status != CycleStatus.IN_REVIEW) {
            throw new IllegalStateException("Can only complete cycles in review");
        }
        this.status = CycleStatus.COMPLETED;
    }

    /**
     * Cancels cycle
     */
    public void cancel() {
        if (this.status == CycleStatus.COMPLETED || this.status == CycleStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or already cancelled cycles");
        }
        this.status = CycleStatus.CANCELLED;
    }

    /**
     * Archives cycle
     */
    public void archive() {
        this.status = CycleStatus.ARCHIVED;
        this.isActive = false;
    }

    /**
     * Adds participant
     */
    public void addParticipant(String participantId) {
        if (this.participantIds == null) {
            this.participantIds = new ArrayList<>();
        }
        if (!this.participantIds.contains(participantId)) {
            this.participantIds.add(participantId);
            this.employeeCount = this.participantIds.size();
            this.pendingCount = this.employeeCount - this.completedCount;
        }
    }

    /**
     * Adds review
     */
    public void addReview(String reviewId) {
        if (this.reviewIds == null) {
            this.reviewIds = new ArrayList<>();
        }
        if (!this.reviewIds.contains(reviewId)) {
            this.reviewIds.add(reviewId);
        }
    }

    /**
     * Increments completed count
     */
    public void incrementCompletedCount() {
        this.completedCount = (this.completedCount != null ? this.completedCount : 0) + 1;
        this.pendingCount = this.employeeCount - this.completedCount;
    }

    /**
     * Sets configuration
     */
    public void setConfiguration(String key, Object value) {
        if (this.configuration == null) {
            this.configuration = new HashMap<>();
        }
        this.configuration.put(key, value);
    }

    /**
     * Sets milestone
     */
    public void setMilestone(String name, Object date) {
        if (this.milestones == null) {
            this.milestones = new HashMap<>();
        }
        this.milestones.put(name, date);
    }

    /**
     * Adds reminder schedule
     */
    public void addReminderSchedule(String scheduleId) {
        if (this.reminderScheduleIds == null) {
            this.reminderScheduleIds = new ArrayList<>();
        }
        if (!this.reminderScheduleIds.contains(scheduleId)) {
            this.reminderScheduleIds.add(scheduleId);
        }
    }

    /**
     * Adds tag
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
     * Gets completion percentage
     */
    public Double getCompletionPercentage() {
        if (this.employeeCount == null || this.employeeCount == 0) {
            return 0.0;
        }
        return (this.completedCount != null ? this.completedCount : 0) * 100.0 / this.employeeCount;
    }

    /**
     * Checks if is active
     */
    public boolean isActiveCycle() {
        return this.isActive && (this.status == CycleStatus.ACTIVE || this.status == CycleStatus.IN_REVIEW);
    }

    /**
     * Generates cycle code
     */
    private static String generateCycleCode(String tenantId, String cycleName) {
        String normalized = cycleName.toUpperCase().replaceAll("\\s+", "_");
        String uniqueId = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "CYC-" + normalized + "-" + uniqueId;
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
