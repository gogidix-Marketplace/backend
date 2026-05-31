package com.gogidix.dashboard.gateway.websocket.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage;
import com.gogidix.dashboard.gateway.websocket.infrastructure.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Redis message listener for pub/sub.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final WebSocketHandler webSocketHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String body = new String(message.getBody());

            log.debug("Received Redis message on channel: {}", channel);

            // Parse the message
            WebSocketMessage wsMessage = objectMapper.readValue(body, WebSocketMessage.class);

            // Determine topic from channel
            String topic = channel.replace("dashboard:", "")
                                  .replace("saga:", "")
                                  .replace("chart:", "")
                                  .replace("monitoring:", "");

            wsMessage.setTopic(topic);
            wsMessage.setSource("redis");

            // Broadcast to WebSocket clients
            if (wsMessage.getTenantId() != null) {
                webSocketHandler.broadcastToTenant(wsMessage.getTenantId(), wsMessage);
            } else {
                webSocketHandler.broadcastToTopic(topic, wsMessage);
            }

        } catch (Exception e) {
            log.error("Error processing Redis message", e);
        }
    }
}
