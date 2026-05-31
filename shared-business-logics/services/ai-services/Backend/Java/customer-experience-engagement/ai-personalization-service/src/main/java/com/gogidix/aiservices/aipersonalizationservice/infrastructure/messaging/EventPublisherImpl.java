package com.gogidix.aiservices.aipersonalizationservice.infrastructure.messaging;

import com.gogidix.aiservices.aipersonalizationservice.domain.port.out.EventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class EventPublisherImpl implements EventPublisherPort {

    @Override
    public void publish(String eventType, Object event) {
        publish(eventType, event, "personalization-events");
    }

    @Override
    public void publish(String eventType, Object event, String topic) {
        log.info("Publishing event {} to topic {}: {}", eventType, topic, event);
        // In production, this would publish to Kafka or another message broker
    }
}
