package com.gogidix.sales.analytics.domain.repository;

import com.gogidix.sales.analytics.domain.model.WinLossMetric;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Win/Loss Metric Repository Interface (Port)
 * Defines the contract for win/loss metric persistence operations
 */
public interface WinLossMetricRepository {

    WinLossMetric save(WinLossMetric metric);

    List<WinLossMetric> saveAll(List<WinLossMetric> metrics);

    Optional<WinLossMetric> findById(String id);

    Optional<WinLossMetric> findByWinLossMetricIdAndTenantId(String metricId, String tenantId);

    List<WinLossMetric> findByTenantId(String tenantId);

    List<WinLossMetric> findByTenantIdAndEntityType(String tenantId, String entityType);

    List<WinLossMetric> findByTenantIdAndEntityTypeAndEntityId(
            String tenantId, String entityType, String entityId);

    List<WinLossMetric> findByTenantIdAndPeriodBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<WinLossMetric> findByTenantIdAndPeriod(String tenantId, WinLossMetric.MetricPeriod period);

    List<WinLossMetric> findByTenantIdOrderByWinRateDesc(String tenantId);

    List<WinLossMetric> findByTenantIdAndWinRateLessThan(String tenantId, BigDecimal winRate);

    List<WinLossMetric> findByTenantIdAndWinRateGreaterThanEqual(String tenantId, BigDecimal winRate);

    Optional<WinLossMetric> findLatestByTenantIdAndEntity(
            String tenantId, String entityType, String entityId);

    List<WinLossMetric> findLowestPerformersByTenantIdAndEntityType(
            String tenantId, String entityType, int limit);

    List<WinLossMetric> findHighestPerformersByTenantIdAndEntityType(
            String tenantId, String entityType, int limit);

    boolean existsByWinLossMetricIdAndTenantId(String metricId, String tenantId);

    void deleteById(String id);

    void deleteByWinLossMetricIdAndTenantId(String metricId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByTenantIdAndPeriod(String tenantId, WinLossMetric.MetricPeriod period);

    long countByTenantId(String tenantId);

    long countByTenantIdAndEntityType(String tenantId, String entityType);
}
