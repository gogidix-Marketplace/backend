package com.gogidix.platform.realtime.interfaces.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket handler for real-time communication.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RealTimeWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        sessions.put(userId, session);
        log.info("WebSocket connection established for user: {}", userId);
        session.sendMessage(new TextMessage(
                objectMapper.writeValueAsString(Map.of("type", "connected", "userId", userId))));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.debug("Received WebSocket message: {}", payload);

        // Echo back for testing
        session.sendMessage(new TextMessage(
                objectMapper.writeValueAsString(Map.of("type", "echo", "data", payload))));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        sessions.remove(userId);
        log.info("WebSocket connection closed for user: {}, status: {}", userId, status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error for session: {}", session.getId(), exception);
    }

    private String getUserIdFromSession(WebSocketSession session) {
        return session.getUri().getQuery().split("userId=")[1];
    }
}
