package com.gogidix.shared.warehousing.ecommerce.domain.repository;

import com.gogidix.shared.warehousing.ecommerce.domain.entity.WarehouseStockLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseStockLevelRepository extends MongoRepository<WarehouseStockLevel, String> {
    Optional<WarehouseStockLevel> findBySkuAndWarehouseId(String sku, String warehouseId);
    List<WarehouseStockLevel> findBySku(String sku);
    List<WarehouseStockLevel> findByWarehouseId(String warehouseId);
    List<WarehouseStockLevel> findByZoneId(String zoneId);
    List<WarehouseStockLevel> findByVendorId(String vendorId);
    List<WarehouseStockLevel> findBySkuAndZoneId(String sku, String zoneId);
}
