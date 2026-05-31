package com.gogidix.shared.warehousing.inventory.stock.application.service;

import com.gogidix.shared.warehousing.inventory.stock.domain.entity.StockItem;
import com.gogidix.shared.warehousing.inventory.stock.domain.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application Service for Stock Inventory Management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockInventoryService {

    private final StockItemRepository stockItemRepository;

    public StockItem createStockItem(StockItem stockItem) {
        log.info("Creating stock item: {}", stockItem.getSku());

        stockItem.setId(UUID.randomUUID().toString());
        stockItem.setQuantityAllocated(0);
        stockItem.setReorderNeeded(false);
        stockItem.setStatus(StockItem.StockStatus.IN_STOCK);
        stockItem.setCreatedAt(LocalDateTime.now());
        stockItem.setUpdatedAt(LocalDateTime.now());

        if (stockItem.getQuantityOnHand() != null) {
            stockItem.setQuantityAvailable(stockItem.getQuantityOnHand());
            stockItem.setReorderNeeded(stockItem.needsReorder());
        }

        return stockItemRepository.save(stockItem);
    }

    public StockItem getBySku(String tenantId, String warehouseId, String sku) {
        return stockItemRepository.findByTenantIdAndWarehouseIdAndSku(tenantId, warehouseId, sku)
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found: " + sku));
    }

    public List<StockItem> getByWarehouse(String tenantId, String warehouseId) {
        return stockItemRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId);
    }

    public List<StockItem> getLowStockItems(String tenantId, String warehouseId) {
        return stockItemRepository.findLowStockItems(tenantId, warehouseId);
    }

    public List<StockItem> getOutOfStockItems(String tenantId, String warehouseId) {
        return stockItemRepository.findOutOfStockItems(tenantId, warehouseId);
    }

    public List<StockItem> getItemsNeedingReorder(String tenantId, String warehouseId) {
        return stockItemRepository.findItemsNeedingReorder(tenantId, warehouseId);
    }

    public List<StockItem> searchByName(String tenantId, String warehouseId, String searchTerm) {
        return stockItemRepository.searchByName(tenantId, warehouseId, searchTerm);
    }

    public StockItem receiveStock(String stockItemId, Integer quantity) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found: " + stockItemId));

        stockItem.updateQuantity(quantity, "RECEIPT");
        stockItem.setLastReceivedDate(LocalDateTime.now());
        return stockItemRepository.save(stockItem);
    }

    public StockItem issueStock(String stockItemId, Integer quantity) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found: " + stockItemId));

        if (stockItem.getAvailableQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock");
        }

        stockItem.updateQuantity(-quantity, "ISSUANCE");
        stockItem.setLastIssuedDate(LocalDateTime.now());
        return stockItemRepository.save(stockItem);
    }

    public StockItem allocateStock(String stockItemId, Integer quantity) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found: " + stockItemId));

        stockItem.allocate(quantity);
        return stockItemRepository.save(stockItem);
    }

    public StockItem deallocateStock(String stockItemId, Integer quantity) {
        StockItem stockItem = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found: " + stockItemId));

        stockItem.deallocate(quantity);
        return stockItemRepository.save(stockItem);
    }

    public List<StockItem> updateReorderFlags(String tenantId, String warehouseId) {
        List<StockItem> items = stockItemRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId);
        for (StockItem item : items) {
            boolean needsReorder = item.needsReorder();
            if (item.getReorderNeeded() == null || item.getReorderNeeded() != needsReorder) {
                item.setReorderNeeded(needsReorder);
                stockItemRepository.save(item);
            }
        }
        return items;
    }
}
