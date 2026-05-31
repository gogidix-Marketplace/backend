package com.gogidix.shared.warehousing.order.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "outbound_orders")
@CompoundIndex(def = "{'tenantId': 1, 'orderNumber': 1}", name = "idx_tenant_order")
@Schema(description = "Outbound order entity")
public class OutboundOrder {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String orderNumber;

    private String customerOrderId;

    private OrderStatus status;

    private String customerId;

    private String customerName;

    private String warehouseId;

    private List<OrderLine> lines;

    private ShippingAddress shippingAddress;

    private String carrier;

    private String serviceLevel;

    private LocalDateTime expectedShipDate;

    private LocalDateTime actualShipDate;

    private Integer totalQuantity;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum OrderStatus {
        PENDING, PICKING, PICKED, PACKED, SHIPPED, CANCELLED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderLine {
        private Integer lineNumber;
        private String sku;
        private String productName;
        private Integer quantity;
        private Integer pickedQuantity;
        private LineStatus status;

        public enum LineStatus {
            PENDING, PICKING, PICKED, SHORT, CANCELLED
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddress {
        private String name;
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String postalCode;
        private String countryCode;
        private String phone;
    }
}
