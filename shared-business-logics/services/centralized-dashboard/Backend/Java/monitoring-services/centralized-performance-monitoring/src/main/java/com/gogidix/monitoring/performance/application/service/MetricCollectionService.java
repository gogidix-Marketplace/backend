package com.gogidix.monitoring.performance.application.service;

import com.gogidix.monitoring.performance.domain.model.MetricData;
import com.gogidix.monitoring.performance.domain.model.PerformanceAlert;
import com.gogidix.monitoring.performance.domain.port.in.CollectMetricUseCase;
import com.gogidix.monitoring.performance.domain.port.in.QueryMetricsUseCase;
import com.gogidix.monitoring.performance.domain.port.out.AlertRepository;
import com.gogidix.monitoring.performance.domain.port.out.MetricPublisher;
import com.gogidix.monitoring.performance.domain.port.out.MetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Application service implementing metric collection use cases.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetricCollectionService implements CollectMetricUseCase {

    private final MetricRepository metricRepository;
    private final AlertRepository alertRepository;
    private final MetricPublisher metricPublisher;

    @Override
    @Transactional
    public MetricData collectMetric(MetricData metric) {
        log.debug("Collecting metric: {} for tenant: {}", metric.getMetricName(), metric.getTenantId());
        MetricData saved = metricRepository.save(metric);
        metricPublisher.publishMetricEvent(saved);
        return saved;
    }

    @Override
    @Transactional
    public List<MetricData> collectMetrics(List<MetricData> metrics) {
        log.debug("Collecting {} metrics in batch", metrics.size());
        List<MetricData> saved = metricRepository.saveAll(metrics);
        saved.forEach(metricPublisher::publishMetricEvent);
        return saved;
    }

    @Override
    @Transactional
    public void createAlert(String tenantId, String serviceId, String alertName,
                          String condition, Double threshold, Double actualValue, String message) {
        log.warn("Creating alert: {} for tenant: {}, service: {}", alertName, tenantId, serviceId);
        PerformanceAlert alert = PerformanceAlert.create(
                tenantId, serviceId, alertName, condition, threshold, actualValue, message);
        PerformanceAlert saved = alertRepository.save(alert);
        metricPublisher.publishAlertEvent(saved.getId(), tenantId, serviceId, message);
    }
}
