package com.gogidix.shared.warehousing.fulfillment.packing.interfaces.rest;

import com.gogidix.shared.warehousing.fulfillment.packing.application.service.PackOrderService;
import com.gogidix.shared.warehousing.fulfillment.packing.domain.entity.PackOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Pack Order Management
 */
@RestController
@RequestMapping("/api/v1/fulfillment/packing")
@RequiredArgsConstructor
@Tag(name = "Pack Orders", description = "APIs for managing warehouse pack orders")
public class PackOrderController {

    private final PackOrderService packOrderService;

    @PostMapping
    @Operation(summary = "Create pack order", description = "Create a new pack order in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pack order created successfully",
                    content = @Content(schema = @Schema(implementation = PackOrder.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<PackOrder> createPackOrder(
            @Parameter(description = "Pack order to create", required = true)
            @Valid @RequestBody PackOrder packOrder) {
        PackOrder created = packOrderService.createPackOrder(packOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{packNumber}")
    @Operation(summary = "Get pack order by number", description = "Retrieve a pack order by its pack number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pack order found"),
            @ApiResponse(responseCode = "404", description = "Pack order not found")
    })
    public ResponseEntity<PackOrder> getByPackNumber(
            @Parameter(description = "Pack number", required = true, example = "PACK-2024-001")
            @PathVariable String packNumber,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        PackOrder packOrder = packOrderService.getByPackNumber(tenantId, packNumber);
        return ResponseEntity.ok(packOrder);
    }

    @GetMapping("/pick/{pickOrderId}")
    @Operation(summary = "Get pack order by pick order ID", description = "Retrieve a pack order by its associated pick order ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pack order found"),
            @ApiResponse(responseCode = "404", description = "Pack order not found")
    })
    public ResponseEntity<PackOrder> getByPickOrderId(
            @Parameter(description = "Pick order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String pickOrderId,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        PackOrder packOrder = packOrderService.getByPickOrderId(tenantId, pickOrderId);
        return ResponseEntity.ok(packOrder);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending pack orders", description = "Retrieve all pending pack orders for a warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pack orders retrieved successfully")
    })
    public ResponseEntity<List<PackOrder>> getPendingOrders(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId,
            @Parameter(description = "Warehouse ID", required = true, example = "warehouse-456")
            @RequestParam String warehouseId) {
        List<PackOrder> orders = packOrderService.getPendingOrders(tenantId, warehouseId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get pack orders by status", description = "Retrieve all pack orders with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pack orders retrieved successfully")
    })
    public ResponseEntity<List<PackOrder>> getByStatus(
            @Parameter(description = "Pack status", required = true, example = "PENDING")
            @PathVariable String status,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        List<PackOrder> orders = packOrderService.getByStatus(tenantId, status);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{packOrderId}/assign")
    @Operation(summary = "Assign packer", description = "Assign a packer to a pack order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Packer assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Pack order not found")
    })
    public ResponseEntity<PackOrder> assignPacker(
            @Parameter(description = "Pack order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String packOrderId,
            @Parameter(description = "Packer ID", required = true, example = "user-789")
            @RequestParam String packerId,
            @Parameter(description = "Packer name", required = true, example = "Jane Doe")
            @RequestParam String packerName) {
        PackOrder packOrder = packOrderService.assignPacker(packOrderId, packerId, packerName);
        return ResponseEntity.ok(packOrder);
    }

    @PostMapping("/{packOrderId}/start")
    @Operation(summary = "Start packing", description = "Start the packing process for a pack order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Packing started successfully"),
            @ApiResponse(responseCode = "404", description = "Pack order not found")
    })
    public ResponseEntity<PackOrder> startPacking(
            @Parameter(description = "Pack order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String packOrderId,
            @Parameter(description = "Packer ID", required = true, example = "user-789")
            @RequestParam String packerId,
            @Parameter(description = "Packer name", required = true, example = "Jane Doe")
            @RequestParam String packerName) {
        PackOrder packOrder = packOrderService.startPacking(packOrderId, packerId, packerName);
        return ResponseEntity.ok(packOrder);
    }

    @PostMapping("/{packOrderId}/items/{itemId}/pack")
    @Operation(summary = "Mark item as packed", description = "Mark a specific item in a pack order as packed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item marked as packed"),
            @ApiResponse(responseCode = "404", description = "Pack order or item not found")
    })
    public ResponseEntity<PackOrder> markItemPacked(
            @Parameter(description = "Pack order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String packOrderId,
            @Parameter(description = "Item ID", required = true, example = "item-123")
            @PathVariable String itemId,
            @Parameter(description = "Quantity packed", required = true, example = "10.0")
            @RequestParam Double quantity,
            @Parameter(description = "Box number", required = true, example = "BOX-001")
            @RequestParam String boxNumber,
            @Parameter(description = "User who packed the item", required = true, example = "user-789")
            @RequestParam String packedBy) {
        PackOrder packOrder = packOrderService.markItemPacked(packOrderId, itemId, quantity, boxNumber, packedBy);
        return ResponseEntity.ok(packOrder);
    }

    @PostMapping("/{packOrderId}/complete")
    @Operation(summary = "Complete packing", description = "Complete the packing process for a pack order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Packing completed successfully"),
            @ApiResponse(responseCode = "404", description = "Pack order not found")
    })
    public ResponseEntity<PackOrder> completePacking(
            @Parameter(description = "Pack order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String packOrderId) {
        PackOrder packOrder = packOrderService.completePacking(packOrderId);
        return ResponseEntity.ok(packOrder);
    }

    @PostMapping("/{packOrderId}/ship")
    @Operation(summary = "Mark as shipped", description = "Mark a pack order as shipped with tracking number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order marked as shipped"),
            @ApiResponse(responseCode = "404", description = "Pack order not found")
    })
    public ResponseEntity<PackOrder> markAsShipped(
            @Parameter(description = "Pack order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String packOrderId,
            @Parameter(description = "Tracking number", required = true, example = "1Z999AA10123456784")
            @RequestParam String trackingNumber) {
        PackOrder packOrder = packOrderService.markAsShipped(packOrderId, trackingNumber);
        return ResponseEntity.ok(packOrder);
    }
}
