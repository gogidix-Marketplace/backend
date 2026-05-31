package com.gogidix.aiservices.aichurnpredictionservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity representing a Customer Prediction.
 * ZERO framework dependencies - pure domain logic.
 * All queries MUST filter by tenantId for multi-tenancy.
 */
@Document(collection = "churn_predictions")
@CompoundIndex(name = "idx_tenant_id", def = "{'tenantId': 1, 'id': 1}")
public class ChurnPrediction {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("criteria")
    private PredictionCriteria criteria;

    @Field("customer_count")
    private Long customerCount;

    @Field("status")
    private PredictionStatus status;

    @Field("segment_type")
    private PredictionType segmentType;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("last_analyzed_at")
    private Instant lastAnalyzedAt;

    @Transient
    private List<String> customerIds;

    // Private constructor for Builder
    private ChurnPrediction() {
        this.customerIds = new ArrayList<>();
        this.customerCount = 0L;
    }

    /**
     * Create a new ChurnPrediction with tenant context.
     *
     * @param tenantId the tenant ID (MANDATORY for multi-tenancy)
     * @param name      the segment name
     * @param criteria  the segment criteria
     */
    public ChurnPrediction(String tenantId, String name, PredictionCriteria criteria) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.name = Objects.requireNonNull(name, "name is required");
        this.criteria = Objects.requireNonNull(criteria, "criteria is required");
        this.status = PredictionStatus.DRAFT;
        this.segmentType = PredictionType.CUSTOM;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.customerCount = 0L;
        this.customerIds = new ArrayList<>();
    }

    // Domain Logic Methods

    /**
     * Activate the segment.
     * Validates that the segment has criteria and is in DRAFT status.
     *
     * @throws IllegalStateException if segment is not in DRAFT status
     * @throws IllegalStateException if no criteria are set
     */
    public void activate() {
        if (this.status != PredictionStatus.DRAFT) {
            throw new IllegalStateException(
                    "Cannot activate segment with status: " + this.status
            );
        }
        if (this.criteria == null) {
            throw new IllegalStateException("Cannot activate segment without criteria");
        }
        this.status = PredictionStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Deactivate the segment.
     *
     * @throws IllegalStateException if segment is not ACTIVE
     */
    public void deactivate() {
        if (this.status != PredictionStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Cannot deactivate segment with status: " + this.status
            );
        }
        this.status = PredictionStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    /**
     * Archive the segment.
     */
    public void archive() {
        if (this.status == PredictionStatus.ARCHIVED) {
            throw new IllegalStateException("Prediction is already archived");
        }
        this.status = PredictionStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    /**
     * Update segment name and description.
     *
     * @param name        the new name
     * @param description the new description
     */
    public void updateDetails(String name, String description) {
        if (this.status == PredictionStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot update archived segment");
        }
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        this.description = description;
        this.updatedAt = Instant.now();
    }

    /**
     * Update segment criteria.
     *
     * @param criteria the new criteria
     */
    public void updateCriteria(PredictionCriteria criteria) {
        if (this.status == PredictionStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot update archived segment");
        }
        if (this.status == PredictionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot update criteria of active segment. Deactivate first.");
        }
        this.criteria = Objects.requireNonNull(criteria, "criteria is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Add customer IDs to the segment.
     *
     * @param customerIds the customer IDs to add
     */
    public void addCustomers(List<String> customerIds) {
        Objects.requireNonNull(customerIds, "customerIds cannot be null");
        if (this.status != PredictionStatus.ACTIVE) {
            throw new IllegalStateException("Can only add customers to active segments");
        }
        if (this.customerIds == null) {
            this.customerIds = new ArrayList<>();
        }
        for (String customerId : customerIds) {
            if (!this.customerIds.contains(customerId)) {
                this.customerIds.add(customerId);
            }
        }
        this.customerCount = (long) this.customerIds.size();
        this.updatedAt = Instant.now();
    }

    /**
     * Remove customer IDs from the segment.
     *
     * @param customerIds the customer IDs to remove
     */
    public void removeCustomers(List<String> customerIds) {
        Objects.requireNonNull(customerIds, "customerIds cannot be null");
        if (this.customerIds == null) {
            this.customerIds = new ArrayList<>();
        }
        this.customerIds.removeAll(customerIds);
        this.customerCount = (long) this.customerIds.size();
        this.updatedAt = Instant.now();
    }

    /**
     * Set the customer count (typically from an analysis job).
     *
     * @param count the customer count
     */
    public void setCustomerCount(Long count) {
        this.customerCount = Objects.requireNonNull(count, "count cannot be null");
        this.updatedAt = Instant.now();
    }

    /**
     * Mark the segment as analyzed.
     */
    public void markAsAnalyzed() {
        this.lastAnalyzedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Validate the segment state.
     *
     * @throws ValidationException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new com.gogidix.aiservices.aichurnpredictionservice.shared.exception.ValidationException("tenantId is required");
        }
        if (name == null || name.isBlank()) {
            throw new com.gogidix.aiservices.aichurnpredictionservice.shared.exception.ValidationException("name is required");
        }
        if (name.length() > 100) {
            throw new com.gogidix.aiservices.aichurnpredictionservice.shared.exception.ValidationException("name must not exceed 100 characters");
        }
        if (criteria == null) {
            throw new com.gogidix.aiservices.aichurnpredictionservice.shared.exception.ValidationException("criteria is required");
        }
    }

    /**
     * Check if the segment can be modified.
     *
     * @return true if modifiable, false otherwise
     */
    public boolean isModifiable() {
        return this.status != PredictionStatus.ARCHIVED;
    }

    /**
     * Check if the segment is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this.status == PredictionStatus.ACTIVE;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public PredictionCriteria getCriteria() {
        return criteria;
    }

    public Long getCustomerCount() {
        return customerCount;
    }

    public PredictionStatus getStatus() {
        return status;
    }

    public PredictionType getPredictionType() {
        return segmentType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastAnalyzedAt() {
        return lastAnalyzedAt;
    }

    public List<String> getCustomerIds() {
        return customerIds != null ? Collections.unmodifiableList(customerIds) : Collections.emptyList();
    }

    // Setters for persistence/MongoDB mapping
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setStatus(PredictionStatus status) {
        this.status = status;
    }

    protected void setPredictionType(PredictionType segmentType) {
        this.segmentType = segmentType;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setLastAnalyzedAt(Instant lastAnalyzedAt) {
        this.lastAnalyzedAt = lastAnalyzedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChurnPrediction that = (ChurnPrediction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId);
    }

    @Override
    public String toString() {
        return "ChurnPrediction{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", segmentType=" + segmentType +
                ", customerCount=" + customerCount +
                '}';
    }

    /**
     * Builder pattern for ChurnPrediction.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ChurnPrediction instance;

        private Builder() {
            this.instance = new ChurnPrediction();
        }

        /**
         * @deprecated Use {@link ChurnPrediction#builder()} instead.
         */
        @Deprecated
        public static Builder builder() {
            return new Builder();
        }

        public Builder id(String id) {
            this.instance.id = id;
            return this;
        }

        public Builder segmentId(String segmentId) {
            this.instance.id = segmentId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.instance.tenantId = tenantId;
            return this;
        }

        public Builder name(String name) {
            this.instance.name = name;
            return this;
        }

        public Builder description(String description) {
            this.instance.description = description;
            return this;
        }

        public Builder criteria(PredictionCriteria criteria) {
            this.instance.criteria = criteria;
            return this;
        }

        public Builder customerCount(Long customerCount) {
            this.instance.customerCount = customerCount;
            return this;
        }

        public Builder status(PredictionStatus status) {
            this.instance.status = status;
            return this;
        }

        public Builder segmentType(PredictionType segmentType) {
            this.instance.segmentType = segmentType;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.instance.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.instance.updatedAt = updatedAt;
            return this;
        }

        public Builder lastAnalyzedAt(Instant lastAnalyzedAt) {
            this.instance.lastAnalyzedAt = lastAnalyzedAt;
            return this;
        }

        public ChurnPrediction build() {
            // Validate before building
            if (this.instance.tenantId == null || this.instance.tenantId.isBlank()) {
                throw new IllegalArgumentException("tenantId is required");
            }
            if (this.instance.name == null || this.instance.name.isBlank()) {
                throw new IllegalArgumentException("name is required");
            }
            if (this.instance.criteria == null) {
                throw new IllegalArgumentException("criteria is required");
            }
            if (this.instance.status == null) {
                this.instance.status = PredictionStatus.DRAFT;
            }
            if (this.instance.segmentType == null) {
                this.instance.segmentType = PredictionType.CUSTOM;
            }
            if (this.instance.createdAt == null) {
                this.instance.createdAt = Instant.now();
            }
            if (this.instance.updatedAt == null) {
                this.instance.updatedAt = Instant.now();
            }
            return this.instance;
        }
    }
}
