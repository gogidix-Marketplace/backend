package com.gogidix.aiservices.aicontentgenerationservice.infrastructure.messaging;

import com.gogidix.aiservices.aicontentgenerationservice.domain.port.out.EventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class EventPublisherImpl implements EventPublisherPort {

    @Override
    public void publish(String eventType, Map<String, Object> payload) {
        log.info("Publishing event: {} with payload: {}", eventType, payload);
    }

    @Override
    public void publishContentGenerated(String contentId, String userId, String contentType) {
        Map<String, Object> payload = Map.of(
                "contentId", contentId,
                "userId", userId,
                "contentType", contentType,
                "timestamp", System.currentTimeMillis()
        );
        publish("content.generated", payload);
    }

    @Override
    public void publishContentFailed(String contentId, String userId, String error) {
        Map<String, Object> payload = Map.of(
                "contentId", contentId,
                "userId", userId,
                "error", error,
                "timestamp", System.currentTimeMillis()
        );
        publish("content.failed", payload);
    }
}
