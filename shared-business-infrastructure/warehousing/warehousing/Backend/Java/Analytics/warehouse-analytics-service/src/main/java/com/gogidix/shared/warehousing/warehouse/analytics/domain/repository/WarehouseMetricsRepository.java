package com.gogidix.shared.warehousing.warehouse.analytics.domain.repository;

import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.WarehouseMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Warehouse Metrics Repository with Multi-Tenant Support
 */
@Repository
public interface WarehouseMetricsRepository extends MongoRepository<WarehouseMetrics, String> {

    /**
     * Find metrics by tenant and warehouse
     */
    List<WarehouseMetrics> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    /**
     * Find metrics by tenant, warehouse and date range
     */
    List<WarehouseMetrics> findByTenantIdAndWarehouseIdAndMetricDateBetween(
        String tenantId, String warehouseId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find latest metrics for a warehouse
     */
    Optional<WarehouseMetrics> findFirstByTenantIdAndWarehouseIdOrderByMetricDateDesc(
        String tenantId, String warehouseId);

    /**
     * Find all metrics for a tenant
     */
    List<WarehouseMetrics> findByTenantId(String tenantId);

    /**
     * Find metrics by tenant and date
     */
    List<WarehouseMetrics> findByTenantIdAndMetricDate(String tenantId, LocalDateTime metricDate);

    /**
     * Find metrics with utilization above threshold
     */
    @Query("{'tenantId': ?0, 'utilizationPercentage': {$gt: ?1}}")
    List<WarehouseMetrics> findByTenantIdAndUtilizationGreaterThan(String tenantId, Double threshold);

    /**
     * Find metrics with utilization below threshold
     */
    @Query("{'tenantId': ?0, 'utilizationPercentage': {$lt: ?1}}")
    List<WarehouseMetrics> findByTenantIdAndUtilizationLessThan(String tenantId, Double threshold);
}
