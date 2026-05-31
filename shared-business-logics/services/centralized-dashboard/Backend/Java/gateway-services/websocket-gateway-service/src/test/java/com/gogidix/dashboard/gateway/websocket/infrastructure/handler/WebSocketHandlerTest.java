package com.gogidix.dashboard.gateway.websocket.infrastructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gogidix.dashboard.gateway.websocket.application.service.ConnectionRegistry;
import com.gogidix.dashboard.gateway.websocket.infrastructure.redis.RedisMessageSubscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WebSocketHandlerTest {

    @Mock
    private ConnectionRegistry connectionRegistry;

    @Mock
    private RedisMessageSubscriber redisMessageSubscriber;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @InjectMocks
    private WebSocketHandler webSocketHandler;

    @Mock
    private WebSocketSession session;

    @Test
    void afterConnectionEstablished_registersAndSendsWelcome() throws Exception {
        when(session.getId()).thenReturn("sess-1");
        when(session.getAttributes()).thenReturn(Map.of("tenantId", "tenant-1"));
        when(session.isOpen()).thenReturn(true);

        webSocketHandler.afterConnectionEstablished(session);

        verify(connectionRegistry).registerConnection("sess-1", "tenant-1", session);
        verify(session).sendMessage(any(TextMessage.class));
        assertEquals(1, webSocketHandler.getSessionCount());
    }

    @Test
    void afterConnectionEstablished_defaultTenant() throws Exception {
        when(session.getId()).thenReturn("sess-2");
        when(session.getAttributes()).thenReturn(Map.of());

        webSocketHandler.afterConnectionEstablished(session);

        verify(connectionRegistry).registerConnection("sess-2", "default", session);
    }

    @Test
    void handleTextMessage_subscribe() throws Exception {
        when(session.getId()).thenReturn("sess-1");
        when(session.getAttributes()).thenReturn(Map.of("tenantId", "t1"));
        when(session.isOpen()).thenReturn(true);

        String payload = objectMapper.writeValueAsString(Map.of(
                "type", "subscribe", "topic", "dashboard"));

        webSocketHandler.handleTextMessage(session, new TextMessage(payload));

        verify(connectionRegistry).subscribe("sess-1", "dashboard");
        verify(session, atLeastOnce()).sendMessage(any(TextMessage.class));
    }

    @Test
    void handleTextMessage_unsubscribe() throws Exception {
        when(session.getId()).thenReturn("sess-1");
        when(session.getAttributes()).thenReturn(Map.of("tenantId", "t1"));
        when(session.isOpen()).thenReturn(true);

        String payload = objectMapper.writeValueAsString(Map.of(
                "type", "unsubscribe", "topic", "dashboard"));

        webSocketHandler.handleTextMessage(session, new TextMessage(payload));

        verify(connectionRegistry).unsubscribe("sess-1", "dashboard");
    }

    @Test
    void handleTextMessage_ping() throws Exception {
        when(session.getId()).thenReturn("sess-1");
        when(session.getAttributes()).thenReturn(Map.of("tenantId", "t1"));
        when(session.isOpen()).thenReturn(true);

        String payload = objectMapper.writeValueAsString(Map.of("type", "ping"));

        webSocketHandler.handleTextMessage(session, new TextMessage(payload));

        verify(session, atLeastOnce()).sendMessage(argThat(msg ->
                ((String) msg.getPayload()).contains("pong")));
    }

    @Test
    void handleTextMessage_unknownType_sendsNoAction() throws Exception {
        when(session.getId()).thenReturn("sess-1");
        when(session.getAttributes()).thenReturn(Map.of("tenantId", "t1"));

        String payload = objectMapper.writeValueAsString(Map.of("type", "unknown-action"));

        webSocketHandler.handleTextMessage(session, new TextMessage(payload));

        verify(connectionRegistry, never()).subscribe(any(), any());
        verify(connectionRegistry, never()).unsubscribe(any(), any());
    }

    @Test
    void handleTextMessage_invalidJson_sendsError() throws Exception {
        when(session.getId()).thenReturn("sess-1");
        when(session.getAttributes()).thenReturn(Map.of("tenantId", "t1"));
        when(session.isOpen()).thenReturn(true);

        webSocketHandler.handleTextMessage(session, new TextMessage("not-json"));

        verify(session, atLeastOnce()).sendMessage(argThat(msg ->
                ((String) msg.getPayload()).contains("error")));
    }

    @Test
    void afterConnectionClosed_unregisters() throws Exception {
        when(session.getId()).thenReturn("sess-1");

        webSocketHandler.afterConnectionClosed(session, CloseStatus.NORMAL);

        verify(connectionRegistry).unregisterConnection("sess-1");
    }

    @Test
    void handleTransportError_unregisters() throws Exception {
        when(session.getId()).thenReturn("sess-1");

        webSocketHandler.handleTransportError(session, new RuntimeException("err"));

        verify(connectionRegistry).unregisterConnection("sess-1");
    }

    @Test
    void broadcastToTenant_sendsToOpenSessions() throws Exception {
        WebSocketSession openSession = mock(WebSocketSession.class);
        when(openSession.isOpen()).thenReturn(true);
        when(openSession.getId()).thenReturn("s1");
        when(connectionRegistry.getSessionsForTenant("t1"))
                .thenReturn(java.util.Set.of(openSession));

        com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage msg =
                com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage.builder()
                        .type("update").data(Map.of("k", "v")).build();

        webSocketHandler.broadcastToTenant("t1", msg);

        verify(openSession).sendMessage(any(TextMessage.class));
    }

    @Test
    void broadcastToTenant_skipsClosedSessions() throws Exception {
        WebSocketSession closedSession = mock(WebSocketSession.class);
        when(closedSession.isOpen()).thenReturn(false);
        when(connectionRegistry.getSessionsForTenant("t1"))
                .thenReturn(java.util.Set.of(closedSession));

        com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage msg =
                com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage.builder()
                        .type("update").build();

        webSocketHandler.broadcastToTenant("t1", msg);

        verify(closedSession, never()).sendMessage(any());
    }

    @Test
    void broadcastToTopic_sendsToSubscribers() throws Exception {
        WebSocketSession openSession = mock(WebSocketSession.class);
        when(openSession.isOpen()).thenReturn(true);
        when(connectionRegistry.getSessionsForTopic("dashboard"))
                .thenReturn(java.util.Set.of(openSession));

        com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage msg =
                com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage.builder()
                        .type("update").build();

        webSocketHandler.broadcastToTopic("dashboard", msg);

        verify(openSession).sendMessage(any(TextMessage.class));
    }

    @Test
    void getSessions_returnsActiveSessions() throws Exception {
        when(session.getId()).thenReturn("s1");
        when(session.getAttributes()).thenReturn(Map.of());
        webSocketHandler.afterConnectionEstablished(session);

        assertNotNull(webSocketHandler.getSessions());
        assertEquals(1, webSocketHandler.getSessionCount());
    }
}
