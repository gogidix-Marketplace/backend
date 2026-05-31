package com.gogidix.aiservices.predictiveanalytics.application.dto.request;

import com.gogidix.aiservices.predictiveanalytics.domain.model.ForecastingMethod;

public class GenerateForecastRequest {
    private String dataSource;
    private String targetField;
    private Integer horizon;
    private ForecastingMethod method;

    public GenerateForecastRequest() {
    }

    public GenerateForecastRequest(String dataSource, String targetField, Integer horizon, ForecastingMethod method) {
        this.dataSource = dataSource;
        this.targetField = targetField;
        this.horizon = horizon;
        this.method = method;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public Integer getHorizon() {
        return horizon;
    }

    public void setHorizon(Integer horizon) {
        this.horizon = horizon;
    }

    public ForecastingMethod getMethod() {
        return method;
    }

    public void setMethod(ForecastingMethod method) {
        this.method = method;
    }

    public static GenerateForecastRequestBuilder builder() {
        return new GenerateForecastRequestBuilder();
    }

    public static class GenerateForecastRequestBuilder {
        private String dataSource;
        private String targetField;
        private Integer horizon;
        private ForecastingMethod method;

        public GenerateForecastRequestBuilder dataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public GenerateForecastRequestBuilder targetField(String targetField) {
            this.targetField = targetField;
            return this;
        }

        public GenerateForecastRequestBuilder horizon(Integer horizon) {
            this.horizon = horizon;
            return this;
        }

        public GenerateForecastRequestBuilder method(ForecastingMethod method) {
            this.method = method;
            return this;
        }

        public GenerateForecastRequest build() {
            return new GenerateForecastRequest(dataSource, targetField, horizon, method);
        }
    }
}
