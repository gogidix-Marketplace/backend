package com.gogidix.dashboard.gateway.websocket.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionInfoTest {

    @Test
    void builderCreatesInstance() {
        LocalDateTime now = LocalDateTime.now();
        ConnectionInfo info = ConnectionInfo.builder()
                .id(1L)
                .sessionId("sess-1")
                .tenantId("tenant-1")
                .userId("user-1")
                .connectedAt(now)
                .lastHeartbeat(now)
                .status(ConnectionInfo.ConnectionStatus.CONNECTED)
                .subscriptions(new HashSet<>(Set.of("topic1", "topic2")))
                .remoteAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, info.getId());
        assertEquals("sess-1", info.getSessionId());
        assertEquals("tenant-1", info.getTenantId());
        assertEquals("user-1", info.getUserId());
        assertEquals(now, info.getConnectedAt());
        assertEquals(ConnectionInfo.ConnectionStatus.CONNECTED, info.getStatus());
        assertEquals(2, info.getSubscriptions().size());
        assertEquals("192.168.1.1", info.getRemoteAddress());
        assertEquals("Mozilla/5.0", info.getUserAgent());
    }

    @Test
    void noArgsConstructorCreatesInstance() {
        ConnectionInfo info = new ConnectionInfo();
        assertNotNull(info);
        assertNull(info.getId());
    }

    @Test
    void allArgsConstructorCreatesInstance() {
        ConnectionInfo info = new ConnectionInfo(1L, "s1", "t1", "u1",
                LocalDateTime.now(), null, LocalDateTime.now(),
                ConnectionInfo.ConnectionStatus.CONNECTED,
                new HashSet<>(), "addr", "agent",
                LocalDateTime.now(), LocalDateTime.now());
        assertEquals("s1", info.getSessionId());
    }

    @Test
    void settersWork() {
        ConnectionInfo info = new ConnectionInfo();
        info.setId(5L);
        info.setSessionId("sess-5");
        info.setTenantId("t2");
        info.setUserId("u2");
        LocalDateTime now = LocalDateTime.now();
        info.setConnectedAt(now);
        info.setDisconnectedAt(now);
        info.setLastHeartbeat(now);
        info.setStatus(ConnectionInfo.ConnectionStatus.DISCONNECTED);
        info.setSubscriptions(Set.of("a"));
        info.setRemoteAddress("10.0.0.1");
        info.setUserAgent("Test");

        assertEquals(5L, info.getId());
        assertEquals("sess-5", info.getSessionId());
        assertEquals(ConnectionInfo.ConnectionStatus.DISCONNECTED, info.getStatus());
        assertEquals(now, info.getDisconnectedAt());
    }

    @Test
    void addSubscription_whenSubscriptionsNull_initializesSet() {
        ConnectionInfo info = ConnectionInfo.builder().build();
        info.setSubscriptions(null);
        info.addSubscription("new-topic");
        assertTrue(info.getSubscriptions().contains("new-topic"));
    }

    @Test
    void addSubscription_addsToExistingSet() {
        ConnectionInfo info = ConnectionInfo.builder().build();
        info.addSubscription("topic-a");
        info.addSubscription("topic-b");
        assertEquals(2, info.getSubscriptions().size());
    }

    @Test
    void removeSubscription_removesFromSet() {
        ConnectionInfo info = ConnectionInfo.builder().build();
        info.addSubscription("topic-x");
        info.removeSubscription("topic-x");
        assertFalse(info.getSubscriptions().contains("topic-x"));
    }

    @Test
    void removeSubscription_whenNull_doesNothing() {
        ConnectionInfo info = ConnectionInfo.builder().build();
        info.setSubscriptions(null);
        assertDoesNotThrow(() -> info.removeSubscription("topic"));
    }

    @Test
    void connectionStatus_enumValues() {
        assertEquals(3, ConnectionInfo.ConnectionStatus.values().length);
        assertNotNull(ConnectionInfo.ConnectionStatus.valueOf("CONNECTED"));
        assertNotNull(ConnectionInfo.ConnectionStatus.valueOf("DISCONNECTED"));
        assertNotNull(ConnectionInfo.ConnectionStatus.valueOf("ERROR"));
    }

    @Test
    void builderDefaults() {
        ConnectionInfo info = ConnectionInfo.builder().build();
        assertEquals(ConnectionInfo.ConnectionStatus.CONNECTED, info.getStatus());
        assertNotNull(info.getSubscriptions());
        assertTrue(info.getSubscriptions().isEmpty());
    }

    @Test
    void equalsAndHashCode() {
        ConnectionInfo c1 = ConnectionInfo.builder().sessionId("s1").build();
        ConnectionInfo c2 = ConnectionInfo.builder().sessionId("s1").build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}
