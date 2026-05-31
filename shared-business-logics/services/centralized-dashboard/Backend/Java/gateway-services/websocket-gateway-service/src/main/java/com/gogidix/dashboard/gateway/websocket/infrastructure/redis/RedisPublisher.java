package com.gogidix.dashboard.gateway.websocket.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis message publisher for broadcasting messages.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Publish message to Redis channel
     */
    public void publish(String channel, WebSocketMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(channel, json);
            log.debug("Published message to channel: {}", channel);
        } catch (Exception e) {
            log.error("Error publishing message to Redis", e);
        }
    }

    /**
     * Publish dashboard update
     */
    public void publishDashboardUpdate(String tenantId, Object data) {
        WebSocketMessage message = WebSocketMessage.builder()
                .type("dashboard-update")
                .tenantId(tenantId)
                .data(data)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        publish("dashboard:updates", message);
    }

    /**
     * Publish saga event
     */
    public void publishSagaEvent(String tenantId, Object data) {
        WebSocketMessage message = WebSocketMessage.builder()
                .type("saga-event")
                .tenantId(tenantId)
                .data(data)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        publish("saga:events", message);
    }

    /**
     * Publish chart update
     */
    public void publishChartUpdate(String tenantId, Object data) {
        WebSocketMessage message = WebSocketMessage.builder()
                .type("chart-update")
                .tenantId(tenantId)
                .data(data)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        publish("chart:updates", message);
    }

    /**
     * Publish monitoring alert
     */
    public void publishMonitoringAlert(String tenantId, Object data) {
        WebSocketMessage message = WebSocketMessage.builder()
                .type("monitoring-alert")
                .tenantId(tenantId)
                .data(data)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        publish("monitoring:alerts", message);
    }
}
