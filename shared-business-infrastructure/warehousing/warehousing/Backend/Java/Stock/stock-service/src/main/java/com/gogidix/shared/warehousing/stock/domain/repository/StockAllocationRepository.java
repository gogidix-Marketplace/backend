package com.gogidix.shared.warehousing.stock.domain.repository;

import com.gogidix.shared.warehousing.stock.domain.entity.StockAllocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockAllocationRepository extends MongoRepository<StockAllocation, String> {

    Optional<StockAllocation> findByOrderIdAndOrderLineId(String orderId, String orderLineId);

    List<StockAllocation> findByOrderId(String orderId);

    List<StockAllocation> findBySku(String sku);

    List<StockAllocation> findByStatus(String status);

    List<StockAllocation> findByTenantIdAndSkuAndWarehouseIdAndQuantityGreaterThanOrderByExpirationDateAsc(
        String tenantId, String sku, String warehouseId, Integer quantity);
}
