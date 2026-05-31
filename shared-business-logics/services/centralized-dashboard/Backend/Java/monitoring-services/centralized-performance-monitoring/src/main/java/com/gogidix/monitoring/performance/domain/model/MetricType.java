package com.gogidix.monitoring.performance.domain.model;

import lombok.Getter;

/**
 * Enumeration of metric types supported by the performance monitoring service.
 */
@Getter
public enum MetricType {
    COUNTER("counter", "Counter metric that only increases"),
    GAUGE("gauge", "Gauge metric that can go up or down"),
    HISTOGRAM("histogram", "Histogram metric with configurable buckets"),
    SUMMARY("summary", "Summary metric with quantiles"),
    TIMING("timing", "Timing metric for duration measurements");

    private final String code;
    private final String description;

    MetricType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
