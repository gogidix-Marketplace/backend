package com.gogidix.ecommerce.customer.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.customer.domain.event.CustomerDomainEvent;
import com.gogidix.ecommerce.customer.domain.port.out.CustomerEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaCustomerEventPublisher implements CustomerEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaCustomerEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaCustomerEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(CustomerDomainEvent event) {
        try {
            streamBridge.send("customerEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
