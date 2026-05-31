package com.gogidix.transaction.status.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.status.domain.entity.Subscriber;
import com.gogidix.transaction.status.domain.repository.SubscriberRepository;
import com.gogidix.transaction.status.service.StatusBroadcastService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class StatusWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(StatusWebSocketHandler.class);

    private final StatusBroadcastService broadcastService;
    private final SubscriberRepository subscriberRepository;
    private final ObjectMapper objectMapper;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket connection established: {}", session.getId());

        // Store session
        sessions.put(session.getId(), session);

        // Create subscriber record
        String userId = getUserIdFromSession(session);
        String transactionFilter = getTransactionFilterFromSession(session);

        Subscriber subscriber = Subscriber.builder()
            .userId(userId)
            .sessionId(session.getId())
            .connectionId(session.getId())
            .transactionFilter(transactionFilter)
            .status(Subscriber.SubscriptionStatus.ACTIVE)
            .build();

        subscriberRepository.save(subscriber);

        // Send welcome message
        Map<String, Object> welcomeMessage = Map.of(
            "type", "CONNECTED",
            "sessionId", session.getId(),
            "timestamp", LocalDateTime.now()
        );

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcomeMessage)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.debug("Received message from session {}: {}", session.getId(), message.getPayload());

        try {
            Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
            String action = (String) payload.get("action");

            switch (action) {
                case "SUBSCRIBE":
                    String transactionId = (String) payload.get("transactionId");
                    broadcastService.subscribeToTransaction(session.getId(), transactionId);
                    break;

                case "UNSUBSCRIBE":
                    transactionId = (String) payload.get("transactionId");
                    broadcastService.unsubscribeFromTransaction(session.getId(), transactionId);
                    break;

                case "PING":
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                        "type", "PONG",
                        "timestamp", LocalDateTime.now()
                    ))));
                    break;

                default:
                    log.warn("Unknown action: {}", action);
            }
        } catch (Exception e) {
            log.error("Error handling message", e);
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of(
                "type", "ERROR",
                "message", e.getMessage()
            ))));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed: {} - {}", session.getId(), status);

        sessions.remove(session.getId());

        // Update subscriber status
        subscriberRepository.findByConnectionId(session.getId()).ifPresent(subscriber -> {
            subscriber.setStatus(Subscriber.SubscriptionStatus.DISCONNECTED);
            subscriber.setDisconnectedAt(LocalDateTime.now());
            subscriberRepository.save(subscriber);
        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error for session: {}", session.getId(), exception);
        sessions.remove(session.getId());
    }

    public void broadcastToSession(String sessionId, String message) {
        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("Failed to send message to session: {}", sessionId, e);
            }
        }
    }

    public void broadcastToAll(String message) {
        sessions.forEach((id, session) -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    log.error("Failed to broadcast to session: {}", id, e);
                }
            }
        });
    }

    private String getUserIdFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }

    private String getTransactionFilterFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("transactionFilter");
    }
}
