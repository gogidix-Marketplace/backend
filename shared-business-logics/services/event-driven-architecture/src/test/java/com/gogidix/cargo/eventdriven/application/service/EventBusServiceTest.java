package com.gogidix.cargo.eventdriven.application.service;

import com.gogidix.cargo.eventdriven.domain.event.BaseDomainEvent;
import com.gogidix.cargo.eventdriven.domain.port.EventPublisherPort;
import com.gogidix.cargo.eventdriven.domain.port.EventStorePort;
import com.gogidix.cargo.eventdriven.infrastructure.persistence.InMemoryEventStoreAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class EventBusServiceTest {
    private EventBusService eventBusService;
    private InMemoryEventStoreAdapter eventStore;

    static class TestEvent extends BaseDomainEvent {
        TestEvent(String tenantId, String correlationId) { super("TEST", tenantId, correlationId); }
        @Override public String getAggregateType() { return "TEST"; }
        @Override public String getAggregateId() { return "agg-1"; }
    }

    @BeforeEach
    void setUp() {
        eventStore = new InMemoryEventStoreAdapter();
        EventPublisherPort publisher = new EventPublisherPort() {
            @Override public void publish(String topic, String key, BaseDomainEvent event) {}
            @Override public void publish(String topic, BaseDomainEvent event) {}
        };
        eventBusService = new EventBusService(publisher, eventStore);
    }

    @Test
    void shouldPublishAndStoreEvent() {
        TestEvent event = new TestEvent("tenant-1", "corr-1");
        eventBusService.publish("test.topic", "key-1", event);
        assertTrue(eventStore.findById(event.getEventId()).isPresent());
    }

    @Test
    void shouldFindEventByCorrelationId() {
        TestEvent event = new TestEvent("tenant-1", "corr-abc");
        eventBusService.publish("test.topic", event);
        List<BaseDomainEvent> found = eventBusService.findEventsByCorrelationId("corr-abc");
        assertEquals(1, found.size());
    }

    @Test
    void shouldFindEventByTenantId() {
        TestEvent event = new TestEvent("tenant-x", "corr-1");
        eventBusService.publish("test.topic", event);
        List<BaseDomainEvent> found = eventBusService.findEventsByTenantId("tenant-x", 10);
        assertEquals(1, found.size());
    }

    @Test
    void shouldReturnEmptyForNonExistentEvent() {
        assertTrue(eventBusService.findEvent("nonexistent").isEmpty());
    }

    @Test
    void shouldPublishWithoutKey() {
        TestEvent event = new TestEvent("t1", "c1");
        eventBusService.publish("test.topic", event);
        assertTrue(eventStore.findById(event.getEventId()).isPresent());
    }
}
