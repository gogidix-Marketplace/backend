package com.gogidix.analytics.metrics.domain.repository;

import com.gogidix.analytics.metrics.domain.model.MetricAggregation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MetricAggregation aggregate.
 */
@Repository
public interface MetricAggregationRepository extends JpaRepository<MetricAggregation, Long> {

    List<MetricAggregation> findByTenantIdAndMetricNameAndAggregationType(
        String tenantId, String metricName, MetricAggregation.AggregationType aggregationType);

    List<MetricAggregation> findByTenantIdAndWindowStartBetween(
        String tenantId, LocalDateTime windowStart, LocalDateTime windowEnd);

    @Query("SELECT m FROM MetricAggregation m WHERE m.tenantId = :tenantId " +
           "AND m.metricName = :metricName AND m.aggregationType = :aggregationType " +
           "AND m.windowStart >= :startTime AND m.windowEnd <= :endTime " +
           "ORDER BY m.windowStart ASC")
    List<MetricAggregation> findAggregatesForTimeRange(
        @Param("tenantId") String tenantId,
        @Param("metricName") String metricName,
        @Param("aggregationType") MetricAggregation.AggregationType aggregationType,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime);

    @Query("SELECT m FROM MetricAggregation m WHERE m.tenantId = :tenantId " +
           "AND m.metricName = :metricName " +
           "AND m.windowStart <= :timestamp AND m.windowEnd > :timestamp")
    Optional<MetricAggregation> findAggregateAtTimestamp(
        @Param("tenantId") String tenantId,
        @Param("metricName") String metricName,
        @Param("timestamp") LocalDateTime timestamp);

    void deleteByWindowEndBefore(LocalDateTime cutoffDate);
}
