package com.gogidix.aiservices.aimodeltrainingservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Domain Entity representing a training job.
 */
@Document(collection = "training_jobs")
@CompoundIndex(name = "idx_training_tenant", def = "{'tenantId': 1, 'id': 1}")
public class TrainingJob {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("job_id")
    private String jobId;

    @Field("model_type")
    private String modelType;

    @Field("training_data_url")
    private String trainingDataUrl;

    @Field("algorithm")
    private TrainingAlgorithm algorithm;

    @Field("hyperparameters")
    private Map<String, Object> hyperparameters;

    @Field("status")
    private TrainingStatus status;

    @Field("model_artifact_url")
    private String modelArtifactUrl;

    @Field("metrics")
    private Map<String, Double> metrics;

    @Field("error_message")
    private String errorMessage;

    @Field("started_at")
    private Instant startedAt;

    @Field("completed_at")
    private Instant completedAt;

    private TrainingJob() {}

    public TrainingJob(String tenantId, String modelType, String trainingDataUrl, TrainingAlgorithm algorithm, Map<String, Object> hyperparameters) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.jobId = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.modelType = Objects.requireNonNull(modelType, "modelType is required");
        this.trainingDataUrl = Objects.requireNonNull(trainingDataUrl, "trainingDataUrl is required");
        this.algorithm = Objects.requireNonNull(algorithm, "algorithm is required");
        this.hyperparameters = hyperparameters;
        this.status = TrainingStatus.QUEUED;
        this.startedAt = Instant.now();
    }

    public void start() {
        if (this.status != TrainingStatus.QUEUED) {
            throw new IllegalStateException("Cannot start job with status: " + this.status);
        }
        this.status = TrainingStatus.RUNNING;
    }

    public void complete(String modelArtifactUrl, Map<String, Double> metrics) {
        if (this.status != TrainingStatus.RUNNING) {
            throw new IllegalStateException("Cannot complete job with status: " + this.status);
        }
        this.status = TrainingStatus.COMPLETED;
        this.modelArtifactUrl = modelArtifactUrl;
        this.metrics = metrics;
        this.completedAt = Instant.now();
    }

    public void fail(String errorMessage) {
        if (this.status != TrainingStatus.RUNNING) {
            throw new IllegalStateException("Cannot fail job with status: " + this.status);
        }
        this.status = TrainingStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
    }

    public void cancel() {
        if (this.status == TrainingStatus.COMPLETED || this.status == TrainingStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel job that is " + this.status);
        }
        this.status = TrainingStatus.CANCELLED;
        this.completedAt = Instant.now();
    }

    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new com.gogidix.aiservices.aimodeltrainingservice.shared.exception.ValidationException("tenantId is required");
        }
        if (modelType == null || modelType.isBlank()) {
            throw new com.gogidix.aiservices.aimodeltrainingservice.shared.exception.ValidationException("modelType is required");
        }
    }

    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getJobId() { return jobId; }
    public String getModelType() { return modelType; }
    public String getTrainingDataUrl() { return trainingDataUrl; }
    public TrainingAlgorithm getAlgorithm() { return algorithm; }
    public Map<String, Object> getHyperparameters() { return hyperparameters; }
    public TrainingStatus getStatus() { return status; }
    public String getModelArtifactUrl() { return modelArtifactUrl; }
    public Map<String, Double> getMetrics() { return metrics; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setJobId(String jobId) { this.jobId = jobId; }
    protected void setModelType(String modelType) { this.modelType = modelType; }
    protected void setTrainingDataUrl(String trainingDataUrl) { this.trainingDataUrl = trainingDataUrl; }
    protected void setAlgorithm(TrainingAlgorithm algorithm) { this.algorithm = algorithm; }
    protected void setHyperparameters(Map<String, Object> hyperparameters) { this.hyperparameters = hyperparameters; }
    protected void setStatus(TrainingStatus status) { this.status = status; }
    protected void setModelArtifactUrl(String modelArtifactUrl) { this.modelArtifactUrl = modelArtifactUrl; }
    protected void setMetrics(Map<String, Double> metrics) { this.metrics = metrics; }
    protected void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    protected void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    protected void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
