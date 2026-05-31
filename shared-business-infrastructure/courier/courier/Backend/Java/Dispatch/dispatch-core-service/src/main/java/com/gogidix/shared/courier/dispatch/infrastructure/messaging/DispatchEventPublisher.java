package com.gogidix.shared.courier.dispatch.infrastructure.messaging;

import com.gogidix.shared.courier.dispatch.domain.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publisher for dispatch-related domain events
 * Publishes events to Kafka topics for downstream processing
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DispatchEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String DISPATCH_CREATED_TOPIC = "dispatch.order.created";
    private static final String STATUS_CHANGED_TOPIC = "dispatch.status.changed";
    private static final String DRIVER_ASSIGNED_TOPIC = "dispatch.driver.assigned";
    private static final String DISPATCH_ASSIGNED_TOPIC = "dispatch.assigned";
    private static final String DISPATCH_CANCELLED_TOPIC = "dispatch.cancelled";
    private static final String DISPATCH_COMPLETED_TOPIC = "dispatch.completed";

    public void publishDispatchCreated(DispatchOrderCreatedEvent event) {
        try {
            kafkaTemplate.send(DISPATCH_CREATED_TOPIC, event.getTenantId(), event);
            log.info("Published DispatchOrderCreatedEvent for dispatch: {}", event.getDispatchId());
        } catch (Exception e) {
            log.error("Failed to publish DispatchOrderCreatedEvent for dispatch: {}", event.getDispatchId(), e);
        }
    }

    public void publishDispatchAssigned(DispatchAssignedEvent event) {
        try {
            kafkaTemplate.send(DISPATCH_ASSIGNED_TOPIC, event.getTenantId(), event);
            log.info("Published DispatchAssignedEvent for dispatch: {} - driver: {}",
                    event.getDispatchId(), event.getDriverId());
        } catch (Exception e) {
            log.error("Failed to publish DispatchAssignedEvent for dispatch: {}", event.getDispatchId(), e);
        }
    }

    public void publishDispatchCancelled(DispatchCancelledEvent event) {
        try {
            kafkaTemplate.send(DISPATCH_CANCELLED_TOPIC, event.getTenantId(), event);
            log.info("Published DispatchCancelledEvent for dispatch: {}", event.getDispatchId());
        } catch (Exception e) {
            log.error("Failed to publish DispatchCancelledEvent for dispatch: {}", event.getDispatchId(), e);
        }
    }

    public void publishDispatchCompleted(DispatchCompletedEvent event) {
        try {
            kafkaTemplate.send(DISPATCH_COMPLETED_TOPIC, event.getTenantId(), event);
            log.info("Published DispatchCompletedEvent for dispatch: {}", event.getDispatchId());
        } catch (Exception e) {
            log.error("Failed to publish DispatchCompletedEvent for dispatch: {}", event.getDispatchId(), e);
        }
    }

    public void publishStatusChanged(DispatchStatusChangedEvent event) {
        try {
            kafkaTemplate.send(STATUS_CHANGED_TOPIC, event.getTenantId(), event);
            log.info("Published DispatchStatusChangedEvent for dispatch: {} - {} -> {}",
                    event.getDispatchId(), event.getOldStatus(), event.getNewStatus());
        } catch (Exception e) {
            log.error("Failed to publish DispatchStatusChangedEvent for dispatch: {}", event.getDispatchId(), e);
        }
    }

    public void publishDriverAssigned(DriverAssignedEvent event) {
        try {
            kafkaTemplate.send(DRIVER_ASSIGNED_TOPIC, event.getTenantId(), event);
            log.info("Published DriverAssignedEvent for dispatch: {} - driver: {}",
                    event.getDispatchId(), event.getDriverId());
        } catch (Exception e) {
            log.error("Failed to publish DriverAssignedEvent for dispatch: {}", event.getDispatchId(), e);
        }
    }
}
