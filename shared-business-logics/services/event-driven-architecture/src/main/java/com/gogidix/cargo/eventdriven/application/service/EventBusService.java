package com.gogidix.cargo.eventdriven.application.service;

import com.gogidix.cargo.eventdriven.domain.event.BaseDomainEvent;
import com.gogidix.cargo.eventdriven.domain.port.EventPublisherPort;
import com.gogidix.cargo.eventdriven.domain.port.EventStorePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class EventBusService {
    private static final Logger log = LoggerFactory.getLogger(EventBusService.class);
    private final EventPublisherPort publisher;
    private final EventStorePort eventStore;

    public EventBusService(EventPublisherPort publisher, EventStorePort eventStore) {
        this.publisher = publisher;
        this.eventStore = eventStore;
    }

    public void publish(String topic, String key, BaseDomainEvent event) {
        log.info("Publishing event: type={}, topic={}, key={}", event.getEventType(), topic, key);
        try {
            eventStore.store(event);
            publisher.publish(topic, key, event);
            log.debug("Event published successfully: eventId={}", event.getEventId());
        } catch (Exception e) {
            log.error("Failed to publish event: eventId={}, topic={}", event.getEventId(), topic, e);
            throw e;
        }
    }

    public void publish(String topic, BaseDomainEvent event) {
        publish(topic, event.getAggregateId(), event);
    }

    public Optional<BaseDomainEvent> findEvent(String eventId) {
        return eventStore.findById(eventId);
    }

    public List<BaseDomainEvent> findEventsByCorrelationId(String correlationId) {
        return eventStore.findByCorrelationId(correlationId);
    }

    public List<BaseDomainEvent> findEventsByAggregateId(String aggregateId) {
        return eventStore.findByAggregateId(aggregateId);
    }

    public List<BaseDomainEvent> findEventsByTenantId(String tenantId, int limit) {
        return eventStore.findByTenantId(tenantId, limit);
    }
}
