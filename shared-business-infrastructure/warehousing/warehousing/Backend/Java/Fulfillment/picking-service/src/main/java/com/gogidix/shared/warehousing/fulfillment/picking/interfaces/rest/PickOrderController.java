package com.gogidix.shared.warehousing.fulfillment.picking.interfaces.rest;

import com.gogidix.shared.warehousing.fulfillment.picking.application.service.PickOrderService;
import com.gogidix.shared.warehousing.fulfillment.picking.domain.entity.PickOrder;
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
 * REST Controller for Pick Order Management
 */
@RestController
@RequestMapping("/api/v1/fulfillment/picking")
@RequiredArgsConstructor
@Tag(name = "Pick Orders", description = "APIs for managing warehouse pick orders")
public class PickOrderController {

    private final PickOrderService pickOrderService;

    @PostMapping
    @Operation(summary = "Create pick order", description = "Create a new pick order in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pick order created successfully",
                    content = @Content(schema = @Schema(implementation = PickOrder.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<PickOrder> createPickOrder(
            @Parameter(description = "Pick order to create", required = true)
            @Valid @RequestBody PickOrder pickOrder) {
        PickOrder created = pickOrderService.createPickOrder(pickOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{pickNumber}")
    @Operation(summary = "Get pick order by number", description = "Retrieve a pick order by its pick number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pick order found"),
            @ApiResponse(responseCode = "404", description = "Pick order not found")
    })
    public ResponseEntity<PickOrder> getByPickNumber(
            @Parameter(description = "Pick number", required = true, example = "PICK-2024-001")
            @PathVariable String pickNumber,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        PickOrder pickOrder = pickOrderService.getByPickNumber(tenantId, pickNumber);
        return ResponseEntity.ok(pickOrder);
    }

    @GetMapping("/order/{orderNumber}")
    @Operation(summary = "Get pick order by order number", description = "Retrieve a pick order by its associated order number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pick order found"),
            @ApiResponse(responseCode = "404", description = "Pick order not found")
    })
    public ResponseEntity<PickOrder> getByOrderNumber(
            @Parameter(description = "Order number", required = true, example = "ORD-2024-001")
            @PathVariable String orderNumber,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        PickOrder pickOrder = pickOrderService.getByOrderNumber(tenantId, orderNumber);
        return ResponseEntity.ok(pickOrder);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending pick orders", description = "Retrieve all pending pick orders for a warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pick orders retrieved successfully")
    })
    public ResponseEntity<List<PickOrder>> getPendingOrders(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId,
            @Parameter(description = "Warehouse ID", required = true, example = "warehouse-456")
            @RequestParam String warehouseId) {
        List<PickOrder> orders = pickOrderService.getPendingOrders(tenantId, warehouseId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get pick orders by status", description = "Retrieve all pick orders with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pick orders retrieved successfully")
    })
    public ResponseEntity<List<PickOrder>> getByStatus(
            @Parameter(description = "Pick status", required = true, example = "PENDING")
            @PathVariable String status,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        List<PickOrder> orders = pickOrderService.getByStatus(tenantId, status);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{pickOrderId}/assign")
    @Operation(summary = "Assign picker", description = "Assign a picker to a pick order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Picker assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Pick order not found")
    })
    public ResponseEntity<PickOrder> assignPicker(
            @Parameter(description = "Pick order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String pickOrderId,
            @Parameter(description = "Picker ID", required = true, example = "user-789")
            @RequestParam String pickerId,
            @Parameter(description = "Picker name", required = true, example = "John Doe")
            @RequestParam String pickerName) {
        PickOrder pickOrder = pickOrderService.assignPicker(pickOrderId, pickerId, pickerName);
        return ResponseEntity.ok(pickOrder);
    }

    @PostMapping("/{pickOrderId}/start")
    @Operation(summary = "Start picking", description = "Start the picking process for a pick order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Picking started successfully"),
            @ApiResponse(responseCode = "404", description = "Pick order not found")
    })
    public ResponseEntity<PickOrder> startPicking(
            @Parameter(description = "Pick order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String pickOrderId,
            @Parameter(description = "Picker ID", required = true, example = "user-789")
            @RequestParam String pickerId,
            @Parameter(description = "Picker name", required = true, example = "John Doe")
            @RequestParam String pickerName) {
        PickOrder pickOrder = pickOrderService.startPicking(pickOrderId, pickerId, pickerName);
        return ResponseEntity.ok(pickOrder);
    }

    @PostMapping("/{pickOrderId}/items/{itemId}/pick")
    @Operation(summary = "Mark item as picked", description = "Mark a specific item in a pick order as picked")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item marked as picked"),
            @ApiResponse(responseCode = "404", description = "Pick order or item not found")
    })
    public ResponseEntity<PickOrder> markItemPicked(
            @Parameter(description = "Pick order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String pickOrderId,
            @Parameter(description = "Item ID", required = true, example = "item-123")
            @PathVariable String itemId,
            @Parameter(description = "Quantity picked", required = true, example = "10.0")
            @RequestParam Double quantity,
            @Parameter(description = "User who picked the item", required = true, example = "user-789")
            @RequestParam String pickedBy) {
        PickOrder pickOrder = pickOrderService.markItemPicked(pickOrderId, itemId, quantity, pickedBy);
        return ResponseEntity.ok(pickOrder);
    }

    @PostMapping("/{pickOrderId}/complete")
    @Operation(summary = "Complete picking", description = "Complete the picking process for a pick order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Picking completed successfully"),
            @ApiResponse(responseCode = "404", description = "Pick order not found")
    })
    public ResponseEntity<PickOrder> completePicking(
            @Parameter(description = "Pick order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String pickOrderId) {
        PickOrder pickOrder = pickOrderService.completePicking(pickOrderId);
        return ResponseEntity.ok(pickOrder);
    }
}
