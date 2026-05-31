package com.gogidix.sales.territory.infrastructure.messaging.kafka;

import com.gogidix.sales.territory.domain.event.QuotaUpdatedEvent;
import com.gogidix.sales.territory.domain.event.TerritoryAssignedEvent;
import com.gogidix.sales.territory.domain.event.TerritoryCreatedEvent;
import com.gogidix.sales.territory.domain.port.out.EventPublisher;
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
 * Publishes territory events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.territory-events:territory-management-service.events}")
    private String territoryEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publishTerritoryEvent(TerritoryCreatedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getTerritoryId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(territoryEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published territory event: {} to topic: {}",
                        event.getEventId(), territoryEventsTopic);
                } else {
                    log.error("Failed to publish territory event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing territory event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAssignmentEvent(TerritoryAssignedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getAssignmentId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(territoryEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published assignment event: {} to topic: {}",
                        event.getEventId(), territoryEventsTopic);
                } else {
                    log.error("Failed to publish assignment event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing assignment event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishQuotaEvent(QuotaUpdatedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getQuotaId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(territoryEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published quota event: {} to topic: {}",
                        event.getEventId(), territoryEventsTopic);
                } else {
                    log.error("Failed to publish quota event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing quota event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<?> events) {
        for (Object event : events) {
            if (event instanceof TerritoryCreatedEvent territoryEvent) {
                publishTerritoryEvent(territoryEvent);
            } else if (event instanceof TerritoryAssignedEvent assignmentEvent) {
                publishAssignmentEvent(assignmentEvent);
            } else if (event instanceof QuotaUpdatedEvent quotaEvent) {
                publishQuotaEvent(quotaEvent);
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
