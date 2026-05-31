package com.gogidix.ecommerce.payment.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.payment.domain.event.PaymentDomainEvent;
import com.gogidix.ecommerce.payment.domain.port.out.PaymentEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentEventPublisher implements PaymentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPaymentEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaPaymentEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(PaymentDomainEvent event) {
        try {
            streamBridge.send("paymentEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
