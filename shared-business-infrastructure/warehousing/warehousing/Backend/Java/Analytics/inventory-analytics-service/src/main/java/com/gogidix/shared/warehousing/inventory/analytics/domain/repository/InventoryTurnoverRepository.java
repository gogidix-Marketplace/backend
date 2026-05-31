package com.gogidix.shared.warehousing.inventory.analytics.domain.repository;

import com.gogidix.shared.warehousing.inventory.analytics.domain.entity.InventoryTurnover;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryTurnoverRepository extends MongoRepository<InventoryTurnover, String> {

    List<InventoryTurnover> findByTenantIdAndSku(String tenantId, String sku);

    List<InventoryTurnover> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<InventoryTurnover> findByTenantIdAndWarehouseIdAndPeriodStartBetween(
        String tenantId, String warehouseId, LocalDateTime startDate, LocalDateTime endDate);

    Optional<InventoryTurnover> findFirstByTenantIdAndSkuOrderByPeriodEndDesc(String tenantId, String sku);

    List<InventoryTurnover> findByTenantIdAndCategory(String tenantId, InventoryTurnover.TurnoverCategory category);

    List<InventoryTurnover> findByTenantId(String tenantId);
}
