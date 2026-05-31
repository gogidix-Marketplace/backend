package com.gogidix.analytics.metrics.domain.port.out;

import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;

/**
 * Output port: Publisher for metric-related events.
 */
public interface MetricEventPublisher {

    void publishMetricIngested(MetricDataPoint metric);

    void publishMetricAggregated(MetricDataPoint metric, String aggregationType);

    void publishAlertTriggered(MetricAlert alert, String reason);

    void publishAlertResolved(MetricAlert alert);
}
