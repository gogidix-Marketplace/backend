package com.gogidix.shared.warehousing.stock.interfaces.rest;

import com.gogidix.shared.warehousing.stock.application.command.AdjustStockCommand;
import com.gogidix.shared.warehousing.stock.application.command.CreateStockCommand;
import com.gogidix.shared.warehousing.stock.application.command.ReserveStockCommand;
import com.gogidix.shared.warehousing.stock.application.dto.StockLevelDTO;
import com.gogidix.shared.warehousing.stock.application.dto.StockMovementDTO;
import com.gogidix.shared.warehousing.stock.application.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Stock Management
 * 
 * Provides endpoints for:
 * - Stock CRUD operations
 * - Stock adjustments (inbound/outbound)
 * - Stock reservations and allocations
 * - Stock movement history
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;

    // ==================== Stock Level Operations ====================

    /**
     * Create initial stock for a SKU at a location
     */
    @PostMapping
    public ResponseEntity<StockLevelDTO> createStock(
            @Valid @RequestBody CreateStockCommand command,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("POST /api/v1/stock - Creating stock for SKU: {} at location: {}", 
            command.getSku(), command.getLocationId());
        
        StockLevelDTO created = stockService.createStock(command, tenantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get stock by SKU and location
     */
    @GetMapping("/sku/{sku}/location/{locationId}")
    public ResponseEntity<StockLevelDTO> getStock(
            @PathVariable String sku,
            @PathVariable String locationId) {
        log.info("GET /api/v1/stock/sku/{}/location/{}", sku, locationId);
        
        return stockService.getStock(sku, locationId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all stock levels for a SKU
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<List<StockLevelDTO>> getStockBySku(@PathVariable String sku) {
        log.info("GET /api/v1/stock/sku/{}", sku);
        return ResponseEntity.ok(stockService.getStockBySku(sku));
    }

    /**
     * Get all stock levels at a location
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<StockLevelDTO>> getStockByLocation(@PathVariable String locationId) {
        log.info("GET /api/v1/stock/location/{}", locationId);
        return ResponseEntity.ok(stockService.getStockByLocation(locationId));
    }

    /**
     * Get all stock levels
     */
    @GetMapping
    public ResponseEntity<List<StockLevelDTO>> getAllStock() {
        log.info("GET /api/v1/stock");
        return ResponseEntity.ok(stockService.getAllStock());
    }

    /**
     * Delete stock
     */
    @DeleteMapping("/sku/{sku}/location/{locationId}")
    public ResponseEntity<Void> deleteStock(
            @PathVariable String sku,
            @PathVariable String locationId) {
        log.info("DELETE /api/v1/stock/sku/{}/location/{}", sku, locationId);
        
        stockService.deleteStock(sku, locationId);
        return ResponseEntity.noContent().build();
    }

    // ==================== Stock Adjustment Operations ====================

    /**
     * Adjust stock quantity
     */
    @PostMapping("/adjust")
    public ResponseEntity<StockLevelDTO> adjustStock(
            @Valid @RequestBody AdjustStockCommand command,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("POST /api/v1/stock/adjust - Adjusting stock for SKU: {} at location: {}", 
            command.getSku(), command.getLocationId());
        
        StockLevelDTO adjusted = stockService.adjustStock(command, tenantId);
        return ResponseEntity.ok(adjusted);
    }

    // ==================== Stock Reservation Operations ====================

    /**
     * Reserve stock for an order
     */
    @PostMapping("/reserve")
    public ResponseEntity<StockLevelDTO> reserveStock(
            @Valid @RequestBody ReserveStockCommand command,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        log.info("POST /api/v1/stock/reserve - Reserving {} units of SKU: {} for order: {}", 
            command.getQuantity(), command.getSku(), command.getOrderId());
        
        StockLevelDTO reserved = stockService.reserveStock(command, tenantId);
        return ResponseEntity.ok(reserved);
    }

    /**
     * Release reserved stock
     */
    @PostMapping("/release")
    public ResponseEntity<StockLevelDTO> releaseStock(
            @RequestParam String sku,
            @RequestParam String locationId,
            @RequestParam Integer quantity,
            @RequestParam String orderId,
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false) String releasedBy) {
        log.info("POST /api/v1/stock/release - Releasing {} units of SKU: {} from order: {}", 
            quantity, sku, orderId);
        
        StockLevelDTO released = stockService.releaseStock(sku, locationId, quantity, 
            orderId, tenantId, releasedBy);
        return ResponseEntity.ok(released);
    }

    /**
     * Allocate reserved stock
     */
    @PostMapping("/allocate")
    public ResponseEntity<StockLevelDTO> allocateStock(
            @RequestParam String sku,
            @RequestParam String locationId,
            @RequestParam Integer quantity,
            @RequestParam String orderId,
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false) String allocatedBy) {
        log.info("POST /api/v1/stock/allocate - Allocating {} units of SKU: {} for order: {}", 
            quantity, sku, orderId);
        
        StockLevelDTO allocated = stockService.allocateStock(sku, locationId, quantity, 
            orderId, tenantId, allocatedBy);
        return ResponseEntity.ok(allocated);
    }

    /**
     * Confirm allocation (stock shipped)
     */
    @PostMapping("/confirm-allocation")
    public ResponseEntity<StockLevelDTO> confirmAllocation(
            @RequestParam String sku,
            @RequestParam String locationId,
            @RequestParam Integer quantity,
            @RequestParam String orderId,
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(required = false) String confirmedBy) {
        log.info("POST /api/v1/stock/confirm-allocation - Confirming {} units of SKU: {} for order: {}", 
            quantity, sku, orderId);
        
        StockLevelDTO confirmed = stockService.confirmAllocation(sku, locationId, quantity, 
            orderId, tenantId, confirmedBy);
        return ResponseEntity.ok(confirmed);
    }

    // ==================== Stock Movement Operations ====================

    /**
     * Get stock movements for a SKU
     */
    @GetMapping("/movements/sku/{sku}")
    public ResponseEntity<List<StockMovementDTO>> getStockMovements(@PathVariable String sku) {
        log.info("GET /api/v1/stock/movements/sku/{}", sku);
        return ResponseEntity.ok(stockService.getStockMovements(sku));
    }

    /**
     * Get stock movements for a SKU with pagination
     */
    @GetMapping("/movements/sku/{sku}/paged")
    public ResponseEntity<Page<StockMovementDTO>> getStockMovementsPaged(
            @PathVariable String sku,
            Pageable pageable) {
        log.info("GET /api/v1/stock/movements/sku/{}/paged", sku);
        return ResponseEntity.ok(stockService.getStockMovements(sku, pageable));
    }

    /**
     * Get stock movements for a SKU at a location
     */
    @GetMapping("/movements/sku/{sku}/location/{locationId}")
    public ResponseEntity<List<StockMovementDTO>> getStockMovements(
            @PathVariable String sku,
            @PathVariable String locationId) {
        log.info("GET /api/v1/stock/movements/sku/{}/location/{}", sku, locationId);
        return ResponseEntity.ok(stockService.getStockMovements(sku, locationId));
    }

    /**
     * Get stock movements by reference ID
     */
    @GetMapping("/movements/reference/{referenceId}")
    public ResponseEntity<List<StockMovementDTO>> getStockMovementsByReference(
            @PathVariable String referenceId) {
        log.info("GET /api/v1/stock/movements/reference/{}", referenceId);
        return ResponseEntity.ok(stockService.getStockMovementsByReference(referenceId));
    }

    // ==================== Stock Alert Operations ====================

    /**
     * Get stock levels below reorder point
     */
    @GetMapping("/alerts/below-reorder-point")
    public ResponseEntity<List<StockLevelDTO>> getStockBelowReorderPoint() {
        log.info("GET /api/v1/stock/alerts/below-reorder-point");
        return ResponseEntity.ok(stockService.getStockBelowReorderPoint());
    }

    /**
     * Get total available quantity for SKU across all locations
     */
    @GetMapping("/sku/{sku}/total-available")
    public ResponseEntity<Integer> getTotalAvailableQuantity(@PathVariable String sku) {
        log.info("GET /api/v1/stock/sku/{}/total-available", sku);
        return ResponseEntity.ok(stockService.getTotalAvailableQuantity(sku));
    }
}