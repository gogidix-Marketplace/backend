package com.gogidix.universal.tracking.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrackingEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TrackingEventPublisher eventPublisher;

    @BeforeEach
    void setUp() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"test\":\"payload\"}");
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(null));
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(null));
    }

    @Test
    void testPublish_Success() throws Exception {
        String topic = "tracking-events";
        Map<String, Object> payload = Map.of(
            "eventId", "123",
            "eventType", "PAGE_VIEW"
        );

        eventPublisher.publish(topic, payload);

        verify(kafkaTemplate).send(eq(topic), anyString());
        verify(objectMapper).writeValueAsString(payload);
    }

    @Test
    void testPublish_WithKey_Success() throws Exception {
        String topic = "tracking-events";
        String key = "session-123";
        Map<String, Object> payload = Map.of(
            "eventId", "123",
            "sessionId", "session-123"
        );

        eventPublisher.publish(topic, key, payload);

        verify(kafkaTemplate).send(eq(topic), eq(key), anyString());
        verify(objectMapper).writeValueAsString(payload);
    }

    @Test
    void testPublish_JsonSerializationFailure() throws Exception {
        String topic = "tracking-events";
        Map<String, Object> payload = Map.of("test", "data");
        when(objectMapper.writeValueAsString(any()))
            .thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("Serialization error") {});

        assertThrows(RuntimeException.class, () -> eventPublisher.publish(topic, payload));
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void testPublish_KafkaSendFailure() throws Exception {
        String topic = "tracking-events";
        Map<String, Object> payload = Map.of("test", "data");
        when(kafkaTemplate.send(anyString(), anyString()))
            .thenThrow(new RuntimeException("Kafka connection failed"));

        assertThrows(RuntimeException.class, () -> eventPublisher.publish(topic, payload));
        verify(objectMapper).writeValueAsString(payload);
    }
}
