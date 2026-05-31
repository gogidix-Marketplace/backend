package com.gogidix.sales.analytics.domain.repository;

import com.gogidix.sales.analytics.domain.model.Metric;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Metric Repository Interface (Port)
 * Defines the contract for metric persistence operations
 */
public interface MetricRepository {

    Metric save(Metric metric);

    List<Metric> saveAll(List<Metric> metrics);

    Optional<Metric> findById(String id);

    Optional<Metric> findByMetricIdAndTenantId(String metricId, String tenantId);

    List<Metric> findByTenantId(String tenantId);

    List<Metric> findByTenantIdAndEntityType(String tenantId, String entityType);

    List<Metric> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId);

    List<Metric> findByTenantIdAndMetricType(String tenantId, Metric.MetricType metricType);

    List<Metric> findByTenantIdAndPeriod(String tenantId, String period);

    List<Metric> findByTenantIdAndDateRange(String tenantId, Instant startDate, Instant endDate);

    List<Metric> findLatestMetricsByTenantIdAndEntity(String tenantId, String entityType, String entityId, int limit);

    List<Metric> findMetricsByTenantIdAndMetricTypes(String tenantId, List<Metric.MetricType> metricTypes);

    boolean existsByMetricIdAndTenantId(String metricId, String tenantId);

    void deleteById(String id);

    void deleteByMetricIdAndTenantId(String metricId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByTenantIdAndPeriod(String tenantId, String period);

    long countByTenantId(String tenantId);

    long countByTenantIdAndMetricType(String tenantId, Metric.MetricType metricType);
}
