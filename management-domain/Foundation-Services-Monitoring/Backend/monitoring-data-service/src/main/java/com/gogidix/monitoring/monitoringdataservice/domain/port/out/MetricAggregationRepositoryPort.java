package com.gogidix.monitoring.monitoringdataservice.domain.port.out;

import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricAggregation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Output port for metric aggregation repository operations.
 */
public interface MetricAggregationRepositoryPort {

    /**
     * Save a metric aggregation.
     *
     * @param aggregation the aggregation to save
     * @return the saved aggregation
     */
    MetricAggregation save(MetricAggregation aggregation);

    /**
     * Find aggregations by service, metric, and time range.
     *
     * @param tenantId    the tenant ID
     * @param serviceName the service name
     * @param metricName  the metric name
     * @param window      the aggregation window
     * @param startTime   start of time range
     * @param endTime     end of time range
     * @return list of aggregations
     */
    List<MetricAggregation> findByServiceMetricAndTimeRange(
            String tenantId,
            String serviceName,
            String metricName,
            MetricAggregation.AggregationWindow window,
            Instant startTime,
            Instant endTime
    );

    /**
     * Find the latest aggregation for a service and metric.
     *
     * @param tenantId    the tenant ID
     * @param serviceName the service name
     * @param metricName  the metric name
     * @param window      the aggregation window
     * @return the latest aggregation if found
     */
    Optional<MetricAggregation> findLatest(
            String tenantId,
            String serviceName,
            String metricName,
            MetricAggregation.AggregationWindow window
    );

    /**
     * Delete aggregations older than the given timestamp.
     *
     * @param beforeTimestamp the timestamp cutoff
     * @return count of deleted aggregations
     */
    long deleteOlderThan(Instant beforeTimestamp);

    /**
     * Delete aggregations by window and time range.
     *
     * @param window  the aggregation window
     * @param beforeTimestamp the timestamp cutoff
     * @return count of deleted aggregations
     */
    long deleteByWindowAndOlderThan(
            MetricAggregation.AggregationWindow window,
            Instant beforeTimestamp
    );
}
