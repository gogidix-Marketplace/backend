package com.gogidix.ecommerce.paymentmethod.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodCreatedEvent;
import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodUpdatedEvent;
import com.gogidix.ecommerce.paymentmethod.domain.event.PaymentMethodDeletedEvent;
import com.gogidix.ecommerce.paymentmethod.domain.port.out.PaymentMethodEventPublisher;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentMethodEventPublisher implements PaymentMethodEventPublisher {

    private final StreamBridge streamBridge;

    public KafkaPaymentMethodEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void publishCreated(PaymentMethod entity) {
        PaymentMethodCreatedEvent event = new PaymentMethodCreatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("paymentmethodEvents-out-0", event);
    }

    @Override
    public void publishUpdated(PaymentMethod entity) {
        PaymentMethodUpdatedEvent event = new PaymentMethodUpdatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("paymentmethodEvents-out-0", event);
    }

    @Override
    public void publishDeleted(PaymentMethod entity) {
        PaymentMethodDeletedEvent event = new PaymentMethodDeletedEvent(entity.getId(), entity.getTenantId());
        streamBridge.send("paymentmethodEvents-out-0", event);
    }
}