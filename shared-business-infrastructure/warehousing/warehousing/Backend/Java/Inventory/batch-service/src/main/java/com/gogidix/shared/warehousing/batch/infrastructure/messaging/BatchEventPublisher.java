package com.gogidix.shared.warehousing.batch.infrastructure.messaging;

import com.gogidix.shared.warehousing.batch.domain.events.BatchCreatedEvent;
import com.gogidix.shared.warehousing.batch.domain.events.BatchLotCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Event publisher for batch domain events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BatchEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.batch-created:batch-created}")
    private String batchCreatedTopic;

    @Value("${kafka.topic.batch-lot-created:batch-lot-created}")
    private String batchLotCreatedTopic;

    public void publishBatchCreated(BatchCreatedEvent event) {
        try {
            kafkaTemplate.send(batchCreatedTopic, event.getBatchId(), event);
            log.info("Published BatchCreatedEvent for batch: {}", event.getBatchId());
        } catch (Exception e) {
            log.error("Failed to publish BatchCreatedEvent", e);
        }
    }

    public void publishBatchLotCreated(BatchLotCreatedEvent event) {
        try {
            kafkaTemplate.send(batchLotCreatedTopic, event.getLotId(), event);
            log.info("Published BatchLotCreatedEvent for lot: {}", event.getLotId());
        } catch (Exception e) {
            log.error("Failed to publish BatchLotCreatedEvent", e);
        }
    }
}
