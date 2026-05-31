package com.gogidix.shared.warehousing.ecommerce.infrastructure.messaging.consumers;

import com.gogidix.shared.warehousing.ecommerce.application.service.EcommerceFulfillmentService;
import com.gogidix.shared.warehousing.ecommerce.application.service.EcommerceStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EcommerceOrderEventConsumer {

    private final EcommerceFulfillmentService fulfillmentService;
    private final EcommerceStockService stockService;

    @KafkaListener(topics = "ecommerce.order.confirmed", groupId = "ecommerce-warehouse-group")
    public void handleOrderConfirmed(Map<String, Object> event) {
        log.info("Received order.confirmed event: {}", event);
        try {
            String orderId = (String) event.get("orderId");
            String subOrderId = (String) event.get("subOrderId");
            String vendorId = (String) event.get("vendorId");
            String warehouseId = (String) event.get("warehouseId");

            fulfillmentService.createFulfillmentFromOrder(orderId, subOrderId, vendorId, warehouseId, event);
            log.info("Created fulfillment for confirmed order {}", orderId);
        } catch (Exception e) {
            log.error("Error processing order.confirmed event", e);
        }
    }

    @KafkaListener(topics = "ecommerce.order.cancelled", groupId = "ecommerce-warehouse-group")
    public void handleOrderCancelled(Map<String, Object> event) {
        log.info("Received order.cancelled event: {}", event);
        try {
            String orderId = (String) event.get("orderId");
            String subOrderId = (String) event.get("subOrderId");
            fulfillmentService.cancelFulfillmentByOrder(orderId, subOrderId);
            log.info("Cancelled fulfillment for order {}", orderId);
        } catch (Exception e) {
            log.error("Error processing order.cancelled event", e);
        }
    }

    @KafkaListener(topics = "ecommerce.vendor.stock_update", groupId = "ecommerce-warehouse-group")
    public void handleVendorStockUpdate(Map<String, Object> event) {
        log.info("Received vendor.stock_update event: {}", event);
        try {
            String vendorId = (String) event.get("vendorId");
            String warehouseId = (String) event.get("warehouseId");
            String sku = (String) event.get("sku");
            Integer quantity = (Integer) event.get("quantity");
            stockService.updateStockLevel(vendorId, warehouseId, sku, quantity);
            log.info("Updated stock for vendor {} SKU {}", vendorId, sku);
        } catch (Exception e) {
            log.error("Error processing vendor.stock_update event", e);
        }
    }

    @KafkaListener(topics = "ecommerce.courier.picked_up", groupId = "ecommerce-warehouse-group")
    public void handleCourierPickedUp(Map<String, Object> event) {
        log.info("Received courier.picked_up event: {}", event);
        try {
            String fulfillmentId = (String) event.get("fulfillmentId");
            String orderId = (String) event.get("orderId");
            fulfillmentService.markHandedToCourier(fulfillmentId);
            log.info("Marked fulfillment {} as handed to courier", fulfillmentId);
        } catch (Exception e) {
            log.error("Error processing courier.picked_up event", e);
        }
    }
}
