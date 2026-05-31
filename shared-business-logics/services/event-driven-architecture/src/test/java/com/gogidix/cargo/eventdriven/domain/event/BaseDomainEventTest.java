package com.gogidix.cargo.eventdriven.domain.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaseDomainEventTest {

    static class TestEvent extends BaseDomainEvent {
        private final String data;
        TestEvent(String eventType, String tenantId, String correlationId, String data) {
            super(eventType, tenantId, correlationId);
            this.data = data;
        }
        @Override public String getAggregateType() { return "TEST"; }
        @Override public String getAggregateId() { return "test-123"; }
        public String getData() { return data; }
    }

    @Test
    void shouldCreateEventWithAllFields() {
        TestEvent event = new TestEvent("TEST_EVENT", "tenant-1", "corr-1", "payload");
        assertNotNull(event.getEventId());
        assertNotNull(event.getTimestamp());
        assertEquals("TEST_EVENT", event.getEventType());
        assertEquals("tenant-1", event.getTenantId());
        assertEquals("corr-1", event.getCorrelationId());
        assertEquals("payload", event.getData());
    }

    @Test
    void shouldCreateEventWithEventTypeOnly() {
        TestEvent event = new TestEvent("SIMPLE", null, null, null);
        assertNotNull(event.getEventId());
        assertNull(event.getTenantId());
        assertNull(event.getCorrelationId());
    }

    @Test
    void shouldSetSource() {
        TestEvent event = new TestEvent("TEST", null, null, null);
        event.setSource("test-service");
        assertEquals("test-service", event.getSource());
    }

    @Test
    void shouldGenerateUniqueEventIds() {
        TestEvent event1 = new TestEvent("TEST", null, null, null);
        TestEvent event2 = new TestEvent("TEST", null, null, null);
        assertNotEquals(event1.getEventId(), event2.getEventId());
    }

    @Test
    void shouldImplementAggregateMethods() {
        TestEvent event = new TestEvent("TEST", null, null, null);
        assertEquals("TEST", event.getAggregateType());
        assertEquals("test-123", event.getAggregateId());
    }
}
