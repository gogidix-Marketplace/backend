package com.gogidix.shared.warehousing.quality.infrastructure.messaging;

import com.gogidix.shared.warehousing.quality.domain.events.QualityCheckCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QualityEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishQualityCheckCompleted(QualityCheckCompletedEvent event) {
        log.info("Publishing quality check completed event: {}", event.getEventId());
        kafkaTemplate.send("quality-check-completed", event.getQualityCheckId(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish event: {}", ex.getMessage());
                }
            });
    }
}
