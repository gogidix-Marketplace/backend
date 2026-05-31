package com.gogidix.monitoring.performance.domain.port.out;

import com.gogidix.monitoring.performance.domain.model.MetricData;

import java.time.Instant;
import java.util.List;

/**
 * Outbound port (repository) for metric data persistence.
 */
public interface MetricRepository {

    /**
     * Save a metric.
     */
    MetricData save(MetricData metric);

    /**
     * Save multiple metrics in batch.
     */
    List<MetricData> saveAll(List<MetricData> metrics);

    /**
     * Query metrics with filters.
     */
    List<MetricData> findByTenantIdAndServiceIdAndMetricNameAndTimestampBetween(
            String tenantId, String serviceId, String metricName, Instant from, Instant to);

    /**
     * Get aggregated metrics.
     */
    List<MetricData> findAggregatedMetrics(String tenantId, String serviceId, String metricName,
                                          Instant from, Instant to, String aggregation);

    /**
     * Delete metrics older than retention period.
     */
    void deleteOlderThan(Instant cutoff);
}
