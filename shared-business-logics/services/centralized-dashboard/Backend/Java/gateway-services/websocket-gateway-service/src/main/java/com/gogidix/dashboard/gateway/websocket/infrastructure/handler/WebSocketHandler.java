package com.gogidix.dashboard.gateway.websocket.infrastructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage;
import com.gogidix.dashboard.gateway.websocket.application.service.ConnectionRegistry;
import com.gogidix.dashboard.gateway.websocket.infrastructure.redis.RedisMessageSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket handler for managing client connections.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ConnectionRegistry connectionRegistry;
    private final RedisMessageSubscriber redisMessageSubscriber;

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket connection established: {}", session.getId());
        sessions.add(session);

        String tenantId = getTenantId(session);
        String sessionId = session.getId();

        connectionRegistry.registerConnection(sessionId, tenantId, session);

        // Send welcome message
        WebSocketMessage welcome = WebSocketMessage.builder()
                .type("connected")
                .tenantId(tenantId)
                .data(Map.of("message", "Connected to WebSocket Gateway", "sessionId", sessionId))
                .timestamp(java.time.LocalDateTime.now())
                .build();

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcome)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.debug("Received message from {}: {}", session.getId(), payload);

        try {
            WebSocketMessage wsMessage = objectMapper.readValue(payload, WebSocketMessage.class);
            String tenantId = getTenantId(session);

            // Handle different message types
            switch (wsMessage.getType()) {
                case "subscribe":
                    connectionRegistry.subscribe(session.getId(), wsMessage.getTopic());
                    sendAck(session, "subscribed", wsMessage.getTopic());
                    break;

                case "unsubscribe":
                    connectionRegistry.unsubscribe(session.getId(), wsMessage.getTopic());
                    sendAck(session, "unsubscribed", wsMessage.getTopic());
                    break;

                case "ping":
                    sendMessage(session, WebSocketMessage.builder()
                            .type("pong")
                            .tenantId(tenantId)
                            .timestamp(java.time.LocalDateTime.now())
                            .build());
                    break;

                default:
                    log.warn("Unknown message type: {}", wsMessage.getType());
            }
        } catch (Exception e) {
            log.error("Error handling message", e);
            sendError(session, "Invalid message format");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed: {} - {}", session.getId(), status);
        sessions.remove(session);
        connectionRegistry.unregisterConnection(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error for session {}", session.getId(), exception);
        sessions.remove(session);
        connectionRegistry.unregisterConnection(session.getId());
    }

    /**
     * Broadcast message to all sessions for a tenant
     */
    public void broadcastToTenant(String tenantId, WebSocketMessage message) {
        connectionRegistry.getSessionsForTenant(tenantId).forEach(session -> {
            try {
                if (session.isOpen()) {
                    sendMessage(session, message);
                }
            } catch (Exception e) {
                log.error("Error broadcasting to session {}", session.getId(), e);
            }
        });
    }

    /**
     * Broadcast message to all subscribers of a topic
     */
    public void broadcastToTopic(String topic, WebSocketMessage message) {
        connectionRegistry.getSessionsForTopic(topic).forEach(session -> {
            try {
                if (session.isOpen()) {
                    sendMessage(session, message);
                }
            } catch (Exception e) {
                log.error("Error broadcasting to session {}", session.getId(), e);
            }
        });
    }

    /**
     * Send message to a specific session
     */
    private void sendMessage(WebSocketSession session, WebSocketMessage message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(json));
    }

    /**
     * Send acknowledgment message
     */
    private void sendAck(WebSocketSession session, String action, String topic) throws IOException {
        sendMessage(session, WebSocketMessage.builder()
                .type("ack")
                .data(Map.of("action", action, "topic", topic))
                .timestamp(java.time.LocalDateTime.now())
                .build());
    }

    /**
     * Send error message
     */
    private void sendError(WebSocketSession session, String error) {
        try {
            sendMessage(session, WebSocketMessage.builder()
                    .type("error")
                    .data(Map.of("error", error))
                    .timestamp(java.time.LocalDateTime.now())
                    .build());
        } catch (IOException e) {
            log.error("Error sending error message", e);
        }
    }

    /**
     * Get tenant ID from session
     */
    private String getTenantId(WebSocketSession session) {
        Object tenantId = session.getAttributes().get("tenantId");
        return tenantId != null ? tenantId.toString() : "default";
    }

    /**
     * Get all active sessions
     */
    public Set<WebSocketSession> getSessions() {
        return sessions;
    }

    /**
     * Get active session count
     */
    public int getSessionCount() {
        return sessions.size();
    }
}
