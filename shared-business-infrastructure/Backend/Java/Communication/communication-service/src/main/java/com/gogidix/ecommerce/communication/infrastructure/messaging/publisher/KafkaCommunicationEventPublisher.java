package com.gogidix.ecommerce.communication.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.communication.domain.event.CommunicationDomainEvent;
import com.gogidix.ecommerce.communication.domain.port.out.CommunicationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaCommunicationEventPublisher implements CommunicationEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaCommunicationEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaCommunicationEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(CommunicationDomainEvent event) {
        try {
            streamBridge.send("communicationEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
