package com.gogidix.hr.globalhrdashboard.domain.repository;

import com.gogidix.hr.globalhrdashboard.domain.model.RetentionMetric;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface for RetentionMetric aggregate
 * Following Hexagonal Architecture - this is a PORT (out)
 */
public interface RetentionMetricRepository {

    /**
     * Save a retention metric
     */
    RetentionMetric save(RetentionMetric metric);

    /**
     * Find metric by ID and tenant
     */
    Optional<RetentionMetric> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all metrics for a tenant
     */
    List<RetentionMetric> findByTenantId(String tenantId);

    /**
     * Find metrics by tenant and country code
     */
    List<RetentionMetric> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    /**
     * Find metric by tenant, country code and period
     */
    Optional<RetentionMetric> findByTenantIdAndCountryCodeAndPeriod(String tenantId, String countryCode, String period);

    /**
     * Find metrics by tenant and period
     */
    List<RetentionMetric> findByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Find metrics by tenant and region
     */
    List<RetentionMetric> findByTenantIdAndRegionCode(String tenantId, String regionCode);

    /**
     * Find active metrics for a tenant
     */
    List<RetentionMetric> findActiveByTenantId(String tenantId);

    /**
     * Find metrics updated since a specific time
     */
    List<RetentionMetric> findByTenantIdAndLastUpdatedAfter(String tenantId, Instant lastUpdated);

    /**
     * Find metrics with high retention rate
     */
    List<RetentionMetric> findWithHighRetentionRate(String tenantId, Double minRate);

    /**
     * Find metrics with high turnover rate
     */
    List<RetentionMetric> findWithHighTurnoverRate(String tenantId, Double maxRate);

    /**
     * Find metrics with low retention rate
     */
    List<RetentionMetric> findWithLowRetentionRate(String tenantId, Double maxRate);

    /**
     * Get average retention rate for a tenant and period
     */
    Double getAverageRetentionRate(String tenantId, String period);

    /**
     * Get average turnover rate for a tenant and period
     */
    Double getAverageTurnoverRate(String tenantId, String period);

    /**
     * Find metrics needing update
     */
    List<RetentionMetric> findMetricsNeedingUpdate(String tenantId, Instant before);

    /**
     * Find retention trend over time for a country
     */
    List<RetentionMetric> findTrendDataByCountry(String tenantId, String countryCode,
                                                  String startPeriod, String endPeriod);

    /**
     * Find retention trend over time for a region
     */
    List<RetentionMetric> findTrendDataByRegion(String tenantId, String regionCode,
                                                 String startPeriod, String endPeriod);

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
    List<RetentionMetric> saveAll(List<RetentionMetric> metrics);

    /**
     * Count metrics by tenant and country
     */
    long countByTenantIdAndCountryCode(String tenantId, String countryCode);

    /**
     * Find latest retention metric for each country for a given period
     */
    List<RetentionMetric> findLatestByPeriod(String tenantId, String period);

    /**
     * Find global retention aggregate for a tenant and period
     */
    Optional<RetentionMetric> findGlobalAggregateByTenantIdAndPeriod(String tenantId, String period);

    /**
     * Get countries with highest retention rate for a tenant and period
     */
    List<RetentionMetric> findTopCountriesByRetentionRate(String tenantId, String period, int limit);

    /**
     * Get countries with lowest retention rate for a tenant and period
     */
    List<RetentionMetric> findBottomCountriesByRetentionRate(String tenantId, String period, int limit);

    /**
     * Find metrics by tenant, region and period
     */
    List<RetentionMetric> findByTenantIdAndRegionCodeAndPeriod(String tenantId, String regionCode, String period);

    /**
     * Calculate global retention rate for tenant and period
     */
    Double calculateGlobalRetentionRate(String tenantId, String period);

    /**
     * Calculate global turnover rate for tenant and period
     */
    Double calculateGlobalTurnoverRate(String tenantId, String period);

    /**
     * Calculate global average tenure for tenant and period
     */
    Double calculateGlobalAverageTenure(String tenantId, String period);
}
