package com.gogidix.aiservices.aiauthenticationservice.infrastructure.messaging;

import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.EventPublisherPort;
import org.springframework.context.ApplicationEventPublisher;

public class EventPublisherImpl implements EventPublisherPort {
    private final ApplicationEventPublisher publisher;

    public EventPublisherImpl(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(String eventType, Object event) {
        publisher.publishEvent(new AuthenticationEvent(eventType, event));
    }

    public record AuthenticationEvent(String type, Object data) {}
}
