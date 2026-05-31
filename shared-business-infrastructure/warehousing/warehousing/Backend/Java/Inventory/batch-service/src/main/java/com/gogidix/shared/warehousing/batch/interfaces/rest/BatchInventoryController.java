package com.gogidix.shared.warehousing.batch.interfaces.rest;

import com.gogidix.shared.warehousing.batch.application.dto.BatchInventoryDTO;
import com.gogidix.shared.warehousing.batch.application.service.BatchInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Batch Inventory REST Controller
 *
 * Provides multi-tenant batch inventory management APIs
 */
@RestController
@RequestMapping("/batch-inventory")
@RequiredArgsConstructor
@Tag(name = "Batch Inventory", description = "APIs for managing batch inventory")
public class BatchInventoryController {

    private final BatchInventoryService batchInventoryService;

    @GetMapping("/{id}")
    @Operation(summary = "Get batch inventory by ID", description = "Retrieve batch inventory by its ID")
    public ResponseEntity<BatchInventoryDTO> getBatchInventory(
            @Parameter(description = "Batch inventory ID") @PathVariable String id) {
        BatchInventoryDTO inventory = batchInventoryService.getBatchInventory(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/lot/{lotId}/sku/{sku}")
    @Operation(summary = "Get batch inventory by lot and SKU", description = "Retrieve batch inventory for a specific lot and SKU")
    public ResponseEntity<BatchInventoryDTO> getBatchInventoryByLotAndSku(
            @Parameter(description = "Lot ID") @PathVariable String lotId,
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        return batchInventoryService.getBatchInventoryByLotAndSku(lotId, sku)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lot/{lotId}")
    @Operation(summary = "Get batch inventory by lot", description = "Retrieve all batch inventory for a specific lot")
    public ResponseEntity<List<BatchInventoryDTO>> getBatchInventoryByLot(
            @Parameter(description = "Lot ID") @PathVariable String lotId) {
        List<BatchInventoryDTO> inventory = batchInventoryService.getBatchInventoryByLot(lotId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get batch inventory by SKU", description = "Retrieve all batch inventory for a specific SKU")
    public ResponseEntity<List<BatchInventoryDTO>> getBatchInventoryBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        List<BatchInventoryDTO> inventory = batchInventoryService.getBatchInventoryBySku(sku);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping
    @Operation(summary = "Get all batch inventory", description = "Retrieve all batch inventory for the current tenant")
    public ResponseEntity<List<BatchInventoryDTO>> getAllBatchInventory() {
        List<BatchInventoryDTO> inventory = batchInventoryService.getAllBatchInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/location/{locationId}")
    @Operation(summary = "Get batch inventory by location", description = "Retrieve all batch inventory for a specific location")
    public ResponseEntity<List<BatchInventoryDTO>> getBatchInventoryByLocation(
            @Parameter(description = "Location ID") @PathVariable String locationId) {
        List<BatchInventoryDTO> inventory = batchInventoryService.getBatchInventoryByLocation(locationId);
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}/adjust")
    @Operation(summary = "Adjust batch inventory quantity", description = "Adjust the quantity on hand for batch inventory")
    public ResponseEntity<BatchInventoryDTO> adjustQuantity(
            @Parameter(description = "Batch inventory ID") @PathVariable String id,
            @Parameter(description = "New quantity on hand") @RequestParam Integer quantityOnHand,
            @Parameter(description = "Unit cost") @RequestParam(required = false) Double unitCost) {
        BatchInventoryDTO inventory = batchInventoryService.adjustQuantity(id, quantityOnHand, unitCost);
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}/allocate")
    @Operation(summary = "Allocate batch inventory quantity", description = "Allocate quantity from batch inventory")
    public ResponseEntity<BatchInventoryDTO> allocateQuantity(
            @Parameter(description = "Batch inventory ID") @PathVariable String id,
            @Parameter(description = "Quantity to allocate") @RequestParam Integer quantity) {
        BatchInventoryDTO inventory = batchInventoryService.allocateQuantity(id, quantity);
        return ResponseEntity.ok(inventory);
    }

    @PatchMapping("/{id}/deallocate")
    @Operation(summary = "Deallocate batch inventory quantity", description = "Deallocate quantity from batch inventory")
    public ResponseEntity<BatchInventoryDTO> deallocateQuantity(
            @Parameter(description = "Batch inventory ID") @PathVariable String id,
            @Parameter(description = "Quantity to deallocate") @RequestParam Integer quantity) {
        BatchInventoryDTO inventory = batchInventoryService.deallocateQuantity(id, quantity);
        return ResponseEntity.ok(inventory);
    }
}
