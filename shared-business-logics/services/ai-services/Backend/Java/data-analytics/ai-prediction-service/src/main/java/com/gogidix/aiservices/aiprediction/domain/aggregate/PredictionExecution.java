package com.gogidix.aiservices.aiprediction.domain.aggregate;

import com.gogidix.aiservices.aiprediction.domain.model.PredictionResult;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class PredictionExecution {
    private static final int MIN_TIMEOUT = 1;
    private static final int MAX_TIMEOUT = 30;

    private String executionId;
    private String modelId;
    private String modelVersion;
    private Map<String, Object> inputData;
    private Status status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant completedAt;
    private int timeout;
    private PredictionResult result;
    private String errorMessage;

    public enum Status {
        PENDING, RUNNING, COMPLETED, FAILED
    }

    private PredictionExecution(String modelId, String modelVersion, Map<String, Object> inputData) {
        if (modelId == null) {
            throw new IllegalArgumentException("Model ID cannot be null");
        }
        if (modelVersion == null) {
            throw new IllegalArgumentException("Model version cannot be null");
        }
        if (inputData == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }

        this.executionId = UUID.randomUUID().toString();
        this.modelId = modelId;
        this.modelVersion = modelVersion;
        this.inputData = inputData;
        this.status = Status.PENDING;
        this.createdAt = Instant.now();
        this.timeout = 30;
    }

    public static PredictionExecution create(String modelId, String modelVersion, Map<String, Object> inputData) {
        return new PredictionExecution(modelId, modelVersion, inputData);
    }

    public static PredictionExecution restore(String executionId, String modelId, String modelVersion,
                                              Map<String, Object> inputData, Status status,
                                              Instant createdAt, Instant startedAt, Instant completedAt,
                                              int timeout, String errorMessage) {
        PredictionExecution execution = new PredictionExecution(modelId, modelVersion, inputData);
        execution.executionId = executionId;
        execution.status = status;
        execution.createdAt = createdAt;
        execution.startedAt = startedAt;
        execution.completedAt = completedAt;
        execution.timeout = timeout;
        execution.errorMessage = errorMessage;
        return execution;
    }

    public void start() {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Can only start pending execution");
        }
        this.status = Status.RUNNING;
        this.startedAt = Instant.now();
    }

    public void complete(PredictionResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null");
        }
        if (status != Status.RUNNING) {
            throw new IllegalStateException("Can only complete running execution");
        }
        this.status = Status.COMPLETED;
        this.completedAt = Instant.now();
        this.result = result;
    }

    public void fail(String errorMessage) {
        this.status = Status.FAILED;
        this.completedAt = Instant.now();
        this.errorMessage = errorMessage;
    }

    public void setTimeout(int timeout) {
        if (timeout < MIN_TIMEOUT) {
            throw new IllegalArgumentException("Timeout must be at least " + MIN_TIMEOUT + " second");
        }
        if (timeout > MAX_TIMEOUT) {
            throw new IllegalArgumentException("Timeout cannot exceed " + MAX_TIMEOUT + " seconds");
        }
        this.timeout = timeout;
    }

    public String getExecutionId() { return executionId; }
    public String getModelId() { return modelId; }
    public String getModelVersion() { return modelVersion; }
    public Map<String, Object> getInputData() { return inputData; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public int getTimeout() { return timeout; }
    public PredictionResult getResult() { return result; }
    public String getErrorMessage() { return errorMessage; }
}
