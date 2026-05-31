package com.gogidix.aiservices.timeseriesforecasting.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain value object representing a time series data point.
 */
public class TimeSeriesDataPoint {

    private final LocalDateTime timestamp;
    private final Double value;

    public TimeSeriesDataPoint(LocalDateTime timestamp, Double value) {
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
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
        return Objects.equals(timestamp, that.timestamp) &&
               Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, value);
    }

    @Override
    public String toString() {
        return "TimeSeriesDataPoint{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
