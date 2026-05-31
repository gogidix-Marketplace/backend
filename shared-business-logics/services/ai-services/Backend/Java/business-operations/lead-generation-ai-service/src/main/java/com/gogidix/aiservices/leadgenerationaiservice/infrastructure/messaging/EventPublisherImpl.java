package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.messaging;

import com.gogidix.aiservices.leadgenerationaiservice.domain.port.out.EventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class EventPublisherImpl implements EventPublisherPort {

    @Override
    public void publish(String eventType, Map<String, Object> payload) {
        log.info("Publishing event: {} with payload: {}", eventType, payload);
        // In production, this would publish to Kafka or another message broker
    }

    @Override
    public void publish(String eventType, Object payload) {
        log.info("Publishing event: {} with payload: {}", eventType, payload);
    }
}
