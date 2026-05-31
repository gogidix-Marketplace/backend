package com.gogidix.shared.warehousing.returns.infrastructure.messaging;

import com.gogidix.shared.warehousing.returns.domain.events.ReturnCreatedEvent;
import com.gogidix.shared.warehousing.returns.domain.events.ReturnUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReturnEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReturnCreated(ReturnCreatedEvent event) {
        log.info("Publishing return created event: {}", event.getEventId());
        kafkaTemplate.send("return-created", event.getReturnId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish return created event: {}", ex.getMessage());
                }
            });
    }

    public void publishReturnUpdated(ReturnUpdatedEvent event) {
        log.info("Publishing return updated event: {}", event.getEventId());
        kafkaTemplate.send("return-updated", event.getReturnId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish return updated event: {}", ex.getMessage());
                }
            });
    }
}
