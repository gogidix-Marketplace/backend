package com.gogidix.shared.warehousing.inventory.stock.interfaces.rest;

import com.gogidix.shared.warehousing.inventory.stock.application.service.StockInventoryService;
import com.gogidix.shared.warehousing.inventory.stock.domain.entity.StockItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Stock Inventory Management
 */
@RestController
@RequestMapping("/api/v1/inventory/stock")
@RequiredArgsConstructor
public class StockInventoryController {

    private final StockInventoryService stockInventoryService;

    @PostMapping
    public ResponseEntity<StockItem> createStockItem(@Valid @RequestBody StockItem stockItem) {
        StockItem created = stockInventoryService.createStockItem(stockItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{sku}")
    public ResponseEntity<StockItem> getBySku(
            @PathVariable String sku,
            @RequestParam String tenantId,
            @RequestParam String warehouseId) {
        StockItem stockItem = stockInventoryService.getBySku(tenantId, warehouseId, sku);
        return ResponseEntity.ok(stockItem);
    }

    @GetMapping
    public ResponseEntity<List<StockItem>> getByWarehouse(
            @RequestParam String tenantId,
            @RequestParam String warehouseId) {
        List<StockItem> items = stockInventoryService.getByWarehouse(tenantId, warehouseId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<StockItem>> getLowStockItems(
            @RequestParam String tenantId,
            @RequestParam String warehouseId) {
        List<StockItem> items = stockInventoryService.getLowStockItems(tenantId, warehouseId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<StockItem>> getOutOfStockItems(
            @RequestParam String tenantId,
            @RequestParam String warehouseId) {
        List<StockItem> items = stockInventoryService.getOutOfStockItems(tenantId, warehouseId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/reorder-needed")
    public ResponseEntity<List<StockItem>> getItemsNeedingReorder(
            @RequestParam String tenantId,
            @RequestParam String warehouseId) {
        List<StockItem> items = stockInventoryService.getItemsNeedingReorder(tenantId, warehouseId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{stockItemId}/receive")
    public ResponseEntity<StockItem> receiveStock(
            @PathVariable String stockItemId,
            @RequestParam Integer quantity) {
        StockItem item = stockInventoryService.receiveStock(stockItemId, quantity);
        return ResponseEntity.ok(item);
    }

    @PostMapping("/{stockItemId}/issue")
    public ResponseEntity<StockItem> issueStock(
            @PathVariable String stockItemId,
            @RequestParam Integer quantity) {
        StockItem item = stockInventoryService.issueStock(stockItemId, quantity);
        return ResponseEntity.ok(item);
    }

    @PostMapping("/{stockItemId}/allocate")
    public ResponseEntity<StockItem> allocateStock(
            @PathVariable String stockItemId,
            @RequestParam Integer quantity) {
        StockItem item = stockInventoryService.allocateStock(stockItemId, quantity);
        return ResponseEntity.ok(item);
    }

    @PostMapping("/update-reorder-flags")
    public ResponseEntity<List<StockItem>> updateReorderFlags(
            @RequestParam String tenantId,
            @RequestParam String warehouseId) {
        List<StockItem> items = stockInventoryService.updateReorderFlags(tenantId, warehouseId);
        return ResponseEntity.ok(items);
    }
}
