package com.gogidix.dashboard.gateway.websocket.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gogidix.dashboard.gateway.websocket.application.dto.WebSocketMessage;
import com.gogidix.dashboard.gateway.websocket.infrastructure.handler.WebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisMessageSubscriberTest {

    @Mock
    private WebSocketHandler webSocketHandler;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private RedisMessageSubscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriber = new RedisMessageSubscriber(objectMapper, webSocketHandler);
    }

    private Message createMessage(String body, String channel) {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(body.getBytes(StandardCharsets.UTF_8));
        when(message.getChannel()).thenReturn(channel.getBytes(StandardCharsets.UTF_8));
        return message;
    }

    @Test
    void onMessage_withTenantId_broadcastsToTenant() throws Exception {
        String body = objectMapper.writeValueAsString(
                WebSocketMessage.builder().type("update").tenantId("t1").data("data").build());
        Message message = createMessage(body, "dashboard:updates");

        subscriber.onMessage(message, null);

        verify(webSocketHandler).broadcastToTenant(eq("t1"), any(WebSocketMessage.class));
    }

    @Test
    void onMessage_withoutTenantId_broadcastsToTopic() throws Exception {
        String body = objectMapper.writeValueAsString(
                WebSocketMessage.builder().type("update").data("data").build());
        Message message = createMessage(body, "chart:updates");

        subscriber.onMessage(message, null);

        verify(webSocketHandler).broadcastToTopic(eq("updates"), any(WebSocketMessage.class));
    }

    @Test
    void onMessage_withInvalidJson_doesNotThrow() {
        Message message = createMessage("not-json", "dashboard:updates");

        assertDoesNotThrow(() -> subscriber.onMessage(message, null));
        verify(webSocketHandler, never()).broadcastToTenant(any(), any());
    }

    @Test
    void onMessage_stripsChannelPrefix() throws Exception {
        String body = objectMapper.writeValueAsString(
                WebSocketMessage.builder().type("update").tenantId("t1").build());
        Message message = createMessage(body, "saga:events");

        subscriber.onMessage(message, null);

        ArgumentCaptor<WebSocketMessage> captor = ArgumentCaptor.forClass(WebSocketMessage.class);
        verify(webSocketHandler).broadcastToTenant(eq("t1"), captor.capture());
        assertEquals("events", captor.getValue().getTopic());
    }
}
