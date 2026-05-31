package com.gogidix.platform.metering.domain.repository;

import com.gogidix.platform.metering.domain.model.UsageAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for UsageAggregate entity.
 */
@Repository
public interface UsageAggregateRepository extends JpaRepository<UsageAggregate, String> {

    /**
     * Find aggregates by tenant and aggregation type
     */
    List<UsageAggregate> findByTenantIdAndAggregationType(String tenantId, UsageAggregate.AggregationType aggregationType);

    /**
     * Find aggregates by tenant, metric, and time range
     */
    @Query("SELECT ua FROM UsageAggregate ua WHERE ua.tenantId = :tenantId " +
           "AND ua.metricName = :metricName " +
           "AND ua.aggregationType = :aggregationType " +
           "AND ua.periodStart BETWEEN :startTime AND :endTime")
    List<UsageAggregate> findByTenantAndMetricAndTimeRange(
        @Param("tenantId") String tenantId,
        @Param("metricName") String metricName,
        @Param("aggregationType") UsageAggregate.AggregationType aggregationType,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    /**
     * Find aggregate by tenant, metric, type, and period
     */
    Optional<UsageAggregate> findByTenantIdAndMetricNameAndAggregationTypeAndPeriodStartAndPeriodEnd(
        String tenantId,
        String metricName,
        UsageAggregate.AggregationType aggregationType,
        LocalDateTime periodStart,
        LocalDateTime periodEnd
    );

    /**
     * Find aggregates where quota is exceeded
     */
    @Query("SELECT ua FROM UsageAggregate ua WHERE ua.tenantId = :tenantId " +
           "AND ua.quotaLimit IS NOT NULL AND ua.totalQuantity > ua.quotaLimit")
    List<UsageAggregate> findQuotaExceededAggregates(@Param("tenantId") String tenantId);
}
