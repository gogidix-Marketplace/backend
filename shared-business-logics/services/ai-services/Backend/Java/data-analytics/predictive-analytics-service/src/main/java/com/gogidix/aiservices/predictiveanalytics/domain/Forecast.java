package com.gogidix.aiservices.predictiveanalytics.domain;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain entity representing a forecast result.
 */
public class Forecast {

    private final String forecastId;
    private final String dataSource;
    private final String targetField;
    private final ForecastMethod method;
    private final Integer horizon;
    private final List<ForecastDataPoint> forecastData;
    private final List<ConfidenceInterval> confidenceIntervals;
    private final ForecastMetrics metrics;
    private final LocalDateTime generatedAt;

    public Forecast(String forecastId, String dataSource, String targetField,
                   ForecastMethod method, Integer horizon,
                   List<ForecastDataPoint> forecastData,
                   List<ConfidenceInterval> confidenceIntervals,
                   ForecastMetrics metrics, LocalDateTime generatedAt) {
        if (forecastId == null || forecastId.trim().isEmpty()) {
            throw new IllegalArgumentException("forecastId cannot be null or empty");
        }
        if (dataSource == null || dataSource.trim().isEmpty()) {
            throw new IllegalArgumentException("dataSource cannot be null or empty");
        }
        if (targetField == null || targetField.trim().isEmpty()) {
            throw new IllegalArgumentException("targetField cannot be null or empty");
        }
        if (method == null) {
            throw new IllegalArgumentException("method cannot be null");
        }
        if (horizon == null || horizon <= 0 || horizon > 365) {
            throw new IllegalArgumentException("horizon must be between 1 and 365");
        }
        if (forecastData == null || forecastData.isEmpty()) {
            throw new IllegalArgumentException("forecastData cannot be null or empty");
        }
        if (metrics == null) {
            throw new IllegalArgumentException("metrics cannot be null");
        }
        if (generatedAt == null) {
            throw new IllegalArgumentException("generatedAt cannot be null");
        }

        this.forecastId = forecastId;
        this.dataSource = dataSource;
        this.targetField = targetField;
        this.method = method;
        this.horizon = horizon;
        this.forecastData = forecastData;
        this.confidenceIntervals = confidenceIntervals != null ? confidenceIntervals : List.of();
        this.metrics = metrics;
        this.generatedAt = generatedAt;
    }

    public String getForecastId() {
        return forecastId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getTargetField() {
        return targetField;
    }

    public ForecastMethod getMethod() {
        return method;
    }

    public Integer getHorizon() {
        return horizon;
    }

    public List<ForecastDataPoint> getForecastData() {
        return forecastData;
    }

    public List<ConfidenceInterval> getConfidenceIntervals() {
        return confidenceIntervals;
    }

    public ForecastMetrics getMetrics() {
        return metrics;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public enum ForecastMethod {
        ARIMA,
        PROPHET,
        LSTM,
        MOVING_AVERAGE,
        EXPONENTIAL_SMOOTHING
    }

    public static class ForecastDataPoint {
        private final LocalDateTime timestamp;
        private final Double value;

        public ForecastDataPoint(LocalDateTime timestamp, Double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public Double getValue() {
            return value;
        }
    }

    public static class ConfidenceInterval {
        private final LocalDateTime timestamp;
        private final Double lowerBound;
        private final Double upperBound;

        public ConfidenceInterval(LocalDateTime timestamp, Double lowerBound, Double upperBound) {
            this.timestamp = timestamp;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public Double getLowerBound() {
            return lowerBound;
        }

        public Double getUpperBound() {
            return upperBound;
        }
    }

    public static class ForecastMetrics {
        private final Double mae;
        private final Double rmse;
        private final Double mape;
        private final Double confidenceLevel;

        public ForecastMetrics(Double mae, Double rmse, Double mape, Double confidenceLevel) {
            this.mae = mae;
            this.rmse = rmse;
            this.mape = mape;
            this.confidenceLevel = confidenceLevel;
        }

        public Double getMae() {
            return mae;
        }

        public Double getRmse() {
            return rmse;
        }

        public Double getMape() {
            return mape;
        }

        public Double getConfidenceLevel() {
            return confidenceLevel;
        }
    }
}
