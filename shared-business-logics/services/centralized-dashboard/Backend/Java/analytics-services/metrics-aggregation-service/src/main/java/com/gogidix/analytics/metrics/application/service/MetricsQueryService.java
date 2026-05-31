package com.gogidix.analytics.metrics.application.service;

import com.gogidix.analytics.metrics.domain.model.MetricAggregation;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.port.in.GetMetricsQuery;
import com.gogidix.analytics.metrics.domain.repository.MetricAggregationRepository;
import com.gogidix.analytics.metrics.domain.repository.MetricAlertRepository;
import com.gogidix.analytics.metrics.domain.repository.MetricDataPointRepository;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * CQRS Query Handler for Metrics operations.
 * Handles all read operations for metrics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsQueryService {

    private final MetricDataPointRepository metricRepository;
    private final MetricAggregationRepository aggregationRepository;
    private final MetricAlertRepository alertRepository;

    /**
     * Get metrics with filters
     */
    public Page<MetricDataPoint> getMetrics(GetMetricsQuery query, Pageable pageable) {
        log.debug("Querying metrics: name={}, from={}, to={}",
            query.getMetricName(), query.getStartTime(), query.getEndTime());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        List<MetricDataPoint> results;

        if (query.getMetricName() != null) {
            results = metricRepository.findByTenantIdAndMetricNameAndTimestampBetween(
                tenantId, query.getMetricName(), query.getStartTime(), query.getEndTime());
        } else if (query.getSourceService() != null) {
            results = metricRepository.findByTenantIdAndSourceServiceAndTimestampBetween(
                tenantId, query.getSourceService(), query.getStartTime(), query.getEndTime());
        } else {
            results = metricRepository.findByTenantIdAndTimestampBetween(
                tenantId, query.getStartTime(), query.getEndTime());
        }

        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), results.size());
        List<MetricDataPoint> pagedResults = results.subList(start, end);

        return new PageImpl<>(pagedResults, pageable, results.size());
    }

    /**
     * Get aggregated metrics
     */
    public List<MetricAggregation> getAggregatedMetrics(
        String metricName,
        String aggregationType,
        LocalDateTime startTime,
        LocalDateTime endTime) {

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        MetricAggregation.AggregationType aggType = aggregationType != null
            ? MetricAggregation.AggregationType.valueOf(aggregationType.toUpperCase())
            : MetricAggregation.AggregationType.AVG;

        return aggregationRepository.findAggregatesForTimeRange(
            tenantId, metricName, aggType, startTime, endTime);
    }

    /**
     * Get current metric value (latest)
     */
    public MetricDataPoint getCurrentMetricValue(String metricName) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        return metricRepository.findFirstByTenantIdAndMetricNameOrderByTimestampDesc(tenantId, metricName)
            .orElseThrow(() -> new NotFoundException("Metric not found: " + metricName));
    }

    /**
     * Get metric statistics for a time range
     */
    public MetricStatistics getMetricStatistics(String metricName, LocalDateTime startTime, LocalDateTime endTime) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        List<MetricDataPoint> metrics = metricRepository.findByTenantIdAndMetricNameAndTimestampBetween(
            tenantId, metricName, startTime, endTime);

        if (metrics.isEmpty()) {
            return new MetricStatistics(metricName, startTime, endTime, 0L, null, null, null, null);
        }

        BigDecimal sum = metrics.stream()
            .map(MetricDataPoint::getMetricValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avg = sum.divide(BigDecimal.valueOf(metrics.size()), 6, RoundingMode.HALF_UP);

        BigDecimal min = metrics.stream()
            .map(MetricDataPoint::getMetricValue)
            .min(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);

        BigDecimal max = metrics.stream()
            .map(MetricDataPoint::getMetricValue)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);

        return new MetricStatistics(metricName, startTime, endTime, (long) metrics.size(), avg, min, max, sum);
    }

    /**
     * Get alert by ID
     */
    public MetricAlert getAlert(String alertId) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        MetricAlert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));

        if (!alert.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Alert belongs to different tenant");
        }

        return alert;
    }

    /**
     * Get all alerts for tenant
     */
    public List<MetricAlert> getAlerts() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        return alertRepository.findByTenantIdAndEnabledTrue(tenantId);
    }

    /**
     * Record class for metric statistics
     */
    public record MetricStatistics(
        String metricName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long count,
        BigDecimal average,
        BigDecimal minimum,
        BigDecimal maximum,
        BigDecimal sum
    ) {}
}
