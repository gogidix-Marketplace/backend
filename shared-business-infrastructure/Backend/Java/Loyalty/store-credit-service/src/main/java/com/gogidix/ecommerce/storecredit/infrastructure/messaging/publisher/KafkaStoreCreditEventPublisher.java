package com.gogidix.ecommerce.storecredit.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.storecredit.domain.event.StoreCreditDomainEvent;
import com.gogidix.ecommerce.storecredit.domain.port.out.StoreCreditEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaStoreCreditEventPublisher implements StoreCreditEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaStoreCreditEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaStoreCreditEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(StoreCreditDomainEvent event) {
        try {
            streamBridge.send("storecreditEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
