package com.gogidix.sales.leadmanagement.infrastructure.messaging.kafka;

import com.gogidix.sales.leadmanagement.domain.event.*;
import com.gogidix.sales.leadmanagement.domain.port.out.EventPublisher;
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
 * Publishes lead events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.lead-events:lead-management-service.events}")
    private String leadEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publish(LeadCreatedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getLeadId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(leadEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published lead created event: {} to topic: {}",
                            event.getEventId(), leadEventsTopic);
                } else {
                    log.error("Failed to publish lead created event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing lead created event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(LeadAssignedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getLeadId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(leadEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published lead assigned event: {} to topic: {}",
                            event.getEventId(), leadEventsTopic);
                } else {
                    log.error("Failed to publish lead assigned event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing lead assigned event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(LeadQualifiedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getLeadId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(leadEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Published lead qualified event: {} to topic: {}",
                            event.getEventId(), leadEventsTopic);
                } else {
                    log.error("Failed to publish lead qualified event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing lead qualified event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(LeadConvertedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getLeadId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(leadEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Published lead converted event: {} to topic: {}",
                            event.getEventId(), leadEventsTopic);
                } else {
                    log.error("Failed to publish lead converted event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing lead converted event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(LeadStageChangedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getLeadId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(leadEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published lead stage changed event: {} to topic: {}",
                            event.getEventId(), leadEventsTopic);
                } else {
                    log.error("Failed to publish lead stage changed event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing lead stage changed event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(LeadLostEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getLeadId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(leadEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Published lead lost event: {} to topic: {}",
                            event.getEventId(), leadEventsTopic);
                } else {
                    log.error("Failed to publish lead lost event: {}", event.getEventId(), ex);
                }
            });

            kafkaTemplate.send(domainEventsTopic, key, event);

        } catch (Exception e) {
            log.error("Error publishing lead lost event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof LeadCreatedEvent createdEvent) {
                publish(createdEvent);
            } else if (event instanceof LeadAssignedEvent assignedEvent) {
                publish(assignedEvent);
            } else if (event instanceof LeadQualifiedEvent qualifiedEvent) {
                publish(qualifiedEvent);
            } else if (event instanceof LeadConvertedEvent convertedEvent) {
                publish(convertedEvent);
            } else if (event instanceof LeadStageChangedEvent stageChangedEvent) {
                publish(stageChangedEvent);
            } else if (event instanceof LeadLostEvent lostEvent) {
                publish(lostEvent);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        try {
            return kafkaTemplate != null;
        } catch (Exception e) {
            return false;
        }
    }
}
