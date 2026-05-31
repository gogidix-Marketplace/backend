package com.gogidix.aiservices.timeseriesforecasting.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Domain entity representing a time series forecast.
 */
public class TimeSeriesForecast {

    private static final int MIN_DATA_POINTS = 20;
    private static final int MAX_FORECAST_HORIZON = 1000;

    private final String forecastId;
    private final List<TimeSeriesDataPoint> timeSeriesData;
    private final Integer forecastHorizon;
    private final Frequency frequency;
    private final Boolean includeSeasonality;
    private final List<ForecastPoint> forecasts;
    private final AccuracyMetrics accuracyMetrics;
    private final LocalDateTime generatedAt;

    @java.lang.SuppressWarnings("all")
    public TimeSeriesForecast(String forecastId,
                             List<TimeSeriesDataPoint> timeSeriesData,
                             Integer forecastHorizon,
                             Frequency frequency,
                             Boolean includeSeasonality,
                             List<ForecastPoint> forecasts,
                             AccuracyMetrics accuracyMetrics,
                             LocalDateTime generatedAt) {
        if (forecastId == null || forecastId.trim().isEmpty()) {
            throw new IllegalArgumentException("forecastId cannot be null or empty");
        }
        if (timeSeriesData == null || timeSeriesData.size() < MIN_DATA_POINTS) {
            throw new IllegalArgumentException(
                "timeSeriesData must contain at least " + MIN_DATA_POINTS + " data points");
        }
        if (forecastHorizon == null || forecastHorizon <= 0 || forecastHorizon > MAX_FORECAST_HORIZON) {
            throw new IllegalArgumentException(
                "forecastHorizon must be between 1 and " + MAX_FORECAST_HORIZON);
        }
        if (frequency == null) {
            throw new IllegalArgumentException("frequency cannot be null");
        }
        if (forecasts == null || forecasts.isEmpty()) {
            throw new IllegalArgumentException("forecasts cannot be null or empty");
        }
        if (accuracyMetrics == null) {
            throw new IllegalArgumentException("accuracyMetrics cannot be null");
        }
        if (generatedAt == null) {
            throw new IllegalArgumentException("generatedAt cannot be null");
        }

        this.forecastId = forecastId;
        this.timeSeriesData = timeSeriesData;
        this.forecastHorizon = forecastHorizon;
        this.frequency = frequency;
        this.includeSeasonality = includeSeasonality != null ? includeSeasonality : false;
        this.forecasts = forecasts;
        this.accuracyMetrics = accuracyMetrics;
        this.generatedAt = generatedAt;
    }

    public String getForecastId() {
        return forecastId;
    }

    public List<TimeSeriesDataPoint> getTimeSeriesData() {
        return timeSeriesData;
    }

    public Integer getForecastHorizon() {
        return forecastHorizon;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public Boolean getIncludeSeasonality() {
        return includeSeasonality;
    }

    public List<ForecastPoint> getForecasts() {
        return forecasts;
    }

    public AccuracyMetrics getAccuracyMetrics() {
        return accuracyMetrics;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSeriesForecast that = (TimeSeriesForecast) o;
        return Objects.equals(forecastId, that.forecastId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forecastId);
    }

    public enum Frequency {
        HOURLY,
        DAILY,
        WEEKLY,
        MONTHLY
    }

    public static class TimeSeriesDataPoint {
        private final LocalDateTime timestamp;
        private final Double value;

        public TimeSeriesDataPoint(LocalDateTime timestamp, Double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public Double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeSeriesDataPoint that = (TimeSeriesDataPoint) o;
            return Objects.equals(timestamp, that.timestamp) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timestamp, value);
        }
    }

    public static class ForecastPoint {
        private final LocalDateTime timestamp;
        private final Double value;
        private final Double lowerBound;
        private final Double upperBound;

        public ForecastPoint(LocalDateTime timestamp, Double value, Double lowerBound, Double upperBound) {
            this.timestamp = timestamp;
            this.value = value;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public Double getValue() {
            return value;
        }

        public Double getLowerBound() {
            return lowerBound;
        }

        public Double getUpperBound() {
            return upperBound;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ForecastPoint that = (ForecastPoint) o;
            return Objects.equals(timestamp, that.timestamp) &&
                   Objects.equals(value, that.value) &&
                   Objects.equals(lowerBound, that.lowerBound) &&
                   Objects.equals(upperBound, that.upperBound);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timestamp, value, lowerBound, upperBound);
        }
    }

    public static class AccuracyMetrics {
        private final Double mae;
        private final Double rmse;
        private final Double mape;
        private final Double directionAccuracy;

        public AccuracyMetrics(Double mae, Double rmse, Double mape, Double directionAccuracy) {
            this.mae = mae;
            this.rmse = rmse;
            this.mape = mape;
            this.directionAccuracy = directionAccuracy;
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

        public Double getDirectionAccuracy() {
            return directionAccuracy;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccuracyMetrics that = (AccuracyMetrics) o;
            return Objects.equals(mae, that.mae) &&
                   Objects.equals(rmse, that.rmse) &&
                   Objects.equals(mape, that.mape) &&
                   Objects.equals(directionAccuracy, that.directionAccuracy);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mae, rmse, mape, directionAccuracy);
        }
    }
}
