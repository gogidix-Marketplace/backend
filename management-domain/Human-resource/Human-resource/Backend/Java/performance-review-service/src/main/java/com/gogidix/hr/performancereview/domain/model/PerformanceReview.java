package com.gogidix.hr.performancereview.domain.model;

import com.gogidix.hr.performancereview.domain.enums.ReviewStatus;
import com.gogidix.hr.performancereview.domain.enums.ReviewType;
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
 * PerformanceReview Domain Entity
 * Represents employee performance reviews
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "performance_reviews")
public class PerformanceReview extends BaseEntity {

    @Indexed(unique = true)
    private String reviewCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String cycleId;

    private String cycleName;

    @Indexed
    private String employeeId;

    private String employeeName;
    private String employeePosition;

    @Indexed
    private String reviewerId;

    private String reviewerName;
    private String reviewerPosition;

    @Indexed
    private String secondaryReviewerId;

    private String secondaryReviewerName;

    @Indexed
    private ReviewType reviewType;

    @Indexed
    private ReviewStatus status;

    @Indexed
    private LocalDate reviewPeriodStart;

    @Indexed
    private LocalDate reviewPeriodEnd;

    private LocalDate scheduledDate;
    private LocalDate completedDate;

    private Integer overallRating;
    private Integer maxRating;

    @Builder.Default
    private List<Rating> ratings = new ArrayList<>();

    private String strengths;
    private String areasForImprovement;
    private String achievements;
    private String goalsSummary;

    @Builder.Default
    private List<String> goalIds = new ArrayList<>();

    @Builder.Default
    private List<String> feedbackIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> competencies = new HashMap<>();

    private String recommendation;
    private String nextSteps;

    @Indexed
    private Boolean isPromotionRecommended;

    private String recommendedRole;
    private String salaryRecommendation;

    @Indexed
    private String approvedBy;

    private LocalDate approvedDate;
    private String approvalComments;

    @Indexed
    private Boolean isAcknowledged;

    private LocalDate acknowledgedDate;
    private String employeeComments;

    @Builder.Default
    private List<String> attachmentIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Rating detail
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rating {
        private String category;
        private Integer score;
        private Integer maxScore;
        private String comments;
        private Double weight;
    }

    /**
     * Creates a new performance review
     */
    public static PerformanceReview create(String tenantId, String cycleId, String cycleName,
                                            String employeeId, String employeeName,
                                            String reviewerId, String reviewerName,
                                            ReviewType reviewType, LocalDate reviewPeriodStart,
                                            LocalDate reviewPeriodEnd) {
        String reviewCode = generateReviewCode(tenantId, employeeId);

        PerformanceReview review = new PerformanceReview();
        review.setTenantId(tenantId);
        review.setCycleId(cycleId);
        review.setCycleName(cycleName);
        review.setEmployeeId(employeeId);
        review.setEmployeeName(employeeName);
        review.setReviewerId(reviewerId);
        review.setReviewerName(reviewerName);
        review.setReviewType(reviewType);
        review.setReviewCode(reviewCode);
        review.setReviewPeriodStart(reviewPeriodStart);
        review.setReviewPeriodEnd(reviewPeriodEnd);
        review.setStatus(ReviewStatus.DRAFT);
        review.setMaxRating(5);
        review.setIsPromotionRecommended(false);
        review.setIsAcknowledged(false);
        review.setRatings(new ArrayList<>());
        review.setGoalIds(new ArrayList<>());
        review.setFeedbackIds(new ArrayList<>());
        review.setCompetencies(new HashMap<>());
        review.setAttachmentIds(new ArrayList<>());
        review.setMetadata(new HashMap<>());

        return review;
    }

    /**
     * Submits review
     */
    public void submit() {
        if (this.status != ReviewStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft reviews");
        }
        this.status = ReviewStatus.SUBMITTED;
    }

    /**
     * Starts review
     */
    public void start() {
        if (this.status != ReviewStatus.DRAFT) {
            throw new IllegalStateException("Can only start draft reviews");
        }
        this.status = ReviewStatus.IN_PROGRESS;
    }

