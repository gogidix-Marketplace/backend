package com.gogidix.ecommerce.email.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.email.domain.event.EmailDomainEvent;
import com.gogidix.ecommerce.email.domain.port.out.EmailEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaEmailEventPublisher implements EmailEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEmailEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaEmailEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(EmailDomainEvent event) {
        try {
            streamBridge.send("emailEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
