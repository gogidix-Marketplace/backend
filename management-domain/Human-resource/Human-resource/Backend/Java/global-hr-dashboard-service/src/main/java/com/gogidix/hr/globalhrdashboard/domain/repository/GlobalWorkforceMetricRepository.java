package com.gogidix.hr.globalhrdashboard.domain.repository;

import com.gogidix.hr.globalhrdashboard.domain.model.GlobalWorkforceMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.model.ExecutiveLevel;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface for GlobalWorkforceMetric aggregate
 * Following Hexagonal Architecture - this is a PORT (out)
 */
public interface GlobalWorkforceMetricRepository {

    /**
     * Save a global workforce metric
     */
    GlobalWorkforceMetric save(GlobalWorkforceMetric metric);

    /**
     * Find metric by ID and tenant
     */
    Optional<GlobalWorkforceMetric> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all metrics for a tenant
     */
    List<GlobalWorkforceMetric> findByTenantId(String tenantId);

    /**
     * Find metrics by tenant and category
     */
    List<GlobalWorkforceMetric> findByTenantIdAndMetricCategory(String tenantId, MetricCategory category);

    /**
     * Find metrics by tenant and executive level
     */
    List<GlobalWorkforceMetric> findByTenantIdAndExecutiveLevel(String tenantId, ExecutiveLevel executiveLevel);

    /**
     * Find metrics by tenant and period
     */
    List<GlobalWorkforceMetric> findByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Find metrics by tenant, category and period
     */
    List<GlobalWorkforceMetric> findByTenantIdAndCategoryAndPeriod(String tenantId, MetricCategory category, String period);

    /**
     * Find active metrics for a tenant
     */
    List<GlobalWorkforceMetric> findActiveByTenantId(String tenantId);

    /**
     * Find metrics by metric name for a tenant
     */
    List<GlobalWorkforceMetric> findByTenantIdAndMetricName(String tenantId, String metricName);

    /**
     * Find latest metrics for a tenant
     */
    List<GlobalWorkforceMetric> findLatestByTenantId(String tenantId, int limit);

    /**
     * Find metrics by tenant and aggregation level
     */
    List<GlobalWorkforceMetric> findByTenantIdAndAggregationLevel(String tenantId,
                                                                   com.gogidix.hr.globalhrdashboard.domain.model.AggregationLevel aggregationLevel);

    /**
     * Find metrics needing aggregation
     */
    List<GlobalWorkforceMetric> findMetricsNeedingAggregation(String tenantId, Instant before);

    /**
     * Delete metric by ID and tenant
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Delete all metrics for a tenant and period
     */
    void deleteByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Check if metric exists for tenant
     */
    boolean existsByIdAndTenantId(String id, String tenantId);

    /**
     * Batch save metrics
     */
    List<GlobalWorkforceMetric> saveAll(List<GlobalWorkforceMetric> metrics);

    /**
     * Count metrics by tenant and category
     */
    long countByTenantIdAndMetricCategory(String tenantId, MetricCategory category);

    /**
     * Find metrics updated since a specific time
     */
    List<GlobalWorkforceMetric> findByTenantIdAndUpdatedAtAfter(String tenantId, Instant updatedAt);
}
