package com.gogidix.courier.tenantservice.infrastructure.messaging.publisher;

import com.gogidix.courier.tenantservice.application.service.TenantEventPublisher;
import com.gogidix.courier.tenantservice.domain.event.DomainEvent;
import com.gogidix.courier.tenantservice.domain.event.TenantConfigChangedEvent;
import com.gogidix.courier.tenantservice.domain.event.TenantCreatedEvent;
import com.gogidix.courier.tenantservice.domain.event.TenantUpdatedEvent;
import com.gogidix.courier.tenantservice.infrastructure.messaging.event.TenantEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka publisher for tenant events.
 */
@Component
public class KafkaTenantEventPublisher implements TenantEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaTenantEventPublisher.class);

    private final KafkaTemplate<String, TenantEvent> kafkaTemplate;
    private final String topicName;

    public KafkaTenantEventPublisher(
            KafkaTemplate<String, TenantEvent> kafkaTemplate,
            @Value("${spring.kafka.topic.tenant-events:tenant-events}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void publish(DomainEvent event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            doPublish(event);
                        }
                    });
        } else {
            doPublish(event);
        }
    }

    private void doPublish(DomainEvent event) {
        TenantEvent kafkaEvent = toKafkaEvent(event);
        log.debug("Publishing event: {} for tenant: {}", kafkaEvent.eventType(), kafkaEvent.tenantId());

        String key = kafkaEvent.tenantId() + ":" + kafkaEvent.aggregateId();

        CompletableFuture<SendResult<String, TenantEvent>> future =
                kafkaTemplate.send(topicName, key, kafkaEvent);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.debug("Event published successfully: {} to partition: {}",
                        kafkaEvent.eventId(), result.getRecordMetadata().partition());
            } else {
                log.error("Failed to publish event: {} - {}", kafkaEvent.eventId(), ex.getMessage());
            }
        });
    }

    private TenantEvent toKafkaEvent(DomainEvent event) {
        if (event instanceof TenantCreatedEvent e) {
            return TenantEvent.fromDomainEvent(
                    e.getEventType(),
                    e.getAggregateId(),
                    e.getTenantId(),
                    e.getTenantName(),
                    "PENDING",
                    null,
                    e.getOccurredAt()
            );
        } else if (event instanceof TenantUpdatedEvent e) {
            return TenantEvent.fromDomainEvent(
                    e.getEventType(),
                    e.getAggregateId(),
                    e.getTenantId(),
                    e.getTenantName(),
                    e.getStatus().name(),
                    null,
                    e.getOccurredAt()
            );
        } else if (event instanceof TenantConfigChangedEvent e) {
            return TenantEvent.fromDomainEvent(
                    e.getEventType(),
                    e.getAggregateId(),
                    e.getTenantId(),
                    e.getTenantName(),
                    null,
                    e.getChangedSettings(),
                    e.getOccurredAt()
            );
        }
        throw new IllegalArgumentException("Unknown event type: " + event.getClass());
    }

    public String getTopicName() {
        return topicName;
    }
}
