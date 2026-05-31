package com.gogidix.ecommerce.sms.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.sms.domain.model.Sms;
import com.gogidix.ecommerce.sms.domain.event.SmsCreatedEvent;
import com.gogidix.ecommerce.sms.domain.event.SmsUpdatedEvent;
import com.gogidix.ecommerce.sms.domain.event.SmsDeletedEvent;
import com.gogidix.ecommerce.sms.domain.port.out.SmsEventPublisher;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaSmsEventPublisher implements SmsEventPublisher {

    private final StreamBridge streamBridge;

    public KafkaSmsEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void publishCreated(Sms entity) {
        SmsCreatedEvent event = new SmsCreatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("smsEvents-out-0", event);
    }

    @Override
    public void publishUpdated(Sms entity) {
        SmsUpdatedEvent event = new SmsUpdatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("smsEvents-out-0", event);
    }

    @Override
    public void publishDeleted(Sms entity) {
        SmsDeletedEvent event = new SmsDeletedEvent(entity.getId(), entity.getTenantId());
        streamBridge.send("smsEvents-out-0", event);
    }
}