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
 * ReviewGoal Domain Entity
 * Represents goals set during performance reviews
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "review_goals")
public class ReviewGoal extends BaseEntity {

    @Indexed(unique = true)
    private String goalCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String reviewId;

    private String reviewCode;

    @Indexed
    private String employeeId;

    private String employeeName;

    @Indexed
    private String reviewerId;

    private String reviewerName;

    private String title;
    private String description;

    @Indexed
    private GoalCategory category;

    @Indexed
    private GoalStatus status;

    @Indexed
    private Integer priority;

    private LocalDate targetDate;
    private LocalDate startDate;

    @Builder.Default
    private List<Milestone> milestones = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metrics = new HashMap<>();

    private String successCriteria;

    @Builder.Default
    private List<String> requiredResources = new ArrayList<>();

    @Builder.Default
    private List<String> dependencies = new ArrayList<>();

    private Integer weight;
    private Integer achievementPercentage;

    private String progressNotes;
    private String managerComments;

    @Indexed
    private Boolean isStretchGoal;

    @Builder.Default
    private List<String> tagIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    public enum GoalCategory {
        PERFORMANCE,
        DEVELOPMENT,
        LEARNING,
        PROJECT,
        BEHAVIORAL,
        LEADERSHIP,
        TECHNICAL,
        SOFT_SKILLS
    }

    public enum GoalStatus {
        DRAFT,
        APPROVED,
        IN_PROGRESS,
        ON_TRACK,
        AT_RISK,
        BEHIND_SCHEDULE,
        COMPLETED,
        CANCELLED,
        ON_HOLD
    }

    /**
     * Milestone
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Milestone {
        private String title;
        private String description;
        private LocalDate dueDate;
        private Boolean isCompleted;
        private LocalDate completedDate;
    }

    /**
     * Creates a new review goal
     */
    public static ReviewGoal create(String tenantId, String reviewId, String reviewCode,
                                      String employeeId, String employeeName,
                                      String reviewerId, String reviewerName,
                                      String title, String description,
                                      GoalCategory category, LocalDate targetDate) {
        String goalCode = generateGoalCode(reviewCode, employeeId);

        ReviewGoal goal = new ReviewGoal();
        goal.setTenantId(tenantId);
        goal.setReviewId(reviewId);
        goal.setReviewCode(reviewCode);
        goal.setEmployeeId(employeeId);
        goal.setEmployeeName(employeeName);
        goal.setReviewerId(reviewerId);
        goal.setReviewerName(reviewerName);
        goal.setTitle(title);
        goal.setDescription(description);
        goal.setCategory(category);
        goal.setGoalCode(goalCode);
        goal.setStatus(GoalStatus.DRAFT);
        goal.setTargetDate(targetDate);
        goal.setStartDate(LocalDate.now());
        goal.setIsStretchGoal(false);
        goal.setMilestones(new ArrayList<>());
        goal.setMetrics(new HashMap<>());
        goal.setRequiredResources(new ArrayList<>());
        goal.setDependencies(new ArrayList<>());
        goal.setTagIds(new ArrayList<>());
        goal.setMetadata(new HashMap<>());

        return goal;
    }

    /**
     * Approves goal
     */
    public void approve() {
        if (this.status != GoalStatus.DRAFT) {
            throw new IllegalStateException("Can only approve draft goals");
        }
        this.status = GoalStatus.APPROVED;
    }

    /**
     * Starts goal
     */
    public void start() {
        if (this.status != GoalStatus.APPROVED) {
            throw new IllegalStateException("Can only start approved goals");
        }
        this.status = GoalStatus.IN_PROGRESS;
    }

    /**
     * Updates progress
     */
    public void updateProgress(Integer achievementPercentage, String progressNotes) {
        if (this.status != GoalStatus.IN_PROGRESS && this.status != GoalStatus.ON_TRACK &&
            this.status != GoalStatus.AT_RISK && this.status != GoalStatus.BEHIND_SCHEDULE) {
            throw new IllegalStateException("Can only update progress for active goals");
        }
        this.achievementPercentage = achievementPercentage;
        this.progressNotes = progressNotes;

        // Auto-update status based on progress
        if (achievementPercentage != null && achievementPercentage >= 100) {
            this.status = GoalStatus.COMPLETED;
        } else if (achievementPercentage != null && achievementPercentage < 50) {
            this.status = GoalStatus.AT_RISK;
        }
    }

