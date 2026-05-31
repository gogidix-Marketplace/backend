package com.gogidix.universal.tracking.domain;

import com.gogidix.universal.tracking.domain.model.TrackingSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TrackingSession domain model.
 */
class TrackingSessionTest {

    @Test
    void testCreateTrackingSession() {
        TrackingSession session = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .userId("user-456")
            .tenantId("tenant-1")
            .source("WEB")
            .startedAt(LocalDateTime.now())
            .lastActivityAt(LocalDateTime.now())
            .eventCount(0)
            .pageViewCount(0)
            .isActive(true)
            .build();

        assertNotNull(session);
        assertEquals("session-123", session.getSessionId());
        assertEquals("user-456", session.getUserId());
        assertEquals("tenant-1", session.getTenantId());
        assertEquals("WEB", session.getSource());
        assertTrue(session.isActiveSession());
        assertEquals(0, session.getEventCount());
        assertEquals(0, session.getPageViewCount());
    }

    @Test
    void testIsActiveSession() {
        TrackingSession session = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .tenantId("tenant-1")
            .isActive(true)
            .startedAt(LocalDateTime.now())
            .lastActivityAt(LocalDateTime.now())
            .build();

        assertTrue(session.isActiveSession());

        session.setIsActive(false);
        assertFalse(session.isActiveSession());

        session.setIsActive(true);
        session.setEndedAt(LocalDateTime.now());
        assertFalse(session.isActiveSession());
    }

    @Test
    void testIsTimedOut() {
        TrackingSession session = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .tenantId("tenant-1")
            .isActive(true)
            .startedAt(LocalDateTime.now())
            .lastActivityAt(LocalDateTime.now().minusMinutes(35))
            .build();

        assertTrue(session.isTimedOut(30));

        session.setLastActivityAt(LocalDateTime.now().minusMinutes(25));
        assertFalse(session.isTimedOut(30));
    }

    @Test
    void testEndSession() {
        TrackingSession session = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .tenantId("tenant-1")
            .isActive(true)
            .startedAt(LocalDateTime.now().minusMinutes(10))
            .lastActivityAt(LocalDateTime.now())
            .build();

        assertTrue(session.isActiveSession());
        assertNull(session.getEndedAt());
        assertNull(session.getDurationSeconds());

        session.endSession();

        assertFalse(session.isActiveSession());
        assertNotNull(session.getEndedAt());
        assertNotNull(session.getDurationSeconds());
    }

    @Test
    void testIncrementEventCount() {
        TrackingSession session = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .tenantId("tenant-1")
            .eventCount(0)
            .build();

        assertEquals(0, session.getEventCount());

        session.incrementEventCount();
        assertEquals(1, session.getEventCount());

        session.incrementEventCount();
        assertEquals(2, session.getEventCount());
    }

    @Test
    void testIncrementPageViewCount() {
        TrackingSession session = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .tenantId("tenant-1")
            .pageViewCount(0)
            .build();

        assertEquals(0, session.getPageViewCount());

        session.incrementPageViewCount();
        assertEquals(1, session.getPageViewCount());

        session.incrementPageViewCount();
        assertEquals(2, session.getPageViewCount());
    }
}
