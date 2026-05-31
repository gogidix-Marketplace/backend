package com.gogidix.courier.performanceservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity representing a Driver's Performance.
 * Tracks driver performance metrics and scores.
 */
@Document(collection = "driver_performance")
@CompoundIndex(name = "idx_driver_date", def = "{'tenantId': 1, 'driverId': 1, 'periodStart': -1}")
public class DriverPerformance {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("period_start")
    private LocalDate periodStart;

    @Field("period_end")
    private LocalDate periodEnd;

    @Field("overall_score")
    private Double overallScore;

    @Field("performance_tier")
    private PerformanceTier performanceTier;

    @Field("metrics")
    private PerformanceMetrics metrics;

    @Field("reviews")
    private List<PerformanceReview> reviews;

    @Field("achievements")
    private List<String> achievements;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected DriverPerformance() {
    }

    /**
     * Create a new DriverPerformance.
     *
     * @param tenantId    the tenant identifier
     * @param driverId    the driver identifier
     * @param periodStart the period start date
     * @param periodEnd   the period end date
     */
    public DriverPerformance(String tenantId, String driverId, LocalDate periodStart, LocalDate periodEnd) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.periodStart = Objects.requireNonNull(periodStart, "periodStart is required");
        this.periodEnd = Objects.requireNonNull(periodEnd, "periodEnd is required");
        this.overallScore = 0.0;
        this.performanceTier = PerformanceTier.UNRATED;
        this.metrics = new PerformanceMetrics();
        this.reviews = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Calculate overall score based on metrics.
     *
     * @param weights the scoring weights
     */
    public void calculateScore(ScoringWeights weights) {
        if (metrics == null) {
            this.overallScore = 0.0;
            return;
        }

        double score = 0.0;
        score += metrics.getOnTimeDeliveryRate() * weights.onTimeDelivery();
        score += metrics.getCompletionRate() * weights.completionRate();
        score += metrics.getAverageRating() / 5.0 * weights.customerRating();
        score += metrics.getRouteEfficiency() * weights.routeEfficiency();

        this.overallScore = Math.min(5.0, Math.max(0.0, score));
        this.performanceTier = PerformanceTier.fromScore(this.overallScore);
        this.updatedAt = Instant.now();
    }

    /**
     * Update performance metrics.
     *
     * @param newMetrics the new metrics
     */
    public void updateMetrics(PerformanceMetrics newMetrics) {
        this.metrics = Objects.requireNonNull(newMetrics, "metrics cannot be null");
        this.updatedAt = Instant.now();
    }

    /**
     * Add a performance review.
     *
     * @param review the review to add
     */
    public void addReview(PerformanceReview review) {
        this.reviews.add(Objects.requireNonNull(review, "review cannot be null"));
        this.updatedAt = Instant.now();
    }

