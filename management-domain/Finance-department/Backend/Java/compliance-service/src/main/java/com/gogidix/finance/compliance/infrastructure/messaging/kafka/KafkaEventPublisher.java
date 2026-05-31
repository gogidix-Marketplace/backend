package com.gogidix.finance.compliance.infrastructure.messaging.kafka;

import com.gogidix.finance.compliance.domain.event.ComplianceCheckCompletedEvent;
import com.gogidix.finance.compliance.domain.event.ComplianceViolationEvent;
import com.gogidix.finance.compliance.domain.port.out.EventPublisher;
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
 * Publishes compliance events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.compliance-events:compliance-service.events}")
    private String complianceEventsTopic;

    @Value("${spring.kafka.topics.domain-events:finance.domain-events}")
    private String domainEventsTopic;

    @Value("${spring.kafka.topics.violation-events:compliance.violations}")
    private String violationEventsTopic;

    @Override
    public void publish(ComplianceViolationEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getRuleId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(violationEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published compliance violation event: {} to topic: {}",
                        event.getEventId(), violationEventsTopic);
                } else {
                    log.error("Failed to publish compliance violation event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing compliance violation event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(ComplianceCheckCompletedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getCheckId();

            CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(complianceEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published compliance check event: {} to topic: {}",
                        event.getEventId(), complianceEventsTopic);
                } else {
                    log.error("Failed to publish compliance check event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing compliance check event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof ComplianceViolationEvent violationEvent) {
                publish(violationEvent);
            } else if (event instanceof ComplianceCheckCompletedEvent checkEvent) {
                publish(checkEvent);
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