    /**
     * Completes review
     */
    public void complete(Integer overallRating) {
        if (this.status != ReviewStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete in-progress reviews");
        }
        if (overallRating == null || overallRating < 1 || overallRating > this.maxRating) {
            throw new IllegalArgumentException("Invalid overall rating");
        }
        this.status = ReviewStatus.COMPLETED;
        this.overallRating = overallRating;
        this.completedDate = LocalDate.now();
    }

    /**
     * Acknowledges review
     */
    public void acknowledge(String employeeComments) {
        if (this.status != ReviewStatus.COMPLETED && this.status != ReviewStatus.APPROVED) {
            throw new IllegalStateException("Can only acknowledge completed or approved reviews");
        }
        this.isAcknowledged = true;
        this.acknowledgedDate = LocalDate.now();
        this.employeeComments = employeeComments;
    }

    /**
     * Approves review
     */
    public void approve(String approvedBy, String approvalComments) {
        if (this.status != ReviewStatus.COMPLETED) {
            throw new IllegalStateException("Can only approve completed reviews");
        }
        this.status = ReviewStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvalComments = approvalComments;
        this.approvedDate = LocalDate.now();
    }

    /**
     * Requests changes
     */
    public void requestChanges(String comments) {
        if (this.status != ReviewStatus.SUBMITTED && this.status != ReviewStatus.COMPLETED) {
            throw new IllegalStateException("Can only request changes for submitted or completed reviews");
        }
        this.status = ReviewStatus.CHANGES_REQUESTED;
        this.approvalComments = comments;
    }

    /**
     * Adds rating
     */
    public void addRating(String category, Integer score, Integer maxScore, String comments, Double weight) {
        if (this.ratings == null) {
            this.ratings = new ArrayList<>();
        }
        Rating rating = new Rating(category, score, maxScore, comments, weight);
        this.ratings.add(rating);
    }

    /**
     * Adds goal
     */
    public void addGoal(String goalId) {
        if (this.goalIds == null) {
            this.goalIds = new ArrayList<>();
        }
        if (!this.goalIds.contains(goalId)) {
            this.goalIds.add(goalId);
        }
    }

    /**
     * Adds feedback
     */
    public void addFeedback(String feedbackId) {
        if (this.feedbackIds == null) {
            this.feedbackIds = new ArrayList<>();
        }
        if (!this.feedbackIds.contains(feedbackId)) {
            this.feedbackIds.add(feedbackId);
        }
    }

    /**
     * Sets competency
     */
    public void setCompetency(String name, Object value) {
        if (this.competencies == null) {
            this.competencies = new HashMap<>();
        }
        this.competencies.put(name, value);
    }

    /**
     * Recommends promotion
     */
    public void recommendPromotion(Boolean recommended, String recommendedRole) {
        this.isPromotionRecommended = recommended;
        if (recommended && recommendedRole != null) {
            this.recommendedRole = recommendedRole;
        }
    }

    /**
     * Sets salary recommendation
     */
    public void setSalaryRecommendation(String salaryRecommendation) {
        this.salaryRecommendation = salaryRecommendation;
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
     * Calculates weighted overall rating
     */
    public Double calculateWeightedRating() {
        if (this.ratings == null || this.ratings.isEmpty()) {
            return null;
        }

        double totalWeight = 0;
        double weightedSum = 0;

        for (Rating rating : this.ratings) {
            if (rating.getWeight() != null && rating.getScore() != null) {
                totalWeight += rating.getWeight();
                weightedSum += (rating.getScore() * rating.getWeight());
            }
        }

        return totalWeight > 0 ? weightedSum / totalWeight : null;
    }

    /**
     * Checks if review is overdue
     */
    public boolean isOverdue() {
        return this.scheduledDate != null &&
               LocalDate.now().isAfter(this.scheduledDate) &&
               this.status != ReviewStatus.COMPLETED &&
               this.status != ReviewStatus.APPROVED;
    }

    /**
     * Generates review code
     */
    private static String generateReviewCode(String tenantId, String employeeId) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "PRV-" + employeeId.substring(0, 8) + "-" + uniqueId;
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
