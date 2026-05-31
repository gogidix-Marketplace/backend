package com.gogidix.ecommerce.sms.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.sms.domain.event.SmsDomainEvent;
import com.gogidix.ecommerce.sms.domain.port.out.SmsEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaSmsEventPublisher implements SmsEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaSmsEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaSmsEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(SmsDomainEvent event) {
        try {
            streamBridge.send("smsEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
