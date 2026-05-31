package com.gogidix.shared.warehousing.fulfillment.interfaces.rest;

import com.gogidix.shared.warehousing.fulfillment.application.dto.*;
import com.gogidix.shared.warehousing.fulfillment.application.service.FulfillmentOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Fulfillment Order Management
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/fulfillment/orders")
@RequiredArgsConstructor
@Tag(name = "Fulfillment Orders", description = "APIs for managing fulfillment orders")
public class FulfillmentOrderController {

    private final FulfillmentOrderService service;

    /**
     * Create a new fulfillment order
     */
    @PostMapping
    @Operation(summary = "Create fulfillment order", description = "Create a new fulfillment order in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fulfillment order created successfully",
                    content = @Content(schema = @Schema(implementation = FulfillmentOrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Order number already exists")
    })
    public ResponseEntity<FulfillmentOrderResponse> createOrder(
            @Parameter(description = "Fulfillment order creation request", required = true)
            @Valid @RequestBody CreateOrderRequest request) {
        log.info("REST request to create fulfillment order: {}", request.getOrderNumber());
        FulfillmentOrderResponse response = service.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * List all fulfillment orders for tenant
     */
    @GetMapping
    @Operation(summary = "List fulfillment orders", description = "Retrieve all fulfillment orders for a tenant with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid tenant ID")
    })
    public ResponseEntity<List<FulfillmentOrderResponse>> getOrders(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId,
            @Parameter(description = "Customer ID to filter by", example = "customer-456")
            @RequestParam(required = false) String customerId,
            @Parameter(description = "Order status to filter by", example = "PENDING")
            @RequestParam(required = false) String status) {

        log.info("REST request to get orders for tenant: {}", tenantId);

        if (customerId != null && !customerId.isEmpty()) {
            return ResponseEntity.ok(service.getOrdersByCustomer(tenantId, customerId));
        }

        if (status != null && !status.isEmpty()) {
            return ResponseEntity.ok(service.getOrdersByStatus(tenantId, status));
        }

        return ResponseEntity.ok(service.getOrders(tenantId));
    }

    /**
     * Get fulfillment order by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific fulfillment order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<FulfillmentOrderResponse> getOrder(
            @Parameter(description = "Order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String id) {
        log.info("REST request to get order: {}", id);
        return ResponseEntity.ok(service.getOrderById(id));
    }

    /**
     * Get order by order number
     */
    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by number", description = "Retrieve a fulfillment order by its order number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<FulfillmentOrderResponse> getOrderByNumber(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId,
            @Parameter(description = "Order number", required = true, example = "ORD-2024-001")
            @PathVariable String orderNumber) {

        log.info("REST request to get order by number: {}", orderNumber);
        return ResponseEntity.ok(service.getOrderByNumber(tenantId, orderNumber));
    }

    /**
     * Update order status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Update the status of a fulfillment order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition")
    })
    public ResponseEntity<FulfillmentOrderResponse> updateStatus(
            @Parameter(description = "Order ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String id,
            @Parameter(description = "Status update request", required = true)
            @Valid @RequestBody UpdateOrderStatusRequest request,
            @Parameter(description = "User performing the update", example = "admin")
            @RequestParam(defaultValue = "system") String updatedBy) {

        log.info("REST request to update order status: {} to {}", id, request.getStatus());
        return ResponseEntity.ok(service.updateOrderStatus(id, request, updatedBy));
    }

    /**
     * Get pending orders
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending orders", description = "Retrieve all pending fulfillment orders for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    public ResponseEntity<List<FulfillmentOrderResponse>> getPendingOrders(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {

        log.info("REST request to get pending orders for tenant: {}", tenantId);
        return ResponseEntity.ok(service.getPendingOrders(tenantId));
    }

    /**
     * Get orders ready for shipping
     */
    @GetMapping("/ready-to-ship")
    @Operation(summary = "Get ready to ship orders", description = "Retrieve all orders ready for shipping")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    public ResponseEntity<List<FulfillmentOrderResponse>> getReadyToShip(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {

        log.info("REST request to get orders ready to ship for tenant: {}", tenantId);
        return ResponseEntity.ok(service.getReadyForShipping(tenantId));
    }

    /**
     * Get high priority orders
     */
    @GetMapping("/high-priority")
    @Operation(summary = "Get high priority orders", description = "Retrieve all high priority fulfillment orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    public ResponseEntity<List<FulfillmentOrderResponse>> getHighPriorityOrders(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {

        log.info("REST request to get high priority orders for tenant: {}", tenantId);
        return ResponseEntity.ok(service.getHighPriorityOrders(tenantId));
    }

    /**
     * Get urgent orders
     */
    @GetMapping("/urgent")
    @Operation(summary = "Get urgent orders", description = "Retrieve all urgent fulfillment orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    public ResponseEntity<List<FulfillmentOrderResponse>> getUrgentOrders(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {

        log.info("REST request to get urgent orders for tenant: {}", tenantId);
        return ResponseEntity.ok(service.getUrgentOrders(tenantId));
    }
}
