package com.gogidix.shared.warehousing.serialization.infrastructure.messaging;

import com.gogidix.shared.warehousing.serialization.domain.events.BatchCreatedEvent;
import com.gogidix.shared.warehousing.serialization.domain.events.SerializedItemCreatedEvent;
import com.gogidix.shared.warehousing.serialization.domain.events.SerializedItemStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Event publisher for serialization domain events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SerializationEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.serialized-item-created:serialized-item-created}")
    private String serializedItemCreatedTopic;

    @Value("${kafka.topic.serialized-item-status-changed:serialized-item-status-changed}")
    private String serializedItemStatusChangedTopic;

    @Value("${kafka.topic.batch-created:batch-created}")
    private String batchCreatedTopic;

    public void publishSerializedItemCreated(SerializedItemCreatedEvent event) {
        try {
            kafkaTemplate.send(serializedItemCreatedTopic, event.getSerializedItemId(), event);
            log.info("Published SerializedItemCreatedEvent for item: {}", event.getSerializedItemId());
        } catch (Exception e) {
            log.error("Failed to publish SerializedItemCreatedEvent", e);
        }
    }

    public void publishStatusChanged(SerializedItemStatusChangedEvent event) {
        try {
            kafkaTemplate.send(serializedItemStatusChangedTopic, event.getSerializedItemId(), event);
            log.info("Published SerializedItemStatusChangedEvent for item: {}", event.getSerializedItemId());
        } catch (Exception e) {
            log.error("Failed to publish SerializedItemStatusChangedEvent", e);
        }
    }

    public void publishBatchCreated(BatchCreatedEvent event) {
        try {
            kafkaTemplate.send(batchCreatedTopic, event.getBatchId(), event);
            log.info("Published BatchCreatedEvent for batch: {}", event.getBatchId());
        } catch (Exception e) {
            log.error("Failed to publish BatchCreatedEvent", e);
        }
    }
}
