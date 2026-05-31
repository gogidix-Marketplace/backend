package com.gogidix.hr.globalhrdashboard.domain.repository;

import com.gogidix.hr.globalhrdashboard.domain.model.RegionalMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface for RegionalMetric aggregate
 * Following Hexagonal Architecture - this is a PORT (out)
 */
public interface RegionalMetricRepository {

    /**
     * Save a regional metric
     */
    RegionalMetric save(RegionalMetric metric);

    /**
     * Find metric by ID and tenant
     */
    Optional<RegionalMetric> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all metrics for a tenant
     */
    List<RegionalMetric> findByTenantId(String tenantId);

    /**
     * Find metrics by tenant and region code
     */
    List<RegionalMetric> findByTenantIdAndRegionCode(String tenantId, String regionCode);

    /**
     * Find metric by tenant, region code, metric name and period
     */
    Optional<RegionalMetric> findByTenantIdAndRegionCodeAndMetricNameAndPeriod(
            String tenantId, String regionCode, String metricName, String period);

    /**
     * Find metrics by tenant and period
     */
    List<RegionalMetric> findByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Find metrics by tenant, region and period
     */
    List<RegionalMetric> findByTenantIdAndRegionCodeAndPeriod(String tenantId, String regionCode, String period);

    /**
     * Find metrics by tenant and metric category
     */
    List<RegionalMetric> findByTenantIdAndMetricCategory(String tenantId, MetricCategory metricCategory);

    /**
     * Find active metrics for a tenant
     */
    List<RegionalMetric> findActiveByTenantId(String tenantId);

    /**
     * Find all regions for a tenant
     */
    List<String> findDistinctRegionsByTenantId(String tenantId);

    /**
     * Find metrics by tenant, region and metric name
     */
    List<RegionalMetric> findByTenantIdAndRegionCodeAndMetricName(String tenantId, String regionCode, String metricName);

    /**
     * Find metrics updated since a specific time
     */
    List<RegionalMetric> findByTenantIdAndLastUpdatedAfter(String tenantId, Instant lastUpdated);

    /**
     * Find metrics needing update
     */
    List<RegionalMetric> findMetricsNeedingUpdate(String tenantId, Instant before);

    /**
     * Delete metric by ID and tenant
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Delete all metrics for a tenant, region and period
     */
    void deleteByTenantIdAndRegionCodeAndPeriod(String tenantId, String regionCode, String period);

    /**
     * Check if metric exists for tenant
     */
    boolean existsByIdAndTenantId(String id, String tenantId);

    /**
     * Batch save metrics
     */
    List<RegionalMetric> saveAll(List<RegionalMetric> metrics);

    /**
     * Count metrics by tenant and region
     */
    long countByTenantIdAndRegionCode(String tenantId, String regionCode);

    /**
     * Get trend data for a region and metric over time
     */
    List<RegionalMetric> findTrendData(String tenantId, String regionCode, String metricName,
                                        String startPeriod, String endPeriod);

    /**
     * Find latest metric for each region for a given metric name and period
     */
    List<RegionalMetric> findLatestByMetricNameAndPeriod(String tenantId, String metricName, String period);
}