    /**
     * Add an achievement.
     *
     * @param achievement the achievement to add
     */
    public void addAchievement(String achievement) {
        if (achievement != null && !achievement.isBlank()) {
            this.achievements.add(achievement);
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Check if performance meets minimum threshold.
     *
     * @param threshold the minimum score threshold
     * @return true if meets threshold
     */
    public boolean meetsThreshold(double threshold) {
        return this.overallScore >= threshold;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDriverId() {
        return driverId;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public PerformanceTier getPerformanceTier() {
        return performanceTier;
    }

    public PerformanceMetrics getMetrics() {
        return metrics;
    }

    public List<PerformanceReview> getReviews() {
        return reviews;
    }

    public List<String> getAchievements() {
        return achievements;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Setters for persistence
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    protected void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    protected void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    protected void setPerformanceTier(PerformanceTier performanceTier) {
        this.performanceTier = performanceTier;
    }

    protected void setMetrics(PerformanceMetrics metrics) {
        this.metrics = metrics;
    }

    protected void setReviews(List<PerformanceReview> reviews) {
        this.reviews = reviews;
    }

    protected void setAchievements(List<String> achievements) {
        this.achievements = achievements;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Performance tier enum.
     */
    public enum PerformanceTier {
        EXCELLENT(5, "Excellent"),
        GOOD(4, "Good"),
        AVERAGE(3, "Average"),
        POOR(2, "Poor"),
        UNSATISFACTORY(1, "Unsatisfactory"),
        UNRATED(0, "Not Rated");

        private final int level;
        private final String description;

        PerformanceTier(int level, String description) {
            this.level = level;
            this.description = description;
        }

        public static PerformanceTier fromScore(double score) {
            if (score >= 4.5) return EXCELLENT;
            if (score >= 4.0) return GOOD;
            if (score >= 3.5) return AVERAGE;
            if (score >= 3.0) return POOR;
            if (score > 0) return UNSATISFACTORY;
            return UNRATED;
        }

        public int getLevel() {
            return level;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Scoring weights record.
     */
    public record ScoringWeights(
            double onTimeDelivery,
            double completionRate,
            double customerRating,
            double routeEfficiency
    ) {
        public ScoringWeights {
            double total = onTimeDelivery + completionRate + customerRating + routeEfficiency;
            if (Math.abs(total - 1.0) > 0.01) {
                throw new IllegalArgumentException("Weights must sum to 1.0");
            }
        }
    }

    /**
     * Performance metrics value object.
     */
    public static class PerformanceMetrics {
        @Field("total_deliveries")
        private Integer totalDeliveries;

        @Field("completed_deliveries")
        private Integer completedDeliveries;

        @Field("on_time_deliveries")
        private Integer onTimeDeliveries;

        @Field("cancelled_deliveries")
        private Integer cancelledDeliveries;

        @Field("average_rating")
        private Double averageRating;

        @Field("total_ratings")
        private Integer totalRatings;

        @Field("route_efficiency")
        private Double routeEfficiency;

        @Field("average_delivery_time")
        private Integer averageDeliveryTime; // in minutes

        @Field("total_distance")
        private Double totalDistance; // in km

        public PerformanceMetrics() {
            this.totalDeliveries = 0;
            this.completedDeliveries = 0;
            this.onTimeDeliveries = 0;
            this.cancelledDeliveries = 0;
            this.averageRating = 0.0;
            this.totalRatings = 0;
            this.routeEfficiency = 0.0;
            this.averageDeliveryTime = 0;
            this.totalDistance = 0.0;
        }

        public double getCompletionRate() {
            return totalDeliveries > 0 ? (double) completedDeliveries / totalDeliveries : 0.0;
        }

        public double getOnTimeDeliveryRate() {
            return completedDeliveries > 0 ? (double) onTimeDeliveries / completedDeliveries : 0.0;
        }

        // Getters and Setters
        public Integer getTotalDeliveries() {
            return totalDeliveries;
        }

        public void setTotalDeliveries(Integer totalDeliveries) {
            this.totalDeliveries = totalDeliveries;
        }

        public Integer getCompletedDeliveries() {
            return completedDeliveries;
        }

        public void setCompletedDeliveries(Integer completedDeliveries) {
            this.completedDeliveries = completedDeliveries;
        }

        public Integer getOnTimeDeliveries() {
            return onTimeDeliveries;
        }

        public void setOnTimeDeliveries(Integer onTimeDeliveries) {
            this.onTimeDeliveries = onTimeDeliveries;
        }

        public Integer getCancelledDeliveries() {
            return cancelledDeliveries;
        }

        public void setCancelledDeliveries(Integer cancelledDeliveries) {
            this.cancelledDeliveries = cancelledDeliveries;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }

        public Integer getTotalRatings() {
            return totalRatings;
        }

        public void setTotalRatings(Integer totalRatings) {
            this.totalRatings = totalRatings;
        }

        public Double getRouteEfficiency() {
            return routeEfficiency;
        }

        public void setRouteEfficiency(Double routeEfficiency) {
            this.routeEfficiency = routeEfficiency;
        }

        public Integer getAverageDeliveryTime() {
            return averageDeliveryTime;
        }

        public void setAverageDeliveryTime(Integer averageDeliveryTime) {
            this.averageDeliveryTime = averageDeliveryTime;
        }

        public Double getTotalDistance() {
            return totalDistance;
        }

        public void setTotalDistance(Double totalDistance) {
            this.totalDistance = totalDistance;
        }
    }

    /**
     * Performance review value object.
     */
    public static class PerformanceReview {
        @Field("review_id")
        private String reviewId;

        @Field("reviewer_id")
        private String reviewerId;

        @Field("reviewer_type")
        private ReviewerType reviewerType;

        @Field("rating")
        private Double rating;

        @Field("comment")
        private String comment;

        @Field("review_date")
        private Instant reviewDate;

        public PerformanceReview() {
        }

        public PerformanceReview(String reviewId, String reviewerId, ReviewerType reviewerType,
                                Double rating, String comment) {
            this.reviewId = reviewId;
            this.reviewerId = reviewerId;
            this.reviewerType = reviewerType;
            this.rating = rating;
            this.comment = comment;
            this.reviewDate = Instant.now();
        }

        // Getters and Setters
        public String getReviewId() {
            return reviewId;
        }

        public void setReviewId(String reviewId) {
            this.reviewId = reviewId;
        }

        public String getReviewerId() {
            return reviewerId;
        }

        public void setReviewerId(String reviewerId) {
            this.reviewerId = reviewerId;
        }

        public ReviewerType getReviewerType() {
            return reviewerType;
        }

        public void setReviewerType(ReviewerType reviewerType) {
            this.reviewerType = reviewerType;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Instant getReviewDate() {
            return reviewDate;
        }

        public void setReviewDate(Instant reviewDate) {
            this.reviewDate = reviewDate;
        }

        /**
         * Reviewer type enum.
         */
        public enum ReviewerType {
            CUSTOMER,
            ADMIN,
            DISPATCHER,
            SELF
        }
    }
}
