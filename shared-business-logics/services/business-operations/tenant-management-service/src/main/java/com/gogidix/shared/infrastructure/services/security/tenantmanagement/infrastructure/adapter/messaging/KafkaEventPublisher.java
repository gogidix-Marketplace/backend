package com.gogidix.shared.infrastructure.services.security.tenantmanagement.infrastructure.adapter.messaging;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.DomainEvent;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out.EventPublisherPort;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

/**
 * Kafka-based implementation of the event publisher port.
 * Publishes domain events to Kafka topics for consumption by other services.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisherPort {

    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;

    @Value("${kafka.topic.prefix:tenant}")
    private String topicPrefix;

    @Override
    public void publish(DomainEvent event) {
        try {
            String tenantId = TenantContextHolder.getTenantId();
            String topic = String.format("%s.%s.events",
                topicPrefix,
                tenantId != null ? tenantId : "default");

            log.info("Publishing event: {} to topic: {} for tenant: {}",
                event.getEventType(), topic, tenantId);

            kafkaTemplate.send(topic, event.getAggregateId(), event)
                .whenComplete((SendResult<String, DomainEvent> result, Throwable ex) -> {
                    if (ex == null) {
                        log.debug("Event published successfully: {} with correlationId: {}",
                            event.getEventType(), event.getCorrelationId());
                    } else {
                        log.error("Failed to publish event: {} for tenant: {}",
                            event.getEventType(), tenantId, ex);
                        // TODO: Implement dead letter queue or retry logic
                    }
                });
        } catch (Exception e) {
            log.error("Error publishing event: {}", event.getEventType(), e);
            // Don't throw - events should be fire-and-forget
        }
    }
}
