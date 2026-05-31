package com.gogidix.aiservices.aiinferenceservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity representing an inference result.
 */
@Document(collection = "inference_results")
@CompoundIndex(name = "idx_inference_tenant", def = "{'tenantId': 1, 'id': 1}")
public class InferenceResult {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("model_id")
    private String modelId;

    @Field("input_data")
    private Object inputData;

    @Field("predictions")
    private List<Prediction> predictions;

    @Field("latency_ms")
    private Long latencyMs;

    @Field("executed_at")
    private Instant executedAt;

    @Field("inference_id")
    private String inferenceId;

    private InferenceResult() {
        this.predictions = new ArrayList<>();
    }

    public InferenceResult(String tenantId, String modelId, Object inputData) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.inferenceId = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.modelId = Objects.requireNonNull(modelId, "modelId is required");
        this.inputData = Objects.requireNonNull(inputData, "inputData is required");
        this.executedAt = Instant.now();
    }

    public void setPredictions(List<Prediction> predictions, long latencyMs) {
        this.predictions = new ArrayList<>(Objects.requireNonNull(predictions, "predictions cannot be null"));
        this.latencyMs = latencyMs;
    }

    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new com.gogidix.aiservices.aiinferenceservice.shared.exception.ValidationException("tenantId is required");
        }
        if (modelId == null || modelId.isBlank()) {
            throw new com.gogidix.aiservices.aiinferenceservice.shared.exception.ValidationException("modelId is required");
        }
    }

    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getModelId() { return modelId; }
    public Object getInputData() { return inputData; }
    public List<Prediction> getPredictions() { return predictions; }
    public Long getLatencyMs() { return latencyMs; }
    public Instant getExecutedAt() { return executedAt; }
    public String getInferenceId() { return inferenceId; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setModelId(String modelId) { this.modelId = modelId; }
    protected void setInputData(Object inputData) { this.inputData = inputData; }
    protected void setExecutedAt(Instant executedAt) { this.executedAt = executedAt; }
    protected void setInferenceId(String inferenceId) { this.inferenceId = inferenceId; }

    public record Prediction(String label, Double confidence, Object value) {}
}
