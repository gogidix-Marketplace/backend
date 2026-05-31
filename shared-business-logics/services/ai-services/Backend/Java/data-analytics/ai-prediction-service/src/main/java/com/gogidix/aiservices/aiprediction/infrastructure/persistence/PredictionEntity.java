package com.gogidix.aiservices.aiprediction.infrastructure.persistence;

import com.gogidix.aiservices.aiprediction.domain.aggregate.PredictionExecution;

import java.time.Instant;
import java.util.Map;

public class PredictionEntity {
    private String executionId;
    private String modelId;
    private String modelVersion;
    private Map<String, Object> inputData;
    private PredictionExecution.Status status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant completedAt;
    private int timeout;
    private String errorMessage;

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public Map<String, Object> getInputData() {
        return inputData;
    }

    public void setInputData(Map<String, Object> inputData) {
        this.inputData = inputData;
    }

    public PredictionExecution.Status getStatus() {
        return status;
    }

    public void setStatus(PredictionExecution.Status status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
