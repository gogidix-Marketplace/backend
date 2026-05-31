package com.gogidix.monitoring.monitoringdataservice.domain.port.out;

import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricDataPoint;

import java.util.List;

/**
 * Output port for publishing metric events.
 * Used to send metric data to Kafka or other messaging systems.
 */
public interface MetricEventPublisherPort {

    /**
     * Publish a single metric event.
     *
     * @param metric the metric to publish
     */
    void publishMetric(MetricDataPoint metric);

    /**
     * Publish multiple metric events in batch.
     *
     * @param metrics the metrics to publish
     */
    void publishMetrics(List<MetricDataPoint> metrics);

    /**
     * Publish an alert event based on metric threshold.
     *
     * @param tenantId    the tenant ID
     * @param serviceName the service name
     * @param metricName  the metric name
     * @param threshold   the threshold that was exceeded
     * @param actualValue the actual value
     * @param message     the alert message
     */
    void publishThresholdExceeded(
            String tenantId,
            String serviceName,
            String metricName,
            double threshold,
            double actualValue,
            String message
    );
}
