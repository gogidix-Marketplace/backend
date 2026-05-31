package com.gogidix.shared.warehousing.warehouse.analytics.domain.repository;

import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.PerformanceData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Performance Data Repository with Multi-Tenant Support
 */
@Repository
public interface PerformanceDataRepository extends MongoRepository<PerformanceData, String> {

    /**
     * Find performance data by tenant and warehouse
     */
    List<PerformanceData> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find performance data by tenant, warehouse and date range
     */
    List<PerformanceData> findByTenantIdAndWarehouseIdAndTimestampBetween(
        String tenantId, String warehouseId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find latest performance data for a warehouse
     */
    Optional<PerformanceData> findFirstByTenantIdAndWarehouseIdOrderByTimestampDesc(
        String tenantId, String warehouseId);

    /**
     * Find all performance data for a tenant
     */
    List<PerformanceData> findByTenantId(String tenantId);

    /**
     * Find performance data by grade
     */
    List<PerformanceData> findByTenantIdAndGrade(String tenantId, PerformanceData.PerformanceGrade grade);

    /**
     * Find performance data with score above threshold
     */
    @Query("{'tenantId': ?0, 'overallScore': {$gte: ?1}}")
    List<PerformanceData> findByTenantIdAndOverallScoreGreaterThanEqual(String tenantId, Double score);

    /**
     * Find performance data with score below threshold
     */
    @Query("{'tenantId': ?0, 'overallScore': {$lt: ?1}}")
    List<PerformanceData> findByTenantIdAndOverallScoreLessThan(String tenantId, Double score);
}
