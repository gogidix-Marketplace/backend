package com.gogidix.aiservices.aifeaturestoreservice.domain.model;

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
 * Domain Entity representing a feature definition in the feature store.
 * All queries MUST filter by tenantId for multi-tenancy.
 */
@Document(collection = "feature_definitions")
@CompoundIndex(name = "idx_feature_def_tenant", def = "{'tenantId': 1, 'featureName': 1}")
public class FeatureDefinition {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("feature_name")
    private String featureName;

    @Field("feature_type")
    private FeatureType featureType;

    @Field("description")
    private String description;

    @Field("metadata")
    private List<FeatureMetadata> metadata;

    @Field("version")
    private String version;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    private FeatureDefinition() {
        this.metadata = new ArrayList<>();
    }

    public FeatureDefinition(String tenantId, String featureName, FeatureType featureType) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.featureName = Objects.requireNonNull(featureName, "featureName is required");
        this.featureType = Objects.requireNonNull(featureType, "featureType is required");
        this.version = "1.0";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void updateMetadata(List<FeatureMetadata> metadata) {
        this.metadata = new ArrayList<>(Objects.requireNonNull(metadata, "metadata cannot be null"));
        this.updatedAt = Instant.now();
    }

    public void incrementVersion() {
        String[] parts = this.version.split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        if (minor >= 99) {
            major++;
            minor = 0;
        } else {
            minor++;
        }
        this.version = major + "." + minor;
        this.updatedAt = Instant.now();
    }

    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException("tenantId is required");
        }
        if (featureName == null || featureName.isBlank()) {
            throw new com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException("featureName is required");
        }
        if (featureName.length() > 100) {
            throw new com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException("featureName must not exceed 100 characters");
        }
    }

    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getFeatureName() { return featureName; }
    public FeatureType getFeatureType() { return featureType; }
    public String getDescription() { return description; }
    public List<FeatureMetadata> getMetadata() {
        return metadata != null ? Collections.unmodifiableList(metadata) : Collections.emptyList();
    }
    public String getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setFeatureName(String featureName) { this.featureName = featureName; }
    protected void setFeatureType(FeatureType featureType) { this.featureType = featureType; }
    public void setDescription(String description) { this.description = description; }
    protected void setMetadata(List<FeatureMetadata> metadata) { this.metadata = metadata; }
    protected void setVersion(String version) { this.version = version; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureDefinition that = (FeatureDefinition) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeatureDefinition{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", featureName='" + featureName + '\'' +
                ", featureType=" + featureType +
                ", version='" + version + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final FeatureDefinition instance = new FeatureDefinition();

        public Builder id(String id) { instance.id = id; return this; }
        public Builder tenantId(String tenantId) { instance.tenantId = tenantId; return this; }
        public Builder featureName(String featureName) { instance.featureName = featureName; return this; }
        public Builder featureType(FeatureType featureType) { instance.featureType = featureType; return this; }
        public Builder description(String description) { instance.description = description; return this; }
        public Builder metadata(List<FeatureMetadata> metadata) { instance.metadata = metadata; return this; }
        public Builder version(String version) { instance.version = version; return this; }

        public FeatureDefinition build() {
            if (instance.tenantId == null || instance.tenantId.isBlank()) {
                throw new IllegalArgumentException("tenantId is required");
            }
            if (instance.featureName == null || instance.featureName.isBlank()) {
                throw new IllegalArgumentException("featureName is required");
            }
            if (instance.featureType == null) {
                throw new IllegalArgumentException("featureType is required");
            }
            if (instance.version == null) instance.version = "1.0";
            if (instance.createdAt == null) instance.createdAt = Instant.now();
            if (instance.updatedAt == null) instance.updatedAt = Instant.now();
            return instance;
        }
    }
}