    /**
     * Completes goal
     */
    public void complete(String notes) {
        if (this.status != GoalStatus.IN_PROGRESS && this.status != GoalStatus.ON_TRACK) {
            throw new IllegalStateException("Can only complete active goals");
        }
        this.status = GoalStatus.COMPLETED;
        this.achievementPercentage = 100;
        this.progressNotes = notes;
    }

    /**
     * Cancels goal
     */
    public void cancel(String reason) {
        if (this.status == GoalStatus.COMPLETED || this.status == GoalStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or already cancelled goals");
        }
        this.status = GoalStatus.CANCELLED;
        this.progressNotes = reason;
    }

    /**
     * Puts goal on hold
     */
    public void putOnHold(String reason) {
        if (this.status == GoalStatus.COMPLETED || this.status == GoalStatus.CANCELLED ||
            this.status == GoalStatus.ON_HOLD) {
            throw new IllegalStateException("Cannot put goal on hold in current status");
        }
        this.status = GoalStatus.ON_HOLD;
        this.progressNotes = reason;
    }

    /**
     * Resumes goal
     */
    public void resume() {
        if (this.status != GoalStatus.ON_HOLD) {
            throw new IllegalStateException("Can only resume goals on hold");
        }
        this.status = GoalStatus.IN_PROGRESS;
    }

    /**
     * Sets as at risk
     */
    public void setAtRisk(String reason) {
        this.status = GoalStatus.AT_RISK;
        this.progressNotes = reason;
    }

    /**
     * Sets as behind schedule
     */
    public void setBehindSchedule(String reason) {
        this.status = GoalStatus.BEHIND_SCHEDULE;
        this.progressNotes = reason;
    }

    /**
     * Sets as on track
     */
    public void setOnTrack() {
        this.status = GoalStatus.ON_TRACK;
    }

    /**
     * Adds milestone
     */
    public void addMilestone(String title, String description, LocalDate dueDate) {
        if (this.milestones == null) {
            this.milestones = new ArrayList<>();
        }
        Milestone milestone = new Milestone();
        milestone.setTitle(title);
        milestone.setDescription(description);
        milestone.setDueDate(dueDate);
        milestone.setIsCompleted(false);
        this.milestones.add(milestone);
    }

    /**
     * Completes milestone
     */
    public void completeMilestone(int index) {
        if (this.milestones == null || index < 0 || index >= this.milestones.size()) {
            throw new IllegalArgumentException("Invalid milestone index");
        }
        Milestone milestone = this.milestones.get(index);
        milestone.setIsCompleted(true);
        milestone.setCompletedDate(LocalDate.now());
    }

    /**
     * Sets metric
     */
    public void setMetric(String name, Object value) {
        if (this.metrics == null) {
            this.metrics = new HashMap<>();
        }
        this.metrics.put(name, value);
    }

    /**
     * Adds required resource
     */
    public void addRequiredResource(String resource) {
        if (this.requiredResources == null) {
            this.requiredResources = new ArrayList<>();
        }
        if (!this.requiredResources.contains(resource)) {
            this.requiredResources.add(resource);
        }
    }

    /**
     * Adds dependency
     */
    public void addDependency(String dependencyId) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList<>();
        }
        if (!this.dependencies.contains(dependencyId)) {
            this.dependencies.add(dependencyId);
        }
    }

    /**
     * Adds tag
     */
    public void addTag(String tagId) {
        if (this.tagIds == null) {
            this.tagIds = new ArrayList<>();
        }
        if (!this.tagIds.contains(tagId)) {
            this.tagIds.add(tagId);
        }
    }

    /**
     * Sets as stretch goal
     */
    public void setAsStretchGoal() {
        this.isStretchGoal = true;
    }

    /**
     * Checks if is overdue
     */
    public boolean isOverdue() {
        return this.targetDate != null && LocalDate.now().isAfter(this.targetDate) &&
               this.status != GoalStatus.COMPLETED && this.status != GoalStatus.CANCELLED;
    }

    /**
     * Checks if is on track
     */
    public boolean isOnTrack() {
        return this.status == GoalStatus.ON_TRACK || this.status == GoalStatus.IN_PROGRESS;
    }

    /**
     * Gets milestone completion percentage
     */
    public Double getMilestoneCompletionPercentage() {
        if (this.milestones == null || this.milestones.isEmpty()) {
            return null;
        }
        long completed = this.milestones.stream().filter(Milestone::getIsCompleted).count();
        return (completed * 100.0) / this.milestones.size();
    }

    /**
     * Generates goal code
     */
    private static String generateGoalCode(String reviewCode, String employeeId) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "GL-" + reviewCode.substring(4) + "-" + uniqueId;
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
