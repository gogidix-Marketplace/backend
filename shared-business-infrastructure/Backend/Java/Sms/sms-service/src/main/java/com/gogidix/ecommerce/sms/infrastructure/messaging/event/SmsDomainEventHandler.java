package com.gogidix.ecommerce.sms.infrastructure.messaging.event;

import com.gogidix.ecommerce.sms.domain.event.SmsCreatedEvent;
import com.gogidix.ecommerce.sms.domain.event.SmsUpdatedEvent;
import com.gogidix.ecommerce.sms.domain.event.SmsDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SmsDomainEventHandler {

    @EventListener
    public void handleCreated(SmsCreatedEvent event) {
    }

    @EventListener
    public void handleUpdated(SmsUpdatedEvent event) {
    }

    @EventListener
    public void handleDeleted(SmsDeletedEvent event) {
    }
}