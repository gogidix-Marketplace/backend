package com.gogidix.ecommerce.email.infrastructure.messaging.publisher;

import com.gogidix.ecommerce.email.domain.model.Email;
import com.gogidix.ecommerce.email.domain.event.EmailCreatedEvent;
import com.gogidix.ecommerce.email.domain.event.EmailUpdatedEvent;
import com.gogidix.ecommerce.email.domain.event.EmailDeletedEvent;
import com.gogidix.ecommerce.email.domain.port.out.EmailEventPublisher;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class KafkaEmailEventPublisher implements EmailEventPublisher {

    private final StreamBridge streamBridge;

    public KafkaEmailEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void publishCreated(Email entity) {
        EmailCreatedEvent event = new EmailCreatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("emailEvents-out-0", event);
    }

    @Override
    public void publishUpdated(Email entity) {
        EmailUpdatedEvent event = new EmailUpdatedEvent(entity.getId(), entity.getTenantId(), entity.getName());
        streamBridge.send("emailEvents-out-0", event);
    }

    @Override
    public void publishDeleted(Email entity) {
        EmailDeletedEvent event = new EmailDeletedEvent(entity.getId(), entity.getTenantId());
        streamBridge.send("emailEvents-out-0", event);
    }
}