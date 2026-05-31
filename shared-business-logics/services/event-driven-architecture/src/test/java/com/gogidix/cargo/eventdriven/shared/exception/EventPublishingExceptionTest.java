package com.gogidix.cargo.eventdriven.shared.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventPublishingExceptionTest {

    @Test
    void shouldCreateExceptionWithDetails() {
        RuntimeException cause = new RuntimeException("connection refused");
        EventPublishingException ex = new EventPublishingException("cargo.test.event", "evt-123", cause);
        assertEquals("cargo.test.event", ex.getTopic());
        assertEquals("evt-123", ex.getEventId());
        assertEquals(cause, ex.getCause());
        assertTrue(ex.getMessage().contains("evt-123"));
        assertTrue(ex.getMessage().contains("cargo.test.event"));
    }
}
