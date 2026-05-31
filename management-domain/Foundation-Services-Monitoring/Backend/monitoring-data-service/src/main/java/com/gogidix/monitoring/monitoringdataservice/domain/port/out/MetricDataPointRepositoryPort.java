package com.gogidix.monitoring.monitoringdataservice.domain.port.out;

import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Output port for metric data point repository operations.
 * This is the persistence abstraction for metric data.
 */
public interface MetricDataPointRepositoryPort {

    /**
     * Save a metric data point.
     *
     * @param dataPoint the metric to save
     * @return the saved metric
     */
    MetricDataPoint save(MetricDataPoint dataPoint);

    /**
     * Save multiple metric data points in batch.
     *
     * @param dataPoints the metrics to save
     * @return count of saved metrics
     */
    long saveAll(List<MetricDataPoint> dataPoints);

    /**
     * Find a metric by ID.
     *
     * @param id the metric ID
     * @return the metric if found
     */
    Optional<MetricDataPoint> findById(String id);

    /**
     * Find metrics by service name and time range.
     *
     * @param tenantId   the tenant ID
     * @param serviceName the service name
     * @param startTime  start of time range
     * @param endTime    end of time range
     * @return list of metrics
     */
    List<MetricDataPoint> findByServiceAndTimeRange(
            String tenantId,
            String serviceName,
            Instant startTime,
            Instant endTime
    );

    /**
     * Find metrics by service name, metric name, and time range.
     *
     * @param tenantId   the tenant ID
     * @param serviceName the service name
     * @param metricName the metric name
     * @param startTime  start of time range
     * @param endTime    end of time range
     * @return list of metrics
     */
    List<MetricDataPoint> findByServiceMetricAndTimeRange(
            String tenantId,
            String serviceName,
            String metricName,
            Instant startTime,
            Instant endTime
    );

    /**
     * Delete metrics older than the given timestamp.
     *
     * @param beforeTimestamp the timestamp cutoff
     * @return count of deleted metrics
     */
    long deleteOlderThan(Instant beforeTimestamp);

    /**
     * Count metrics by tenant.
     *
     * @param tenantId the tenant ID
     * @return count of metrics
     */
    long countByTenant(String tenantId);

    /**
     * Get the latest metric timestamp for a service.
     *
     * @param tenantId   the tenant ID
     * @param serviceName the service name
     * @return the latest timestamp or null if no metrics exist
     */
    Optional<Instant> getLatestMetricTimestamp(String tenantId, String serviceName);
}
