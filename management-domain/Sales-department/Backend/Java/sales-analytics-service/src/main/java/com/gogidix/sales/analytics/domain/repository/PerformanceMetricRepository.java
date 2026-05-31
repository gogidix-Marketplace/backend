package com.gogidix.sales.analytics.domain.repository;

import com.gogidix.sales.analytics.domain.model.PerformanceMetric;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Performance Metric Repository Interface (Port)
 * Defines the contract for performance metric persistence operations
 */
public interface PerformanceMetricRepository {

    PerformanceMetric save(PerformanceMetric metric);

    List<PerformanceMetric> saveAll(List<PerformanceMetric> metrics);

    Optional<PerformanceMetric> findById(String id);

    Optional<PerformanceMetric> findByPerformanceMetricIdAndTenantId(String metricId, String tenantId);

    List<PerformanceMetric> findByTenantId(String tenantId);

    List<PerformanceMetric> findByTenantIdAndEntityType(String tenantId, String entityType);

    List<PerformanceMetric> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId);

    List<PerformanceMetric> findByTenantIdAndEntityTypeAndPeriodBetween(
            String tenantId, String entityType, LocalDate startDate, LocalDate endDate);

    List<PerformanceMetric> findByTenantIdAndPeriod(String tenantId, PerformanceMetric.PerformancePeriod period);

    List<PerformanceMetric> findTopPerformersByTenantIdAndEntityType(
            String tenantId, String entityType, int limit);

    List<PerformanceMetric> findByTenantIdAndManagerId(String tenantId, String managerId);

    List<PerformanceMetric> findByTenantIdAndRegionId(String tenantId, String regionId);

    Optional<PerformanceMetric> findLatestByTenantIdAndEntity(
            String tenantId, String entityType, String entityId);

    List<PerformanceMetric> findRankedMetricsByTenantIdAndEntityType(
            String tenantId, String entityType);

    boolean existsByPerformanceMetricIdAndTenantId(String metricId, String tenantId);

    void deleteById(String id);

    void deleteByPerformanceMetricIdAndTenantId(String metricId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByTenantIdAndPeriod(String tenantId, PerformanceMetric.PerformancePeriod period);

    long countByTenantId(String tenantId);

    long countByTenantIdAndEntityType(String tenantId, String entityType);
}
