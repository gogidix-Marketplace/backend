package com.gogidix.dashboard.gateway.chart.domain.repository;

import com.gogidix.dashboard.gateway.chart.domain.model.TimeSeriesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for TimeSeriesData entities.
 */
@Repository
public interface TimeSeriesDataRepository extends JpaRepository<TimeSeriesData, Long> {

    /**
     * Find time series data by metric name, tenant, and time range
     */
    List<TimeSeriesData> findByMetricNameAndTenantIdAndTimestampBetweenOrderByTimestampAsc(
            String metricName, String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Find latest data points for a metric
     */
    @Query("SELECT t FROM TimeSeriesData t WHERE t.metricName = :metricName AND t.tenantId = :tenantId " +
           "AND t.timestamp >= :startTime ORDER BY t.timestamp ASC")
    List<TimeSeriesData> findLatestDataPoints(@Param("metricName") String metricName,
                                              @Param("tenantId") String tenantId,
                                              @Param("startTime") LocalDateTime startTime);

    /**
     * Find all metrics for tenant
     */
    @Query("SELECT DISTINCT t.metricName FROM TimeSeriesData t WHERE t.tenantId = :tenantId")
    List<String> findDistinctMetricNamesByTenantId(@Param("tenantId") String tenantId);

    /**
     * Aggregate data by time window
     */
    @Query("SELECT AVG(t.value) FROM TimeSeriesData t WHERE t.metricName = :metricName " +
           "AND t.tenantId = :tenantId AND t.timestamp >= :startTime AND t.timestamp <= :endTime")
    Double getAverageValue(@Param("metricName") String metricName,
                          @Param("tenantId") String tenantId,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);
}
