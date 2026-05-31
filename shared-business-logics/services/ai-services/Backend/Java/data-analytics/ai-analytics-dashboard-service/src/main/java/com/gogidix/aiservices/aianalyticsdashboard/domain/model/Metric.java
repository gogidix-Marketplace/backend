package com.gogidix.aiservices.aianalyticsdashboard.domain.model;

import java.time.Instant;
import java.util.*;

public class Metric {
    private static final int MAX_TIME_SERIES_POINTS = 1000;
    private static final double TREND_THRESHOLD = 0.05; // 5%

    private final String metricId;
    private final String name;
    private double value;
    private Double previousValue;
    private Double change;
    private Trend trend;
    private final List<TimeSeriesPoint> timeSeries;
    private final Instant timestamp;

    private Metric(String name, double value) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Metric name cannot be null or empty");
        }

        this.metricId = UUID.randomUUID().toString();
        this.name = name;
        this.value = value;
        this.previousValue = null;
        this.change = null;
        this.trend = null;
        this.timeSeries = new ArrayList<>();
        this.timestamp = Instant.now();
    }

    public static Metric create(String name, double value) {
        return new Metric(name, value);
    }

    public String getMetricId() { return metricId; }
    public String getName() { return name; }
    public double getValue() { return value; }
    public Double getPreviousValue() { return previousValue; }
    public Double getChange() { return change; }
    public Trend getTrend() { return trend; }
    public List<TimeSeriesPoint> getTimeSeries() { return Collections.unmodifiableList(timeSeries); }
    public Instant getTimestamp() { return timestamp; }

    public void setValue(double value) {
        this.previousValue = this.value;
        this.value = value;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    public void calculateTrend() {
        if (previousValue == null || previousValue == 0) {
            this.change = 0.0;
            this.trend = Trend.STABLE;
            return;
        }

        this.change = ((value - previousValue) / previousValue) * 100;

        if (Math.abs(this.change) < TREND_THRESHOLD * 100) {
            this.trend = Trend.STABLE;
        } else if (this.change > 0) {
            this.trend = Trend.UP;
        } else {
            this.trend = Trend.DOWN;
        }
    }

    public void addTimeSeriesPoint(Instant timestamp, double value) {
        timeSeries.add(new TimeSeriesPoint(timestamp, value));

        while (timeSeries.size() > MAX_TIME_SERIES_POINTS) {
            timeSeries.remove(0);
        }
    }

    public double calculateAverage() {
        if (timeSeries.isEmpty()) {
            return value;
        }
        return timeSeries.stream().mapToDouble(TimeSeriesPoint::value).average().orElse(0.0);
    }

    public double calculateSum() {
        if (timeSeries.isEmpty()) {
            return value;
        }
        return timeSeries.stream().mapToDouble(TimeSeriesPoint::value).sum();
    }

    public double calculateMax() {
        if (timeSeries.isEmpty()) {
            return value;
        }
        return timeSeries.stream().mapToDouble(TimeSeriesPoint::value).max().orElse(value);
    }

    public double calculateMin() {
        if (timeSeries.isEmpty()) {
            return value;
        }
        return timeSeries.stream().mapToDouble(TimeSeriesPoint::value).min().orElse(value);
    }

    public record TimeSeriesPoint(Instant timestamp, double value) {}
}
