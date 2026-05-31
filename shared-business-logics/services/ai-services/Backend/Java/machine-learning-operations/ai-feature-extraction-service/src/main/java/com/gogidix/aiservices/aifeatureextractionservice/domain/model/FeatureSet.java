package com.gogidix.aiservices.aifeatureextractionservice.domain.model;

import org.springframework.data.annotation.Id;
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
 * Domain Entity representing a set of extracted features.
 * ZERO framework dependencies - pure domain logic.
 * All queries MUST filter by tenantId for multi-tenancy.
 */
@Document(collection = "feature_sets")
@CompoundIndex(name = "idx_feature_set_tenant", def = "{'tenantId': 1, 'id': 1}")
public class FeatureSet {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("data_source")
    private String dataSource;

    @Field("status")
    private FeatureExtractionStatus status;

    @Field("extraction_methods")
    private List<ExtractionMethod> extractionMethods;

    @Field("features")
    private List<FeatureValue> features;

    @Field("feature_count")
    private Integer featureCount;

    @Field("normalized")
    private Boolean normalized;

    @Field("error_message")
    private String errorMessage;

    @Field("created_at")
    private Instant createdAt;

    @Field("completed_at")
    private Instant completedAt;

    private FeatureSet() {
        this.features = new ArrayList<>();
        this.extractionMethods = new ArrayList<>();
        this.featureCount = 0;
        this.normalized = true;
    }

