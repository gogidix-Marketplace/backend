package com.gogidix.aiservices.aiinferenceservice.domain.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a loaded model.
 */
@Document(collection = "loaded_models")
@CompoundIndex(name = "idx_loaded_model_tenant", def = "{'tenantId': 1, 'modelId': 1}")
public class LoadedModel {

    @org.springframework.data.annotation.Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("model_id")
    private String modelId;

    @Field("status")
    private ModelStatus status;

    @Field("loaded_at")
    private Instant loadedAt;

    @Field("cache_expiry")
    private Instant cacheExpiry;

    public LoadedModel(String tenantId, String modelId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.modelId = Objects.requireNonNull(modelId, "modelId is required");
        this.status = ModelStatus.LOADING;
        this.loadedAt = Instant.now();
        this.cacheExpiry = Instant.now().plusSeconds(3600); // 1 hour cache
    }

    public void markAsLoaded() {
        this.status = ModelStatus.LOADED;
    }

    public void markAsUnloaded() {
        this.status = ModelStatus.UNLOADED;
    }

    public void markAsError() {
        this.status = ModelStatus.ERROR;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(cacheExpiry);
    }

    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getModelId() { return modelId; }
    public ModelStatus getStatus() { return status; }
    public Instant getLoadedAt() { return loadedAt; }
    public Instant getCacheExpiry() { return cacheExpiry; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setModelId(String modelId) { this.modelId = modelId; }
    protected void setStatus(ModelStatus status) { this.status = status; }
    protected void setLoadedAt(Instant loadedAt) { this.loadedAt = loadedAt; }
    protected void setCacheExpiry(Instant cacheExpiry) { this.cacheExpiry = cacheExpiry; }
}
