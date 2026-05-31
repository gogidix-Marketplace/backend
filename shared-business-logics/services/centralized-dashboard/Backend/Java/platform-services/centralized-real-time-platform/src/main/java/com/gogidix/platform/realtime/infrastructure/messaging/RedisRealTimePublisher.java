package com.gogidix.platform.realtime.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.platform.realtime.domain.model.RealTimeMessage;
import com.gogidix.platform.realtime.domain.port.out.RealTimePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis and Kafka implementation of RealTimePublisher.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisRealTimePublisher implements RealTimePublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(String channelId, RealTimeMessage message) {
        try {
            String topic = "realtime:channel:" + channelId;
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(topic, json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message", e);
        }
    }

    @Override
    public void publishEvent(String eventType, Object payload) {
        try {
            String topic = "realtime.events." + eventType;
            String json = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event", e);
        }
    }
}
