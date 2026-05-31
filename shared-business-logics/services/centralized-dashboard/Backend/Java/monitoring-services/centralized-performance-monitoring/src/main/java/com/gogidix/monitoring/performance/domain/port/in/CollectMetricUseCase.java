package com.gogidix.monitoring.performance.domain.port.in;

import com.gogidix.monitoring.performance.domain.model.MetricData;

import java.util.List;

/**
 * Inbound port (use case) for collecting metrics.
 */
public interface CollectMetricUseCase {

    /**
     * Collect a single metric.
     */
    MetricData collectMetric(MetricData metric);

    /**
     * Collect multiple metrics in batch.
     */
    List<MetricData> collectMetrics(List<MetricData> metrics);

    /**
     * Create an alert for a metric.
     */
    void createAlert(String tenantId, String serviceId, String alertName,
                   String condition, Double threshold, Double actualValue, String message);
}
