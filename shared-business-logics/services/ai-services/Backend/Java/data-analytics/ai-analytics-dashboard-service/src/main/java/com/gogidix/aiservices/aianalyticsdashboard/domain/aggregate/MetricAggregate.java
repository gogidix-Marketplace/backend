package com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.Trend;

import java.time.Instant;
import java.util.*;

public class MetricAggregate {
    private static final int MAX_TIME_SERIES_POINTS = 1000;
    private static final double TREND_THRESHOLD = 0.05;

    private String metricId;
    private final String name;
    private double value;
    private Double previousValue;
    private Double change;
    private Trend trend;
    private final List<TimeSeriesPoint> timeSeries;
    private Instant timestamp;
    private String unit;
    private Map<String, String> tags;

    private MetricAggregate(String name, double value) {
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
        this.unit = null;
        this.tags = new HashMap<>();
    }

    public static MetricAggregate create(String name, double value) {
        return new MetricAggregate(name, value);
    }

    public static MetricAggregate restore(String metricId, String name, double value,
                                         Double previousValue, Double change, Trend trend,
                                         List<TimeSeriesPoint> timeSeries, Instant timestamp,
                                         String unit, Map<String, String> tags) {
        MetricAggregate metric = new MetricAggregate(name, value);
        metric.metricId = metricId;
        metric.previousValue = previousValue;
        metric.change = change;
        metric.trend = trend;
        metric.timeSeries.addAll(timeSeries);
        metric.timestamp = timestamp;
        metric.unit = unit;
        metric.tags = tags != null ? new HashMap<>(tags) : new HashMap<>();
        return metric;
    }

    // Getters
    public String getMetricId() { return metricId; }
    public String getName() { return name; }
    public double getValue() { return value; }
    public Double getPreviousValue() { return previousValue; }
    public Double getChange() { return change; }
    public Trend getTrend() { return trend; }
    public List<TimeSeriesPoint> getTimeSeries() { return Collections.unmodifiableList(timeSeries); }
    public Instant getTimestamp() { return timestamp; }
    public String getUnit() { return unit; }
    public Map<String, String> getTags() { return Collections.unmodifiableMap(tags); }

    // Setters
    public void setValue(double value) {
        this.previousValue = this.value;
        this.value = value;
        calculateTrend();
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
        calculateTrend();
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setTag(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag key cannot be null or empty");
        }
        tags.put(key, value);
    }

    public void removeTag(String key) {
        tags.remove(key);
    }

    // Business methods
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

    public void addTimeSeriesPoints(List<TimeSeriesPoint> points) {
        timeSeries.addAll(points);

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

    public double calculatePercentile(double percentile) {
        if (timeSeries.isEmpty()) {
            return value;
        }
        List<Double> sorted = timeSeries.stream()
                .map(TimeSeriesPoint::value)
                .sorted()
                .toList();
        int index = (int) Math.ceil(percentile * sorted.size()) - 1;
        return sorted.get(Math.max(0, Math.min(index, sorted.size() - 1)));
    }

    public record TimeSeriesPoint(Instant timestamp, double value) {}
}
