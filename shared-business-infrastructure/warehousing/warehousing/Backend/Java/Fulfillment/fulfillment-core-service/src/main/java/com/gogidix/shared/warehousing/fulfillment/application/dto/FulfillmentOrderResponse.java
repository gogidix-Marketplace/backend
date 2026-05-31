package com.gogidix.shared.warehousing.fulfillment.application.dto;

import com.gogidix.shared.warehousing.fulfillment.domain.entity.FulfillmentOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for fulfillment order response
 */
@Data
public class FulfillmentOrderResponse {

    private String id;
    private String tenantId;
    private String orderNumber;
    private String customerId;
    private String warehouseId;
    private String status;
    private String priority;
    private List<OrderItemDto> items;
    private Integer totalItems;
    private BigDecimal totalWeight;
    private BigDecimal totalVolume;
    private String shippingAddress;
    private String shippingMethod;
    private BigDecimal shippingCost;
    private LocalDateTime estimatedShipDate;
    private LocalDateTime actualShipDate;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private String trackingNumber;
    private String carrier;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class OrderItemDto {
        private String sku;
        private String productName;
        private Integer quantity;
        private BigDecimal weight;
        private BigDecimal volume;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }

    public static FulfillmentOrderResponse fromEntity(FulfillmentOrder order) {
        FulfillmentOrderResponse response = new FulfillmentOrderResponse();
        response.setId(order.getId());
        response.setTenantId(order.getTenantId());
        response.setOrderNumber(order.getOrderNumber());
        response.setCustomerId(order.getCustomerId());
        response.setWarehouseId(order.getWarehouseId());
        response.setStatus(order.getStatus());
        response.setPriority(order.getPriority());

        if (order.getItems() != null) {
            response.setItems(order.getItems().stream()
                .map(item -> {
                    OrderItemDto dto = new OrderItemDto();
                    dto.setSku(item.getSku());
                    dto.setProductName(item.getProductName());
                    dto.setQuantity(item.getQuantity());
                    dto.setWeight(item.getWeight());
                    dto.setVolume(item.getVolume());
                    dto.setUnitPrice(item.getUnitPrice());
                    dto.setTotalPrice(item.getTotalPrice());
                    return dto;
                })
                .collect(Collectors.toList()));
        }

        response.setTotalItems(order.getTotalItems());
        response.setTotalWeight(order.getTotalWeight());
        response.setTotalVolume(order.getTotalVolume());
        response.setShippingAddress(order.getShippingAddress());
        response.setShippingMethod(order.getShippingMethod());
        response.setShippingCost(order.getShippingCost());
        response.setEstimatedShipDate(order.getEstimatedShipDate());
        response.setActualShipDate(order.getActualShipDate());
        response.setEstimatedDeliveryDate(order.getEstimatedDeliveryDate());
        response.setActualDeliveryDate(order.getActualDeliveryDate());
        response.setTrackingNumber(order.getTrackingNumber());
        response.setCarrier(order.getCarrier());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}
