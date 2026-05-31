package com.gogidix.shared.warehousing.shipping.infrastructure.messaging;

import com.gogidix.shared.warehousing.shipping.domain.events.ShipmentCreatedEvent;
import com.gogidix.shared.warehousing.shipping.domain.events.ShipmentUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShippingEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.shipment-created:shipment-created}")
    private String shipmentCreatedTopic;

    @Value("${kafka.topic.shipment-updated:shipment-updated}")
    private String shipmentUpdatedTopic;

    public void publishShipmentCreated(ShipmentCreatedEvent event) {
        log.info("Publishing shipment created event: {}", event.getEventId());
        kafkaTemplate.send(shipmentCreatedTopic, event.getShipmentId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish shipment created event: {}", ex.getMessage());
                }
            });
    }

    public void publishShipmentUpdated(ShipmentUpdatedEvent event) {
        log.info("Publishing shipment updated event: {}", event.getEventId());
        kafkaTemplate.send(shipmentUpdatedTopic, event.getShipmentId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish shipment updated event: {}", ex.getMessage());
                }
            });
    }
}
