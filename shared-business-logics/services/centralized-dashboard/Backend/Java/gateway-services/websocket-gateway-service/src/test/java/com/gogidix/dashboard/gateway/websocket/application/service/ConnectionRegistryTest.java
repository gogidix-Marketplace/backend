package com.gogidix.dashboard.gateway.websocket.application.service;

import com.gogidix.dashboard.gateway.websocket.domain.model.ConnectionInfo;
import com.gogidix.dashboard.gateway.websocket.domain.repository.ConnectionInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ConnectionRegistryTest {

    @Mock
    private ConnectionInfoRepository connectionInfoRepository;

    @InjectMocks
    private ConnectionRegistry connectionRegistry;

    @Mock
    private WebSocketSession session;

    @Test
    void registerConnection_savesToDb() {
        when(session.getId()).thenReturn("sess-1");
        when(connectionInfoRepository.save(any(ConnectionInfo.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        connectionRegistry.registerConnection("sess-1", "tenant-1", session);

        ArgumentCaptor<ConnectionInfo> captor = ArgumentCaptor.forClass(ConnectionInfo.class);
        verify(connectionInfoRepository).save(captor.capture());
        assertEquals("sess-1", captor.getValue().getSessionId());
        assertEquals("tenant-1", captor.getValue().getTenantId());
        assertEquals(ConnectionInfo.ConnectionStatus.CONNECTED, captor.getValue().getStatus());
    }

    @Test
    void unregisterConnection_whenSessionExists_updatesDb() {
        when(session.getId()).thenReturn("sess-1");
        when(session.isOpen()).thenReturn(true);
        when(connectionInfoRepository.save(any(ConnectionInfo.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        connectionRegistry.registerConnection("sess-1", "tenant-1", session);

        ConnectionInfo dbInfo = ConnectionInfo.builder()
                .sessionId("sess-1").status(ConnectionInfo.ConnectionStatus.CONNECTED).build();
        when(connectionInfoRepository.findBySessionId("sess-1"))
                .thenReturn(Optional.of(dbInfo));

        connectionRegistry.unregisterConnection("sess-1");

        verify(connectionInfoRepository, times(2)).save(any(ConnectionInfo.class));
        assertEquals(ConnectionInfo.ConnectionStatus.DISCONNECTED, dbInfo.getStatus());
        assertNotNull(dbInfo.getDisconnectedAt());
    }

    @Test
    void unregisterConnection_whenSessionNotExists_doesNothing() {
        connectionRegistry.unregisterConnection("unknown-session");
        verify(connectionInfoRepository, never()).save(any());
    }

    @Test
    void subscribe_updatesSubscription() {
        when(connectionInfoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ConnectionInfo info = ConnectionInfo.builder().sessionId("s1").build();
        when(connectionInfoRepository.findBySessionId("s1")).thenReturn(Optional.of(info));

        connectionRegistry.subscribe("s1", "topic-1");

        assertTrue(info.getSubscriptions().contains("topic-1"));
        verify(connectionInfoRepository).save(info);
    }

    @Test
    void subscribe_whenNoDbEntry_doesNotSave() {
        when(connectionInfoRepository.findBySessionId("s1")).thenReturn(Optional.empty());

        connectionRegistry.subscribe("s1", "topic-1");

        verify(connectionInfoRepository, never()).save(any());
    }

    @Test
    void unsubscribe_removesSubscription() {
        ConnectionInfo info = ConnectionInfo.builder().sessionId("s1").build();
        info.addSubscription("topic-1");
        when(connectionInfoRepository.findBySessionId("s1")).thenReturn(Optional.of(info));

        connectionRegistry.unsubscribe("s1", "topic-1");

        assertFalse(info.getSubscriptions().contains("topic-1"));
        verify(connectionInfoRepository).save(info);
    }

    @Test
    void unsubscribe_whenNoSubscribers_doesNothing() {
        connectionRegistry.unsubscribe("s1", "nonexistent");
        verify(connectionInfoRepository, never()).save(any());
    }

    @Test
    void getSessionsForTenant_returnsOpenSessions() {
        when(session.getId()).thenReturn("sess-1");
        when(session.isOpen()).thenReturn(true);
        when(connectionInfoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        connectionRegistry.registerConnection("sess-1", "tenant-1", session);

        Set<WebSocketSession> result = connectionRegistry.getSessionsForTenant("tenant-1");

        assertEquals(1, result.size());
        assertTrue(result.contains(session));
    }

    @Test
    void getSessionsForTenant_whenNoSessions_returnsEmpty() {
        Set<WebSocketSession> result = connectionRegistry.getSessionsForTenant("unknown");
        assertTrue(result.isEmpty());
    }

    @Test
    void getSessionsForTopic_returnsSubscribedSessions() {
        when(session.getId()).thenReturn("sess-1");
        when(session.isOpen()).thenReturn(true);
        when(connectionInfoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(connectionInfoRepository.findBySessionId("sess-1")).thenReturn(Optional.empty());

        connectionRegistry.registerConnection("sess-1", "tenant-1", session);
        connectionRegistry.subscribe("sess-1", "topic-1");

        Set<WebSocketSession> result = connectionRegistry.getSessionsForTopic("topic-1");

        assertEquals(1, result.size());
    }

    @Test
    void updateHeartbeat_updatesTimestamp() {
        ConnectionInfo info = ConnectionInfo.builder().sessionId("s1").build();
        when(connectionInfoRepository.findBySessionId("s1")).thenReturn(Optional.of(info));

        connectionRegistry.updateHeartbeat("s1");

        assertNotNull(info.getLastHeartbeat());
        verify(connectionInfoRepository).save(info);
    }

    @Test
    void updateHeartbeat_whenNotFound_doesNothing() {
        when(connectionInfoRepository.findBySessionId("s1")).thenReturn(Optional.empty());
        connectionRegistry.updateHeartbeat("s1");
        verify(connectionInfoRepository, never()).save(any());
    }

    @Test
    void getStatistics_returnsCorrectMap() {
        when(session.getId()).thenReturn("sess-1");
        when(connectionInfoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        connectionRegistry.registerConnection("sess-1", "tenant-1", session);

        Map<String, Object> stats = connectionRegistry.getStatistics();

        assertEquals(1, stats.get("totalConnections"));
        assertEquals(1, stats.get("tenants"));
    }
}
