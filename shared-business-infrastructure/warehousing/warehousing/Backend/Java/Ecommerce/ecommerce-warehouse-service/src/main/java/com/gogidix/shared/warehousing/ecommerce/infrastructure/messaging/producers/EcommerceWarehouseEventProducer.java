package com.gogidix.shared.warehousing.ecommerce.infrastructure.messaging.producers;

import com.gogidix.shared.warehousing.ecommerce.domain.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EcommerceWarehouseEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishStockRegistered(String vendorId, String warehouseId, String zoneId,
                                        java.util.List<com.gogidix.shared.warehousing.ecommerce.domain.events.WarehouseStockRegisteredEvent.StockItemInfo> items) {
        WarehouseStockRegisteredEvent event = WarehouseStockRegisteredEvent.builder()
                .vendorId(vendorId)
                .warehouseId(warehouseId)
                .zoneId(zoneId)
                .items(items)
                .registeredAt(LocalDateTime.now())
                .build();
        kafkaTemplate.send("ecommerce.warehouse.stock_registered", vendorId, event);
        log.info("Published stock_registered event for vendor {}", vendorId);
    }

    public void publishFulfillmentReceived(String fulfillmentId, String orderId, String subOrderId,
                                            String warehouseId, String zoneId) {
        FulfillmentReceivedEvent event = FulfillmentReceivedEvent.builder()
                .fulfillmentId(fulfillmentId)
                .orderId(orderId)
                .subOrderId(subOrderId)
                .warehouseId(warehouseId)
                .zoneId(zoneId)
                .receivedAt(LocalDateTime.now())
                .build();
        kafkaTemplate.send("ecommerce.warehouse.fulfillment_received", orderId, event);
        log.info("Published fulfillment_received event for order {}", orderId);
    }

    public void publishFulfillmentPicking(String fulfillmentId, String orderId, String subOrderId,
                                           String staffId, String staffName) {
        FulfillmentPickingEvent event = FulfillmentPickingEvent.builder()
                .fulfillmentId(fulfillmentId)
                .orderId(orderId)
                .subOrderId(subOrderId)
                .staffId(staffId)
                .staffName(staffName)
                .startedAt(LocalDateTime.now())
                .build();
        kafkaTemplate.send("ecommerce.warehouse.fulfillment_picking", orderId, event);
        log.info("Published fulfillment_picking event for order {}", orderId);
    }

    public void publishFulfillmentPacked(String fulfillmentId, String orderId, String subOrderId,
                                          Double packageWeight,
                                          java.util.Map<String, Double> packageDimensions) {
        FulfillmentPackedEvent event = FulfillmentPackedEvent.builder()
                .fulfillmentId(fulfillmentId)
                .orderId(orderId)
                .subOrderId(subOrderId)
                .packageWeight(packageWeight)
                .packageDimensions(packageDimensions)
                .packedAt(LocalDateTime.now())
                .build();
        kafkaTemplate.send("ecommerce.warehouse.fulfillment_packed", orderId, event);
        log.info("Published fulfillment_packed event for order {}", orderId);
    }

    public void publishReadyForPickup(ReadyForPickupEvent event) {
        kafkaTemplate.send("ecommerce.warehouse.ready_for_pickup", event.getOrderId(), event);
        log.info("Published ready_for_pickup event for order {}", event.getOrderId());
    }

    public void publishStockLow(String vendorId, String warehouseId, String sku,
                                 Integer currentStock, Integer reorderThreshold) {
        StockLowEvent event = StockLowEvent.builder()
                .vendorId(vendorId)
                .warehouseId(warehouseId)
                .sku(sku)
                .currentStock(currentStock)
                .reorderThreshold(reorderThreshold)
                .build();
        kafkaTemplate.send("ecommerce.warehouse.stock_low", vendorId, event);
        log.warn("Published stock_low event for SKU {} at warehouse {}", sku, warehouseId);
    }

    public void publishStockDepleted(String vendorId, String warehouseId, String sku) {
        StockDepletedEvent event = StockDepletedEvent.builder()
                .vendorId(vendorId)
                .warehouseId(warehouseId)
                .sku(sku)
                .depletedAt(LocalDateTime.now())
                .build();
        kafkaTemplate.send("ecommerce.warehouse.stock_depleted", vendorId, event);
        log.error("Published stock_depleted event for SKU {} at warehouse {}", sku, warehouseId);
    }
}
