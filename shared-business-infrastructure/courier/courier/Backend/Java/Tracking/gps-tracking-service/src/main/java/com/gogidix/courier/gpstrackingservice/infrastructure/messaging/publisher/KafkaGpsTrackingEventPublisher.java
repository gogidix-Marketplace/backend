package com.gogidix.courier.gpstrackingservice.infrastructure.messaging.publisher;

import com.gogidix.courier.gpstrackingservice.application.service.GpsTrackingEventPublisher;
import com.gogidix.courier.gpstrackingservice.domain.event.DomainEvent;
import com.gogidix.courier.gpstrackingservice.domain.event.LocationUpdatedEvent;
import com.gogidix.courier.gpstrackingservice.domain.event.TrackingSessionEndedEvent;
import com.gogidix.courier.gpstrackingservice.domain.event.TrackingSessionStartedEvent;
import com.gogidix.courier.gpstrackingservice.infrastructure.messaging.event.GpsTrackingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka-based implementation of GpsTrackingEventPublisher.
 */
@Component
public class KafkaGpsTrackingEventPublisher implements GpsTrackingEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaGpsTrackingEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topicName;

    public KafkaGpsTrackingEventPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${spring.kafka.topic.gps-tracking-events:gps-tracking-events}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    @Transactional
    public void publish(DomainEvent event) {
        log.debug("Publishing event: {}", event.getEventType());

        GpsTrackingEvent externalEvent = toExternalEvent(event);
        String key = event.getTenantId();

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topicName, key, externalEvent);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish event: {}", event.getEventType(), ex);
            } else {
                log.debug("Event published successfully: {} to partition {}",
                        event.getEventType(), result.getRecordMetadata().partition());
            }
        });
    }

    @Override
    @Transactional
    public void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }

    /**
     * Convert domain event to external event format.
     */
    private GpsTrackingEvent toExternalEvent(DomainEvent event) {
        Map<String, Object> data = new HashMap<>();

        if (event instanceof LocationUpdatedEvent lee) {
            data.put("driverId", lee.getDriverId());
            data.put("latitude", lee.getLatitude());
            data.put("longitude", lee.getLongitude());
            data.put("altitude", lee.getAltitude());
            data.put("accuracy", lee.getAccuracy());
            data.put("speed", lee.getSpeed());
            data.put("heading", lee.getHeading());
        } else if (event instanceof TrackingSessionStartedEvent tsse) {
            data.put("sessionId", tsse.getSessionId());
            data.put("driverId", tsse.getDriverId());
            data.put("orderId", tsse.getOrderId());
        } else if (event instanceof TrackingSessionEndedEvent tsee) {
            data.put("sessionId", tsee.getSessionId());
            data.put("driverId", tsee.getDriverId());
            data.put("endReason", tsee.getEndReason());
        }

        return new GpsTrackingEvent(
                event.getEventId(),
                event.getEventType(),
                event.getAggregateId(),
                event.getTenantId(),
                event.getOccurredAt(),
                data
        );
    }
}
