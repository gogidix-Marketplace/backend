package com.gogidix.aiservices.aimanagementservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a registered ML model.
 */
@Document(collection = "ml_models")
@CompoundIndex(name = "idx_ml_model_tenant", def = "{'tenantId': 1, 'id': 1}")
public class MLModel {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("model_name")
    private String modelName;

    @Field("framework")
    private ModelFramework framework;

    @Field("version")
    private String version;

    @Field("model_artifact_url")
    private String modelArtifactUrl;

    @Field("status")
    private ModelStatus status;

    @Field("model_size_bytes")
    private Long modelSizeBytes;

    @Field("registered_at")
    private Instant registeredAt;

    @Field("deployed_at")
    private Instant deployedAt;

    private MLModel() {}

    public MLModel(String tenantId, String modelName, ModelFramework framework, String version, String modelArtifactUrl) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.modelName = Objects.requireNonNull(modelName, "modelName is required");
        this.framework = Objects.requireNonNull(framework, "framework is required");
        this.version = Objects.requireNonNull(version, "version is required");
        this.modelArtifactUrl = Objects.requireNonNull(modelArtifactUrl, "modelArtifactUrl is required");
        this.status = ModelStatus.REGISTERED;
        this.registeredAt = Instant.now();
    }

    public void deploy() {
        if (this.status != ModelStatus.REGISTERED && this.status != ModelStatus.UNDEPLOYED) {
            throw new IllegalStateException("Cannot deploy model with status: " + this.status);
        }
        this.status = ModelStatus.DEPLOYING;
    }

    public void completeDeployment() {
        if (this.status != ModelStatus.DEPLOYING) {
            throw new IllegalStateException("Model is not deploying");
        }
        this.status = ModelStatus.DEPLOYED;
        this.deployedAt = Instant.now();
    }

    public void undeploy() {
        if (this.status != ModelStatus.DEPLOYED) {
            throw new IllegalStateException("Cannot undeploy model with status: " + this.status);
        }
        this.status = ModelStatus.UNDEPLOYING;
    }

    public void completeUndeployment() {
        if (this.status != ModelStatus.UNDEPLOYING) {
            throw new IllegalStateException("Model is not undeploying");
        }
        this.status = ModelStatus.UNDEPLOYED;
        this.deployedAt = null;
    }

    public void archive() {
        if (this.status == ModelStatus.DEPLOYED || this.status == ModelStatus.DEPLOYING) {
            throw new IllegalStateException("Cannot archive deployed model");
        }
        this.status = ModelStatus.ARCHIVED;
    }

    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new com.gogidix.aiservices.aimanagementservice.shared.exception.ValidationException("tenantId is required");
        }
        if (modelName == null || modelName.isBlank()) {
            throw new com.gogidix.aiservices.aimanagementservice.shared.exception.ValidationException("modelName is required");
        }
        if (version == null || version.isBlank()) {
            throw new com.gogidix.aiservices.aimanagementservice.shared.exception.ValidationException("version is required");
        }
    }

    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getModelName() { return modelName; }
    public ModelFramework getFramework() { return framework; }
    public String getVersion() { return version; }
    public String getModelArtifactUrl() { return modelArtifactUrl; }
    public ModelStatus getStatus() { return status; }
    public Long getModelSizeBytes() { return modelSizeBytes; }
    public Instant getRegisteredAt() { return registeredAt; }
    public Instant getDeployedAt() { return deployedAt; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setModelName(String modelName) { this.modelName = modelName; }
    protected void setFramework(ModelFramework framework) { this.framework = framework; }
    protected void setVersion(String version) { this.version = version; }
    protected void setModelArtifactUrl(String modelArtifactUrl) { this.modelArtifactUrl = modelArtifactUrl; }
    protected void setStatus(ModelStatus status) { this.status = status; }
    protected void setModelSizeBytes(Long modelSizeBytes) { this.modelSizeBytes = modelSizeBytes; }
    protected void setRegisteredAt(Instant registeredAt) { this.registeredAt = registeredAt; }
    protected void setDeployedAt(Instant deployedAt) { this.deployedAt = deployedAt; }
}
