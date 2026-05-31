package com.gogidix.shared.warehousing.inventory_analytics.domain.repository;

import com.gogidix.shared.warehousing.inventory_analytics.domain.entity.InventoryMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryMetricsRepository extends MongoRepository<InventoryMetrics, String> {

    List<InventoryMetrics> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<InventoryMetrics> findByTenantIdAndWarehouseIdAndMetricDateBetween(
            String tenantId, String warehouseId, LocalDateTime startDate, LocalDateTime endDate);

    List<InventoryMetrics> findFirstByTenantIdAndWarehouseIdOrderByMetricDateDesc(
            String tenantId, String warehouseId);

    List<InventoryMetrics> findByTenantId(String tenantId);
}
