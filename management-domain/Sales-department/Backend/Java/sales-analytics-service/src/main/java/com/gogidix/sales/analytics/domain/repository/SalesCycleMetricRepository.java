package com.gogidix.sales.analytics.domain.repository;

import com.gogidix.sales.analytics.domain.model.SalesCycleMetric;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Sales Cycle Metric Repository Interface (Port)
 * Defines the contract for sales cycle metric persistence operations
 */
public interface SalesCycleMetricRepository {

    SalesCycleMetric save(SalesCycleMetric metric);

    List<SalesCycleMetric> saveAll(List<SalesCycleMetric> metrics);

    Optional<SalesCycleMetric> findById(String id);

    Optional<SalesCycleMetric> findBySalesCycleMetricIdAndTenantId(String metricId, String tenantId);

    List<SalesCycleMetric> findByTenantId(String tenantId);

    List<SalesCycleMetric> findByTenantIdAndEntityType(String tenantId, String entityType);

    List<SalesCycleMetric> findByTenantIdAndEntityTypeAndEntityId(
            String tenantId, String entityType, String entityId);

    List<SalesCycleMetric> findByTenantIdAndPeriodBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<SalesCycleMetric> findByTenantIdAndPeriod(String tenantId, SalesCycleMetric.MetricPeriod period);

    List<SalesCycleMetric> findByTenantIdAndHealthScore(
            String tenantId, SalesCycleMetric.CycleHealthScore healthScore);

    Optional<SalesCycleMetric> findLatestByTenantIdAndEntity(
            String tenantId, String entityType, String entityId);

    List<SalesCycleMetric> findSlowestCyclesByTenantId(String tenantId, int limit);

    List<SalesCycleMetric> findFastestCyclesByTenantId(String tenantId, int limit);

    boolean existsBySalesCycleMetricIdAndTenantId(String metricId, String tenantId);

    void deleteById(String id);

    void deleteBySalesCycleMetricIdAndTenantId(String metricId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByTenantIdAndPeriod(String tenantId, SalesCycleMetric.MetricPeriod period);

    long countByTenantId(String tenantId);

    long countByTenantIdAndHealthScore(String tenantId, SalesCycleMetric.CycleHealthScore healthScore);
}
