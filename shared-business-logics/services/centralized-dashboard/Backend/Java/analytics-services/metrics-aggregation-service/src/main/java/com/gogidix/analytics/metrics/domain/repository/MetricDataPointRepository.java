package com.gogidix.analytics.metrics.domain.repository;

import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MetricDataPoint aggregate.
 */
@Repository
public interface MetricDataPointRepository extends JpaRepository<MetricDataPoint, Long> {

    List<MetricDataPoint> findByTenantIdAndTimestampBetween(
        String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    List<MetricDataPoint> findByTenantIdAndMetricNameAndTimestampBetween(
        String tenantId, String metricName, LocalDateTime startTime, LocalDateTime endTime);

    List<MetricDataPoint> findByTenantIdAndSourceServiceAndTimestampBetween(
        String tenantId, String sourceService, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT m FROM MetricDataPoint m WHERE m.tenantId = :tenantId " +
           "AND m.metricName = :metricName AND m.timestamp >= :startTime " +
           "ORDER BY m.timestamp DESC")
    List<MetricDataPoint> findLatestByTenantAndMetric(
        @Param("tenantId") String tenantId,
        @Param("metricName") String metricName,
        @Param("startTime") LocalDateTime startTime);

    @Query("SELECT m FROM MetricDataPoint m WHERE m.tenantId = :tenantId " +
           "AND m.timestamp >= :startTime AND m.timestamp < :endTime " +
           "ORDER BY m.timestamp ASC")
    List<MetricDataPoint> findForAggregation(
        @Param("tenantId") String tenantId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(m) FROM MetricDataPoint m WHERE m.tenantId = :tenantId " +
           "AND m.metricName = :metricName AND m.timestamp >= :startTime")
    Long countByTenantAndMetricSince(
        @Param("tenantId") String tenantId,
        @Param("metricName") String metricName,
        @Param("startTime") LocalDateTime startTime);

    Optional<MetricDataPoint> findFirstByTenantIdAndMetricNameOrderByTimestampDesc(
        String tenantId, String metricName);

    void deleteByTimestampBefore(LocalDateTime cutoffDate);
}
