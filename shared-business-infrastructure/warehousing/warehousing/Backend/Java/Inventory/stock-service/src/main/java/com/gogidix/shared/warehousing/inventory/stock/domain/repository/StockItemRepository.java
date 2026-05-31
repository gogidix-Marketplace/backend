package com.gogidix.shared.warehousing.inventory.stock.domain.repository;

import com.gogidix.shared.warehousing.inventory.stock.domain.entity.StockItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for StockItem entity
 */
@Repository
public interface StockItemRepository extends MongoRepository<StockItem, String> {

    List<StockItem> findByTenantId(String tenantId);

    List<StockItem> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    Optional<StockItem> findByTenantIdAndWarehouseIdAndSku(String tenantId, String warehouseId, String sku);

    List<StockItem> findByTenantIdAndSku(String tenantId, String sku);

    List<StockItem> findByTenantIdAndStatus(String tenantId, StockItem.StockStatus status);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'reorderNeeded': true }")
    List<StockItem> findItemsNeedingReorder(String tenantId, String warehouseId);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'status': 'LOW_STOCK' }")
    List<StockItem> findLowStockItems(String tenantId, String warehouseId);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'status': 'OUT_OF_STOCK' }")
    List<StockItem> findOutOfStockItems(String tenantId, String warehouseId);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'category': ?2 }")
    List<StockItem> findByCategory(String tenantId, String warehouseId, String category);

    List<StockItem> findByTenantIdAndSupplierId(String tenantId, String supplierId);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'productName': { $regex: ?2, $options: 'i' } }")
    List<StockItem> searchByName(String tenantId, String warehouseId, String searchTerm);

    @Query("{ 'tenantId': ?0, 'lastReceivedDate': { $gte: ?1 } }")
    List<StockItem> findRecentlyReceived(String tenantId, LocalDateTime since);

    boolean existsByTenantIdAndWarehouseIdAndSku(String tenantId, String warehouseId, String sku);
}
