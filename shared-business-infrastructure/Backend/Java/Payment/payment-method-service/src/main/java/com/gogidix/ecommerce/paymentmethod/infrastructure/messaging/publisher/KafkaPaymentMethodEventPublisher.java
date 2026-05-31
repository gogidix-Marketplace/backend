package com.gogidix.ecommerce.paymentmethod.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodDomainEvent;
import com.gogidix.ecommerce.paymentmethod.domain.port.out.PaymentMethodEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentMethodEventPublisher implements PaymentMethodEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPaymentMethodEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaPaymentMethodEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(PaymentMethodDomainEvent event) {
        try {
            streamBridge.send("paymentmethodEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
