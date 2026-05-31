package com.gogidix.dashboard.gateway.websocket.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisPublisherTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @InjectMocks
    private RedisPublisher redisPublisher;

    @Test
    void publish_sendsToRedisChannel() {
        WebSocketMessage msg = WebSocketMessage.builder()
                .type("test").data(Map.of("k", "v")).build();

        redisPublisher.publish("test-channel", msg);

        verify(redisTemplate).convertAndSend(eq("test-channel"), anyString());
    }

    @Test
    void publishDashboardUpdate_sendsToDashboardChannel() {
        redisPublisher.publishDashboardUpdate("t1", Map.of("data", "value"));
        verify(redisTemplate).convertAndSend(eq("dashboard:updates"), anyString());
    }

    @Test
    void publishSagaEvent_sendsToSagaChannel() {
        redisPublisher.publishSagaEvent("t1", Map.of("event", "completed"));
        verify(redisTemplate).convertAndSend(eq("saga:events"), anyString());
    }

    @Test
    void publishChartUpdate_sendsToChartChannel() {
        redisPublisher.publishChartUpdate("t1", Map.of("chart", "sales"));
        verify(redisTemplate).convertAndSend(eq("chart:updates"), anyString());
    }

    @Test
    void publishMonitoringAlert_sendsToMonitoringChannel() {
        redisPublisher.publishMonitoringAlert("t1", Map.of("alert", "high-cpu"));
        verify(redisTemplate).convertAndSend(eq("monitoring:alerts"), anyString());
    }

    @Test
    void publish_withNullTenant_succeeds() {
        redisPublisher.publishDashboardUpdate(null, Map.of("data", "x"));
        verify(redisTemplate).convertAndSend(eq("dashboard:updates"), anyString());
    }
}
