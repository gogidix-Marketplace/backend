package com.gogidix.shared.warehousing.availability.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Availability Event Publisher
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AvailabilityEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String CAPACITY_RESERVED_TOPIC = "capacity-reserved";
    private static final String CAPACITY_RELEASED_TOPIC = "capacity-released";
    private static final String AVAILABILITY_UPDATED_TOPIC = "availability-updated";

    public void publishCapacityReserved(String tenantId, String warehouseId, String resourceId,
                                       String reservationId, Integer quantity) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "CAPACITY_RESERVED");
        event.put("tenantId", tenantId);
        event.put("warehouseId", warehouseId);
        event.put("resourceId", resourceId);
        event.put("reservationId", reservationId);
        event.put("quantity", quantity);
        event.put("timestamp", LocalDateTime.now());

        try {
            kafkaTemplate.send(CAPACITY_RESERVED_TOPIC, reservationId, event);
            log.info("Published capacity reserved event: {}", reservationId);
        } catch (Exception e) {
            log.error("Failed to publish capacity reserved event", e);
        }
    }

    public void publishCapacityReleased(String tenantId, String warehouseId, String resourceId,
                                       String reservationId, Integer quantity) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "CAPACITY_RELEASED");
        event.put("tenantId", tenantId);
        event.put("warehouseId", warehouseId);
        event.put("resourceId", resourceId);
        event.put("reservationId", reservationId);
        event.put("quantity", quantity);
        event.put("timestamp", LocalDateTime.now());

        try {
            kafkaTemplate.send(CAPACITY_RELEASED_TOPIC, reservationId, event);
            log.info("Published capacity released event: {}", reservationId);
        } catch (Exception e) {
            log.error("Failed to publish capacity released event", e);
        }
    }

    public void publishAvailabilityUpdated(String tenantId, String warehouseId, String zoneId,
                                          Integer availableCapacity, String status) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "AVAILABILITY_UPDATED");
        event.put("tenantId", tenantId);
        event.put("warehouseId", warehouseId);
        event.put("zoneId", zoneId);
        event.put("availableCapacity", availableCapacity);
        event.put("status", status);
        event.put("timestamp", LocalDateTime.now());

        try {
            kafkaTemplate.send(AVAILABILITY_UPDATED_TOPIC, zoneId, event);
            log.info("Published availability updated event: {}", zoneId);
        } catch (Exception e) {
            log.error("Failed to publish availability updated event", e);
        }
    }
}
