package com.gogidix.dashboard.gateway.websocket.application.service;

import com.gogidix.dashboard.gateway.websocket.domain.model.ConnectionInfo;
import com.gogidix.dashboard.gateway.websocket.domain.repository.ConnectionInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Registry for managing WebSocket connections.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionRegistry {

    private final ConnectionInfoRepository connectionInfoRepository;

    // In-memory storage for active sessions
    private final Map<String, WebSocketSession> sessionsById = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> sessionsByTenant = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> sessionsByTopic = new ConcurrentHashMap<>();

    /**
     * Register a new connection
     */
    public void registerConnection(String sessionId, String tenantId, WebSocketSession session) {
        sessionsById.put(sessionId, session);
        sessionsByTenant.computeIfAbsent(tenantId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);

        // Persist connection info
        ConnectionInfo connectionInfo = ConnectionInfo.builder()
                .sessionId(sessionId)
                .tenantId(tenantId)
                .connectedAt(LocalDateTime.now())
                .lastHeartbeat(LocalDateTime.now())
                .status(ConnectionInfo.ConnectionStatus.CONNECTED)
                .build();

        connectionInfoRepository.save(connectionInfo);
        log.debug("Registered connection: {} for tenant: {}", sessionId, tenantId);
    }

    /**
     * Unregister a connection
     */
    public void unregisterConnection(String sessionId) {
        WebSocketSession session = sessionsById.remove(sessionId);

        if (session != null) {
            // Find and update connection info
            connectionInfoRepository.findBySessionId(sessionId).ifPresent(info -> {
                info.setStatus(ConnectionInfo.ConnectionStatus.DISCONNECTED);
                info.setDisconnectedAt(LocalDateTime.now());
                connectionInfoRepository.save(info);
            });

            // Remove from tenant mapping
            sessionsByTenant.values().forEach(set -> set.remove(sessionId));

            // Remove from all topic subscriptions
            sessionsByTopic.values().forEach(set -> set.remove(sessionId));

            log.debug("Unregistered connection: {}", sessionId);
        }
    }

    /**
     * Subscribe a session to a topic
     */
    public void subscribe(String sessionId, String topic) {
        sessionsByTopic.computeIfAbsent(topic, k -> ConcurrentHashMap.newKeySet()).add(sessionId);

        // Update connection info
        connectionInfoRepository.findBySessionId(sessionId).ifPresent(info -> {
            info.addSubscription(topic);
            connectionInfoRepository.save(info);
        });

        log.debug("Session {} subscribed to topic: {}", sessionId, topic);
    }

    /**
     * Unsubscribe a session from a topic
     */
    public void unsubscribe(String sessionId, String topic) {
        Set<String> subscribers = sessionsByTopic.get(topic);
        if (subscribers != null) {
            subscribers.remove(sessionId);
        }

        // Update connection info
        connectionInfoRepository.findBySessionId(sessionId).ifPresent(info -> {
            info.removeSubscription(topic);
            connectionInfoRepository.save(info);
        });

        log.debug("Session {} unsubscribed from topic: {}", sessionId, topic);
    }

    /**
     * Get all sessions for a tenant
     */
    public Set<WebSocketSession> getSessionsForTenant(String tenantId) {
        Set<String> sessionIds = sessionsByTenant.getOrDefault(tenantId, Set.of());
        return sessionIds.stream()
                .map(sessionsById::get)
                .filter(session -> session != null && session.isOpen())
                .collect(Collectors.toSet());
    }

    /**
     * Get all sessions subscribed to a topic
     */
    public Set<WebSocketSession> getSessionsForTopic(String topic) {
        Set<String> sessionIds = sessionsByTopic.getOrDefault(topic, Set.of());
        return sessionIds.stream()
                .map(sessionsById::get)
                .filter(session -> session != null && session.isOpen())
                .collect(Collectors.toSet());
    }

    /**
     * Update heartbeat for a session
     */
    public void updateHeartbeat(String sessionId) {
        connectionInfoRepository.findBySessionId(sessionId).ifPresent(info -> {
            info.setLastHeartbeat(LocalDateTime.now());
            connectionInfoRepository.save(info);
        });
    }

    /**
     * Get connection statistics
     */
    public Map<String, Object> getStatistics() {
        return Map.of(
                "totalConnections", sessionsById.size(),
                "tenants", sessionsByTenant.size(),
                "topics", sessionsByTopic.size(),
                "timestamp", LocalDateTime.now()
        );
    }
}
