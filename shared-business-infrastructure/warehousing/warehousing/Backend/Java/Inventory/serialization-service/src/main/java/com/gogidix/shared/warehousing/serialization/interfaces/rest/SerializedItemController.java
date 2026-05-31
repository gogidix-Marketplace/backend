package com.gogidix.shared.warehousing.serialization.interfaces.rest;

import com.gogidix.shared.warehousing.serialization.application.command.CreateSerializedItemCommand;
import com.gogidix.shared.warehousing.serialization.application.command.UpdateSerializedItemStatusCommand;
import com.gogidix.shared.warehousing.serialization.application.dto.SerializedItemDTO;
import com.gogidix.shared.warehousing.serialization.application.service.SerializedItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Serialized Item REST Controller
 *
 * Provides multi-tenant serialized item management APIs
 * All endpoints require X-Tenant-ID header for tenant isolation
 */
@RestController
@RequestMapping("/serialized-items")
@RequiredArgsConstructor
@Tag(name = "Serialized Items", description = "APIs for managing serialized inventory items")
public class SerializedItemController {

    private final SerializedItemService serializedItemService;

    /**
     * Create serialized item (tenant-scoped)
     * POST /api/v1/serialization/serialized-items
     */
    @PostMapping
    @Operation(summary = "Create a serialized item", description = "Create a new serialized item with unique serial number")
    public ResponseEntity<SerializedItemDTO> createSerializedItem(
            @Valid @RequestBody CreateSerializedItemCommand command) {
        SerializedItemDTO item = serializedItemService.createSerializedItem(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    /**
     * Get serialized item by ID (tenant-scoped)
     * GET /api/v1/serialization/serialized-items/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get serialized item by ID", description = "Retrieve a serialized item by its ID")
    public ResponseEntity<SerializedItemDTO> getSerializedItem(
            @Parameter(description = "Serialized item ID") @PathVariable String id) {
        SerializedItemDTO item = serializedItemService.getSerializedItem(id);
        return ResponseEntity.ok(item);
    }

    /**
     * Get serialized item by serial number (tenant-scoped)
     * GET /api/v1/serialization/serialized-items/serial/{serialNumber}
     */
    @GetMapping("/serial/{serialNumber}")
    @Operation(summary = "Get serialized item by serial number", description = "Retrieve a serialized item by its serial number")
    public ResponseEntity<SerializedItemDTO> getSerializedItemBySerialNumber(
            @Parameter(description = "Serial number") @PathVariable String serialNumber) {
        SerializedItemDTO item = serializedItemService.getSerializedItemBySerialNumber(serialNumber);
        return ResponseEntity.ok(item);
    }

    /**
     * Get all serialized items for current tenant
     * GET /api/v1/serialization/serialized-items
     */
    @GetMapping
    @Operation(summary = "Get all serialized items", description = "Retrieve all serialized items for the current tenant")
    public ResponseEntity<List<SerializedItemDTO>> getAllSerializedItems() {
        List<SerializedItemDTO> items = serializedItemService.getAllSerializedItems();
        return ResponseEntity.ok(items);
    }

    /**
     * Get serialized items by SKU (tenant-scoped)
     * GET /api/v1/serialization/serialized-items/sku/{sku}
     */
    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get serialized items by SKU", description = "Retrieve all serialized items for a specific SKU")
    public ResponseEntity<List<SerializedItemDTO>> getSerializedItemsBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        List<SerializedItemDTO> items = serializedItemService.getSerializedItemsBySku(sku);
        return ResponseEntity.ok(items);
    }

    /**
     * Get serialized items by batch ID
     * GET /api/v1/serialization/serialized-items/batch/{batchId}
     */
    @GetMapping("/batch/{batchId}")
    @Operation(summary = "Get serialized items by batch", description = "Retrieve all serialized items in a batch")
    public ResponseEntity<List<SerializedItemDTO>> getSerializedItemsByBatch(
            @Parameter(description = "Batch ID") @PathVariable String batchId) {
        List<SerializedItemDTO> items = serializedItemService.getSerializedItemsByBatch(batchId);
        return ResponseEntity.ok(items);
    }

    /**
     * Update serialized item status (tenant-scoped)
     * PUT /api/v1/serialization/serialized-items/{id}/status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update serialized item status", description = "Update the status of a serialized item")
    public ResponseEntity<SerializedItemDTO> updateStatus(
            @Parameter(description = "Serialized item ID") @PathVariable String id,
            @Valid @RequestBody UpdateSerializedItemStatusCommand command) {
        SerializedItemDTO item = serializedItemService.updateStatus(id, command);
        return ResponseEntity.ok(item);
    }

    /**
     * Update serialized item location (tenant-scoped)
     * PUT /api/v1/serialization/serialized-items/{id}/location
     */
    @PutMapping("/{id}/location")
    @Operation(summary = "Update serialized item location", description = "Update the location of a serialized item")
    public ResponseEntity<SerializedItemDTO> updateLocation(
            @Parameter(description = "Serialized item ID") @PathVariable String id,
            @Parameter(description = "Location ID") @RequestParam String locationId,
            @Parameter(description = "Bin location") @RequestParam(required = false) String binLocation) {
        SerializedItemDTO item = serializedItemService.updateLocation(id, locationId, binLocation);
        return ResponseEntity.ok(item);
    }

    /**
     * Delete serialized item (tenant-scoped)
     * DELETE /api/v1/serialization/serialized-items/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete serialized item", description = "Delete a serialized item by ID")
    public ResponseEntity<Void> deleteSerializedItem(
            @Parameter(description = "Serialized item ID") @PathVariable String id) {
        serializedItemService.deleteSerializedItem(id);
        return ResponseEntity.noContent().build();
    }
}