    /**
     * Create a new FeatureSet with tenant context.
     *
     * @param tenantId         the tenant ID (MANDATORY for multi-tenancy)
     * @param dataSource       the data source identifier
     * @param extractionMethods the extraction methods to use
     */
    public FeatureSet(String tenantId, String dataSource, List<ExtractionMethod> extractionMethods) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource is required");
        this.extractionMethods = new ArrayList<>(Objects.requireNonNull(extractionMethods, "extractionMethods is required"));
        this.status = FeatureExtractionStatus.PENDING;
        this.createdAt = Instant.now();
        this.normalized = true;
    }

    // Domain Logic Methods

    /**
     * Mark extraction as processing.
     */
    public void markAsProcessing() {
        if (this.status != FeatureExtractionStatus.PENDING) {
            throw new IllegalStateException("Can only start extraction from PENDING status");
        }
        this.status = FeatureExtractionStatus.PROCESSING;
    }

    /**
     * Complete the extraction with features.
     *
     * @param features the extracted features
     */
    public void completeWith(List<FeatureValue> features) {
        if (this.status != FeatureExtractionStatus.PROCESSING) {
            throw new IllegalStateException("Can only complete extraction from PROCESSING status");
        }
        Objects.requireNonNull(features, "features cannot be null");
        if (features.size() > 1000) {
            throw new IllegalArgumentException("Max 1000 features allowed per extraction");
        }

        this.features = new ArrayList<>(features);
        this.featureCount = features.size();
        this.status = FeatureExtractionStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    /**
     * Mark extraction as failed.
     *
     * @param errorMessage the error message
     */
    public void failWith(String errorMessage) {
        if (this.status != FeatureExtractionStatus.PROCESSING) {
            throw new IllegalStateException("Can only fail extraction from PROCESSING status");
        }
        this.errorMessage = Objects.requireNonNull(errorMessage, "errorMessage is required");
        this.status = FeatureExtractionStatus.FAILED;
        this.completedAt = Instant.now();
    }

    /**
     * Cancel the extraction.
     */
    public void cancel() {
        if (this.status == FeatureExtractionStatus.COMPLETED || this.status == FeatureExtractionStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel extraction that is already " + this.status);
        }
        this.status = FeatureExtractionStatus.CANCELLED;
        this.completedAt = Instant.now();
    }

    /**
     * Add a feature to the set.
     *
     * @param feature the feature to add
     */
    public void addFeature(FeatureValue feature) {
        Objects.requireNonNull(feature, "feature cannot be null");
        if (this.features.size() >= 1000) {
            throw new IllegalStateException("Max 1000 features allowed per extraction");
        }
        this.features.add(feature);
        this.featureCount = this.features.size();
    }

    /**
     * Check if features are normalized.
     *
     * @return true if normalized, false otherwise
     */
    public boolean isNormalized() {
        return Boolean.TRUE.equals(this.normalized);
    }

    /**
     * Set normalization flag.
     *
     * @param normalized the normalization flag
     */
    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }

    /**
     * Validate the feature set state.
     *
     * @throws ValidationException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException("tenantId is required");
        }
        if (dataSource == null || dataSource.isBlank()) {
            throw new com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException("dataSource is required");
        }
        if (extractionMethods == null || extractionMethods.isEmpty()) {
            throw new com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException("At least one extraction method is required");
        }
    }

    /**
     * Check if extraction can be modified.
     *
     * @return true if modifiable, false otherwise
     */
    public boolean isModifiable() {
        return this.status == FeatureExtractionStatus.PENDING;
    }

    /**
     * Check if extraction is completed.
     *
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return this.status == FeatureExtractionStatus.COMPLETED;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public FeatureExtractionStatus getStatus() {
        return status;
    }

    public List<ExtractionMethod> getExtractionMethods() {
        return Collections.unmodifiableList(extractionMethods);
    }

    public List<FeatureValue> getFeatures() {
        return features != null ? Collections.unmodifiableList(features) : Collections.emptyList();
    }

    public Integer getFeatureCount() {
        return featureCount;
    }

    public Boolean getNormalized() {
        return normalized;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    // Setters for persistence/MongoDB mapping
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    protected void setStatus(FeatureExtractionStatus status) {
        this.status = status;
    }

    protected void setExtractionMethods(List<ExtractionMethod> extractionMethods) {
        this.extractionMethods = extractionMethods;
    }

    protected void setFeatures(List<FeatureValue> features) {
        this.features = features;
    }

    protected void setFeatureCount(Integer featureCount) {
        this.featureCount = featureCount;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureSet that = (FeatureSet) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId);
    }

    @Override
    public String toString() {
        return "FeatureSet{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", status=" + status +
                ", featureCount=" + featureCount +
                '}';
    }

    /**
     * Builder pattern for FeatureSet.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FeatureSet instance;

        private Builder() {
            this.instance = new FeatureSet();
        }

        public Builder id(String id) {
            this.instance.id = id;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.instance.tenantId = tenantId;
            return this;
        }

        public Builder dataSource(String dataSource) {
            this.instance.dataSource = dataSource;
            return this;
        }

        public Builder status(FeatureExtractionStatus status) {
            this.instance.status = status;
            return this;
        }

        public Builder extractionMethods(List<ExtractionMethod> extractionMethods) {
            this.instance.extractionMethods = extractionMethods;
            return this;
        }

        public Builder features(List<FeatureValue> features) {
            this.instance.features = features;
            return this;
        }

        public Builder featureCount(Integer featureCount) {
            this.instance.featureCount = featureCount;
            return this;
        }

        public Builder normalized(Boolean normalized) {
            this.instance.normalized = normalized;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.instance.errorMessage = errorMessage;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.instance.createdAt = createdAt;
            return this;
        }

        public Builder completedAt(Instant completedAt) {
            this.instance.completedAt = completedAt;
            return this;
        }

        public FeatureSet build() {
            if (this.instance.tenantId == null || this.instance.tenantId.isBlank()) {
                throw new IllegalArgumentException("tenantId is required");
            }
            if (this.instance.dataSource == null || this.instance.dataSource.isBlank()) {
                throw new IllegalArgumentException("dataSource is required");
            }
            if (this.instance.status == null) {
                this.instance.status = FeatureExtractionStatus.PENDING;
            }
            if (this.instance.createdAt == null) {
                this.instance.createdAt = Instant.now();
            }
            if (this.instance.normalized == null) {
                this.instance.normalized = true;
            }
            return this.instance;
        }
    }
}
