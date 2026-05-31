package com.gogidix.platform.metering.domain.repository;

import com.gogidix.platform.metering.domain.model.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for UsageRecord entity.
 */
@Repository
public interface UsageRecordRepository extends JpaRepository<UsageRecord, String> {

    /**
     * Find usage records by tenant ID
     */
    List<UsageRecord> findByTenantId(String tenantId);

    /**
     * Find usage records by tenant and time range
     */
    @Query("SELECT ur FROM UsageRecord ur WHERE ur.tenantId = :tenantId " +
           "AND ur.eventTime BETWEEN :startTime AND :endTime")
    List<UsageRecord> findByTenantIdAndTimeRange(
        @Param("tenantId") String tenantId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    /**
     * Find usage records by metric name and time range
     */
    @Query("SELECT ur FROM UsageRecord ur WHERE ur.metricName = :metricName " +
           "AND ur.eventTime BETWEEN :startTime AND :endTime")
    List<UsageRecord> findByMetricNameAndTimeRange(
        @Param("metricName") String metricName,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    /**
     * Find aggregatable records (not too old)
     */
    @Query("SELECT ur FROM UsageRecord ur WHERE ur.eventTime > :cutoff")
    List<UsageRecord> findAggregatableRecords(@Param("cutoff") LocalDateTime cutoff);

    /**
     * Delete old records
     */
    void deleteByEventTimeBefore(LocalDateTime cutoff);
}
