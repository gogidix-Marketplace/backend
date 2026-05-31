package com.gogidix.monitoring.performance.domain.port.out;

import com.gogidix.monitoring.performance.domain.model.MetricData;

/**
 * Outbound port for publishing metric events.
 */
public interface MetricPublisher {

    /**
     * Publish a metric event.
     */
    void publishMetricEvent(MetricData metric);

    /**
     * Publish an alert event.
     */
    void publishAlertEvent(String alertId, String tenantId, String serviceId, String message);
}
