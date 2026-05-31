package com.gogidix.sales.communication.infrastructure.messaging.kafka;

import com.gogidix.sales.communication.domain.event.ConversationCreatedEvent;
import com.gogidix.sales.communication.domain.event.ConversationUpdatedEvent;
import com.gogidix.sales.communication.domain.event.MessageReadEvent;
import com.gogidix.sales.communication.domain.event.MessageSentEvent;
import com.gogidix.sales.communication.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher Implementation
 * Publishes domain events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.message-events:communication-service.events}")
    private String messageEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    private volatile boolean ready = true;

    @Override
    public void publish(MessageSentEvent event) {
        try {
            String key = event.getMessageId();
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(messageEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish MessageSentEvent: {}", event.getMessageId(), ex);
                    ready = false;
                } else {
                    log.debug("Published MessageSentEvent: {} to partition: {}",
                            event.getMessageId(), result.getRecordMetadata().partition());
                    ready = true;
                }
            });
        } catch (Exception e) {
            log.error("Error publishing MessageSentEvent", e);
            ready = false;
        }
    }

    @Override
    public void publish(MessageReadEvent event) {
        try {
            String key = event.getMessageId();
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(messageEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish MessageReadEvent: {}", event.getMessageId(), ex);
                } else {
                    log.debug("Published MessageReadEvent: {}", event.getMessageId());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing MessageReadEvent", e);
        }
    }

    @Override
    public void publish(ConversationCreatedEvent event) {
        try {
            String key = event.getConversationId();
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(domainEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish ConversationCreatedEvent: {}", event.getConversationId(), ex);
                } else {
                    log.debug("Published ConversationCreatedEvent: {}", event.getConversationId());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing ConversationCreatedEvent", e);
        }
    }

    @Override
    public void publish(ConversationUpdatedEvent event) {
        try {
            String key = event.getConversationId();
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(domainEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish ConversationUpdatedEvent: {}", event.getConversationId(), ex);
                } else {
                    log.debug("Published ConversationUpdatedEvent: {}", event.getConversationId());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing ConversationUpdatedEvent", e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof MessageSentEvent) {
                publish((MessageSentEvent) event);
            } else if (event instanceof MessageReadEvent) {
                publish((MessageReadEvent) event);
            } else if (event instanceof ConversationCreatedEvent) {
                publish((ConversationCreatedEvent) event);
            } else if (event instanceof ConversationUpdatedEvent) {
                publish((ConversationUpdatedEvent) event);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        return ready;
    }
}
