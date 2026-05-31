package com.gogidix.transaction.status.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Subscriber Tests")
class SubscriberTest {

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        Subscriber s = Subscriber.builder()
            .userId("user-1").sessionId("sess-1").connectionId("conn-1")
            .transactionFilter("tx-123").build();
        assertEquals("user-1", s.getUserId());
        assertEquals("sess-1", s.getSessionId());
        assertEquals("conn-1", s.getConnectionId());
        assertEquals("tx-123", s.getTransactionFilter());
        assertEquals(Subscriber.SubscriptionStatus.ACTIVE, s.getStatus());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        Subscriber s = new Subscriber();
        assertNull(s.getUserId());
        assertNotNull(s.getId());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        Subscriber s = new Subscriber();
        UUID id = UUID.randomUUID();
        s.setId(id); s.setUserId("u1"); s.setSessionId("s1");
        s.setConnectionId("c1"); s.setTransactionFilter("tf1");
        s.setStatus(Subscriber.SubscriptionStatus.PAUSED);
        s.setFilterExpression("expr"); s.setMetadata("meta");
        s.setLastActivity(LocalDateTime.now());
        s.setDisconnectedAt(LocalDateTime.now());
        assertEquals(id, s.getId());
        assertEquals("u1", s.getUserId());
        assertEquals(Subscriber.SubscriptionStatus.PAUSED, s.getStatus());
    }

    @Test
    @DisplayName("Should test SubscriptionStatus enum")
    void shouldTestEnum() {
        assertEquals(4, Subscriber.SubscriptionStatus.values().length);
    }

    @Test
    @DisplayName("Should isActive return true when ACTIVE")
    void shouldIsActiveReturnTrue() {
        Subscriber s = Subscriber.builder().status(Subscriber.SubscriptionStatus.ACTIVE).build();
        assertTrue(s.isActive());
    }

    @Test
    @DisplayName("Should isActive return false when not ACTIVE")
    void shouldIsActiveReturnFalse() {
        Subscriber s = Subscriber.builder().status(Subscriber.SubscriptionStatus.DISCONNECTED).build();
        assertFalse(s.isActive());
    }

    @Test
    @DisplayName("Should updateLastActivity")
    void shouldUpdateLastActivity() {
        Subscriber s = new Subscriber();
        assertNull(s.getLastActivity());
        s.updateLastActivity();
        assertNotNull(s.getLastActivity());
    }

    @Test
    @DisplayName("Should markAsDisconnected")
    void shouldMarkAsDisconnected() {
        Subscriber s = Subscriber.builder().build();
        s.markAsDisconnected();
        assertEquals(Subscriber.SubscriptionStatus.DISCONNECTED, s.getStatus());
        assertNotNull(s.getDisconnectedAt());
    }

    @Test
    @DisplayName("Should markAsExpired")
    void shouldMarkAsExpired() {
        Subscriber s = Subscriber.builder().build();
        s.markAsExpired();
        assertEquals(Subscriber.SubscriptionStatus.EXPIRED, s.getStatus());
        assertNotNull(s.getDisconnectedAt());
    }

    @Test
    @DisplayName("Should set connectedAt on onCreate")
    void shouldSetConnectedAtOnCreate() {
        Subscriber s = new Subscriber();
        s.onCreate();
        assertNotNull(s.getConnectedAt());
        assertEquals(Subscriber.SubscriptionStatus.ACTIVE, s.getStatus());
    }

    @Test
    @DisplayName("Should set disconnectedAt on onUpdate when DISCONNECTED")
    void shouldSetDisconnectedAtOnUpdate() {
        Subscriber s = new Subscriber();
        s.setStatus(Subscriber.SubscriptionStatus.DISCONNECTED);
        s.onUpdate();
        assertNotNull(s.getDisconnectedAt());
    }
}
