package com.gogidix.shared.warehousing.inventory.interfaces.rest;

import com.gogidix.shared.warehousing.inventory.application.command.CreateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.command.UpdateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.dto.InventoryDTO;
import com.gogidix.shared.warehousing.inventory.application.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Inventory REST Controller
 *
 * Provides multi-tenant inventory management APIs
 * All endpoints require X-Tenant-ID header for tenant isolation
 */
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Create inventory item (tenant-scoped)
     * POST /api/v1/inventory/inventory
     */
    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(
            @Valid @RequestBody CreateInventoryCommand command) {
        InventoryDTO inventory = inventoryService.createInventory(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    /**
     * Get inventory by ID (tenant-scoped)
     * GET /api/v1/inventory/inventory/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventory(@PathVariable String id) {
        InventoryDTO inventory = inventoryService.getInventory(id);
        return ResponseEntity.ok(inventory);
    }

    /**
     * Get all inventory for current tenant
     * GET /api/v1/inventory/inventory
     */
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        List<InventoryDTO> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    /**
     * Get inventory by SKU (tenant-scoped)
     * GET /api/v1/inventory/inventory/sku/{sku}
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<List<InventoryDTO>> getInventoryBySku(@PathVariable String sku) {
        List<InventoryDTO> inventory = inventoryService.getInventoryBySku(sku);
        return ResponseEntity.ok(inventory);
    }

    /**
     * Get available inventory (quantity > 0, tenant-scoped)
     * GET /api/v1/inventory/inventory/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<InventoryDTO>> getAvailableInventory() {
        List<InventoryDTO> inventory = inventoryService.getAvailableInventory();
        return ResponseEntity.ok(inventory);
    }

    /**
     * Update inventory (tenant-scoped)
     * PUT /api/v1/inventory/inventory/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(
            @PathVariable String id,
            @Valid @RequestBody UpdateInventoryCommand command) {
        InventoryDTO inventory = inventoryService.updateInventory(id, command);
        return ResponseEntity.ok(inventory);
    }

    /**
     * Adjust inventory quantity (tenant-scoped)
     * PATCH /api/v1/inventory/inventory/{id}/quantity
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<InventoryDTO> adjustQuantity(
            @PathVariable String id,
            @RequestParam Integer adjustment) {
        InventoryDTO inventory = inventoryService.adjustQuantity(id, adjustment);
        return ResponseEntity.ok(inventory);
    }

    /**
     * Delete inventory (tenant-scoped)
     * DELETE /api/v1/inventory/inventory/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable String id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check inventory availability (tenant-scoped)
     * POST /api/v1/inventory/inventory/check
     */
    @PostMapping("/check")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestParam String sku,
            @RequestParam Integer quantity) {
        boolean available = inventoryService.checkAvailability(sku, quantity);
        return ResponseEntity.ok(available);
    }
}
