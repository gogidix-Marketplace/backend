package com.gogidix.shared.courier.ecommerce.infrastructure.messaging.producers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.gogidix.shared.courier.ecommerce.domain.events.CourierAssignedEvent;
import com.gogidix.shared.courier.ecommerce.domain.events.CourierAtHubEvent;
import com.gogidix.shared.courier.ecommerce.domain.events.CourierDeliveredEvent;
import com.gogidix.shared.courier.ecommerce.domain.events.CourierPickedUpEvent;
import com.gogidix.shared.courier.ecommerce.domain.events.CourierStatusChangedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EcommerceCourierEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EcommerceCourierEventPublisher.class);

    private static final String ASSIGNED_TOPIC = "ecommerce.courier.assigned";
    private static final String PICKED_UP_TOPIC = "ecommerce.courier.picked_up";
    private static final String DELIVERED_TOPIC = "ecommerce.courier.delivered";
    private static final String AT_HUB_TOPIC = "ecommerce.courier.at_hub";
    private static final String STATUS_CHANGED_TOPIC = "ecommerce.courier.status_changed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCourierAssigned(CourierAssignedEvent event) {
        log.info("Publishing courier assigned event for order {}", event.getOrderId());
        kafkaTemplate.send(ASSIGNED_TOPIC, event.getOrderId(), event);
    }

    public void publishCourierPickedUp(CourierPickedUpEvent event) {
        log.info("Publishing courier picked up event for order {}", event.getOrderId());
        kafkaTemplate.send(PICKED_UP_TOPIC, event.getOrderId(), event);
    }

    public void publishCourierDelivered(CourierDeliveredEvent event) {
        log.info("Publishing courier delivered event for order {}", event.getOrderId());
        kafkaTemplate.send(DELIVERED_TOPIC, event.getOrderId(), event);
    }

    public void publishCourierAtHub(CourierAtHubEvent event) {
        log.info("Publishing courier at hub event for order {}", event.getOrderId());
        kafkaTemplate.send(AT_HUB_TOPIC, event.getOrderId(), event);
    }

    public void publishStatusChanged(CourierStatusChangedEvent event) {
        log.info("Publishing status changed event for order {}: {} -> {}",
            event.getOrderId(), event.getPreviousStatus(), event.getNewStatus());
        kafkaTemplate.send(STATUS_CHANGED_TOPIC, event.getOrderId(), event);
    }
}
