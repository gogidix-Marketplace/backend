package com.gogidix.aiservices.aiprediction.application.dto.request;

import lombok.Builder;

import java.util.Map;

@Builder
public class GeneratePredictionRequest {
    private String modelId;
    private String modelVersion;
    private Map<String, Object> inputData;
    private Map<String, Object> options;
    private Integer timeout;

    public String getModelId() {
        return modelId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public Map<String, Object> getInputData() {
        return inputData;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public void setInputData(Map<String, Object> inputData) {
        this.inputData = inputData;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
