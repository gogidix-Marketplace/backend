package com.gogidix.centralizeddashboard.realtime.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RealTimeDataEventTest {

    @Test
    void noArgsConstructor_createsInstance() {
        RealTimeDataEvent event = new RealTimeDataEvent();
        assertNotNull(event);
        assertFalse(event.getProcessed());
    }

    @Test
    void builder_createsEvent() {
        LocalDateTime now = LocalDateTime.now();
        RealTimeDataEvent event = RealTimeDataEvent.builder()
                .id("id1")
                .eventId("evt-001")
                .source("courier-service")
                .eventType("ORDER_CREATED")
                .data("{\"orderId\":123}")
                .userId("user-1")
                .sessionId("sess-1")
                .value(99.99)
                .unit("USD")
                .createdAt(now)
                .processed(true)
                .build();

        assertEquals("id1", event.getId());
        assertEquals("evt-001", event.getEventId());
        assertEquals("courier-service", event.getSource());
        assertEquals("ORDER_CREATED", event.getEventType());
        assertEquals("{\"orderId\":123}", event.getData());
        assertEquals("user-1", event.getUserId());
        assertEquals("sess-1", event.getSessionId());
        assertEquals(99.99, event.getValue());
        assertEquals("USD", event.getUnit());
        assertEquals(now, event.getCreatedAt());
        assertTrue(event.getProcessed());
    }

    @Test
    void allArgsConstructor_works() {
        RealTimeDataEvent event = new RealTimeDataEvent("id", "eid", "src", "type", "data", "uid", "sid", 1.0, "u", LocalDateTime.now(), true);
        assertEquals("id", event.getId());
    }

    @Test
    void setters_work() {
        RealTimeDataEvent event = new RealTimeDataEvent();
        event.setId("new-id");
        event.setEventId("new-eid");
        event.setSource("new-src");
        event.setEventType("NEW_TYPE");
        event.setData("new-data");
        event.setUserId("new-uid");
        event.setSessionId("new-sid");
        event.setValue(42.0);
        event.setUnit("EUR");
        event.setProcessed(true);

        assertEquals("new-id", event.getId());
        assertEquals("new-eid", event.getEventId());
        assertEquals("new-src", event.getSource());
        assertEquals("NEW_TYPE", event.getEventType());
        assertEquals("new-data", event.getData());
        assertEquals(42.0, event.getValue());
        assertTrue(event.getProcessed());
    }

    @Test
    void defaultProcessed_isFalse() {
        RealTimeDataEvent event = RealTimeDataEvent.builder().build();
        assertFalse(event.getProcessed());
    }
}
