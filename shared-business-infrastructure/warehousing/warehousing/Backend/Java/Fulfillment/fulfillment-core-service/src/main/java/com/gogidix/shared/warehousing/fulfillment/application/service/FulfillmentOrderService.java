package com.gogidix.shared.warehousing.fulfillment.application.service;

import com.gogidix.shared.warehousing.fulfillment.application.dto.*;
import com.gogidix.shared.warehousing.fulfillment.domain.entity.FulfillmentOrder;
import com.gogidix.shared.warehousing.fulfillment.domain.repository.FulfillmentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing fulfillment orders
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FulfillmentOrderService {

    private final FulfillmentOrderRepository repository;

    /**
     * Create a new fulfillment order
     */
    @Transactional
    public FulfillmentOrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating fulfillment order for tenant: {}, order: {}", request.getTenantId(), request.getOrderNumber());

        // Check if order number already exists
        if (repository.existsByTenantIdAndOrderNumber(request.getTenantId(), request.getOrderNumber())) {
            throw new IllegalArgumentException("Order number already exists: " + request.getOrderNumber());
        }

        // Convert items
        List<FulfillmentOrder.OrderItem> items = request.getItems().stream()
            .map(itemDto -> {
                FulfillmentOrder.OrderItem item = FulfillmentOrder.OrderItem.builder()
                    .sku(itemDto.getSku())
                    .productName(itemDto.getProductName())
                    .quantity(itemDto.getQuantity())
                    .weight(itemDto.getWeight() != null ? BigDecimal.valueOf(itemDto.getWeight()) : BigDecimal.ZERO)
                    .volume(itemDto.getVolume() != null ? BigDecimal.valueOf(itemDto.getVolume()) : BigDecimal.ZERO)
                    .unitPrice(itemDto.getUnitPrice() != null ? BigDecimal.valueOf(itemDto.getUnitPrice()) : BigDecimal.ZERO)
                    .build();

                if (itemDto.getUnitPrice() != null) {
                    item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }

                return item;
            })
            .collect(Collectors.toList());

        // Build order
        FulfillmentOrder order = FulfillmentOrder.builder()
            .tenantId(request.getTenantId())
            .orderNumber(request.getOrderNumber())
            .customerId(request.getCustomerId())
            .warehouseId(request.getWarehouseId())
            .status("PENDING")
            .priority(request.getPriority())
            .items(items)
            .shippingAddress(request.getShippingAddress())
            .shippingMethod(request.getShippingMethod())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        // Calculate totals
        order.calculateTotals();

        FulfillmentOrder saved = repository.save(order);
        log.info("Fulfillment order created with ID: {}", saved.getId());

        return FulfillmentOrderResponse.fromEntity(saved);
    }

    /**
     * Get all orders for tenant
     */
    public List<FulfillmentOrderResponse> getOrders(String tenantId) {
        log.debug("Fetching orders for tenant: {}", tenantId);
        return repository.findByTenantId(tenantId).stream()
            .map(FulfillmentOrderResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Get order by ID
     */
    public FulfillmentOrderResponse getOrderById(String id) {
        FulfillmentOrder order = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        return FulfillmentOrderResponse.fromEntity(order);
    }

    /**
     * Get orders by customer
     */
    public List<FulfillmentOrderResponse> getOrdersByCustomer(String tenantId, String customerId) {
        log.debug("Fetching orders for customer: {}", customerId);
        return repository.findByTenantIdAndCustomerId(tenantId, customerId).stream()
            .map(FulfillmentOrderResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Get orders by status
     */
    public List<FulfillmentOrderResponse> getOrdersByStatus(String tenantId, String status) {
        log.debug("Fetching orders with status: {}", status);
        return repository.findByTenantIdAndStatus(tenantId, status).stream()
            .map(FulfillmentOrderResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Get order by tenant and order number
     */
    public FulfillmentOrderResponse getOrderByNumber(String tenantId, String orderNumber) {
        FulfillmentOrder order = repository.findByTenantIdAndOrderNumber(tenantId, orderNumber);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderNumber);
        }
        return FulfillmentOrderResponse.fromEntity(order);
    }

    /**
     * Update order status
     */
    @Transactional
    public FulfillmentOrderResponse updateOrderStatus(String id, UpdateOrderStatusRequest request, String updatedBy) {
        log.info("Updating order status: {} to {}", id, request.getStatus());

        FulfillmentOrder order = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        order.setStatus(request.getStatus());
        order.setUpdatedAt(LocalDateTime.now());

        // Set tracking information if provided
        if (request.getTrackingNumber() != null) {
            order.setTrackingNumber(request.getTrackingNumber());
        }
        if (request.getCarrier() != null) {
            order.setCarrier(request.getCarrier());
        }

        // Update audit fields based on status
        switch (request.getStatus()) {
            case "PICKING":
                order.setPickedBy(updatedBy);
                break;
            case "PACKING":
                order.setPackedBy(updatedBy);
                break;
            case "SHIPPED":
                order.setShippedBy(updatedBy);
                order.setActualShipDate(LocalDateTime.now());
                break;
            case "DELIVERED":
                order.setActualDeliveryDate(LocalDateTime.now());
                break;
        }

        FulfillmentOrder updated = repository.save(order);
        log.info("Order status updated: {}", id);

        return FulfillmentOrderResponse.fromEntity(updated);
    }

    /**
     * Get pending orders
     */
    public List<FulfillmentOrderResponse> getPendingOrders(String tenantId) {
        return repository.findPendingOrders(tenantId).stream()
            .map(FulfillmentOrderResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Get orders ready for shipping
     */
    public List<FulfillmentOrderResponse> getReadyForShipping(String tenantId) {
        return repository.findReadyForShipping(tenantId).stream()
            .map(FulfillmentOrderResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Get high priority orders
     */
    public List<FulfillmentOrderResponse> getHighPriorityOrders(String tenantId) {
        return repository.findByTenantIdAndPriority(tenantId, "HIGH").stream()
            .map(FulfillmentOrderResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Get urgent orders
     */
    public List<FulfillmentOrderResponse> getUrgentOrders(String tenantId) {
        return repository.findByTenantIdAndPriority(tenantId, "URGENT").stream()
            .map(FulfillmentOrderResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Count orders by status
     */
    public long countOrdersByStatus(String tenantId, String status) {
        return repository.countByTenantIdAndStatus(tenantId, status);
    }
}
