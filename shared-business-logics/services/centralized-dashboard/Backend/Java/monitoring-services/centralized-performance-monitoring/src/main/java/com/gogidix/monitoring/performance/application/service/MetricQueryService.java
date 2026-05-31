package com.gogidix.monitoring.performance.application.service;

import com.gogidix.monitoring.performance.domain.model.AlertStatus;
import com.gogidix.monitoring.performance.domain.model.MetricData;
import com.gogidix.monitoring.performance.domain.model.PerformanceAlert;
import com.gogidix.monitoring.performance.domain.port.in.QueryMetricsUseCase;
import com.gogidix.monitoring.performance.domain.port.out.AlertRepository;
import com.gogidix.monitoring.performance.domain.port.out.MetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Application service implementing metric query use cases.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetricQueryService implements QueryMetricsUseCase {

    private final MetricRepository metricRepository;
    private final AlertRepository alertRepository;

    @Override
    public List<MetricData> queryMetrics(String tenantId, String serviceId, String metricName,
                                         Instant from, Instant to, Map<String, String> labels) {
        log.debug("Querying metrics for tenant: {}, service: {}, metric: {}", tenantId, serviceId, metricName);
        return metricRepository.findByTenantIdAndServiceIdAndMetricNameAndTimestampBetween(
                tenantId, serviceId, metricName, from, to);
    }

    @Override
    public Map<String, Double> getAggregatedMetrics(String tenantId, String serviceId, String metricName,
                                                 Instant from, Instant to, String aggregation) {
        log.debug("Getting aggregated metrics for tenant: {}, metric: {}, aggregation: {}",
                  tenantId, metricName, aggregation);
        List<MetricData> metrics = metricRepository.findAggregatedMetrics(
                tenantId, serviceId, metricName, from, to, aggregation);

        return metrics.stream()
                .collect(Collectors.toMap(
                        m -> m.getLabels() != null ? m.getLabels().getOrDefault("instance", "default") : "default",
                        MetricData::getValue,
                        (v1, v2) -> aggregation.equals("avg") ? (v1 + v2) / 2 : v1 + v2
                ));
    }

    @Override
    public List<PerformanceAlert> getActiveAlerts(String tenantId, String serviceId) {
        log.debug("Getting active alerts for tenant: {}, service: {}", tenantId, serviceId);
        return alertRepository.findActiveAlerts(tenantId, serviceId);
    }

    @Override
    public PerformanceAlert getAlert(String alertId) {
        return alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + alertId));
    }

    @Override
    public PerformanceAlert resolveAlert(String alertId) {
        log.info("Resolving alert: {}", alertId);
        alertRepository.updateStatus(alertId, AlertStatus.RESOLVED.getCode());
        PerformanceAlert alert = getAlert(alertId);
        alert.resolve();
        return alertRepository.save(alert);
    }
}
