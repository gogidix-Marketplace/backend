package com.gogidix.shared.warehousing.fulfillment.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Fulfillment Order Entity
 *
 * Represents a fulfillment order to be processed
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fulfillment_orders")
@CompoundIndex(def = "{'tenantId': 1, 'id': 1}", name = "idx_tenant_id")
@CompoundIndex(def = "{'tenantId': 1, 'orderNumber': 1}", unique = true)
@CompoundIndex(def = "{'tenantId': 1, 'status': 1}")
@CompoundIndex(def = "{'tenantId': 1, 'customerId': 1}")
public class FulfillmentOrder {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String orderNumber; // Unique within tenant

    @Indexed
    private String customerId;

    private String warehouseId;

    // Order details
    private String status; // PENDING, PICKING, PACKING, SHIPPED, DELIVERED, CANCELLED
    private String priority; // LOW, NORMAL, HIGH, URGENT

    // Items
    private List<OrderItem> items;

    // Totals
    private Integer totalItems;
    private BigDecimal totalWeight;
    private BigDecimal totalVolume;

    // Shipping
    private String shippingAddress;
    private String shippingMethod;
    private BigDecimal shippingCost;
    private LocalDateTime estimatedShipDate;
    private LocalDateTime actualShipDate;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;

    // Tracking
    private String trackingNumber;
    private String carrier;

    // Audit
    private String pickedBy;
    private String packedBy;
    private String shippedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Order Item inner class
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem {
        private String sku;
        private String productName;
        private Integer quantity;
        private BigDecimal weight;
        private BigDecimal volume;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }

    /**
     * Calculate totals
     */
    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            totalItems = 0;
            totalWeight = BigDecimal.ZERO;
            totalVolume = BigDecimal.ZERO;
            return;
        }

        totalItems = items.stream()
            .mapToInt(OrderItem::getQuantity)
            .sum();

        totalWeight = items.stream()
            .map(OrderItem::getWeight)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalVolume = items.stream()
            .map(OrderItem::getVolume)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Check if order can be picked
     */
    public boolean canBePicked() {
        return "PENDING".equals(status);
    }

    /**
     * Check if order can be packed
     */
    public boolean canBePacked() {
        return "PICKING".equals(status);
    }

    /**
     * Check if order can be shipped
     */
    public boolean canBeShipped() {
        return "PACKING".equals(status);
    }
}
