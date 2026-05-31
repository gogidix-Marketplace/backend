package com.gogidix.finance.conversion.infrastructure.messaging.kafka;

import com.gogidix.finance.conversion.domain.event.ConversionCompletedEvent;
import com.gogidix.finance.conversion.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher
 * Publishes conversion events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.conversion-events:currency-conversion-service.events}")
    private String conversionEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publish(ConversionCompletedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getConversionId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(conversionEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published conversion event: {} to topic: {}",
                            event.getEventId(), conversionEventsTopic);
                } else {
                    log.error("Failed to publish conversion event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing conversion event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof ConversionCompletedEvent conversionEvent) {
                publish(conversionEvent);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        try {
            // Kafka health check would go here
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
