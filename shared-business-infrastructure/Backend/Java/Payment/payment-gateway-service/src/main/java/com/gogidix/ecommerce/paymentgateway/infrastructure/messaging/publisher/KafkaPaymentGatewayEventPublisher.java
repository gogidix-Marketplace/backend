package com.gogidix.ecommerce.paymentgateway.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayDomainEvent;
import com.gogidix.ecommerce.paymentgateway.domain.port.out.PaymentGatewayEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentGatewayEventPublisher implements PaymentGatewayEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPaymentGatewayEventPublisher.class);
    private final StreamBridge streamBridge;

    public KafkaPaymentGatewayEventPublisher(StreamBridge streamBridge) { this.streamBridge = streamBridge; }

    @Override
    public void publish(PaymentGatewayDomainEvent event) {
        try {
            streamBridge.send("paymentgatewayEvents-out-0", event);
            log.info("Published event: type={}, tenant={}", event.eventType(), event.tenantId());
        } catch (Exception e) {
            log.error("Failed to publish event: {}", e.getMessage());
        }
    }
}
