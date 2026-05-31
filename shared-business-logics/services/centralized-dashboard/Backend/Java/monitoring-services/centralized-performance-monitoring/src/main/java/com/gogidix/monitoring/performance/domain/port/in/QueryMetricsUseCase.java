package com.gogidix.monitoring.performance.domain.port.in;

import com.gogidix.monitoring.performance.domain.model.MetricData;
import com.gogidix.monitoring.performance.domain.model.PerformanceAlert;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Inbound port (use case) for querying metrics and alerts.
 */
public interface QueryMetricsUseCase {

    /**
     * Query metrics for a tenant.
     */
    List<MetricData> queryMetrics(String tenantId, String serviceId, String metricName,
                                  Instant from, Instant to, Map<String, String> labels);

    /**
     * Get aggregated metrics.
     */
    Map<String, Double> getAggregatedMetrics(String tenantId, String serviceId, String metricName,
                                             Instant from, Instant to, String aggregation);

    /**
     * Get active alerts for a tenant.
     */
    List<PerformanceAlert> getActiveAlerts(String tenantId, String serviceId);

    /**
     * Get alert by ID.
     */
    PerformanceAlert getAlert(String alertId);

    /**
     * Resolve an alert.
     */
    PerformanceAlert resolveAlert(String alertId);
}
