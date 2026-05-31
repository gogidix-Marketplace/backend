package com.gogidix.courier.performanceservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a Performance Review for a driver.
 */
@Document(collection = "performance_reviews")
@CompoundIndex(name = "idx_review_driver", def = "{'tenantId': 1, 'driverId': 1, 'reviewDate': -1}")
public class PerformanceReview {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("reviewer_id")
    private String reviewerId;

    @Field("reviewer_type")
    private ReviewerType reviewerType;

    @Field("rating")
    private Double rating;

    @Field("comment")
    private String comment;

    @Field("review_categories")
    private ReviewCategories reviewCategories;

    @Field("review_date")
    private Instant reviewDate;

    @Field("created_at")
    private Instant createdAt;

    /**
     * Default constructor for persistence.
     */
    protected PerformanceReview() {
    }

    /**
     * Create a new PerformanceReview.
     *
     * @param tenantId   the tenant identifier
     * @param driverId   the driver identifier
     * @param reviewerId the reviewer identifier
     * @param reviewerType the reviewer type
     * @param rating     the rating (1-5)
     * @param comment    the review comment
     */
    public PerformanceReview(String tenantId, String driverId, String reviewerId,
                            ReviewerType reviewerType, Double rating, String comment) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.reviewerId = Objects.requireNonNull(reviewerId, "reviewerId is required");
        this.reviewerType = Objects.requireNonNull(reviewerType, "reviewerType is required");
        this.rating = Objects.requireNonNull(rating, "rating is required");
        this.comment = comment;
        this.reviewCategories = new ReviewCategories();
        this.reviewDate = Instant.now();
        this.createdAt = Instant.now();

        validate();
    }

    /**
     * Validate the review.
     */
    public void validate() {
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1.0 and 5.0");
        }
    }

    /**
     * Update review categories.
     *
     * @param punctuality   punctuality score
     * @param professionalism professionalism score
     * @param communication communication score
     * @param safety        safety score
     */
    public void updateCategories(Double punctuality, Double professionalism,
                                Double communication, Double safety) {
        this.reviewCategories = new ReviewCategories(punctuality, professionalism, communication, safety);
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

    public String getReviewerId() {
        return reviewerId;
    }

    public ReviewerType getReviewerType() {
        return reviewerType;
    }

    public Double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public ReviewCategories getReviewCategories() {
        return reviewCategories;
    }

    public Instant getReviewDate() {
        return reviewDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    protected void setReviewerType(ReviewerType reviewerType) {
        this.reviewerType = reviewerType;
    }

    protected void setRating(Double rating) {
        this.rating = rating;
    }

    protected void setComment(String comment) {
        this.comment = comment;
    }

    protected void setReviewCategories(ReviewCategories reviewCategories) {
        this.reviewCategories = reviewCategories;
    }

    protected void setReviewDate(Instant reviewDate) {
        this.reviewDate = reviewDate;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Reviewer type enum.
     */
    public enum ReviewerType {
        CUSTOMER,
        ADMIN,
        DISPATCHER,
        SELF,
        SYSTEM
    }

    /**
     * Review categories value object.
     */
    public static class ReviewCategories {
        @Field("punctuality")
        private Double punctuality;

        @Field("professionalism")
        private Double professionalism;

        @Field("communication")
        private Double communication;

        @Field("safety")
        private Double safety;

        public ReviewCategories() {
        }

        public ReviewCategories(Double punctuality, Double professionalism,
                               Double communication, Double safety) {
            this.punctuality = punctuality;
            this.professionalism = professionalism;
            this.communication = communication;
            this.safety = safety;
        }

        public Double getPunctuality() {
            return punctuality;
        }

        public void setPunctuality(Double punctuality) {
            this.punctuality = punctuality;
        }

        public Double getProfessionalism() {
            return professionalism;
        }

        public void setProfessionalism(Double professionalism) {
            this.professionalism = professionalism;
        }

        public Double getCommunication() {
            return communication;
        }

        public void setCommunication(Double communication) {
            this.communication = communication;
        }

        public Double getSafety() {
            return safety;
        }

        public void setSafety(Double safety) {
            this.safety = safety;
        }

        public Double getAverage() {
            if (punctuality == null && professionalism == null &&
                communication == null && safety == null) {
                return null;
            }
            double sum = 0.0;
            int count = 0;
            if (punctuality != null) { sum += punctuality; count++; }
            if (professionalism != null) { sum += professionalism; count++; }
            if (communication != null) { sum += communication; count++; }
            if (safety != null) { sum += safety; count++; }
            return count > 0 ? sum / count : null;
        }
    }
}
