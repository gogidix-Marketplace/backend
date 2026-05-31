package com.gogidix.universal.tracking.domain;

import com.gogidix.universal.tracking.domain.model.TrackingEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TrackingEvent domain model.
 */
class TrackingEventTest {

    @Test
    void testCreateTrackingEvent() {
        TrackingEvent event = TrackingEvent.builder()
            .id(UUID.randomUUID())
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .userId("user-456")
            .source("WEB")
            .timestamp(LocalDateTime.now())
            .eventName("Homepage Visit")
            .tenantId("tenant-1")
            .priority(5)
            .processed(false)
            .build();

        assertNotNull(event);
        assertEquals("PAGE_VIEW", event.getEventType());
        assertEquals("session-123", event.getSessionId());
        assertEquals("user-456", event.getUserId());
        assertEquals("WEB", event.getSource());
        assertFalse(event.isProcessed());
        assertEquals(5, event.getPriority());
    }

    @Test
    void testMarkAsProcessed() {
        TrackingEvent event = TrackingEvent.builder()
            .id(UUID.randomUUID())
            .eventType("CLICK")
            .tenantId("tenant-1")
            .processed(false)
            .build();

        assertFalse(event.isProcessed());
        assertNull(event.getProcessedAt());

        event.markAsProcessed();

        assertTrue(event.isProcessed());
        assertNotNull(event.getProcessedAt());
    }

    @Test
    void testMarkAsFailed() {
        TrackingEvent event = TrackingEvent.builder()
            .id(UUID.randomUUID())
            .eventType("CLICK")
            .tenantId("tenant-1")
            .processed(false)
            .build();

        event.markAsFailed("Processing failed");

        assertFalse(event.isProcessed());
        assertEquals("Processing failed", event.getErrorMessage());
    }
}
