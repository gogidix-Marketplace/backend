package com.gogidix.cargo.eventdriven.infrastructure.messaging;

import com.gogidix.cargo.eventdriven.application.mapper.EventMapper;
import com.gogidix.cargo.eventdriven.domain.event.BaseDomainEvent;
import com.gogidix.cargo.eventdriven.domain.port.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaEventPublisherAdapter implements EventPublisherPort {
    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisherAdapter.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventPublisherAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String topic, String key, BaseDomainEvent event) {
        String payload = EventMapper.serialize(event);
        log.debug("Publishing to Kafka: topic={}, key={}", topic, key);
        kafkaTemplate.send(topic, key, payload)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish event to Kafka: topic={}, key={}", topic, key, ex);
                } else {
                    log.debug("Event published: topic={}, partition={}, offset={}",
                        topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                }
            });
    }

    @Override
    public void publish(String topic, BaseDomainEvent event) {
        publish(topic, event.getAggregateId(), event);
    }
}
