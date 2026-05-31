package com.gogidix.aiservices.aifeaturestoreservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a feature value for an entity.
 * All queries MUST filter by tenantId for multi-tenancy.
 */
@Document(collection = "feature_values")
@CompoundIndex(name = "idx_feature_value_tenant", def = "{'tenantId': 1, 'featureName': 1, 'entityId': 1}")
public class FeatureValue {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("feature_name")
    private String featureName;

    @Indexed
    @Field("entity_id")
    private String entityId;

    @Field("value")
    private Object value;

    @Field("created_at")
    private Instant createdAt;

    private FeatureValue() {}

    public FeatureValue(String tenantId, String featureName, String entityId, Object value) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.featureName = Objects.requireNonNull(featureName, "featureName is required");
        this.entityId = Objects.requireNonNull(entityId, "entityId is required");
        this.value = Objects.requireNonNull(value, "value is required");
        this.createdAt = Instant.now();
    }

    public void updateValue(Object value) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException("tenantId is required");
        }
        if (featureName == null || featureName.isBlank()) {
            throw new com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException("featureName is required");
        }
        if (entityId == null || entityId.isBlank()) {
            throw new com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException("entityId is required");
        }
        if (value == null) {
            throw new com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException("value is required");
        }
    }

    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getFeatureName() { return featureName; }
    public String getEntityId() { return entityId; }
    public Object getValue() { return value; }
    public Instant getCreatedAt() { return createdAt; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setFeatureName(String featureName) { this.featureName = featureName; }
    protected void setEntityId(String entityId) { this.entityId = entityId; }
    protected void setValue(Object value) { this.value = value; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureValue that = (FeatureValue) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
