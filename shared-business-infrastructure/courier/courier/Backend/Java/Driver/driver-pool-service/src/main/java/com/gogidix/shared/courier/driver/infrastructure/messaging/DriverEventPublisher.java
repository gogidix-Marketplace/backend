package com.gogidix.shared.courier.driver.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.courier.driver.domain.events.DriverCreatedEvent;
import com.gogidix.shared.courier.driver.domain.events.DriverLocationUpdatedEvent;
import com.gogidix.shared.courier.driver.domain.events.DriverStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Publisher for driver-related domain events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DriverEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DRIVER_CREATED_TOPIC = "driver.created";
    private static final String DRIVER_LOCATION_UPDATED_TOPIC = "driver.location.updated";
    private static final String DRIVER_STATUS_CHANGED_TOPIC = "driver.status.changed";

    public void publishDriverCreated(DriverCreatedEvent event) {
        try {
            event.setEventId(UUID.randomUUID().toString());
            event.setOccurredAt(LocalDateTime.now());
            kafkaTemplate.send(DRIVER_CREATED_TOPIC, event.getTenantId(), event);
            log.info("Published DriverCreatedEvent for driver: {}", event.getDriverId());
        } catch (Exception e) {
            log.error("Failed to publish DriverCreatedEvent", e);
        }
    }

    public void publishDriverLocationUpdated(DriverLocationUpdatedEvent event) {
        try {
            event.setEventId(UUID.randomUUID().toString());
            event.setOccurredAt(LocalDateTime.now());
            kafkaTemplate.send(DRIVER_LOCATION_UPDATED_TOPIC, event.getTenantId(), event);
            log.info("Published DriverLocationUpdatedEvent for driver: {}", event.getDriverId());
        } catch (Exception e) {
            log.error("Failed to publish DriverLocationUpdatedEvent", e);
        }
    }

    public void publishDriverStatusChanged(DriverStatusChangedEvent event) {
        try {
            event.setEventId(UUID.randomUUID().toString());
            event.setOccurredAt(LocalDateTime.now());
            kafkaTemplate.send(DRIVER_STATUS_CHANGED_TOPIC, event.getTenantId(), event);
            log.info("Published DriverStatusChangedEvent for driver: {}", event.getDriverId());
        } catch (Exception e) {
            log.error("Failed to publish DriverStatusChangedEvent", e);
        }
    }
}
