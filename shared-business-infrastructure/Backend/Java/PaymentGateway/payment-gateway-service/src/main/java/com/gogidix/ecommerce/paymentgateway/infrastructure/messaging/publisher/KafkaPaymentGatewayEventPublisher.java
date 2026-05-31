package com.gogidix.ecommerce.paymentgateway.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayCreatedEvent;
import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayUpdatedEvent;
import com.gogidix.ecommerce.paymentgateway.domain.event.PaymentGatewayDeletedEvent;
import com.gogidix.ecommerce.paymentgateway.domain.port.out.PaymentGatewayEventPublisher;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentGatewayEventPublisher implements PaymentGatewayEventPublisher {

    private final StreamBridge streamBridge;

    public KafkaPaymentGatewayEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void publishCreated(PaymentGateway entity) {
        PaymentGatewayCreatedEvent event = new PaymentGatewayCreatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("paymentgatewayEvents-out-0", event);
    }

    @Override
    public void publishUpdated(PaymentGateway entity) {
        PaymentGatewayUpdatedEvent event = new PaymentGatewayUpdatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("paymentgatewayEvents-out-0", event);
    }

    @Override
    public void publishDeleted(PaymentGateway entity) {
        PaymentGatewayDeletedEvent event = new PaymentGatewayDeletedEvent(entity.getId(), entity.getTenantId());
        streamBridge.send("paymentgatewayEvents-out-0", event);
    }
}