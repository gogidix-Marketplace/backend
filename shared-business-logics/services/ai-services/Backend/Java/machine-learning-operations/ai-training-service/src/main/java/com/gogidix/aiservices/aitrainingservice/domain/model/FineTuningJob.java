package com.gogidix.aiservices.aitrainingservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a fine-tuning job.
 */
@Document(collection = "fine_tuning_jobs")
@CompoundIndex(name = "idx_finetuning_tenant", def = "{'tenantId': 1, 'id': 1}")
public class FineTuningJob {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("job_id")
    private String jobId;

    @Field("base_model")
    private String baseModel;

    @Field("training_data_url")
    private String trainingDataUrl;

    @Field("epochs")
    private Integer epochs;

    @Field("learning_rate")
    private Double learningRate;

    @Field("status")
    private FineTuningStatus status;

    @Field("fine_tuned_model_url")
    private String fineTunedModelUrl;

    @Field("metrics")
    private TrainingMetrics metrics;

    @Field("error_message")
    private String errorMessage;

    @Field("created_at")
    private Instant createdAt;

    @Field("completed_at")
    private Instant completedAt;

    private FineTuningJob() {}

    public FineTuningJob(String tenantId, String baseModel, String trainingDataUrl, Integer epochs, Double learningRate) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.jobId = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.baseModel = Objects.requireNonNull(baseModel, "baseModel is required");
        this.trainingDataUrl = Objects.requireNonNull(trainingDataUrl, "trainingDataUrl is required");
        this.epochs = Objects.requireNonNull(epochs, "epochs is required");
        if (epochs < 1 || epochs > 1000) {
            throw new IllegalArgumentException("epochs must be between 1 and 1000");
        }
        this.learningRate = Objects.requireNonNull(learningRate, "learningRate is required");
        if (learningRate < 0.0001) {
            throw new IllegalArgumentException("learning rate must be at least 0.0001");
        }
        this.status = FineTuningStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public void start() {
        if (this.status != FineTuningStatus.PENDING) {
            throw new IllegalStateException("Cannot start job with status: " + this.status);
        }
        this.status = FineTuningStatus.RUNNING;
    }

    public void complete(String fineTunedModelUrl, TrainingMetrics metrics) {
        if (this.status != FineTuningStatus.RUNNING) {
            throw new IllegalStateException("Cannot complete job with status: " + this.status);
        }
        this.status = FineTuningStatus.COMPLETED;
        this.fineTunedModelUrl = fineTunedModelUrl;
        this.metrics = metrics;
        this.completedAt = Instant.now();
    }

    public void fail(String errorMessage) {
        if (this.status != FineTuningStatus.RUNNING) {
            throw new IllegalStateException("Cannot fail job with status: " + this.status);
        }
        this.status = FineTuningStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
    }

    public void cancel() {
        if (this.status == FineTuningStatus.COMPLETED || this.status == FineTuningStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel job that is " + this.status);
        }
        this.status = FineTuningStatus.CANCELLED;
        this.completedAt = Instant.now();
    }

    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new com.gogidix.aiservices.aitrainingservice.shared.exception.ValidationException("tenantId is required");
        }
        if (baseModel == null || baseModel.isBlank()) {
            throw new com.gogidix.aiservices.aitrainingservice.shared.exception.ValidationException("baseModel is required");
        }
    }

    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getJobId() { return jobId; }
    public String getBaseModel() { return baseModel; }
    public String getTrainingDataUrl() { return trainingDataUrl; }
    public Integer getEpochs() { return epochs; }
    public Double getLearningRate() { return learningRate; }
    public FineTuningStatus getStatus() { return status; }
    public String getFineTunedModelUrl() { return fineTunedModelUrl; }
    public TrainingMetrics getMetrics() { return metrics; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getCompletedAt() { return completedAt; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setJobId(String jobId) { this.jobId = jobId; }
    protected void setBaseModel(String baseModel) { this.baseModel = baseModel; }
    protected void setTrainingDataUrl(String trainingDataUrl) { this.trainingDataUrl = trainingDataUrl; }
    protected void setEpochs(Integer epochs) { this.epochs = epochs; }
    protected void setLearningRate(Double learningRate) { this.learningRate = learningRate; }
    protected void setStatus(FineTuningStatus status) { this.status = status; }
    protected void setFineTunedModelUrl(String fineTunedModelUrl) { this.fineTunedModelUrl = fineTunedModelUrl; }
    protected void setMetrics(TrainingMetrics metrics) { this.metrics = metrics; }
    protected void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }

    public record TrainingMetrics(Double loss, Double accuracy, Integer epochsCompleted) {}
}
