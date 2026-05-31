package com.gogidix.finance.globalfinancedashboard.domain.repository;

import com.gogidix.finance.globalfinancedashboard.domain.model.FinancialMetric;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface for Financial Aggregate
 * Following Hexagonal Architecture - this is a PORT (out)
 */
public interface FinancialMetricRepository {

    /**
     * Save a financial metric
     */
    FinancialMetric save(FinancialMetric metric);

    /**
     * Find metric by ID and tenant
     */
    Optional<FinancialMetric> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find all metrics for a tenant
     */
    List<FinancialMetric> findByTenantId(String tenantId);

    /**
     * Find metrics by tenant and type
     */
    List<FinancialMetric> findByTenantIdAndMetricType(String tenantId, FinancialMetric.MetricType metricType);

    /**
     * Find metrics by tenant and period range
     */
    List<FinancialMetric> findByTenantIdAndPeriodBetween(String tenantId, String startPeriod, String endPeriod);

    /**
     * Find metrics by tenant, region, and period
     */
    List<FinancialMetric> findByTenantIdAndRegionAndPeriod(String tenantId, String region, String period);

    /**
     * Delete metric by ID and tenant
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Check if metric exists for tenant
     */
    boolean existsByIdAndTenantId(String id, String tenantId);
}
