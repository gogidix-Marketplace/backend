package com.gogidix.hr.globalhrdashboard.application.service;

import com.gogidix.hr.globalhrdashboard.domain.model.GlobalWorkforceMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.model.ExecutiveLevel;
import com.gogidix.hr.globalhrdashboard.domain.model.AggregationLevel;
import com.gogidix.hr.globalhrdashboard.domain.repository.GlobalWorkforceMetricRepository;
import com.gogidix.hr.globalhrdashboard.shared.exception.NotFoundException;
import com.gogidix.hr.globalhrdashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Query Service for Global Workforce Metrics
 * Handles all read operations for global HR metrics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GlobalMetricsQueryService {

    private final GlobalWorkforceMetricRepository globalWorkforceMetricRepository;

    /**
     * Get all global workforce metrics for the current tenant
     */
    public List<GlobalWorkforceMetric> getAllMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all global metrics for tenant: {}", tenantId);
        return globalWorkforceMetricRepository.findByTenantId(tenantId);
    }

    /**
     * Get metrics by category
     */
    public List<GlobalWorkforceMetric> getMetricsByCategory(MetricCategory category) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics by category: {} for tenant: {}", category, tenantId);
        return globalWorkforceMetricRepository.findByTenantIdAndMetricCategory(tenantId, category);
    }

    /**
     * Get metrics by executive level
     */
    public List<GlobalWorkforceMetric> getMetricsByExecutiveLevel(ExecutiveLevel executiveLevel) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics by executive level: {} for tenant: {}", executiveLevel, tenantId);
        return globalWorkforceMetricRepository.findByTenantIdAndExecutiveLevel(tenantId, executiveLevel);
    }

    /**
     * Get metrics by period
     */
    public List<GlobalWorkforceMetric> getMetricsByPeriod(String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics by period: {} for tenant: {}", period, tenantId);
        return globalWorkforceMetricRepository.findByTenantIdAndPeriod(tenantId, period);
    }

    /**
     * Get metrics by category and period
     */
    public List<GlobalWorkforceMetric> getMetricsByCategoryAndPeriod(MetricCategory category, String period) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics by category: {} and period: {} for tenant: {}", category, period, tenantId);
        return globalWorkforceMetricRepository.findByTenantIdAndCategoryAndPeriod(tenantId, category, period);
    }

    /**
     * Get metric by ID
     */
    public GlobalWorkforceMetric getMetricById(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metric by ID: {} for tenant: {}", metricId, tenantId);
        return globalWorkforceMetricRepository.findByIdAndTenantId(metricId, tenantId)
                .orElseThrow(() -> new NotFoundException("GlobalWorkforceMetric", metricId));
    }

    /**
     * Get metrics by name
     */
    public List<GlobalWorkforceMetric> getMetricsByName(String metricName) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics by name: {} for tenant: {}", metricName, tenantId);
        return globalWorkforceMetricRepository.findByTenantIdAndMetricName(tenantId, metricName);
    }

    /**
     * Get active metrics
     */
    public List<GlobalWorkforceMetric> getActiveMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching active metrics for tenant: {}", tenantId);
        return globalWorkforceMetricRepository.findActiveByTenantId(tenantId);
    }

    /**
     * Get latest metrics
     */
    public List<GlobalWorkforceMetric> getLatestMetrics(int limit) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest {} metrics for tenant: {}", limit, tenantId);
        return globalWorkforceMetricRepository.findLatestByTenantId(tenantId, limit);
    }

    /**
     * Get metrics by aggregation level
     */
    public List<GlobalWorkforceMetric> getMetricsByAggregationLevel(AggregationLevel aggregationLevel) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics by aggregation level: {} for tenant: {}", aggregationLevel, tenantId);
        return globalWorkforceMetricRepository.findByTenantIdAndAggregationLevel(tenantId, aggregationLevel);
    }

    /**
     * Get metrics summary
     */
    public MetricsSummary getMetricsSummary() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics summary for tenant: {}", tenantId);

        List<GlobalWorkforceMetric> allMetrics = globalWorkforceMetricRepository.findByTenantId(tenantId);

        Map<MetricCategory, Long> countByCategory = allMetrics.stream()
                .collect(Collectors.groupingBy(
                        GlobalWorkforceMetric::getMetricCategory,
                        Collectors.counting()
                ));

        Map<ExecutiveLevel, Long> countByLevel = allMetrics.stream()
                .collect(Collectors.groupingBy(
                        GlobalWorkforceMetric::getExecutiveLevel,
                        Collectors.counting()
                ));

        long activeCount = allMetrics.stream()
                .filter(m -> m.getIsActive() != null && m.getIsActive())
                .count();

        return new MetricsSummary(
                allMetrics.size(),
                activeCount,
                countByCategory,
                countByLevel
        );
    }

    /**
     * Get metrics needing aggregation
     */
    public List<GlobalWorkforceMetric> getMetricsNeedingAggregation(Instant before) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics needing aggregation before: {} for tenant: {}", before, tenantId);
        return globalWorkforceMetricRepository.findMetricsNeedingAggregation(tenantId, before);
    }

    /**
     * Get metrics updated since a specific time
     */
    public List<GlobalWorkforceMetric> getMetricsUpdatedSince(Instant updatedAt) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching metrics updated since: {} for tenant: {}", updatedAt, tenantId);
        return globalWorkforceMetricRepository.findByTenantIdAndUpdatedAtAfter(tenantId, updatedAt);
    }

    /**
     * Count metrics by category
     */
    public long countMetricsByCategory(MetricCategory category) {
        String tenantId = RequestContextHolder.getTenantId();
        return globalWorkforceMetricRepository.countByTenantIdAndMetricCategory(tenantId, category);
    }

    /**
     * Check if metric exists
     */
    public boolean metricExists(String metricId) {
        String tenantId = RequestContextHolder.getTenantId();
        return globalWorkforceMetricRepository.existsByIdAndTenantId(metricId, tenantId);
    }

    /**
     * Metrics summary record
     */
    public record MetricsSummary(
            long totalMetrics,
            long activeMetrics,
            Map<MetricCategory, Long> countByCategory,
            Map<ExecutiveLevel, Long> countByLevel
    ) {}
}
