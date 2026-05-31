package com.gogidix.management.executive.analytics.domain.repository;

import com.gogidix.management.executive.analytics.domain.model.AnalyticsData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AnalyticsData entities
 */
@Repository
public interface AnalyticsDataRepository extends MongoRepository<AnalyticsData, String> {

    /**
     * Find analytics data by tenant ID
     */
    List<AnalyticsData> findByTenantId(String tenantId);

    /**
     * Find analytics data by tenant ID and not deleted
     */
    List<AnalyticsData> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find analytics data by tenant ID and metric type
     */
    List<AnalyticsData> findByTenantIdAndMetricType(String tenantId, String metricType);

    /**
     * Find analytics data by tenant ID and date range
     */
    List<AnalyticsData> findByTenantIdAndTimestampBetween(String tenantId, Instant start, Instant end);

    /**
     * Find latest analytics data for a tenant
     */
    Optional<AnalyticsData> findFirstByTenantIdOrderByTimestampDesc(String tenantId);

    /**
     * Delete analytics data by tenant ID (soft delete)
     */
    void deleteByTenantId(String tenantId);
}
