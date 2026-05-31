package com.gogidix.aiservices.timeseriesforecasting.application.dto.response;

import java.util.List;
import java.util.Objects;

public class ForecastResponse {
    private String forecastId;
    private List<Object> forecasts;
    private Object accuracyMetrics;

    public ForecastResponse() {
    }

    public ForecastResponse(String forecastId, List<Object> forecasts, Object accuracyMetrics) {
        this.forecastId = forecastId;
        this.forecasts = forecasts;
        this.accuracyMetrics = accuracyMetrics;
    }

    public static ForecastResponseBuilder builder() {
        return new ForecastResponseBuilder();
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

    public Object getAccuracyMetrics() {
        return accuracyMetrics;
    }

    public void setAccuracyMetrics(Object accuracyMetrics) {
        this.accuracyMetrics = accuracyMetrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForecastResponse that = (ForecastResponse) o;
        return Objects.equals(forecastId, that.forecastId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forecastId);
    }

    public static class ForecastResponseBuilder {
        private String forecastId;
        private List<Object> forecasts;
        private Object accuracyMetrics;

        public ForecastResponseBuilder forecastId(String forecastId) {
            this.forecastId = forecastId;
            return this;
        }

        public ForecastResponseBuilder forecasts(List<Object> forecasts) {
            this.forecasts = forecasts;
            return this;
        }

        public ForecastResponseBuilder accuracyMetrics(Object accuracyMetrics) {
            this.accuracyMetrics = accuracyMetrics;
            return this;
        }

        public ForecastResponse build() {
            return new ForecastResponse(forecastId, forecasts, accuracyMetrics);
        }
    }
}
