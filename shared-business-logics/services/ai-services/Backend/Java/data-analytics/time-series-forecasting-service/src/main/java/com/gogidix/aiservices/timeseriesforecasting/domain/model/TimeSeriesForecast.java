package com.gogidix.aiservices.timeseriesforecasting.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

public class TimeSeriesForecast {
    private static final int MIN_DATA_POINTS = 20;
    private static final int MAX_FORECAST_HORIZON = 1000;

    private final String forecastId;
    private final List<TimeSeriesPoint> timeSeriesData;
    private final int forecastHorizon;
    private final Frequency frequency;
    private final boolean includeSeasonality;
    private final double confidenceLevel;
    private final Instant createdAt;
    private Status status;
    private List<ForecastValue> forecasts;
    private AccuracyMetrics accuracyMetrics;

    public enum Status {
        PROCESSING, COMPLETED, FAILED
    }

    private TimeSeriesForecast(List<TimeSeriesPoint> timeSeriesData, int forecastHorizon, Frequency frequency, boolean includeSeasonality) {
        if (timeSeriesData == null) {
            throw new IllegalArgumentException("Time series data cannot be null");
        }
        if (timeSeriesData.size() < MIN_DATA_POINTS) {
            throw new IllegalArgumentException("Time series must contain at least " + MIN_DATA_POINTS + " data points");
        }
        if (forecastHorizon < 1 || forecastHorizon > MAX_FORECAST_HORIZON) {
            throw new IllegalArgumentException("Forecast horizon must be between 1 and " + MAX_FORECAST_HORIZON);
        }
        if (frequency == null) {
            throw new IllegalArgumentException("Frequency cannot be null");
        }

        this.forecastId = UUID.randomUUID().toString();
        this.timeSeriesData = timeSeriesData;
        this.forecastHorizon = forecastHorizon;
        this.frequency = frequency;
        this.includeSeasonality = includeSeasonality;
        this.confidenceLevel = 0.95;
        this.createdAt = Instant.now();
        this.status = Status.PROCESSING;
    }

    public static TimeSeriesForecast create(List<TimeSeriesPoint> timeSeriesData, int forecastHorizon, Frequency frequency) {
        return new TimeSeriesForecast(timeSeriesData, forecastHorizon, frequency, false);
    }

    public TimeSeriesForecast withSeasonality(boolean includeSeasonality) {
        return new TimeSeriesForecast(timeSeriesData, forecastHorizon, frequency, includeSeasonality);
    }

    public void setIncludeSeasonality(boolean includeSeasonality) {
        // Would update internal state
    }

    public void complete(List<ForecastValue> forecasts) {
        this.status = Status.COMPLETED;
        this.forecasts = forecasts;
    }

    public String getForecastId() { return forecastId; }
    public List<TimeSeriesPoint> getTimeSeriesData() { return timeSeriesData; }
    public int getForecastHorizon() { return forecastHorizon; }
    public Frequency getFrequency() { return frequency; }
    public boolean isIncludeSeasonality() { return includeSeasonality; }
    public double getConfidenceLevel() { return confidenceLevel; }
    public Instant getCreatedAt() { return createdAt; }
    public Status getStatus() { return status; }
    public List<ForecastValue> getForecasts() { return forecasts; }
    public AccuracyMetrics getAccuracyMetrics() { return accuracyMetrics; }

    public static class TimeSeriesPoint {
        private final String timestamp;
        private final double value;

        public TimeSeriesPoint(String timestamp, double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public String getTimestamp() { return timestamp; }
        public double getValue() { return value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeSeriesPoint that = (TimeSeriesPoint) o;
            return Double.compare(that.value, value) == 0 &&
                   Objects.equals(timestamp, that.timestamp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timestamp, value);
        }
    }

    public static class ForecastValue {
        private final String timestamp;
        private final double value;
        private final double lowerBound;
        private final double upperBound;

        public ForecastValue(String timestamp, double value, double lowerBound, double upperBound) {
            this.timestamp = timestamp;
            this.value = value;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        public String getTimestamp() { return timestamp; }
        public double getValue() { return value; }
        public double getLowerBound() { return lowerBound; }
        public double getUpperBound() { return upperBound; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ForecastValue that = (ForecastValue) o;
            return Double.compare(that.value, value) == 0 &&
                   Double.compare(that.lowerBound, lowerBound) == 0 &&
                   Double.compare(that.upperBound, upperBound) == 0 &&
                   Objects.equals(timestamp, that.timestamp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timestamp, value, lowerBound, upperBound);
        }
    }

    public static class AccuracyMetrics {
        private final double mae;
        private final double rmse;
        private final double mape;

        public AccuracyMetrics(double mae, double rmse, double mape) {
            this.mae = mae;
            this.rmse = rmse;
            this.mape = mape;
        }

        public double getMae() { return mae; }
        public double getRmse() { return rmse; }
        public double getMape() { return mape; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AccuracyMetrics that = (AccuracyMetrics) o;
            return Double.compare(that.mae, mae) == 0 &&
                   Double.compare(that.rmse, rmse) == 0 &&
                   Double.compare(that.mape, mape) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(mae, rmse, mape);
        }
    }
}
