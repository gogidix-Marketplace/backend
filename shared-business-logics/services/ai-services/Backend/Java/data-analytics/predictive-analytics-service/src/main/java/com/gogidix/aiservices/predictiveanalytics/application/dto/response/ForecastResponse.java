package com.gogidix.aiservices.predictiveanalytics.application.dto.response;

import java.util.List;

public class ForecastResponse {
    private String forecastId;
    private List<Object> forecasts;
    private List<Object> confidenceIntervals;
    private Object metrics;

    public ForecastResponse() {
    }

    public ForecastResponse(String forecastId, List<Object> forecasts, List<Object> confidenceIntervals, Object metrics) {
        this.forecastId = forecastId;
        this.forecasts = forecasts;
        this.confidenceIntervals = confidenceIntervals;
        this.metrics = metrics;
    }

    public String getForecastId() {
        return forecastId;
    }

    public void setForecastId(String forecastId) {
        this.forecastId = forecastId;
    }

    public List<Object> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Object> forecasts) {
        this.forecasts = forecasts;
    }

    public List<Object> getConfidenceIntervals() {
        return confidenceIntervals;
    }

    public void setConfidenceIntervals(List<Object> confidenceIntervals) {
        this.confidenceIntervals = confidenceIntervals;
    }

    public Object getMetrics() {
        return metrics;
    }

    public void setMetrics(Object metrics) {
        this.metrics = metrics;
    }

    public static ForecastResponseBuilder builder() {
        return new ForecastResponseBuilder();
    }

    public static class ForecastResponseBuilder {
        private String forecastId;
        private List<Object> forecasts;
        private List<Object> confidenceIntervals;
        private Object metrics;

        public ForecastResponseBuilder forecastId(String forecastId) {
            this.forecastId = forecastId;
            return this;
        }

        public ForecastResponseBuilder forecasts(List<Object> forecasts) {
            this.forecasts = forecasts;
            return this;
        }

        public ForecastResponseBuilder confidenceIntervals(List<Object> confidenceIntervals) {
            this.confidenceIntervals = confidenceIntervals;
            return this;
        }

        public ForecastResponseBuilder metrics(Object metrics) {
            this.metrics = metrics;
            return this;
        }

        public ForecastResponse build() {
            return new ForecastResponse(forecastId, forecasts, confidenceIntervals, metrics);
        }
    }
}
