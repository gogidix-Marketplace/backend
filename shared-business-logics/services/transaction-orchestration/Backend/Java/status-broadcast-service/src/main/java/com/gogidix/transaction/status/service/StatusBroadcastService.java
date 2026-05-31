package com.gogidix.transaction.status.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.status.domain.entity.Subscriber;
import com.gogidix.transaction.status.domain.entity.Subscriber.SubscriptionStatus;
import com.gogidix.transaction.status.domain.repository.SubscriberRepository;
import com.gogidix.transaction.status.websocket.StatusWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class StatusBroadcastService {

    private static final Logger log = LoggerFactory.getLogger(StatusBroadcastService.class);

    private final SubscriberRepository subscriberRepository;
    private final StatusWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    private final Map<String, List<String>> transactionSubscriptions = new ConcurrentHashMap<>();

    // Kafka listener removed for local development
    // Status updates will need to be triggered manually via REST API in local mode

    @Transactional
    public void subscribeToTransaction(String sessionId, String transactionId) {
        log.info("Session {} subscribing to transaction: {}", sessionId, transactionId);

        transactionSubscriptions.computeIfAbsent(sessionId, k -> new java.util.ArrayList<>()).add(transactionId);

        // Update subscriber record
        subscriberRepository.findByConnectionId(sessionId).ifPresent(subscriber -> {
            subscriber.setTransactionFilter(transactionId);
            subscriber.setLastActivity(LocalDateTime.now());
            subscriberRepository.save(subscriber);
        });
    }

    @Transactional
    public void unsubscribeFromTransaction(String sessionId, String transactionId) {
        log.info("Session {} unsubscribing from transaction: {}", sessionId, transactionId);

        List<String> subscriptions = transactionSubscriptions.get(sessionId);
        if (subscriptions != null) {
            subscriptions.remove(transactionId);
        }
    }

    @Transactional
    public void broadcastStatusUpdate(String transactionId, String status, Map<String, Object> data) {
        log.debug("Broadcasting status update for transaction: {}, status: {}", transactionId, status);

        try {
            // Find all subscribers for this transaction
            List<Subscriber> subscribers = subscriberRepository.findActiveSubscribersByTransactionFilter(
                transactionId, SubscriptionStatus.ACTIVE);

            Map<String, Object> message = Map.of(
                "type", "STATUS_UPDATE",
                "transactionId", transactionId,
                "status", status,
                "data", data != null ? data : Map.of(),
                "timestamp", LocalDateTime.now().toString()
            );

            String messageJson = objectMapper.writeValueAsString(message);

            for (Subscriber subscriber : subscribers) {
                webSocketHandler.broadcastToSession(subscriber.getSessionId(), messageJson);
                // Update last activity
                subscriber.updateLastActivity();
                subscriberRepository.save(subscriber);
            }
        } catch (Exception e) {
            log.error("Failed to broadcast status update for transaction: {}", transactionId, e);
        }
    }

    @Transactional
    public void broadcastToAll(String messageType, Map<String, Object> data) {
        log.debug("Broadcasting message type: {} to all subscribers", messageType);

        try {
            List<Subscriber> activeSubscribers = subscriberRepository.findByStatus(SubscriptionStatus.ACTIVE);

            Map<String, Object> message = Map.of(
                "type", messageType,
                "data", data != null ? data : Map.of(),
                "timestamp", LocalDateTime.now().toString()
            );

            String messageJson = objectMapper.writeValueAsString(message);

            activeSubscribers.forEach(subscriber -> {
                webSocketHandler.broadcastToSession(subscriber.getSessionId(), messageJson);
                subscriber.updateLastActivity();
                subscriberRepository.save(subscriber);
            });
        } catch (Exception e) {
            log.error("Failed to broadcast message type: {}", messageType, e);
        }
    }

    @Transactional
    public void registerSubscriber(String sessionId, String userId, String connectionId) {
        log.info("Registering subscriber: sessionId={}, userId={}, connectionId={}", sessionId, userId, connectionId);

        Subscriber subscriber = Subscriber.builder()
            .userId(userId)
            .sessionId(sessionId)
            .connectionId(connectionId)
            .status(SubscriptionStatus.ACTIVE)
            .lastActivity(LocalDateTime.now())
            .build();

        subscriberRepository.save(subscriber);
    }

    @Transactional
    public void unregisterSubscriber(String connectionId) {
        log.info("Unregistering subscriber: connectionId={}", connectionId);

        subscriberRepository.findByConnectionId(connectionId).ifPresent(subscriber -> {
            subscriber.markAsDisconnected();
            subscriberRepository.save(subscriber);

            // Remove from in-memory subscriptions
            transactionSubscriptions.remove(subscriber.getSessionId());
        });
    }

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void cleanupInactiveSubscribers() {
        log.debug("Cleaning up inactive subscribers");

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);
        List<Subscriber> inactiveSubscribers = subscriberRepository.findInactiveSubscribers(
            SubscriptionStatus.ACTIVE, threshold);

        inactiveSubscribers.forEach(subscriber -> {
            subscriber.markAsExpired();
            subscriberRepository.save(subscriber);

            transactionSubscriptions.remove(subscriber.getSessionId());
        });

        log.info("Cleaned up {} inactive subscribers", inactiveSubscribers.size());
    }

    @Transactional(readOnly = true)
    public long getActiveSubscriberCount() {
        return subscriberRepository.countByStatus(SubscriptionStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Subscriber> getActiveSubscribers() {
        return subscriberRepository.findByStatus(SubscriptionStatus.ACTIVE);
    }
}
