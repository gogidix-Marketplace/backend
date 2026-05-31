package com.gogidix.shared.warehousing.stock.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Stock Event Publisher
 * 
 * Publishes stock-related events to Kafka for other services to consume
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String STOCK_UPDATED_TOPIC = "stock.updated";
    private static final String STOCK_RESERVED_TOPIC = "stock.reserved";
    private static final String STOCK_RELEASED_TOPIC = "stock.released";
    private static final String STOCK_ALLOCATED_TOPIC = "stock.allocated";
    private static final String RESERVATION_EXPIRED_TOPIC = "stock.reservation-expired";
    private static final String LOW_STOCK_ALERT_TOPIC = "stock.low-stock-alert";

    /**
     * Publish stock updated event
     */
    public void publishStockUpdated(String sku, String locationId, String tenantId,
                                     Integer availableQuantity, Integer reservedQuantity,
                                     Integer allocatedQuantity) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "STOCK_UPDATED");
        event.put("sku", sku);
        event.put("locationId", locationId);
        event.put("tenantId", tenantId);
        event.put("availableQuantity", availableQuantity);
        event.put("reservedQuantity", reservedQuantity);
        event.put("allocatedQuantity", allocatedQuantity);
        event.put("timestamp", LocalDateTime.now().toString());

        log.info("Publishing STOCK_UPDATED event for SKU: {} at location: {}", sku, locationId);
        kafkaTemplate.send(STOCK_UPDATED_TOPIC, sku, event);
    }

    /**
     * Publish stock reserved event
     */
    public void publishStockReserved(String sku, String locationId, String tenantId,
                                      Integer quantity, String orderId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "STOCK_RESERVED");
        event.put("sku", sku);
        event.put("locationId", locationId);
        event.put("tenantId", tenantId);
        event.put("quantity", quantity);
        event.put("orderId", orderId);
        event.put("timestamp", LocalDateTime.now().toString());

        log.info("Publishing STOCK_RESERVED event for SKU: {} order: {}", sku, orderId);
        kafkaTemplate.send(STOCK_RESERVED_TOPIC, sku, event);
    }

    /**
     * Publish stock released event
     */
    public void publishStockReleased(String sku, String locationId, String tenantId,
                                      Integer quantity, String orderId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "STOCK_RELEASED");
        event.put("sku", sku);
        event.put("locationId", locationId);
        event.put("tenantId", tenantId);
        event.put("quantity", quantity);
        event.put("orderId", orderId);
        event.put("timestamp", LocalDateTime.now().toString());

        log.info("Publishing STOCK_RELEASED event for SKU: {} order: {}", sku, orderId);
        kafkaTemplate.send(STOCK_RELEASED_TOPIC, sku, event);
    }

    /**
     * Publish low stock alert event
     */
    public void publishLowStockAlert(String sku, String locationId, String tenantId,
                                      Integer availableQuantity, Integer reorderPoint) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "LOW_STOCK_ALERT");
        event.put("sku", sku);
        event.put("locationId", locationId);
        event.put("tenantId", tenantId);
        event.put("availableQuantity", availableQuantity);
        event.put("reorderPoint", reorderPoint);
        event.put("timestamp", LocalDateTime.now().toString());

        log.warn("Publishing LOW_STOCK_ALERT for SKU: {} at location: {} - Available: {}, Reorder Point: {}",
            sku, locationId, availableQuantity, reorderPoint);
        kafkaTemplate.send(LOW_STOCK_ALERT_TOPIC, sku, event);
    }

    public void publishStockAllocated(String tenantId, String orderId, String sku,
                                       Integer totalQuantity) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "STOCK_ALLOCATED");
        event.put("tenantId", tenantId);
        event.put("orderId", orderId);
        event.put("sku", sku);
        event.put("totalQuantity", totalQuantity);
        event.put("timestamp", LocalDateTime.now().toString());

        log.info("Publishing STOCK_ALLOCATED event for order: {}, SKU: {}", orderId, sku);
        kafkaTemplate.send(STOCK_ALLOCATED_TOPIC, orderId, event);
    }

    public void publishReservationExpired(String tenantId, String reservationId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "RESERVATION_EXPIRED");
        event.put("tenantId", tenantId);
        event.put("reservationId", reservationId);
        event.put("timestamp", LocalDateTime.now().toString());

        log.info("Publishing RESERVATION_EXPIRED event for reservation: {}", reservationId);
        kafkaTemplate.send(RESERVATION_EXPIRED_TOPIC, reservationId, event);
    }
}
